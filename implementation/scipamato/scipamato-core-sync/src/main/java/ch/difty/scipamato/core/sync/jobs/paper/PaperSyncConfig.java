package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.db.core.public_.tables.Paper.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.batch.core.Job;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.core.sync.jobs.SyncConfig;
import ch.difty.scipamato.db.core.public_.tables.Code;
import ch.difty.scipamato.db.core.public_.tables.Paper;
import ch.difty.scipamato.db.core.public_.tables.PaperCode;
import ch.difty.scipamato.db.core.public_.tables.records.PaperRecord;

/**
 * Defines the paper synchronization job, applying two steps:
 * <ol>
 * <li> paperInsertingOrUpdating: inserts new records or updates if already present</li>
 * <li> paperPurging: removes records that have not been touched by the first step
 *      (within a defined grace time in minutes)</li>
 * </ol>
 * @author u.joss
 */
@Configuration
public class PaperSyncConfig extends SyncConfig<PublicPaper, ch.difty.scipamato.db.public_.public_.tables.records.PaperRecord> {

    private static final String TOPIC = "paper";
    private static final int CHUNK_SIZE = 100;

    private static final String ALIAS_CODES = "codes";

    // relevant fields of the core Paper record
    private static final TableField<PaperRecord, Long> C_ID = PAPER.ID;
    private static final TableField<PaperRecord, Long> C_NUMBER = PAPER.NUMBER;
    private static final TableField<PaperRecord, Integer> C_PM_ID = PAPER.PM_ID;
    private static final TableField<PaperRecord, String> C_AUTHORS = PAPER.AUTHORS;
    private static final TableField<PaperRecord, String> C_TITLE = PAPER.TITLE;
    private static final TableField<PaperRecord, String> C_LOCATION = PAPER.LOCATION;
    private static final TableField<PaperRecord, Integer> C_PUB_YEAR = PAPER.PUBLICATION_YEAR;
    private static final TableField<PaperRecord, String> C_GOALS = PAPER.GOALS;
    private static final TableField<PaperRecord, String> C_METHODS = PAPER.METHODS;
    private static final TableField<PaperRecord, String> C_POPULATION = PAPER.POPULATION;
    private static final TableField<PaperRecord, String> C_RESULT = PAPER.RESULT;
    private static final TableField<PaperRecord, String> C_COMMENT = PAPER.COMMENT;
    private static final TableField<PaperRecord, Integer> C_VERSION = PAPER.VERSION;
    private static final TableField<PaperRecord, Timestamp> C_CREATED = PAPER.CREATED;
    private static final TableField<PaperRecord, Timestamp> C_LAST_MODIFIED = PAPER.LAST_MODIFIED;

    protected PaperSyncConfig() {
        super(TOPIC, CHUNK_SIZE);
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
            .select(C_ID, C_NUMBER, C_PM_ID, C_AUTHORS, C_TITLE, C_LOCATION, C_PUB_YEAR, C_GOALS, C_METHODS, C_POPULATION, C_RESULT, C_COMMENT, C_VERSION, C_CREATED, C_LAST_MODIFIED,
                    DSL.arrayAgg(PaperCode.PAPER_CODE.CODE).as(ALIAS_CODES))
            .from(Paper.PAPER)
            .innerJoin(PaperCode.PAPER_CODE)
            .on(Paper.PAPER.ID.eq(PaperCode.PAPER_CODE.PAPER_ID))
            .innerJoin(Code.CODE)
            .on(PaperCode.PAPER_CODE.CODE.eq(Code.CODE.CODE_))
            .groupBy(C_ID, C_NUMBER, C_PM_ID, C_AUTHORS, C_TITLE, C_LOCATION, C_PUB_YEAR, C_GOALS, C_METHODS, C_POPULATION, C_RESULT, C_COMMENT, C_VERSION, C_CREATED, C_LAST_MODIFIED)
            .getSQL();
    }

    @Override
    protected PublicPaper makeEntity(final ResultSet rs) throws SQLException {
        final PublicPaper paper = PublicPaper
            .builder()
            .id(getLong(C_ID, rs))
            .number(getLong(C_NUMBER, rs))
            .pmId(getInt(C_PM_ID, rs))
            .authors(getString(C_AUTHORS, rs))
            .title(getString(C_TITLE, rs))
            .location(getString(C_LOCATION, rs))
            .publicationYear(getInt(C_PUB_YEAR, rs))
            .goals(getString(C_GOALS, rs))
            .methods(getString(C_METHODS, rs))
            .population(getString(C_POPULATION, rs))
            .result(getString(C_RESULT, rs))
            .comment(getString(C_COMMENT, rs))
            .codes(extractCodes(ALIAS_CODES, rs))
            .version(getInt(C_VERSION, rs))
            .created(getTimestamp(C_CREATED, rs))
            .lastModified(getTimestamp(C_LAST_MODIFIED, rs))
            .lastSynched(getNow())
            .build();
        paper.setCodesPopulation(getPopulationCodes(paper));
        paper.setCodesStudyDesign(getStudyDesignCodes(paper));
        return paper;
    }

    // Population (1: Children (Codes 3A+3B), 2: Adults (Codes 3C)
    private Short[] getPopulationCodes(final PublicPaper paper) {
        final List<Short> pcList = new ArrayList<>();
        if (Stream.of(paper.getCodes()).anyMatch(x -> x.equals("3A") || x.equals("3B")))
            pcList.add((short) 1);
        if (Stream.of(paper.getCodes()).anyMatch(x -> x.equals("3C")))
            pcList.add((short) 2);
        return pcList.toArray(new Short[pcList.size()]);
    }

    // (1: Experimental Studies (5A+5B+5C), 2: Epidemiolog. Studies (5E+5F+5G+5H+5I)), 3. Overview/Methodology (5U+5M)
    private Short[] getStudyDesignCodes(final PublicPaper paper) {
        final List<Short> pcList = new ArrayList<>();
        if (Stream.of(paper.getCodes()).anyMatch(x -> x.equals("5A") || x.equals("5B") || x.equals("5C")))
            pcList.add((short) 1);
        if (Stream.of(paper.getCodes()).anyMatch(x -> x.equals("5E") || x.equals("5F") || x.equals("5G") || x.equals("5H") || x.equals("5I")))
            pcList.add((short) 2);
        if (Stream.of(paper.getCodes()).anyMatch(x -> x.equals("5U") || x.equals("5M")))
            pcList.add((short) 3);
        return pcList.toArray(new Short[pcList.size()]);
    }

    private String[] extractCodes(final String alias, final ResultSet rs) throws SQLException {
        return (String[]) rs.getArray(alias).getArray();
    }

    @Override
    protected DeleteConditionStep<ch.difty.scipamato.db.public_.public_.tables.records.PaperRecord> getPurgeDcs(final Timestamp cutOff) {
        // @formatter:off
        return getJooqPublic()
                .delete(ch.difty.scipamato.db.public_.public_.tables.Paper.PAPER)
                .where(ch.difty.scipamato.db.public_.public_.tables.Paper.PAPER.LAST_SYNCHED.lessThan(cutOff));
        // @formatter:on
    }

}
