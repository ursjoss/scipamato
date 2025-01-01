package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.NewsletterTopic
import ch.difty.scipamato.core.db.tables.NewsletterTopicTr
import ch.difty.scipamato.core.sync.PublicNewsletterTopic
import ch.difty.scipamato.core.sync.PublicNewsletterTopicTable
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicNewsletterTopic
import ch.difty.scipamato.publ.db.tables.records.NewsletterTopicRecord
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
 * Defines the newsletter synchronization job, applying two steps:
 *
 *  1. insertingOrUpdating: inserts new records or updates if already present
 *  1. purging: removes records that have not been touched during the first step
 *     (within a defined grace time in minutes)
 */
@Configuration
@Profile("!wickettest")
open class NewsletterTopicSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
) : SyncConfig<PublicNewsletterTopic, NewsletterTopicRecord>(
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
    open fun syncNewsletterTopicJob(): Job = createJob()

    override val jobName: String
        get() = "syncNewsletterTopicJob"

    override fun publicWriter(): ItemWriter<PublicNewsletterTopic> = NewsletterTopicItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(C_ID, C_LANG_CODE, C_TITLE, C_VERSION, C_CREATED, C_LAST_MODIFIED)
            .from(NewsletterTopic.NEWSLETTER_TOPIC)
            .innerJoin(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
            .on(NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .sql

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicNewsletterTopic = newPublicNewsletterTopic(
        id = getInteger(C_ID, rs),
        langCode = getString(C_LANG_CODE, rs),
        title = getString(C_TITLE, rs),
        version = getInteger(C_VERSION, rs),
        created = getTimestamp(C_CREATED, rs),
        lastModified = getTimestamp(C_LAST_MODIFIED, rs),
        lastSynched = getNow(),
    )

    override fun lastSynchedField(): TableField<NewsletterTopicRecord, Timestamp> =
        PublicNewsletterTopicTable.NEWSLETTER_TOPIC.LAST_SYNCHED

    companion object {
        private const val TOPIC = "newsletterTopic"
        private const val CHUNK_SIZE = 50

        // relevant fields of the core newsletter record
        private val C_ID = NewsletterTopic.NEWSLETTER_TOPIC.ID
        private val C_LANG_CODE = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE
        private val C_TITLE = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE
        private val C_VERSION = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION
        private val C_CREATED = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED
        private val C_LAST_MODIFIED = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED
    }
}
