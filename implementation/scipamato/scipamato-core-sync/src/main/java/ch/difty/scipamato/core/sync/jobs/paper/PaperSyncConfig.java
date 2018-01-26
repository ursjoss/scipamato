package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.core.db.public_.tables.Paper.PAPER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.public_.tables.Code;
import ch.difty.scipamato.core.db.public_.tables.Paper;
import ch.difty.scipamato.core.db.public_.tables.PaperCode;
import ch.difty.scipamato.core.db.public_.tables.records.PaperRecord;
import ch.difty.scipamato.core.sync.code.CodeAggregator;
import ch.difty.scipamato.core.sync.jobs.SyncConfig;

/**
 * Defines the paper synchronization job, applying two steps:
 * <ol>
 * <li>paperInsertingOrUpdating: inserts new records or updates if already
 * present</li>
 * <li>paperPurging: removes records that have not been touched by the first
 * step (within a defined grace time in minutes)</li>
 * </ol>
 *
 * @author u.joss
 */
@Configuration
public class PaperSyncConfig
        extends SyncConfig<PublicPaper, ch.difty.scipamato.publ.db.public_.tables.records.PaperRecord> {

    private static final String TOPIC      = "paper";
    private static final int    CHUNK_SIZE = 100;

    private static final String ALIAS_CODES = "codes";

    // relevant fields of the core Paper record
    private static final TableField<PaperRecord, Long>      C_ID            = PAPER.ID;
    private static final TableField<PaperRecord, Long>      C_NUMBER        = PAPER.NUMBER;
    private static final TableField<PaperRecord, Integer>   C_PM_ID         = PAPER.PM_ID;
    private static final TableField<PaperRecord, String>    C_AUTHORS       = PAPER.AUTHORS;
    private static final TableField<PaperRecord, String>    C_TITLE         = PAPER.TITLE;
    private static final TableField<PaperRecord, String>    C_LOCATION      = PAPER.LOCATION;
    private static final TableField<PaperRecord, Integer>   C_PUB_YEAR      = PAPER.PUBLICATION_YEAR;
    private static final TableField<PaperRecord, String>    C_GOALS         = PAPER.GOALS;
    private static final TableField<PaperRecord, String>    C_METHODS       = PAPER.METHODS;
    private static final TableField<PaperRecord, String>    C_POPULATION    = PAPER.POPULATION;
    private static final TableField<PaperRecord, String>    C_RESULT        = PAPER.RESULT;
    private static final TableField<PaperRecord, String>    C_COMMENT       = PAPER.COMMENT;
    private static final TableField<PaperRecord, Integer>   C_VERSION       = PAPER.VERSION;
    private static final TableField<PaperRecord, Timestamp> C_CREATED       = PAPER.CREATED;
    private static final TableField<PaperRecord, Timestamp> C_LAST_MODIFIED = PAPER.LAST_MODIFIED;

    private final CodeAggregator codeAggregator;

    protected PaperSyncConfig(CodeAggregator codeAggregator, @Qualifier("dslContext") DSLContext jooqCore,
            @Qualifier("publicDslContext") DSLContext jooqPublic,
            @Qualifier("dataSource") DataSource scipamatoCoreDataSource, JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        super(TOPIC, CHUNK_SIZE, jooqCore, jooqPublic, scipamatoCoreDataSource, jobBuilderFactory, stepBuilderFactory,
                dateTimeService);
        this.codeAggregator = codeAggregator;
        setInternalCodes();
    }

    private void setInternalCodes() {
        codeAggregator.setInternalCodes(fetchInternalCodesFromDb());
    }

    private List<String> fetchInternalCodesFromDb() {
        return getJooqCore().select()
            .from(Code.CODE)
            .where(Code.CODE.INTERNAL.isTrue())
            .fetch(Code.CODE.CODE_);
    }

    @Bean
    public Job syncPaperJob() {
        return createJob();
    }

    @Override
    protected String getJobName() {
        return "syncPaperJob";
    }

    @Override
    protected ItemWriter<PublicPaper> publicWriter() {
        return new PaperItemWriter(getJooqPublic());
    }

    @Override
    protected String selectSql() {
        return getJooqCore()
            .select(C_ID, C_NUMBER, C_PM_ID, C_AUTHORS, C_TITLE, C_LOCATION, C_PUB_YEAR, C_GOALS, C_METHODS,
                C_POPULATION, C_RESULT, C_COMMENT, C_VERSION, C_CREATED, C_LAST_MODIFIED,
                DSL.arrayAgg(PaperCode.PAPER_CODE.CODE)
                    .as(ALIAS_CODES))
            .from(Paper.PAPER)
            .innerJoin(PaperCode.PAPER_CODE)
            .on(Paper.PAPER.ID.eq(PaperCode.PAPER_CODE.PAPER_ID))
            .innerJoin(Code.CODE)
            .on(PaperCode.PAPER_CODE.CODE.eq(Code.CODE.CODE_))
            .groupBy(C_ID, C_NUMBER, C_PM_ID, C_AUTHORS, C_TITLE, C_LOCATION, C_PUB_YEAR, C_GOALS, C_METHODS,
                C_POPULATION, C_RESULT, C_COMMENT, C_VERSION, C_CREATED, C_LAST_MODIFIED)
            .getSQL();
    }

    @Override
    protected PublicPaper makeEntity(final ResultSet rs) throws SQLException {
        final PublicPaper paper = PublicPaper.builder()
            .id(getLong(C_ID, rs))
            .number(getLong(C_NUMBER, rs))
            .pmId(getInteger(C_PM_ID, rs))
            .authors(getString(C_AUTHORS, rs))
            .title(getString(C_TITLE, rs))
            .location(getString(C_LOCATION, rs))
            .publicationYear(getInteger(C_PUB_YEAR, rs))
            .goals(getString(C_GOALS, rs))
            .methods(getString(C_METHODS, rs))
            .population(getString(C_POPULATION, rs))
            .result(getString(C_RESULT, rs))
            .comment(getString(C_COMMENT, rs))
            .codes(extractCodes(ALIAS_CODES, rs))
            .version(getInteger(C_VERSION, rs))
            .created(getTimestamp(C_CREATED, rs))
            .lastModified(getTimestamp(C_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
        codeAggregator.load(paper.getCodes());
        paper.setCodesPopulation(codeAggregator.getCodesPopulation());
        paper.setCodesStudyDesign(codeAggregator.getCodesStudyDesign());
        paper.setCodes(codeAggregator.getAggregatedCodes());
        return paper;
    }

    private String[] extractCodes(final String alias, final ResultSet rs) throws SQLException {
        return (String[]) rs.getArray(alias)
            .getArray();
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.publ.db.public_.tables.records.PaperRecord> getPurgeDcs(
            final Timestamp cutOff) {
        return getJooqPublic().delete(ch.difty.scipamato.publ.db.public_.tables.Paper.PAPER)
            .where(ch.difty.scipamato.publ.db.public_.tables.Paper.PAPER.LAST_SYNCHED.lessThan(cutOff));
    }

}
