package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.common.FrozenDateTimeService
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockConnection
import org.jooq.tools.jdbc.MockDataProvider
import org.junit.jupiter.api.Test
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import java.sql.Date
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

internal class NewsletterSyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobBuilderFactory = mockk<JobBuilderFactory>()
    private val stepBuilderFactory = mockk<StepBuilderFactory>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = NewsletterSyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobBuilderFactory,
        stepBuilderFactory,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncNewsletterJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf NewsletterItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."newsletter"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getInt("id") } returns 1
            every { getString("issue") } returns "myissue"
            every { getDate("issue_date") } returns Date(345097788394)
            every { getInt("version") } returns 3
            every { getTimestamp("created") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { getTimestamp("last_modified") } returns Timestamp.valueOf("2020-04-04 13:14:15")
            every { wasNull() } returns false
        }

        val newsletter = config.makeEntity(resultSet)

        newsletter.id shouldBeEqualTo 1
        newsletter.issue shouldBeEqualTo "myissue"
        newsletter.issueDate.toString() shouldBeEqualTo "1980-12-08"
        newsletter.version shouldBeEqualTo 3
        newsletter.created.toString() shouldBeEqualTo "2020-04-04 12:13:14.0"
        newsletter.lastModified.toString() shouldBeEqualTo "2020-04-04 13:14:15.0"
        newsletter.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Test
    fun selectSql() {
        /* ktlint-disable max-line-length */
        config.selectSql() shouldBeEqualTo
            """select "public"."newsletter"."id", "public"."newsletter"."issue", "public"."newsletter"."issue_date", "public"."newsletter"."version", "public"."newsletter"."created", "public"."newsletter"."last_modified" from "public"."newsletter" where "public"."newsletter"."publication_status" = 1"""
        /* ktlint-enable max-line-length */
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.shouldBeNull()
    }
}
