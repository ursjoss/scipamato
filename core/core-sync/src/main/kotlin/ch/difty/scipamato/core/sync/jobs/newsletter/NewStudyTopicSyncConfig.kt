package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.Newsletter
import ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic
import ch.difty.scipamato.core.db.tables.NewsletterTopicTr
import ch.difty.scipamato.core.db.tables.PaperNewsletter
import ch.difty.scipamato.core.sync.PublicNewStudyTopic
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.publ.db.tables.NewStudyTopic
import ch.difty.scipamato.publ.db.tables.NewsletterTopic
import ch.difty.scipamato.publ.db.tables.records.NewStudyTopicRecord
import org.jooq.DSLContext
import org.jooq.DeleteConditionStep
import org.jooq.TableField
import org.jooq.conf.ParamType
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import javax.sql.DataSource

private const val NTTLM = "NTTLM"
private const val NNTLM = "NNTLM"

/**
 * Defines the newStudyTopic synchronization job, applying two steps:
 *
 *  1. insertingOrUpdating: inserts new records or updates if already present
 *  1. purging: removes records that have not been touched during the first step (within a defined grace time in minutes)
 */
@Configuration
@Profile("!wickettest")
open class NewStudyTopicSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobBuilderFactory: JobBuilderFactory,
    stepBuilderFactory: StepBuilderFactory,
    dateTimeService: DateTimeService
) : SyncConfig<PublicNewStudyTopic, NewStudyTopicRecord>(TOPIC,
    CHUNK_SIZE,
    jooqCore,
    jooqPublic,
    coreDataSource,
    jobBuilderFactory,
    stepBuilderFactory,
    dateTimeService
) {

    @Bean
    open fun syncNewStudyTopicJob(): Job = createJob()

    override val jobName: String
        get() = "syncNewStudyTopicJob"

    override fun publicWriter(): ItemWriter<PublicNewStudyTopic> = NewStudyTopicItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(PN_NEWSLETTER_ID, NTT_NEWSLETTER_TOPIC_ID, NTT_VERSION, NTT_CREATED, NTT_LAST_MODIFIED.`as`(NTTLM),
                NNT_SORT, NNT_LAST_MODIFIED.`as`(NNTLM))
            .from(PaperNewsletter.PAPER_NEWSLETTER)
            .innerJoin(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
            .on(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID))
            .innerJoin(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
            .on(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .leftOuterJoin(NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC)
            .on(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.eq(NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID))
            .and(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID))
            .innerJoin(Newsletter.NEWSLETTER)
            .on(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.eq(Newsletter.NEWSLETTER.ID))
            .where(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(PUBLICATION_STATUS_PUBLISHED))
            .getSQL(ParamType.INLINED)

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicNewStudyTopic =
        PublicNewStudyTopic(
            newsletterId = getInteger(PN_NEWSLETTER_ID, rs),
            newsletterTopicId = getInteger(NTT_NEWSLETTER_TOPIC_ID, rs),
            sort = getSortOrMaxIntFrom(rs),
            version = getInteger(NTT_VERSION, rs),
            created = getTimestamp(NTT_CREATED, rs),
            lastModified = getLaterTimeStampFrom(NTTLM, NNTLM, rs),
            lastSynched = getNow(),
        )

    @Throws(SQLException::class)
    private fun getSortOrMaxIntFrom(rs: ResultSet): Int = getInteger(NNT_SORT, rs) ?: Int.MAX_VALUE

    @Suppress("SameParameterValue")
    @Throws(SQLException::class)
    private fun getLaterTimeStampFrom(
        nttLastModified: String,
        nntLastModified: String,
        rs: ResultSet
    ): Timestamp? {
        val ts1 = getTimestamp(nttLastModified, rs)
        val ts2 = getTimestamp(nntLastModified, rs)
        if (ts1 == null || ts2 == null) return ts1 ?: ts2
        return if (ts1.after(ts2)) ts1 else ts2
    }

    override fun lastSynchedField(): TableField<NewStudyTopicRecord, Timestamp> = NewStudyTopic.NEW_STUDY_TOPIC.LAST_SYNCHED

    override val pseudoFkDcs: DeleteConditionStep<NewStudyTopicRecord>?
        get() = jooqPublic
            .delete(NewStudyTopic.NEW_STUDY_TOPIC)
            .where(NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.notIn(jooqPublic
                .selectDistinct(NewsletterTopic.NEWSLETTER_TOPIC.ID)
                .from(NewsletterTopic.NEWSLETTER_TOPIC)))

    companion object {
        private const val TOPIC = "newStudyTopic"
        private const val CHUNK_SIZE = 100

        // relevant fields of the core paperNewsletter as well as newsletter_topic and newsletter_topic_tr records
        private val PN_NEWSLETTER_ID = PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID
        private val NTT_NEWSLETTER_TOPIC_ID = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID
        private val NTT_VERSION = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION
        private val NTT_CREATED = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED
        private val NTT_LAST_MODIFIED = NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED
        private val NNT_SORT = NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT
        private val NNT_LAST_MODIFIED = NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.LAST_MODIFIED
    }
}
