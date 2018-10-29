package ch.difty.scipamato.core.sync.jobs.language;

import static ch.difty.scipamato.core.db.public_.tables.Language.LANGUAGE;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.DeleteWhereStep;
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
import ch.difty.scipamato.publ.db.public_.tables.records.LanguageRecord;

/**
 * Defines the language synchronization job.
 *
 * @author u.joss
 */
@Configuration
public class LanguageSyncConfig
    extends SyncConfig<PublicLanguage, ch.difty.scipamato.publ.db.public_.tables.records.LanguageRecord> {

    private static final String TOPIC      = "language";
    private static final int    CHUNK_SIZE = 100;

    // relevant fields of the core Language record
    private static final TableField<ch.difty.scipamato.core.db.public_.tables.records.LanguageRecord, String>  C_CODE = LANGUAGE.CODE;
    private static final TableField<ch.difty.scipamato.core.db.public_.tables.records.LanguageRecord, Boolean> C_MAIN = LANGUAGE.MAIN_LANGUAGE;

    protected LanguageSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncLanguageJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncLanguageJob";
    }

    @Override
    protected ItemWriter<PublicLanguage> publicWriter() {
        return new LanguageItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(C_CODE, C_MAIN)
            .from(LANGUAGE)
            .getSQL();
    }

    @Override
    protected PublicLanguage makeEntity(final ResultSet rs) throws SQLException {
        return PublicLanguage
            .builder()
            .code(getString(C_CODE, rs))
            .mainLanguage(getBoolean(C_MAIN, rs))
            .lastSynched(getNow())
            .build();
    }

    @Override
    protected DeleteWhereStep<LanguageRecord> getDeleteWhereStep() {
        return getJooqPublic().delete(ch.difty.scipamato.publ.db.public_.tables.Language.LANGUAGE);
    }

    @Override
    protected TableField<LanguageRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.public_.tables.Language.LANGUAGE.LAST_SYNCHED;
    }

}
