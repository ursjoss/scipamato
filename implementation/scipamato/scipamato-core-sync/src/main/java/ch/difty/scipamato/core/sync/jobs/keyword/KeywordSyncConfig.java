package ch.difty.scipamato.core.sync.jobs.keyword;

import static ch.difty.scipamato.core.db.public_.tables.Keyword.KEYWORD;
import static ch.difty.scipamato.core.db.public_.tables.KeywordTr.KEYWORD_TR;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
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
import ch.difty.scipamato.core.db.public_.tables.Keyword;
import ch.difty.scipamato.core.db.public_.tables.KeywordTr;
import ch.difty.scipamato.core.db.public_.tables.records.KeywordRecord;
import ch.difty.scipamato.core.db.public_.tables.records.KeywordTrRecord;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;

/**
 * Defines the code synchronization job, applying two steps:
 * <ol>
 * <li>keywordInsertingOrUpdating: inserts new records or updates if already
 * present</li>
 * <li>keywordPurging: removes records that have not been touched by the first step
 * (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@Configuration
public class KeywordSyncConfig
    extends SyncConfig<PublicKeyword, ch.difty.scipamato.publ.db.public_.tables.records.KeywordRecord> {

    private static final String TOPIC      = "keyword";
    private static final int    CHUNK_SIZE = 100;

    // relevant fields of the core Keyword class record
    private static final TableField<KeywordTrRecord, Integer>   KT_ID              = KEYWORD_TR.ID;
    private static final TableField<KeywordRecord, Integer>     KW_ID              = KEYWORD.ID;
    private static final String                                 ALIAS_KEYWORD_ID   = "KeywordId";
    private static final TableField<KeywordRecord, String>      KW_SEARCH_OVERRIDE = KEYWORD.SEARCH_OVERRIDE;
    private static final TableField<KeywordTrRecord, String>    KT_LANG_CODE       = KEYWORD_TR.LANG_CODE;
    private static final TableField<KeywordTrRecord, String>    KT_NAME            = KEYWORD_TR.NAME;
    private static final TableField<KeywordTrRecord, Integer>   KT_VERSION         = KEYWORD_TR.VERSION;
    private static final TableField<KeywordTrRecord, Timestamp> KT_CREATED         = KEYWORD_TR.CREATED;
    private static final TableField<KeywordTrRecord, Timestamp> KT_LAST_MODIFIED   = KEYWORD_TR.LAST_MODIFIED;

    protected KeywordSyncConfig(@Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory,
            dateTimeService);
    }

    @Bean
    public Job syncKeywordJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncKeywordJob";
    }

    @Override
    protected ItemWriter<PublicKeyword> publicWriter() {
        return new KeywordItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(KT_ID, KW_ID.as(ALIAS_KEYWORD_ID), KT_LANG_CODE, KT_NAME, KT_VERSION, KT_CREATED, KT_LAST_MODIFIED,
                KW_SEARCH_OVERRIDE)
            .from(Keyword.KEYWORD)
            .innerJoin(KeywordTr.KEYWORD_TR)
            .on(KW_ID.eq(KeywordTr.KEYWORD_TR.KEYWORD_ID))
            .getSQL();
    }

    @Override
    protected PublicKeyword makeEntity(final ResultSet rs) throws SQLException {
        return PublicKeyword
            .builder()
            .id(getInteger(KT_ID, rs))
            .keywordId(rs.getInt(ALIAS_KEYWORD_ID))
            .langCode(getString(KT_LANG_CODE, rs))
            .name(getString(KT_NAME, rs))
            .version(getInteger(KT_VERSION, rs))
            .created(getTimestamp(KT_CREATED, rs))
            .lastModified(getTimestamp(KT_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .searchOverride(getString(KW_SEARCH_OVERRIDE, rs))
            .build();
    }

    @Override
    protected DeleteWhereStep<ch.difty.scipamato.publ.db.public_.tables.records.KeywordRecord> getDeleteWhereStep() {
        return getJooqPublic().delete(ch.difty.scipamato.publ.db.public_.tables.Keyword.KEYWORD);
    }

    @Override
    protected TableField<ch.difty.scipamato.publ.db.public_.tables.records.KeywordRecord, Timestamp> lastSynchedField() {
        return ch.difty.scipamato.publ.db.public_.tables.Keyword.KEYWORD.LAST_SYNCHED;
    }
}
