package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.core.db.public_.tables.Paper.PAPER;
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
import ch.difty.scipamato.core.db.public_.tables.records.PaperNewsletterRecord;
import ch.difty.scipamato.core.db.public_.tables.records.PaperRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.publ.db.public_.tables.NewStudy;

/**
 * Defines the newStudy synchronization job, applying two steps:
 * <ol>
 * <li>insertingOrUpdating: inserts new records or updates if already present</li>
 * <li>purging: removes records that have not been touched during the first
 * step (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@Configuration
public class NewStudySyncConfig
    extends SyncConfig<PublicNewStudy, ch.difty.scipamato.publ.db.public_.tables.records.NewStudyRecord> {

    private static final String TOPIC      = "newStudy";
    private static final int    CHUNK_SIZE = 50;

    // relevant fields of the core paperNewsletter as well as paper record
    private static final TableField<PaperNewsletterRecord, Long>      PN_PAPER_ID            = PAPER_NEWSLETTER.PAPER_ID;
    private static final TableField<PaperNewsletterRecord, Integer>   PN_NEWSLETTER_ID       = PAPER_NEWSLETTER.NEWSLETTER_ID;
    private static final TableField<PaperNewsletterRecord, Integer>   PN_NEWSLETTER_TOPIC_ID = PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID;
    private static final TableField<PaperNewsletterRecord, String>    PN_HEADLINE            = PAPER_NEWSLETTER.HEADLINE;
    private static final TableField<PaperRecord, Integer>             P_YEAR                 = PAPER.PUBLICATION_YEAR;
    private static final TableField<PaperRecord, Long>                P_NUMBER               = PAPER.NUMBER;
    private static final TableField<PaperRecord, String>              P_FIRST_AUTHOR         = PAPER.FIRST_AUTHOR;
    private static final TableField<PaperRecord, String>              P_AUTHORS              = PAPER.AUTHORS;
    private static final TableField<PaperRecord, String>              P_GOALS                = PAPER.GOALS;
    private static final TableField<PaperNewsletterRecord, Integer>   PN_VERSION             = PAPER_NEWSLETTER.VERSION;
    private static final TableField<PaperNewsletterRecord, Timestamp> PN_CREATED             = PAPER_NEWSLETTER.CREATED;
    private static final TableField<PaperNewsletterRecord, Timestamp> PN_LAST_MODIFIED       = PAPER_NEWSLETTER.LAST_MODIFIED;

    protected NewStudySyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncNewStudyJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncNewStudyJob";
    }

    @Override
    protected ItemWriter<PublicNewStudy> publicWriter() {
        return new NewStudyItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(PN_NEWSLETTER_ID, PN_PAPER_ID, PN_NEWSLETTER_TOPIC_ID, P_YEAR, P_NUMBER, P_FIRST_AUTHOR, P_AUTHORS,
                PN_HEADLINE, P_GOALS, PN_VERSION, PN_CREATED, PN_LAST_MODIFIED)
            .from(PAPER_NEWSLETTER)
            .innerJoin(PAPER)
            .on(PAPER_NEWSLETTER.PAPER_ID.eq(PAPER.ID))
            .getSQL();
    }

    @Override
    protected PublicNewStudy makeEntity(final ResultSet rs) throws SQLException {
        return PublicNewStudy
            .builder()
            .newsletterId(getInteger(PN_NEWSLETTER_ID, rs))
            .newsletterTopicId(getInteger(PN_NEWSLETTER_TOPIC_ID, rs))
            .sort(1) // TODO
            .paperNumber(getLong(P_NUMBER, rs))
            .year(getInteger(P_YEAR, rs))
            .authors(getAuthors(P_FIRST_AUTHOR, P_AUTHORS, rs))
            .headline(getString(PN_HEADLINE, rs))
            .description(getString(P_GOALS, rs))
            .version(getInteger(PN_VERSION, rs))
            .created(getTimestamp(PN_CREATED, rs))
            .lastModified(getTimestamp(PN_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    private String getAuthors(final TableField<PaperRecord, String> firstAuthor,
        final TableField<PaperRecord, String> authors, final ResultSet rs) throws SQLException {
        final String firstAuthorString = getString(firstAuthor, rs);
        final String authorString = getString(authors, rs);
        if (authorString.contains(","))
            return firstAuthorString + " et al.";
        else
            return firstAuthorString;
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.publ.db.public_.tables.records.NewStudyRecord> getPurgeDcs(
        final Timestamp cutOff) {
        return getJooqPublic()
            .delete(NewStudy.NEW_STUDY)
            .where(NewStudy.NEW_STUDY.LAST_SYNCHED.lessThan(cutOff));
    }

}
