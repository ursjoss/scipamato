package ch.difty.scipamato.core.sync.jobs.keyword

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

internal class KeywordSyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobBuilderFactory = mockk<JobBuilderFactory>()
    private val stepBuilderFactory = mockk<StepBuilderFactory>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = KeywordSyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobBuilderFactory,
        stepBuilderFactory,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncKeywordJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf KeywordItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."keyword"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getInt("id") } returns 1
            every { getInt("KeywordId") } returns 2
            every { getString("lang_code") } returns "mylangcode"
            every { getString("name") } returns "myname"
            every { getInt("version") } returns 3
            every { getTimestamp("created") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { getTimestamp("last_modified") } returns Timestamp.valueOf("2020-04-04 13:14:15")
            every { getString("search_override") } returns "mysearchoverride"
            every { wasNull() } returns false
        }

        val keyword = config.makeEntity(resultSet)

        keyword.id shouldBeEqualTo 1
        keyword.keywordId shouldBeEqualTo 2
        keyword.langCode shouldBeEqualTo "mylangcode"
        keyword.name shouldBeEqualTo "myname"
        keyword.version shouldBeEqualTo 3
        keyword.created.toString() shouldBeEqualTo "2020-04-04 12:13:14.0"
        keyword.searchOverride shouldBeEqualTo "mysearchoverride"
        keyword.lastModified.toString() shouldBeEqualTo "2020-04-04 13:14:15.0"
        keyword.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Test
    fun selectSql() {
        /* ktlint-disable max-line-length */
        config.selectSql() shouldBeEqualTo
            """select "public"."keyword_tr"."id", "public"."keyword"."id" as "KeywordId", "public"."keyword_tr"."lang_code", "public"."keyword_tr"."name", "public"."keyword_tr"."version", "public"."keyword_tr"."created", "public"."keyword_tr"."last_modified", "public"."keyword"."search_override" from "public"."keyword" join "public"."keyword_tr" on "public"."keyword"."id" = "public"."keyword_tr"."keyword_id""""
        /* ktlint-enable max-line-length */
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.shouldBeNull()
    }
}
