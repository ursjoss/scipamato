package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.core.db.public_.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.public_.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.public_.tables.NewsletterTopicTr.NEWSLETTER_TOPIC_TR;
import static ch.difty.scipamato.core.db.public_.tables.PaperNewsletter.PAPER_NEWSLETTER;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteWhereStep;
import org.jooq.TableField;
import org.jooq.conf.ParamType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.public_.tables.Newsletter;
import ch.difty.scipamato.core.db.public_.tables.records.NewsletterNewsletterTopicRecord;
import ch.difty.scipamato.core.db.public_.tables.records.NewsletterTopicTrRecord;
import ch.difty.scipamato.core.db.public_.tables.records.PaperNewsletterRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.publ.db.public_.tables.NewStudyTopic;
import ch.difty.scipamato.publ.db.public_.tables.NewsletterTopic;
import ch.difty.scipamato.publ.db.public_.tables.records.NewStudyTopicRecord;

/**
 * Defines the newStudyTopic synchronization job, applying two steps:
 * <ol>
 * <li>insertingOrUpdating: inserts new records or updates if already present</li>
 * <li>purging: removes records that have not been touched during the first
 * step (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@Configuration
@SuppressWarnings("SameParameterValue")
public class NewStudyTopicSyncConfig
    extends SyncConfig<PublicNewStudyTopic, ch.difty.scipamato.publ.db.public_.tables.records.NewStudyTopicRecord> {

    private static final String TOPIC      = "newStudyTopic";
    private static final int    CHUNK_SIZE = 100;

    // relevant fields of the core paperNewsletter as well as newsletter_topic and newsletter_topic_tr records
    private static final TableField<PaperNewsletterRecord, Integer>             PN_NEWSLETTER_ID        = PAPER_NEWSLETTER.NEWSLETTER_ID;
    private static final TableField<NewsletterTopicTrRecord, Integer>           NTT_NEWSLETTER_TOPIC_ID = NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID;
    private static final TableField<NewsletterTopicTrRecord, Integer>           NTT_VERSION             = NEWSLETTER_TOPIC_TR.VERSION;
    private static final TableField<NewsletterTopicTrRecord, Timestamp>         NTT_CREATED             = NEWSLETTER_TOPIC_TR.CREATED;
    private static final TableField<NewsletterTopicTrRecord, Timestamp>         NTT_LAST_MODIFIED       = NEWSLETTER_TOPIC_TR.LAST_MODIFIED;
    private static final TableField<NewsletterNewsletterTopicRecord, Integer>   NNT_SORT                = NEWSLETTER_NEWSLETTER_TOPIC.SORT;
    private static final TableField<NewsletterNewsletterTopicRecord, Timestamp> NNT_LAST_MODIFIED       = NEWSLETTER_NEWSLETTER_TOPIC.LAST_MODIFIED;

    protected NewStudyTopicSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncNewStudyTopicJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncNewStudyTopicJob";
    }

    @Override
    protected ItemWriter<PublicNewStudyTopic> publicWriter() {
        return new NewStudyTopicItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(PN_NEWSLETTER_ID, NTT_NEWSLETTER_TOPIC_ID, NTT_VERSION, NTT_CREATED, NTT_LAST_MODIFIED, NNT_SORT,
                NNT_LAST_MODIFIED)
            .from(PAPER_NEWSLETTER)
            .innerJoin(NEWSLETTER_TOPIC)
            .on(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC.ID))
            .innerJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .leftOuterJoin(NEWSLETTER_NEWSLETTER_TOPIC)
            .on(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID))
            .and(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID))
            .innerJoin(Newsletter.NEWSLETTER)
            .on(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(Newsletter.NEWSLETTER.ID))
            .where(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(PUBLICATION_STATUS_PUBLISHED))
            .getSQL(ParamType.INLINED);
    }

    @Override
    protected PublicNewStudyTopic makeEntity(final ResultSet rs) throws SQLException {
        return PublicNewStudyTopic
            .builder()
            .newsletterId(getInteger(PN_NEWSLETTER_ID, rs))
            .newsletterTopicId(getInteger(NTT_NEWSLETTER_TOPIC_ID, rs))
            .sort(getSortOrMaxIntFrom(rs))
            .version(getInteger(NTT_VERSION, rs))
            .created(getTimestamp(NTT_CREATED, rs))
            .lastModified(getLaterTimeStampFrom(NTT_LAST_MODIFIED, NNT_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    private int getSortOrMaxIntFrom(final ResultSet rs) throws SQLException {
        final Integer sort = getInteger(NNT_SORT, rs);
        return sort != null ? sort : Integer.MAX_VALUE;
    }

    private Timestamp getLaterTimeStampFrom(final TableField<NewsletterTopicTrRecord, Timestamp> nttLastModified,
        final TableField<NewsletterNewsletterTopicRecord, Timestamp> nntLastModified, final ResultSet rs)
        throws SQLException {
        final Timestamp ts1 = getTimestamp(nttLastModified, rs);
        final Timestamp ts2 = getTimestamp(nntLastModified, rs);
        if (ts2 != null) {
            return ts1.after(ts2) ? ts1 : ts2;
        } else {
            return ts1;
        }
    }

    @Override
    protected DeleteWhereStep<NewStudyTopicRecord> getDeleteWhereStep() {
        return getJooqPublic().delete(NewStudyTopic.NEW_STUDY_TOPIC);
    }

    @Override
    protected TableField<NewStudyTopicRecord, Timestamp> lastSynchedField() {
        return NewStudyTopic.NEW_STUDY_TOPIC.LAST_SYNCHED;
    }

    @Override
    public DeleteConditionStep<ch.difty.scipamato.publ.db.public_.tables.records.NewStudyTopicRecord> getPseudoFkDcs() {
        return getJooqPublic()
            .delete(NewStudyTopic.NEW_STUDY_TOPIC)
            .where(NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.notIn(getJooqPublic()
                .selectDistinct(NewsletterTopic.NEWSLETTER_TOPIC.ID)
                .from(NewsletterTopic.NEWSLETTER_TOPIC)));
    }

}
