package ch.difty.scipamato.core.sync.jobs.codeclass

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
import org.springframework.batch.core.repository.JobRepository
import org.springframework.transaction.PlatformTransactionManager
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

internal class CodeClassSyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobRepository = mockk<JobRepository>()
    private val transactionManager = mockk<PlatformTransactionManager>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = CodeClassSyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobRepository,
        transactionManager,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncCodeClassJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf CodeClassItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."code_class"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getInt("id") } returns 1
            every { getString("lang_code") } returns "mylangcode"
            every { getString("name") } returns "myname"
            every { getString("description") } returns "mydescription"
            every { getInt("version") } returns 2
            every { getTimestamp("created") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { getTimestamp("last_modified") } returns Timestamp.valueOf("2020-04-04 13:14:15")
            every { wasNull() } returns false
        }

        val codeClass = config.makeEntity(resultSet)

        codeClass.codeClassId shouldBeEqualTo 1
        codeClass.langCode shouldBeEqualTo "mylangcode"
        codeClass.name shouldBeEqualTo "myname"
        codeClass.description shouldBeEqualTo "mydescription"
        codeClass.version shouldBeEqualTo 2
        codeClass.created.toString() shouldBeEqualTo "2020-04-04 12:13:14.0"
        codeClass.lastModified.toString() shouldBeEqualTo "2020-04-04 13:14:15.0"
        codeClass.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Suppress("MaxLineLength")
    @Test
    fun selectSql() {
        /* ktlint-disable max-line-length */
        config.selectSql() shouldBeEqualTo
            """select "public"."code_class"."id", "public"."code_class_tr"."lang_code", "public"."code_class_tr"."name", "public"."code_class_tr"."description", "public"."code_class_tr"."version", "public"."code_class_tr"."created", "public"."code_class_tr"."last_modified" from "public"."code_class" join "public"."code_class_tr" on "public"."code_class"."id" = "public"."code_class_tr"."code_class_id""""
        /* ktlint-enable max-line-length */
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.shouldBeNull()
    }
}
