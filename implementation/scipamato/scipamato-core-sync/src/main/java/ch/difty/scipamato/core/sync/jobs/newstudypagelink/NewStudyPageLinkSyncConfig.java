package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

import static ch.difty.scipamato.core.db.public_.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK;

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
import ch.difty.scipamato.core.sync.jobs.SyncConfig;

/**
 * Defines the newStudyPageLink synchronization job.
 *
 * @author u.joss
 */
@Configuration
public class NewStudyPageLinkSyncConfig extends
    SyncConfig<PublicNewStudyPageLink, ch.difty.scipamato.publ.db.public_.tables.records.NewStudyPageLinkRecord> {

    private static final String TOPIC      = "newStudyPageLink";
    private static final int    CHUNK_SIZE = 50;

    // relevant fields of the core Language record
    private static final TableField<ch.difty.scipamato.core.db.public_.tables.records.NewStudyPageLinkRecord, String>  C_LANG_CODE = NEW_STUDY_PAGE_LINK.LANG_CODE;
    private static final TableField<ch.difty.scipamato.core.db.public_.tables.records.NewStudyPageLinkRecord, Integer> C_SORT      = NEW_STUDY_PAGE_LINK.SORT;
    private static final TableField<ch.difty.scipamato.core.db.public_.tables.records.NewStudyPageLinkRecord, String>  C_TITLE     = NEW_STUDY_PAGE_LINK.TITLE;
    private static final TableField<ch.difty.scipamato.core.db.public_.tables.records.NewStudyPageLinkRecord, String>  C_URL       = NEW_STUDY_PAGE_LINK.URL;

    protected NewStudyPageLinkSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncNewStudyPageLinkJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncNewStudyPageLinkJob";
    }

    @Override
    protected ItemWriter<PublicNewStudyPageLink> publicWriter() {
        return new NewStudyPageLinkItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(C_LANG_CODE, C_SORT, C_TITLE, C_URL)
            .from(NEW_STUDY_PAGE_LINK)
            .getSQL();
    }

    @Override
    protected PublicNewStudyPageLink makeEntity(final ResultSet rs) throws SQLException {
        return PublicNewStudyPageLink
            .builder()
            .langCode(getString(C_LANG_CODE, rs))
            .sort(getInteger(C_SORT, rs))
            .title(getString(C_TITLE, rs))
            .url(getString(C_URL, rs))
            .lastSynched(getNow())
            .build();
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.publ.db.public_.tables.records.NewStudyPageLinkRecord> getPurgeDcs(
        final Timestamp cutOff) {
        return getJooqPublic()
            .delete(ch.difty.scipamato.publ.db.public_.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK)
            .where(ch.difty.scipamato.publ.db.public_.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.LAST_SYNCHED.lessThan(
                cutOff));
    }

}
