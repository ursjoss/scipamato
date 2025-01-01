package ch.difty.scipamato.core.sync.jobs.keyword

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.Keyword
import ch.difty.scipamato.core.db.tables.KeywordTr
import ch.difty.scipamato.core.sync.PublicKeyword
import ch.difty.scipamato.core.sync.PublicKeywordTable
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicKeyword
import ch.difty.scipamato.publ.db.tables.records.KeywordRecord
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
 * Defines the code synchronization job, applying two steps:
 *
 *  1. keywordInsertingOrUpdating: inserts new records or updates if already present
 *  1. keywordPurging: removes records that have not been touched by the first step
 *     (within a defined grace time in minutes)
 */
@Configuration
@Profile("!wickettest")
open class KeywordSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
) : SyncConfig<PublicKeyword, KeywordRecord>(
    TOPIC,
    CHUNK_SIZE,
    jooqCore,
    jooqPublic,
    coreDataSource,
    jobRepository,
    transactionManager,
    dateTimeService
) {
    @Bean
    open fun syncKeywordJob(): Job = createJob()

    override val jobName: String
        get() = "syncKeywordJob"

    override fun publicWriter(): ItemWriter<PublicKeyword> = KeywordItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(KT_ID, KW_ID.`as`(ALIAS_KEYWORD_ID), KT_LANG_CODE, KT_NAME, KT_VERSION, KT_CREATED, KT_LAST_MODIFIED, KW_SEARCH_OVERRIDE)
            .from(Keyword.KEYWORD)
            .innerJoin(KeywordTr.KEYWORD_TR)
            .on(KW_ID.eq(KeywordTr.KEYWORD_TR.KEYWORD_ID))
            .sql

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicKeyword = newPublicKeyword(
        id = getInteger(KT_ID, rs) ?: -1,
        keywordId = rs.getInt(ALIAS_KEYWORD_ID),
        langCode = getString(KT_LANG_CODE, rs),
        name = getString(KT_NAME, rs),
        version = getInteger(KT_VERSION, rs),
        created = getTimestamp(KT_CREATED, rs),
        lastModified = getTimestamp(KT_LAST_MODIFIED, rs),
        lastSynched = getNow(),
        searchOverride = getString(KW_SEARCH_OVERRIDE, rs),
    )

    override fun lastSynchedField(): TableField<KeywordRecord, Timestamp> = PublicKeywordTable.KEYWORD.LAST_SYNCHED

    companion object {
        private const val TOPIC = "keyword"
        private const val CHUNK_SIZE = 100

        // relevant fields of the core Keyword class record
        private val KT_ID = KeywordTr.KEYWORD_TR.ID
        private val KW_ID = Keyword.KEYWORD.ID
        private const val ALIAS_KEYWORD_ID = "KeywordId"
        private val KW_SEARCH_OVERRIDE = Keyword.KEYWORD.SEARCH_OVERRIDE
        private val KT_LANG_CODE = KeywordTr.KEYWORD_TR.LANG_CODE
        private val KT_NAME = KeywordTr.KEYWORD_TR.NAME
        private val KT_VERSION = KeywordTr.KEYWORD_TR.VERSION
        private val KT_CREATED = KeywordTr.KEYWORD_TR.CREATED
        private val KT_LAST_MODIFIED = KeywordTr.KEYWORD_TR.LAST_MODIFIED
    }
}
