package ch.difty.scipamato.core.sync.jobs.code;

import static ch.difty.scipamato.db.core.public_.tables.Code.*;
import static ch.difty.scipamato.db.core.public_.tables.CodeTr.*;

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

    // fields of the public Code class record
    private static final TableField<CodeRecord, String> P_CODE = CODE.CODE_;
    private static final TableField<CodeTrRecord, String> P_LANG_CODE = CODE_TR.LANG_CODE;
    private static final TableField<CodeRecord, Integer> P_CODE_CLASS_ID = CODE.CODE_CLASS_ID;
    private static final TableField<CodeTrRecord, String> P_NAME = CODE_TR.NAME;
    private static final TableField<CodeTrRecord, String> P_COMMENT = CODE_TR.COMMENT;
    private static final TableField<CodeRecord, Integer> P_SORT = CODE.SORT;
    private static final TableField<CodeTrRecord, Integer> P_VERSION = CODE_TR.VERSION;
    private static final TableField<CodeTrRecord, Timestamp> P_CREATED = CODE_TR.CREATED;
    private static final TableField<CodeTrRecord, Timestamp> P_LAST_MODIFIED = CODE_TR.LAST_MODIFIED;

    protected CodeSyncConfig() {
        super(TOPIC, CHUNK_SIZE);
    }

    @Bean
    public Job syncCodeJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "codeSyncJob";
    }

    @Override
    protected ItemWriter<? super PublicCode> publicWriter() {
        return new CodeItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        // @formatter:off
        return getJooqCore()
            .select(P_CODE, P_LANG_CODE, P_CODE_CLASS_ID, P_NAME, P_COMMENT, P_SORT, P_VERSION, P_CREATED, P_LAST_MODIFIED)
            .from(Code.CODE)
            .innerJoin(CodeTr.CODE_TR)
            .on(P_CODE.eq(CodeTr.CODE_TR.CODE))
            .where(CODE.INTERNAL.isTrue())
            // TODO union with aggregated codes (yet to be implemented in scipamato-core
            .getSQL();
        // @formatter:on
    }

    @Override
    protected PublicCode makeEntity(final ResultSet rs) throws SQLException {
        return new PublicCode(
        // @formatter:off
            rs.getString(P_CODE.getName()),
            rs.getString(P_LANG_CODE.getName()),
            rs.getInt(P_CODE_CLASS_ID.getName()),
            rs.getString(P_NAME.getName()),
            rs.getString(P_COMMENT.getName()),
            rs.getInt(P_SORT.getName()),
            rs.getInt(P_VERSION.getName()),
            rs.getTimestamp(P_CREATED.getName()),
            rs.getTimestamp(P_LAST_MODIFIED.getName()),
            Timestamp.valueOf(LocalDateTime.now())
        // @formatter:on
        );
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.db.public_.public_.tables.records.CodeRecord> getPurgeDdl(final Timestamp cutOff) {
        // @formatter:off
        return getJooqPublic()
                .delete(ch.difty.scipamato.db.public_.public_.tables.Code.CODE)
                .where(ch.difty.scipamato.db.public_.public_.tables.Code.CODE.LAST_SYNCHED.lessThan(cutOff));
        // @formatter:on
    }

}
