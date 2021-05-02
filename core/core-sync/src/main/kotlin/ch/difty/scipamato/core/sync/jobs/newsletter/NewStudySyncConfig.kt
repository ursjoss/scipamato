package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.Newsletter
import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.PaperNewsletter
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.sync.PublicNewStudy
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.publ.db.tables.NewStudy
import ch.difty.scipamato.publ.db.tables.NewsletterTopic
import ch.difty.scipamato.publ.db.tables.records.NewStudyRecord
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

/**
 * Defines the newStudy synchronization job, applying two steps:
 *
 *  1. insertingOrUpdating: inserts new records or updates if already present
 *  1. purging: removes records that have not been touched during the first step (within a defined grace time in minutes)
 */
@Configuration
@Profile("!wickettest")
open class NewStudySyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobBuilderFactory: JobBuilderFactory,
    stepBuilderFactory: StepBuilderFactory,
    dateTimeService: DateTimeService
) : SyncConfig<PublicNewStudy, NewStudyRecord>(
    TOPIC,
    CHUNK_SIZE,
    jooqCore,
    jooqPublic,
    coreDataSource,
    jobBuilderFactory,
    stepBuilderFactory,
    dateTimeService
) {

    @Bean
    open fun syncNewStudyJob(): Job = createJob()

    override val jobName: String
        get() = "syncNewStudyJob"

    override fun publicWriter(): ItemWriter<PublicNewStudy> = NewStudyItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(PN_NEWSLETTER_ID, PN_PAPER_ID, PN_NEWSLETTER_TOPIC_ID, P_YEAR, P_NUMBER, P_FIRST_AUTHOR, P_AUTHORS,
                PN_HEADLINE, P_GOALS, PN_VERSION, PN_CREATED, PN_LAST_MODIFIED)
            .from(PaperNewsletter.PAPER_NEWSLETTER)
            .innerJoin(Paper.PAPER)
            .on(PaperNewsletter.PAPER_NEWSLETTER.PAPER_ID.eq(Paper.PAPER.ID))
            .innerJoin(Newsletter.NEWSLETTER)
            .on(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.eq(Newsletter.NEWSLETTER.ID))
            .where(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(PUBLICATION_STATUS_PUBLISHED))
            .getSQL(ParamType.INLINED)

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicNewStudy =
        PublicNewStudy(
            newsletterId = getInteger(PN_NEWSLETTER_ID, rs),
            paperNumber = getLong(P_NUMBER, rs),
            newsletterTopicId = getInteger(PN_NEWSLETTER_TOPIC_ID, rs),
            sort = 1, // change this if the users will request to be able to sort the studies within the topic
            year = getInteger(P_YEAR, rs),
            authors = getAuthors(P_FIRST_AUTHOR, P_AUTHORS, rs),
            headline = getString(PN_HEADLINE, rs),
            description = getString(P_GOALS, rs),
            version = getInteger(PN_VERSION, rs),
            created = getTimestamp(PN_CREATED, rs),
            lastModified = getTimestamp(PN_LAST_MODIFIED, rs),
            lastSynched = getNow(),
        )

    @Suppress("SameParameterValue")
    @Throws(SQLException::class)
    private fun getAuthors(
        firstAuthor: TableField<PaperRecord, String?>,
        authors: TableField<PaperRecord, String?>,
        rs: ResultSet
    ): String {
        val firstAuthorString = getString(firstAuthor, rs)
        val authorString = getString(authors, rs)
        return if (authorString?.contains(",") == true) "$firstAuthorString et al." else firstAuthorString ?: "n.a"
    }

    override fun lastSynchedField(): TableField<NewStudyRecord, Timestamp> = NewStudy.NEW_STUDY.LAST_SYNCHED

    override val pseudoFkDcs: DeleteConditionStep<NewStudyRecord>?
        get() = jooqPublic
            .delete(NewStudy.NEW_STUDY)
            .where(NewStudy.NEW_STUDY.NEWSLETTER_TOPIC_ID.notIn(jooqPublic
                .selectDistinct(NewsletterTopic.NEWSLETTER_TOPIC.ID)
                .from(NewsletterTopic.NEWSLETTER_TOPIC)))

    companion object {
        private const val TOPIC = "newStudy"
        private const val CHUNK_SIZE = 100

        // relevant fields of the core paperNewsletter as well as paper record
        private val PN_PAPER_ID = PaperNewsletter.PAPER_NEWSLETTER.PAPER_ID
        private val PN_NEWSLETTER_ID = PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID
        private val PN_NEWSLETTER_TOPIC_ID = PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID
        private val PN_HEADLINE = PaperNewsletter.PAPER_NEWSLETTER.HEADLINE
        private val P_YEAR = Paper.PAPER.PUBLICATION_YEAR
        private val P_NUMBER = Paper.PAPER.NUMBER
        private val P_FIRST_AUTHOR = Paper.PAPER.FIRST_AUTHOR
        private val P_AUTHORS = Paper.PAPER.AUTHORS
        private val P_GOALS = Paper.PAPER.GOALS
        private val PN_VERSION = PaperNewsletter.PAPER_NEWSLETTER.VERSION
        private val PN_CREATED = PaperNewsletter.PAPER_NEWSLETTER.CREATED
        private val PN_LAST_MODIFIED = PaperNewsletter.PAPER_NEWSLETTER.LAST_MODIFIED
    }
}
