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
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

internal class NewsletterTopicSyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobBuilderFactory = mockk<JobBuilderFactory>()
    private val stepBuilderFactory = mockk<StepBuilderFactory>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = NewsletterTopicSyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobBuilderFactory,
        stepBuilderFactory,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncNewsletterTopicJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf NewsletterTopicItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."newsletter_topic"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getInt("id") } returns 1
            every { getString("lang_code") } returns "mylangcode"
            every { getString("title") } returns "mytitle"
            every { getInt("sort") } returns 2
            every { getInt("version") } returns 3
            every { getTimestamp("created") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { getTimestamp("last_modified") } returns Timestamp.valueOf("2020-04-04 13:14:15")
            every { wasNull() } returns false
        }

        val newsletterTopic = config.makeEntity(resultSet)
        newsletterTopic.id shouldBeEqualTo 1
        newsletterTopic.langCode shouldBeEqualTo "mylangcode"
        newsletterTopic.title shouldBeEqualTo "mytitle"
        newsletterTopic.version shouldBeEqualTo 3
        newsletterTopic.created.toString() shouldBeEqualTo "2020-04-04 12:13:14.0"
        newsletterTopic.lastModified.toString() shouldBeEqualTo "2020-04-04 13:14:15.0"
        newsletterTopic.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Test
    fun selectSql() {
        /* ktlint-disable max-line-length */
        config.selectSql() shouldBeEqualTo
            """select "public"."newsletter_topic"."id", "public"."newsletter_topic_tr"."lang_code", "public"."newsletter_topic_tr"."title", "public"."newsletter_topic_tr"."version", "public"."newsletter_topic_tr"."created", "public"."newsletter_topic_tr"."last_modified" from "public"."newsletter_topic" join "public"."newsletter_topic_tr" on "public"."newsletter_topic"."id" = "public"."newsletter_topic_tr"."newsletter_topic_id""""
        /* ktlint-enable max-line-length */
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.shouldBeNull()
    }
}
