package ch.difty.scipamato.core.sync.jobs.language;

import static ch.difty.scipamato.core.db.tables.Language.LANGUAGE;

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
import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.publ.db.tables.records.LanguageRecord;

/**
 * Defines the language synchronization job.
 *
 * @author u.joss
 */
@Configuration
@Profile("!wickettest")
public class LanguageSyncConfig
    extends SyncConfig<PublicLanguage, ch.difty.scipamato.publ.db.tables.records.LanguageRecord> {

    private static final String TOPIC      = "language";
    private static final int    CHUNK_SIZE = 100;

    // relevant fields of the core Language record
    private static final TableField<ch.difty.scipamato.core.db.tables.records.LanguageRecord, String>  C_CODE = LANGUAGE.CODE;
    private static final TableField<ch.difty.scipamato.core.db.tables.records.LanguageRecord, Boolean> C_MAIN = LANGUAGE.MAIN_LANGUAGE;

    protected LanguageSyncConfig(@Qualifier("dslContext") @NotNull final DSLContext jooqCore,
        @Qualifier("publicDslContext") @NotNull final DSLContext jooqPublic,
        @Qualifier("dataSource") @NotNull final DataSource coreDataSource,
        @NotNull final JobBuilderFactory jobBuilderFactory, @NotNull final StepBuilderFactory stepBuilderFactory,
        @NotNull final DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @NotNull
    @Bean
    public Job syncLanguageJob() {
        return createJob();
    }

    @NotNull
    @Override
    protected String getJobName() {
        return "syncLanguageJob";
    }

    @NotNull
    @Override
    protected ItemWriter<PublicLanguage> publicWriter() {
        return new LanguageItemWriter(getJooqPublic());
    }

    @NotNull
    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(C_CODE, C_MAIN)
            .from(LANGUAGE)
            .getSQL();
    }

    @NotNull
    @Override
    protected PublicLanguage makeEntity(@NotNull final ResultSet rs) throws SQLException {
        final Boolean value = getBoolean(C_MAIN, rs);
        return PublicLanguage
            .builder()
            .code(getString(C_CODE, rs))
            .mainLanguage(value != null && value)
            .lastSynched(getNow())
            .build();
    }

    @NotNull
    @Override
    protected TableField<LanguageRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.tables.Language.LANGUAGE.LAST_SYNCHED;
    }
}
