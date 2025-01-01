package ch.difty.scipamato.core.sync.jobs.code

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.Code
import ch.difty.scipamato.core.db.tables.CodeTr
import ch.difty.scipamato.core.sync.PublicCode
import ch.difty.scipamato.core.sync.PublicCodeTable
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicCode
import ch.difty.scipamato.publ.db.tables.CodeClass
import ch.difty.scipamato.publ.db.tables.records.CodeRecord
import org.jooq.DSLContext
import org.jooq.DeleteConditionStep
import org.jooq.TableField
import org.jooq.conf.ParamType
import org.jooq.impl.DSL
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
 * Defines the code synchronization job, applying two steps:
 *
 *  1. codeInsertingOrUpdating: inserts new records or updates if already present
 *  1. codePurging: removes records that have not been touched by the first step
 *     (within a defined grace time in minutes)
 */
@Configuration
@Profile("!wickettest")
open class CodeSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
) : SyncConfig<PublicCode, CodeRecord>(
    topic = TOPIC,
    chunkSize = CHUNK_SIZE,
    jooqCore = jooqCore,
    jooqPublic = jooqPublic,
    coreDataSource = coreDataSource,
    jobRepository = jobRepository,
    transactionManager = transactionManager,
    dateTimeService = dateTimeService) {

    @Bean
    open fun syncCodeJob(): Job = createJob()

    override val jobName: String
        get() = "syncCodeJob"

    override fun publicWriter(): ItemWriter<PublicCode> = CodeItemWriter(jooqPublic)

    @Suppress("MagicNumber")
    /**
     * HARDCODED consider moving the aggregated code 5abc into some table in
     * scipamato-core. See also HidingInternalsCodeAggregator#filterAndEnrich
     */
    override fun selectSql(): String {
        val now = getNow()
        val comm = "aggregated codes"
        val aggDe = DSL.row(
            "5abc", "de", 5, "Experimentelle Studie", comm, 1, 1, now, now)
        val aggEn = DSL.row(
            "5abc", "en", 5, "Experimental study", comm, 1, 1, now, now)
        val aggFr = DSL.row(
            "5abc", "fr", 5, "Etude expérimentale", comm, 1, 1, now, now)
        return jooqCore
            .select(C_CODE, C_LANG_CODE, C_CODE_CLASS_ID, C_NAME, C_COMMENT, C_SORT, C_VERSION, C_CREATED,
                C_LAST_MODIFIED)
            .from(Code.CODE)
            .innerJoin(CodeTr.CODE_TR)
            .on(C_CODE.eq(CodeTr.CODE_TR.CODE))
            .where(Code.CODE.INTERNAL.isFalse)
            .unionAll(DSL.selectFrom(DSL.values(aggDe, aggEn, aggFr)))
            .getSQL(ParamType.INLINED)
    }

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicCode = newPublicCode(
        code = getString(C_CODE, rs),
        langCode = getString(C_LANG_CODE, rs),
        codeClassId = getInteger(C_CODE_CLASS_ID, rs),
        name = getString(C_NAME, rs),
        comment = getString(C_COMMENT, rs),
        sort = getInteger(C_SORT, rs),
        version = getInteger(C_VERSION, rs),
        created = getTimestamp(C_CREATED, rs),
        lastModified = getTimestamp(C_LAST_MODIFIED, rs),
        lastSynched = getNow(),
    )

    override fun lastSynchedField(): TableField<CodeRecord, Timestamp> =
        PublicCodeTable.CODE.LAST_SYNCHED

    override val pseudoFkDcs: DeleteConditionStep<CodeRecord>?
        get() = jooqPublic
            .delete(PublicCodeTable.CODE)
            .where(PublicCodeTable.CODE.CODE_CLASS_ID.notIn(jooqPublic
                .selectDistinct(CodeClass.CODE_CLASS.CODE_CLASS_ID)
                .from(CodeClass.CODE_CLASS)))

    companion object {
        private const val TOPIC = "code"
        private const val CHUNK_SIZE = 100

        // relevant fields of the core Code class record
        private val C_CODE = Code.CODE.CODE_
        private val C_LANG_CODE = CodeTr.CODE_TR.LANG_CODE
        private val C_CODE_CLASS_ID = Code.CODE.CODE_CLASS_ID
        private val C_NAME = CodeTr.CODE_TR.NAME
        private val C_COMMENT = CodeTr.CODE_TR.COMMENT
        private val C_SORT = Code.CODE.SORT
        private val C_VERSION = CodeTr.CODE_TR.VERSION
        private val C_CREATED = CodeTr.CODE_TR.CREATED
        private val C_LAST_MODIFIED = CodeTr.CODE_TR.LAST_MODIFIED
    }
}
