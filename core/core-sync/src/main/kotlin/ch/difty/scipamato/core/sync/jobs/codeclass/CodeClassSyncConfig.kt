package ch.difty.scipamato.core.sync.jobs.codeclass

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.CodeClass
import ch.difty.scipamato.core.db.tables.CodeClassTr
import ch.difty.scipamato.core.sync.PublicCodeClass
import ch.difty.scipamato.core.sync.PublicCodeClassTable
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicCodeClass
import ch.difty.scipamato.publ.db.tables.records.CodeClassRecord
import org.jooq.DSLContext
import org.jooq.TableField
import org.springframework.batch.core.Job
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.PlatformTransactionManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import javax.sql.DataSource

/**
 * Defines the code class synchronization job, applying two steps:
 *
 *  1. insertingOrUpdating: inserts new records or updates if already present
 *  1. purging: removes records that have not been touched by the first step
 *     (within a defined grace time in minutes)
 */
@Configuration
@Profile("!wickettest")
open class CodeClassSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
) : SyncConfig<PublicCodeClass, CodeClassRecord>(
    TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobRepository, transactionManager,
    dateTimeService
) {
    @Bean
    open fun syncCodeClassJob(): Job = createJob()

    override val jobName: String
        get() = "syncCodeClassJob"

    override fun publicWriter(): ItemWriter<PublicCodeClass> = CodeClassItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(C_ID, C_LANG_CODE, C_NAME, C_DESCRIPTION, C_VERSION, C_CREATED, C_LAST_MODIFIED)
            .from(CodeClass.CODE_CLASS)
            .innerJoin(CodeClassTr.CODE_CLASS_TR)
            .on(C_ID.eq(CodeClassTr.CODE_CLASS_TR.CODE_CLASS_ID))
            .sql

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicCodeClass = newPublicCodeClass(
        codeClassId = getInteger(C_ID, rs),
        langCode = getString(C_LANG_CODE, rs),
        name = getString(C_NAME, rs),
        description = getString(C_DESCRIPTION, rs),
        version = getInteger(C_VERSION, rs),
        created = getTimestamp(C_CREATED, rs),
        lastModified = getTimestamp(C_LAST_MODIFIED, rs),
        lastSynched = getNow(),
    )

    override fun lastSynchedField(): TableField<CodeClassRecord, Timestamp> =
        PublicCodeClassTable.CODE_CLASS.LAST_SYNCHED

    companion object {
        private const val TOPIC = "codeClass"
        private const val CHUNK_SIZE = 50

        // relevant fields of the core Code class record
        private val C_ID = CodeClass.CODE_CLASS.ID
        private val C_LANG_CODE = CodeClassTr.CODE_CLASS_TR.LANG_CODE
        private val C_NAME = CodeClassTr.CODE_CLASS_TR.NAME
        private val C_DESCRIPTION = CodeClassTr.CODE_CLASS_TR.DESCRIPTION
        private val C_VERSION = CodeClassTr.CODE_CLASS_TR.VERSION
        private val C_CREATED = CodeClassTr.CODE_CLASS_TR.CREATED
        private val C_LAST_MODIFIED = CodeClassTr.CODE_CLASS_TR.LAST_MODIFIED
    }
}
