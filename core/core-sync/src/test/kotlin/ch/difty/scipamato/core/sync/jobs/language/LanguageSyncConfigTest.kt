package ch.difty.scipamato.core.sync.jobs.language

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

internal class LanguageSyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobBuilderFactory = mockk<JobBuilderFactory>()
    private val stepBuilderFactory = mockk<StepBuilderFactory>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = LanguageSyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobBuilderFactory,
        stepBuilderFactory,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncLanguageJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf LanguageItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."language"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getString("code") } returns "mycode"
            every { getBoolean("main_language") } returns true
            every { getTimestamp("last_synched") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { wasNull() } returns false
        }

        val language = config.makeEntity(resultSet)

        language.code shouldBeEqualTo "mycode"
        language.mainLanguage shouldBeEqualTo true
        language.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Test
    fun selectSql() {
        config.selectSql() shouldBeEqualTo
            """select "public"."language"."code", "public"."language"."main_language" from "public"."language""""
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.shouldBeNull()
    }
}
