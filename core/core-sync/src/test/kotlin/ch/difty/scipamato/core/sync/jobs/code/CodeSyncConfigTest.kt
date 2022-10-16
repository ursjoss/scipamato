package ch.difty.scipamato.core.sync.jobs.code

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

internal class CodeSyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobBuilderFactory = mockk<JobBuilderFactory>()
    private val stepBuilderFactory = mockk<StepBuilderFactory>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = CodeSyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobBuilderFactory,
        stepBuilderFactory,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncCodeJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf CodeItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."code"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getString("code") } returns "mycode"
            every { getString("lang_code") } returns "mylangcode"
            every { getInt("code_class_id") } returns 1
            every { getString("name") } returns "myname"
            every { getString("comment") } returns "comment"
            every { getInt("sort") } returns 2
            every { getInt("version") } returns 3
            every { getTimestamp("created") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { getTimestamp("last_modified") } returns Timestamp.valueOf("2020-04-04 13:14:15")
            every { wasNull() } returns false
        }

        val code = config.makeEntity(resultSet)

        code.code shouldBeEqualTo "mycode"
        code.langCode shouldBeEqualTo "mylangcode"
        code.codeClassId shouldBeEqualTo 1
        code.name shouldBeEqualTo "myname"
        code.comment shouldBeEqualTo "comment"
        code.sort shouldBeEqualTo 2
        code.version shouldBeEqualTo 3
        code.created.toString() shouldBeEqualTo "2020-04-04 12:13:14.0"
        code.lastModified.toString() shouldBeEqualTo "2020-04-04 13:14:15.0"
        code.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Suppress("MaxLineLength")
    @Test
    fun selectSql() {
        /* ktlint-disable max-line-length */
        config.selectSql() shouldBeEqualTo
            """select "public"."code"."code", "public"."code_tr"."lang_code", "public"."code"."code_class_id", "public"."code_tr"."name", "public"."code_tr"."comment", "public"."code"."sort", "public"."code_tr"."version", "public"."code_tr"."created", "public"."code_tr"."last_modified" from "public"."code" join "public"."code_tr" on "public"."code"."code" = "public"."code_tr"."code" where "public"."code"."internal" = false union all select "v"."c1", "v"."c2", "v"."c3", "v"."c4", "v"."c5", "v"."c6", "v"."c7", "v"."c8", "v"."c9" from (values ('5abc', 'de', 5, 'Experimentelle Studie', 'aggregated codes', 1, 1, timestamp '2016-12-09 06:02:13.0', timestamp '2016-12-09 06:02:13.0'), ('5abc', 'en', 5, 'Experimental study', 'aggregated codes', 1, 1, timestamp '2016-12-09 06:02:13.0', timestamp '2016-12-09 06:02:13.0'), ('5abc', 'fr', 5, 'Etude expérimentale', 'aggregated codes', 1, 1, timestamp '2016-12-09 06:02:13.0', timestamp '2016-12-09 06:02:13.0')) as "v" ("c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9")"""
        /* ktlint-enable max-line-length */
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.toString() shouldBeEqualTo
            """delete from "public"."code"
                |where "public"."code"."code_class_id" not in (
                |  select distinct "public"."code_class"."code_class_id"
                |  from "public"."code_class"
                |)""".trimMargin()
    }
}
