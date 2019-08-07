package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.core.db.public_.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.public_.tables.NewsletterTopicTr.NEWSLETTER_TOPIC_TR;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.public_.tables.records.NewsletterTopicRecord;
import ch.difty.scipamato.core.db.public_.tables.records.NewsletterTopicTrRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;

/**
 * Defines the newsletter synchronization job, applying two steps:
 * <ol>
 * <li>insertingOrUpdating: inserts new records or updates if already present</li>
 * <li>purging: removes records that have not been touched during the first
 * step (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@Configuration
@Profile("!wickettest")
public class NewsletterTopicSyncConfig
    extends SyncConfig<PublicNewsletterTopic, ch.difty.scipamato.publ.db.public_.tables.records.NewsletterTopicRecord> {

    private static final String TOPIC      = "newsletterTopic";
    private static final int    CHUNK_SIZE = 50;

    // relevant fields of the core newsletter record
    private static final TableField<NewsletterTopicRecord, Integer>     C_ID            = NEWSLETTER_TOPIC.ID;
    private static final TableField<NewsletterTopicTrRecord, String>    C_LANG_CODE     = NEWSLETTER_TOPIC_TR.LANG_CODE;
    private static final TableField<NewsletterTopicTrRecord, String>    C_TITLE         = NEWSLETTER_TOPIC_TR.TITLE;
    private static final TableField<NewsletterTopicTrRecord, Integer>   C_VERSION       = NEWSLETTER_TOPIC_TR.VERSION;
    private static final TableField<NewsletterTopicTrRecord, Timestamp> C_CREATED       = NEWSLETTER_TOPIC_TR.CREATED;
    private static final TableField<NewsletterTopicTrRecord, Timestamp> C_LAST_MODIFIED = NEWSLETTER_TOPIC_TR.LAST_MODIFIED;

    protected NewsletterTopicSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncNewsletterTopicJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncNewsletterTopicJob";
    }

    @Override
    protected ItemWriter<PublicNewsletterTopic> publicWriter() {
        return new NewsletterTopicItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(C_ID, C_LANG_CODE, C_TITLE, C_VERSION, C_CREATED, C_LAST_MODIFIED)
            .from(NEWSLETTER_TOPIC)
            .innerJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .getSQL();
    }

    @Override
    protected PublicNewsletterTopic makeEntity(final ResultSet rs) throws SQLException {
        return PublicNewsletterTopic
            .builder()
            .id(getInteger(C_ID, rs))
            .langCode(getString(C_LANG_CODE, rs))
            .title(getString(C_TITLE, rs))
            .version(getInteger(C_VERSION, rs))
            .created(getTimestamp(C_CREATED, rs))
            .lastModified(getTimestamp(C_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    @Override
    protected TableField<ch.difty.scipamato.publ.db.public_.tables.records.NewsletterTopicRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.public_.tables.NewsletterTopic.NEWSLETTER_TOPIC.LAST_SYNCHED;
    }

}
