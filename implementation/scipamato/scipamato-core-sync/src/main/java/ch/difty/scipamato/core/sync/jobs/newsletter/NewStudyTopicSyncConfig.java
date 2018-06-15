package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.core.db.public_.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.public_.tables.NewsletterTopicTr.NEWSLETTER_TOPIC_TR;
import static ch.difty.scipamato.core.db.public_.tables.PaperNewsletter.PAPER_NEWSLETTER;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.public_.tables.records.NewsletterTopicTrRecord;
import ch.difty.scipamato.core.db.public_.tables.records.PaperNewsletterRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.publ.db.public_.tables.NewStudyTopic;

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
public class NewStudyTopicSyncConfig
    extends SyncConfig<PublicNewStudyTopic, ch.difty.scipamato.publ.db.public_.tables.records.NewStudyTopicRecord> {

    private static final String TOPIC      = "newStudyTopic";
    private static final int    CHUNK_SIZE = 50;

    // relevant fields of the core paperNewsletter as well as newsletter_topic and newsletter_topic_tr records
    private static final TableField<PaperNewsletterRecord, Integer>     PN_NEWSLETTER_ID        = PAPER_NEWSLETTER.NEWSLETTER_ID;
    private static final TableField<NewsletterTopicTrRecord, Integer>   NTT_NEWSLETTER_TOPIC_ID = NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID;
    private static final TableField<NewsletterTopicTrRecord, Integer>   NTT_VERSION             = NEWSLETTER_TOPIC_TR.VERSION;
    private static final TableField<NewsletterTopicTrRecord, Timestamp> NTT_CREATED             = NEWSLETTER_TOPIC_TR.CREATED;
    private static final TableField<NewsletterTopicTrRecord, Timestamp> NTT_LAST_MODIFIED       = NEWSLETTER_TOPIC_TR.LAST_MODIFIED;

    protected NewStudyTopicSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic,
        @Qualifier("dataSource") DataSource scipamatoCoreDataSource, JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, scipamatoCoreDataSource, jobBuilderFactory, stepBuilderFactory,
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
            .select(PN_NEWSLETTER_ID, NTT_NEWSLETTER_TOPIC_ID, NTT_VERSION, NTT_CREATED, NTT_LAST_MODIFIED)
            .from(PAPER_NEWSLETTER)
            .innerJoin(NEWSLETTER_TOPIC)
            .on(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC.ID))
            .innerJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .getSQL();
    }

    @Override
    protected PublicNewStudyTopic makeEntity(final ResultSet rs) throws SQLException {
        return PublicNewStudyTopic
            .builder()
            .newsletterId(getInteger(PN_NEWSLETTER_ID, rs))
            .newsletterTopicId(getInteger(NTT_NEWSLETTER_TOPIC_ID, rs))
            .sort(1) // TODO
            .version(getInteger(NTT_VERSION, rs))
            .created(getTimestamp(NTT_CREATED, rs))
            .lastModified(getTimestamp(NTT_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.publ.db.public_.tables.records.NewStudyTopicRecord> getPurgeDcs(
        final Timestamp cutOff) {
        return getJooqPublic()
            .delete(NewStudyTopic.NEW_STUDY_TOPIC)
            .where(NewStudyTopic.NEW_STUDY_TOPIC.LAST_SYNCHED.lessThan(cutOff));
    }

}
