package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;
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
import ch.difty.scipamato.core.db.tables.NewStudyPageLink;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.publ.db.tables.records.NewStudyPageLinkRecord;

/**
 * Defines the newStudyPageLink synchronization job.
 *
 * @author u.joss
 */
@Configuration
@Profile("!wickettest")
public class NewStudyPageLinkSyncConfig
    extends SyncConfig<PublicNewStudyPageLink, ch.difty.scipamato.publ.db.tables.records.NewStudyPageLinkRecord> {

    private static final String TOPIC      = "newStudyPageLink";
    private static final int    CHUNK_SIZE = 50;

    // relevant fields of the core Language record
    private static final TableField<ch.difty.scipamato.core.db.tables.records.NewStudyPageLinkRecord, String>  C_LANG_CODE = NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE;
    private static final TableField<ch.difty.scipamato.core.db.tables.records.NewStudyPageLinkRecord, Integer> C_SORT      = NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT;
    private static final TableField<ch.difty.scipamato.core.db.tables.records.NewStudyPageLinkRecord, String>  C_TITLE     = NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE;
    private static final TableField<ch.difty.scipamato.core.db.tables.records.NewStudyPageLinkRecord, String>  C_URL       = NewStudyPageLink.NEW_STUDY_PAGE_LINK.URL;

    protected NewStudyPageLinkSyncConfig(@Qualifier("dslContext") @NotNull final DSLContext jooqCore,
        @Qualifier("publicDslContext") @NotNull final DSLContext jooqPublic,
        @Qualifier("dataSource") @NotNull final DataSource coreDataSource,
        @NotNull final JobBuilderFactory jobBuilderFactory, @NotNull final StepBuilderFactory stepBuilderFactory,
        @NotNull final DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @NotNull
    @Bean
    public Job syncNewStudyPageLinkJob() {
        return createJob();
    }

    @NotNull
    @Override
    protected String getJobName() {
        return "syncNewStudyPageLinkJob";
    }

    @NotNull
    @Override
    protected ItemWriter<PublicNewStudyPageLink> publicWriter() {
        return new NewStudyPageLinkItemWriter(getJooqPublic());
    }

    @NotNull
    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(C_LANG_CODE, C_SORT, C_TITLE, C_URL)
            .from(NewStudyPageLink.NEW_STUDY_PAGE_LINK)
            .getSQL();
    }

    @NotNull
    @Override
    protected PublicNewStudyPageLink makeEntity(@NotNull final ResultSet rs) throws SQLException {
        return PublicNewStudyPageLink
            .builder()
            .langCode(getString(C_LANG_CODE, rs))
            .sort(getInteger(C_SORT, rs))
            .title(getString(C_TITLE, rs))
            .url(getString(C_URL, rs))
            .lastSynched(getNow())
            .build();
    }

    @NotNull
    @Override
    protected TableField<NewStudyPageLinkRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.LAST_SYNCHED;
    }
}
