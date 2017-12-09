package ch.difty.scipamato.core.sync.jobs.code;

import static ch.difty.scipamato.db.core.public_.tables.Code.*;
import static ch.difty.scipamato.db.core.public_.tables.CodeTr.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.springframework.batch.core.Job;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.db.core.public_.tables.Code;
import ch.difty.scipamato.db.core.public_.tables.CodeTr;
import ch.difty.scipamato.db.core.public_.tables.records.CodeRecord;
import ch.difty.scipamato.db.core.public_.tables.records.CodeTrRecord;

/**
 * Defines the code synchronization job, applying two steps:
 * <ol>
 * <li> codeInsertingOrUpdating: inserts new records or updates if already present</li>
 * <li> codePurging: removes records that have not been touched by the first step
 *      (within a defined grace time in minutes)</li>
 * </ol>
 * @author u.joss
 */
@Configuration
public class CodeSyncConfig extends SyncConfig<PublicCode, ch.difty.scipamato.db.public_.public_.tables.records.CodeRecord> {

    private static final String TOPIC = "code";
    private static final int CHUNK_SIZE = 50;

    // relevant fields of the core Code class record
    private static final TableField<CodeRecord, String> C_CODE = CODE.CODE_;
    private static final TableField<CodeTrRecord, String> C_LANG_CODE = CODE_TR.LANG_CODE;
    private static final TableField<CodeRecord, Integer> C_CODE_CLASS_ID = CODE.CODE_CLASS_ID;
    private static final TableField<CodeTrRecord, String> C_NAME = CODE_TR.NAME;
    private static final TableField<CodeTrRecord, String> C_COMMENT = CODE_TR.COMMENT;
    private static final TableField<CodeRecord, Integer> C_SORT = CODE.SORT;
    private static final TableField<CodeTrRecord, Integer> C_VERSION = CODE_TR.VERSION;
    private static final TableField<CodeTrRecord, Timestamp> C_CREATED = CODE_TR.CREATED;
    private static final TableField<CodeTrRecord, Timestamp> C_LAST_MODIFIED = CODE_TR.LAST_MODIFIED;

    protected CodeSyncConfig() {
        super(TOPIC, CHUNK_SIZE);
    }

    @Bean
    public Job syncCodeJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncCodeJob";
    }

    @Override
    protected ItemWriter<PublicCode> publicWriter() {
        return new CodeItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        // @formatter:off
        return getJooqCore()
            .select(C_CODE, C_LANG_CODE, C_CODE_CLASS_ID, C_NAME, C_COMMENT, C_SORT, C_VERSION, C_CREATED, C_LAST_MODIFIED)
            .from(Code.CODE)
            .innerJoin(CodeTr.CODE_TR)
            .on(C_CODE.eq(CodeTr.CODE_TR.CODE))
            .where(CODE.INTERNAL.isFalse())
            // TODO union with aggregated codes (yet to be implemented in scipamato-core
            .getSQL();
        // @formatter:on
    }

    @Override
    protected PublicCode makeEntity(final ResultSet rs) throws SQLException {
        return PublicCode
            .builder()
            .code(getString(C_CODE, rs))
            .langCode(getString(C_LANG_CODE, rs))
            .codeClassId(getInt(C_CODE_CLASS_ID, rs))
            .name(getString(C_NAME, rs))
            .comment(getString(C_COMMENT, rs))
            .sort(getInt(C_SORT, rs))
            .version(getInt(C_VERSION, rs))
            .created(getTimestamp(C_CREATED, rs))
            .lastModified(getTimestamp(C_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.db.public_.public_.tables.records.CodeRecord> getPurgeDcs(final Timestamp cutOff) {
        // @formatter:off
        return getJooqPublic()
                .delete(ch.difty.scipamato.db.public_.public_.tables.Code.CODE)
                .where(ch.difty.scipamato.db.public_.public_.tables.Code.CODE.LAST_SYNCHED.lessThan(cutOff));
        // @formatter:on
    }

}
