package ch.difty.scipamato.core.sync.jobs.codeclass;

import static ch.difty.scipamato.db.core.public_.tables.CodeClass.*;
import static ch.difty.scipamato.db.core.public_.tables.CodeClassTr.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.springframework.batch.core.Job;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.db.core.public_.tables.CodeClass;
import ch.difty.scipamato.db.core.public_.tables.CodeClassTr;
import ch.difty.scipamato.db.core.public_.tables.records.CodeClassRecord;
import ch.difty.scipamato.db.core.public_.tables.records.CodeClassTrRecord;

/**
 * Defines the code class synchronization job, applying two steps:
 * <ol>
 * <li> codeClassInsertingOrUpdating: inserts new records or updates if already present</li>
 * <li> codeClassPurging: removes records that have not been touched by the first step
 *      (within a defined grace time in minutes)</li>
 * </ol>
 * @author u.joss
 */
@Configuration
public class CodeClassSyncConfig extends SyncConfig<PublicCodeClass, ch.difty.scipamato.db.public_.public_.tables.records.CodeClassRecord> {

    private static final String TOPIC = "codeClass";
    private static final int CHUNK_SIZE = 50;

    // fields of the public Code class record
    private static final TableField<CodeClassRecord, Integer> P_ID = CODE_CLASS.ID;
    private static final TableField<CodeClassTrRecord, String> P_LANG_CODE = CODE_CLASS_TR.LANG_CODE;
    private static final TableField<CodeClassTrRecord, String> P_NAME = CODE_CLASS_TR.NAME;
    private static final TableField<CodeClassTrRecord, String> P_DESCRIPTION = CODE_CLASS_TR.DESCRIPTION;
    private static final TableField<CodeClassTrRecord, Integer> P_VERSION = CODE_CLASS_TR.VERSION;
    private static final TableField<CodeClassTrRecord, Timestamp> P_CREATED = CODE_CLASS_TR.CREATED;
    private static final TableField<CodeClassTrRecord, Timestamp> P_LAST_MODIFIED = CODE_CLASS_TR.LAST_MODIFIED;

    protected CodeClassSyncConfig() {
        super(TOPIC, CHUNK_SIZE);
    }

    @Bean
    public Job syncCodeClassJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "codeClassSyncJob";
    }

    @Override
    protected ItemWriter<? super PublicCodeClass> publicWriter() {
        return new CodeClassItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        // @formatter:off
        return getJooqCore()
            .select(P_ID, P_LANG_CODE, P_NAME, P_DESCRIPTION, P_VERSION, P_CREATED, P_LAST_MODIFIED)
            .from(CodeClass.CODE_CLASS)
            .innerJoin(CodeClassTr.CODE_CLASS_TR)
            .on(P_ID.eq(CodeClassTr.CODE_CLASS_TR.CODE_CLASS_ID))
            .getSQL();
        // @formatter:on
    }

    @Override
    protected PublicCodeClass makeEntity(final ResultSet rs) throws SQLException {
        return new PublicCodeClass(
        // @formatter:off
            rs.getInt(P_ID.getName()),
            rs.getString(P_LANG_CODE.getName()),
            rs.getString(P_NAME.getName()),
            rs.getString(P_DESCRIPTION.getName()),
            rs.getInt(P_VERSION.getName()),
            rs.getTimestamp(P_CREATED.getName()),
            rs.getTimestamp(P_LAST_MODIFIED.getName()),
            Timestamp.valueOf(LocalDateTime.now())
        // @formatter:on
        );
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.db.public_.public_.tables.records.CodeClassRecord> getPurgeDdl(final Timestamp cutOff) {
        // @formatter:off
        return getJooqPublic()
                .delete(ch.difty.scipamato.db.public_.public_.tables.CodeClass.CODE_CLASS)
                .where(ch.difty.scipamato.db.public_.public_.tables.CodeClass.CODE_CLASS.LAST_SYNCHED.lessThan(cutOff));
        // @formatter:on
    }

}
