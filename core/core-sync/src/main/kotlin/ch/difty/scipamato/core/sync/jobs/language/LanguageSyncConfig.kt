package ch.difty.scipamato.core.sync.jobs.language

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.Language
import ch.difty.scipamato.core.sync.PublicLanguage
import ch.difty.scipamato.core.sync.PublicLanguageTable
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicLanguage
import ch.difty.scipamato.publ.db.tables.records.LanguageRecord
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
 * Defines the language synchronization job.
 */
@Configuration
@Profile("!wickettest")
open class LanguageSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
) : SyncConfig<PublicLanguage, LanguageRecord>(
    TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobRepository, transactionManager,
    dateTimeService
) {
    @Bean
    open fun syncLanguageJob(): Job = createJob()

    override val jobName: String
        get() = "syncLanguageJob"

    override fun publicWriter(): ItemWriter<PublicLanguage> {
        return LanguageItemWriter(jooqPublic)
    }

    override fun selectSql(): String {
        return jooqCore
            .select(C_CODE, C_MAIN)
            .from(Language.LANGUAGE)
            .sql
    }

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicLanguage {
        val value = getBoolean(C_MAIN, rs)
        return newPublicLanguage(
            code = getString(C_CODE, rs),
            lastSynched = getNow(),
            mainLanguage = value,
        )
    }

    override fun lastSynchedField(): TableField<LanguageRecord, Timestamp> =
        PublicLanguageTable.LANGUAGE.LAST_SYNCHED

    companion object {
        private const val TOPIC = "language"
        private const val CHUNK_SIZE = 100

        // relevant fields of the core Language record
        private val C_CODE = Language.LANGUAGE.CODE
        private val C_MAIN = Language.LANGUAGE.MAIN_LANGUAGE
    }
}
