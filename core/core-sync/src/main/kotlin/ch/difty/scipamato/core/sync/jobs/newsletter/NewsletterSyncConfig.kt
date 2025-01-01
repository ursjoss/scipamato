package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.Newsletter
import ch.difty.scipamato.core.sync.PublicNewsletter
import ch.difty.scipamato.core.sync.PublicNewsletterTable
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicNewsletter
import ch.difty.scipamato.publ.db.tables.records.NewsletterRecord
import org.jooq.DSLContext
import org.jooq.TableField
import org.jooq.conf.ParamType
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
 * Defines the newsletter synchronization job, applying two steps:
 *
 *  1. insertingOrUpdating: inserts new records or updates if already present
 *  1. purging: removes records that have not been touched during the first step
 *     (within a defined grace time in minutes)
 */
@Configuration
@Profile("!wickettest")
open class NewsletterSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
) : SyncConfig<PublicNewsletter, NewsletterRecord>(
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
    open fun syncNewsletterJob(): Job = createJob()

    override val jobName: String
        get() = "syncNewsletterJob"

    override fun publicWriter(): ItemWriter<PublicNewsletter> = NewsletterItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(N_ID, N_ISSUE, N_ISSUE_DATE, N_VERSION, N_CREATED, N_LAST_MODIFIED)
            .from(Newsletter.NEWSLETTER)
            .where(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(PUBLICATION_STATUS_PUBLISHED))
            .getSQL(ParamType.INLINED)

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicNewsletter = newPublicNewsletter(
        id = getInteger(N_ID, rs),
        issue = getString(N_ISSUE, rs),
        issueDate = getDate(N_ISSUE_DATE, rs),
        version = getInteger(N_VERSION, rs),
        created = getTimestamp(N_CREATED, rs),
        lastModified = getTimestamp(N_LAST_MODIFIED, rs),
        lastSynched = getNow(),
    )


    override fun lastSynchedField(): TableField<NewsletterRecord, Timestamp> =
        PublicNewsletterTable.NEWSLETTER.LAST_SYNCHED

    companion object {
        private const val TOPIC = "newsletter"
        private const val CHUNK_SIZE = 50

        // relevant fields of the core newsletter record
        private val N_ID = Newsletter.NEWSLETTER.ID
        private val N_ISSUE = Newsletter.NEWSLETTER.ISSUE
        private val N_ISSUE_DATE = Newsletter.NEWSLETTER.ISSUE_DATE
        private val N_VERSION = Newsletter.NEWSLETTER.VERSION
        private val N_CREATED = Newsletter.NEWSLETTER.CREATED
        private val N_LAST_MODIFIED = Newsletter.NEWSLETTER.LAST_MODIFIED
    }
}
