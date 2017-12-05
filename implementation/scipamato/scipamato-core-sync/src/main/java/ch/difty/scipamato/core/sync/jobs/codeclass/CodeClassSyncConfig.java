package ch.difty.scipamato.core.sync.jobs.codeclass;

import static ch.difty.scipamato.db.core.public_.tables.CodeClass.*;
import static ch.difty.scipamato.db.core.public_.tables.CodeClassTr.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import ch.difty.scipamato.core.sync.houskeeping.HouseKeeper;
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
public class CodeClassSyncConfig {

    private static final String JOB_NAME = "codeClassSyncJob";

    @Value("${purge_grace_time_in_minutes:30}")
    private int graceTime;

    private static final int CHUNK_SIZE = 50;

    // fields of the public Code class record
    private static final TableField<CodeClassRecord, Integer> P_ID = CODE_CLASS.ID;
    private static final TableField<CodeClassTrRecord, String> P_LANG_CODE = CODE_CLASS_TR.LANG_CODE;
    private static final TableField<CodeClassTrRecord, String> P_NAME = CODE_CLASS_TR.NAME;
    private static final TableField<CodeClassTrRecord, String> P_DESCRIPTION = CODE_CLASS_TR.DESCRIPTION;
    private static final TableField<CodeClassTrRecord, Integer> P_VERSION = CODE_CLASS_TR.VERSION;
    private static final TableField<CodeClassTrRecord, Timestamp> P_CREATED = CODE_CLASS_TR.CREATED;
    private static final TableField<CodeClassTrRecord, Timestamp> P_LAST_MODIFIED = CODE_CLASS_TR.LAST_MODIFIED;

    @Autowired
    @Qualifier("coreDslContext")
    private DSLContext jooqCore;

    @Autowired
    @Qualifier("publicDslContext")
    private DSLContext jooqPublic;

    @Autowired
    @Qualifier("scipamatoCoreDataSource")
    private DataSource scipamatoCoreDataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job syncCodeClassJob() {
        // @formatter:off
        return jobBuilderFactory
            .get(JOB_NAME).incrementer(new RunIdIncrementer())
                .flow(codeClassInsertingOrUpdatingStep())
                .next(codeClassPurgingStep())
            .end()
            .build();
        // @formatter:on
    }

    private Step codeClassInsertingOrUpdatingStep() {
        // @formatter:off
        return stepBuilderFactory
            .get("codeClassInsertingOrUpdatingStep")
            .<PublicCodeClass, PublicCodeClass> chunk(CHUNK_SIZE)
                .reader(coreReader())
                .writer(publicWriter())
            .build();
        // @formatter:on
    }

    private ItemReader<? extends PublicCodeClass> coreReader() {
        final JdbcCursorItemReader<PublicCodeClass> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(scipamatoCoreDataSource);
        reader.setSql(selectSql());
        reader.setRowMapper(new RowMapper<PublicCodeClass>() {
            @Override
            public PublicCodeClass mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                return makeEntity(rs);
            }
        });
        return reader;
    };

    private String selectSql() {
        // @formatter:off
        return jooqCore
            .select(P_ID, P_LANG_CODE, P_NAME, P_DESCRIPTION, P_VERSION, P_CREATED, P_LAST_MODIFIED)
            .from(CodeClass.CODE_CLASS)
            .innerJoin(CodeClassTr.CODE_CLASS_TR)
            .on(P_ID.eq(CodeClassTr.CODE_CLASS_TR.CODE_CLASS_ID))
            .getSQL();
        // @formatter:on
    }

    private PublicCodeClass makeEntity(final ResultSet rs) throws SQLException {
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

    private ItemWriter<? super PublicCodeClass> publicWriter() {
        return new CodeClassItemWriter(jooqPublic);
    }

    private Step codeClassPurgingStep() {
        final Timestamp cutOff = Timestamp.valueOf(LocalDateTime.now().minusMinutes(graceTime));
        // @formatter:off
        final DeleteConditionStep<ch.difty.scipamato.db.public_.public_.tables.records.CodeClassRecord> ddl =
                jooqPublic
                    .delete(ch.difty.scipamato.db.public_.public_.tables.CodeClass.CODE_CLASS)
                    .where(ch.difty.scipamato.db.public_.public_.tables.CodeClass.CODE_CLASS.LAST_SYNCHED.lessThan(cutOff));
        return stepBuilderFactory
            .get("codeClassPurgingStep")
                .tasklet(new HouseKeeper<ch.difty.scipamato.db.public_.public_.tables.records.CodeClassRecord>(ddl, graceTime))
            .build();
        // @formatter:on
    }

}
