package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.common.FrozenDateTimeService
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockConnection
import org.jooq.tools.jdbc.MockDataProvider
import org.junit.jupiter.api.Test
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

internal class NewStudySyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobBuilderFactory = mockk<JobBuilderFactory>()
    private val stepBuilderFactory = mockk<StepBuilderFactory>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = NewStudySyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobBuilderFactory,
        stepBuilderFactory,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncNewStudyJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf NewStudyItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."new_study"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getInt("newsletter_id") } returns 10
            every { getInt("newsletter_topic_id") } returns 11
            every { getLong("number") } returns 12
            every { getInt("publication_year") } returns 13
            every { getString("first_author") } returns "myfirstauthor"
            every { getString("authors") } returns "a2,a3"
            every { getString("goals") } returns "mygoals"
            every { getString("headline") } returns "myheadline"
            every { getInt("version") } returns 14
            every { getTimestamp("created") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { getTimestamp("last_modified") } returns Timestamp.valueOf("2020-04-04 13:14:15")
            every { wasNull() } returns false
        }

        val newStudy = config.makeEntity(resultSet)

        newStudy.newsletterId shouldBeEqualTo 10
        newStudy.newsletterTopicId shouldBeEqualTo 11
        newStudy.sort shouldBeEqualTo 1
        newStudy.paperNumber shouldBeEqualTo 12
        newStudy.year shouldBeEqualTo 13
        newStudy.authors shouldBeEqualTo "myfirstauthor et al."
        newStudy.headline shouldBeEqualTo "myheadline"
        newStudy.description shouldBeEqualTo "mygoals"
        newStudy.version shouldBeEqualTo 14
        newStudy.created.toString() shouldBeEqualTo "2020-04-04 12:13:14.0"
        newStudy.lastModified.toString() shouldBeEqualTo "2020-04-04 13:14:15.0"
        newStudy.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Suppress("MaxLineLength")
    @Test
    fun selectSql() {
        /* ktlint-disable max-line-length */
        config.selectSql() shouldBeEqualTo
            """select "public"."paper_newsletter"."newsletter_id", "public"."paper_newsletter"."paper_id", "public"."paper_newsletter"."newsletter_topic_id", "public"."paper"."publication_year", "public"."paper"."number", "public"."paper"."first_author", "public"."paper"."authors", "public"."paper_newsletter"."headline", "public"."paper"."goals", "public"."paper_newsletter"."version", "public"."paper_newsletter"."created", "public"."paper_newsletter"."last_modified" from "public"."paper_newsletter" join "public"."paper" on "public"."paper_newsletter"."paper_id" = "public"."paper"."id" join "public"."paper_code" on "public"."paper"."id" = "public"."paper_code"."paper_id" join "public"."newsletter" on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id" where "public"."newsletter"."publication_status" = 1"""
        /* ktlint-enable max-line-length */
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.toString() shouldBeEqualTo
            """delete from "public"."new_study"
            |where "public"."new_study"."newsletter_topic_id" not in (
            |  select distinct "public"."newsletter_topic"."id"
            |  from "public"."newsletter_topic"
            |)""".trimMargin()
    }
}
