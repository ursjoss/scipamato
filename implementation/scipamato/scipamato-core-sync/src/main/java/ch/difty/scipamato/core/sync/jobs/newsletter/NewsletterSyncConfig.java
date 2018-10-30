package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.core.db.public_.tables.Newsletter.NEWSLETTER;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DSLContext;
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
import ch.difty.scipamato.core.db.public_.tables.records.NewsletterRecord;
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
public class NewsletterSyncConfig
    extends SyncConfig<PublicNewsletter, ch.difty.scipamato.publ.db.public_.tables.records.NewsletterRecord> {

    private static final String TOPIC      = "newsletter";
    private static final int    CHUNK_SIZE = 50;

    // relevant fields of the core newsletter record
    private static final TableField<NewsletterRecord, Integer>   N_ID            = NEWSLETTER.ID;
    private static final TableField<NewsletterRecord, String>    N_ISSUE         = NEWSLETTER.ISSUE;
    private static final TableField<NewsletterRecord, Date>      N_ISSUE_DATE    = NEWSLETTER.ISSUE_DATE;
    private static final TableField<NewsletterRecord, Integer>   N_VERSION       = NEWSLETTER.VERSION;
    private static final TableField<NewsletterRecord, Timestamp> N_CREATED       = NEWSLETTER.CREATED;
    private static final TableField<NewsletterRecord, Timestamp> N_LAST_MODIFIED = NEWSLETTER.LAST_MODIFIED;

    protected NewsletterSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncNewsletterJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncNewsletterJob";
    }

    @Override
    protected ItemWriter<PublicNewsletter> publicWriter() {
        return new NewsletterItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(N_ID, N_ISSUE, N_ISSUE_DATE, N_VERSION, N_CREATED, N_LAST_MODIFIED)
            .from(Newsletter.NEWSLETTER)
            .where(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(PUBLICATION_STATUS_PUBLISHED))
            .getSQL(ParamType.INLINED);
    }

    @Override
    protected PublicNewsletter makeEntity(final ResultSet rs) throws SQLException {
        return PublicNewsletter
            .builder()
            .id(getInteger(N_ID, rs))
            .issue(getString(N_ISSUE, rs))
            .issueDate(getDate(N_ISSUE_DATE, rs))
            .version(getInteger(N_VERSION, rs))
            .created(getTimestamp(N_CREATED, rs))
            .lastModified(getTimestamp(N_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    @Override
    protected TableField<ch.difty.scipamato.publ.db.public_.tables.records.NewsletterRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.public_.tables.Newsletter.NEWSLETTER.LAST_SYNCHED;
    }

}
