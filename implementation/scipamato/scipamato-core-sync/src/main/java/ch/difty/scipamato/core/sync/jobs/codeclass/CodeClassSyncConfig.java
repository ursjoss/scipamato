package ch.difty.scipamato.core.sync.jobs.codeclass;

import static ch.difty.scipamato.core.db.public_.tables.CodeClass.CODE_CLASS;
import static ch.difty.scipamato.core.db.public_.tables.CodeClassTr.CODE_CLASS_TR;

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
import ch.difty.scipamato.core.db.public_.tables.CodeClass;
import ch.difty.scipamato.core.db.public_.tables.CodeClassTr;
import ch.difty.scipamato.core.db.public_.tables.records.CodeClassRecord;
import ch.difty.scipamato.core.db.public_.tables.records.CodeClassTrRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;

/**
 * Defines the code class synchronization job, applying two steps:
 * <ol>
 * <li>insertingOrUpdating: inserts new records or updates if already present</li>
 * <li>purging: removes records that have not been touched by the first step
 * (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@Configuration
public class CodeClassSyncConfig
    extends SyncConfig<PublicCodeClass, ch.difty.scipamato.publ.db.public_.tables.records.CodeClassRecord> {

    private static final String TOPIC      = "codeClass";
    private static final int    CHUNK_SIZE = 50;

    // relevant fields of the core Code class record
    private static final TableField<CodeClassRecord, Integer>     C_ID            = CODE_CLASS.ID;
    private static final TableField<CodeClassTrRecord, String>    C_LANG_CODE     = CODE_CLASS_TR.LANG_CODE;
    private static final TableField<CodeClassTrRecord, String>    C_NAME          = CODE_CLASS_TR.NAME;
    private static final TableField<CodeClassTrRecord, String>    C_DESCRIPTION   = CODE_CLASS_TR.DESCRIPTION;
    private static final TableField<CodeClassTrRecord, Integer>   C_VERSION       = CODE_CLASS_TR.VERSION;
    private static final TableField<CodeClassTrRecord, Timestamp> C_CREATED       = CODE_CLASS_TR.CREATED;
    private static final TableField<CodeClassTrRecord, Timestamp> C_LAST_MODIFIED = CODE_CLASS_TR.LAST_MODIFIED;

    protected CodeClassSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncCodeClassJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncCodeClassJob";
    }

    @Override
    protected ItemWriter<PublicCodeClass> publicWriter() {
        return new CodeClassItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(C_ID, C_LANG_CODE, C_NAME, C_DESCRIPTION, C_VERSION, C_CREATED, C_LAST_MODIFIED)
            .from(CodeClass.CODE_CLASS)
            .innerJoin(CodeClassTr.CODE_CLASS_TR)
            .on(C_ID.eq(CodeClassTr.CODE_CLASS_TR.CODE_CLASS_ID))
            .getSQL();
    }

    @Override
    protected PublicCodeClass makeEntity(final ResultSet rs) throws SQLException {
        return PublicCodeClass
            .builder()
            .codeClassId(getInteger(C_ID, rs))
            .langCode(getString(C_LANG_CODE, rs))
            .name(getString(C_NAME, rs))
            .description(getString(C_DESCRIPTION, rs))
            .version(getInteger(C_VERSION, rs))
            .created(getTimestamp(C_CREATED, rs))
            .lastModified(getTimestamp(C_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    @Override
    protected DeleteWhereStep<ch.difty.scipamato.publ.db.public_.tables.records.CodeClassRecord> getDeleteWhereStep() {
        return getJooqPublic().delete(ch.difty.scipamato.publ.db.public_.tables.CodeClass.CODE_CLASS);
    }

    @Override
    protected TableField<ch.difty.scipamato.publ.db.public_.tables.records.CodeClassRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.public_.tables.CodeClass.CODE_CLASS.LAST_SYNCHED;
    }

}
