package ch.difty.scipamato.core.sync.jobs.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.jooq.*;
import org.jooq.tools.jdbc.MockArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoSpyBean;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.tables.Code;
import ch.difty.scipamato.core.db.tables.Paper;
import ch.difty.scipamato.core.sync.code.CodeAggregator;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.tables.records.PaperRecord;

@SpringBootTest
class PaperSyncConfigTest extends SyncConfigTest<PaperRecord> {

    private PaperSyncConfig config;

    @Autowired
    private JobBuilderFactory  jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DateTimeService    dateTimeService;

    @MockK
    private CodeAggregator             codeAggregator;
    @MockK
    private SyncShortFieldConcatenator shortFieldConcatenator;

    @MockitoSpyBean(name = "dslContext")
    private DSLContext jooqCore;

    @MockK
    private DSLContext jooqPublic;
    @MockK
    private DataSource coreDataSource;

    @MockK
    private SelectSelectStep<Record>    selectSelectStep;
    @MockK
    private SelectJoinStep<Record>      selectJoinStep;
    @MockK
    private SelectConditionStep<Record> selectConditionStep;
    @MockK
    private ResultSet                   rs;

    @MockK
    private DeleteWhereStep<PaperRecord>     deleteWhereStep;
    @MockK
    private DeleteConditionStep<PaperRecord> deleteConditionStep;

    private final List<String> internalCodes = Arrays.asList("1N", "1U", "1Z");

    @BeforeEach
    void setUp() {
        when(jooqCore.select()).thenReturn(selectSelectStep);
        when(selectSelectStep.from(Code.CODE)).thenReturn(selectJoinStep);
        when(selectJoinStep.where(Code.CODE.INTERNAL.isTrue())).thenReturn(selectConditionStep);
        when(selectConditionStep.fetch(Code.CODE.CODE_)).thenReturn(internalCodes);

        when(jooqPublic.delete(ch.difty.scipamato.publ.db.tables.Paper.PAPER)).thenReturn(deleteWhereStep);
        Timestamp ref = Timestamp.valueOf(LocalDateTime.parse("2016-12-09T05:32:13.0"));
        when(
            deleteWhereStep.where(ch.difty.scipamato.publ.db.tables.Paper.PAPER.LAST_SYNCHED.lessThan(ref))).thenReturn(
            deleteConditionStep);

        config = new PaperSyncConfig(codeAggregator, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory,
            stepBuilderFactory, dateTimeService, shortFieldConcatenator);
    }

    @AfterEach
    void tearDown() {
        confirmVerified(codeAggregator, jooqPublic, coreDataSource);
    }

    @Override
    protected Job getJob() {
        return config.syncPaperJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<PaperRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<PaperRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncPaperJob";
    }

    @Override
    protected String expectedSelectSql() {
        return
            "select \"public\".\"paper\".\"id\", \"public\".\"paper\".\"number\", \"public\".\"paper\".\"pm_id\", \"public\".\"paper\".\"authors\", "
            + "\"public\".\"paper\".\"title\", \"public\".\"paper\".\"location\", \"public\".\"paper\".\"publication_year\", \"public\".\"paper\".\"goals\", "
            + "\"public\".\"paper\".\"methods\", \"public\".\"paper\".\"population\", \"public\".\"paper\".\"result\", \"public\".\"paper\".\"comment\", "
            + "\"public\".\"paper\".\"version\", \"public\".\"paper\".\"created\", \"public\".\"paper\".\"last_modified\", "
            + "array_agg(\"public\".\"paper_code\".\"code\") as \"codes\", \"public\".\"paper\".\"method_study_design\", "
            + "\"public\".\"paper\".\"method_outcome\", \"public\".\"paper\".\"exposure_pollutant\", \"public\".\"paper\".\"exposure_assessment\", "
            + "\"public\".\"paper\".\"method_statistics\", \"public\".\"paper\".\"method_confounders\", "
            + "\"public\".\"paper\".\"population_place\", \"public\".\"paper\".\"population_participants\", \"public\".\"paper\".\"population_duration\", "
            + "\"public\".\"paper\".\"result_exposure_range\", \"public\".\"paper\".\"result_effect_estimate\", \"public\".\"paper\".\"result_measured_outcome\", "
            + "\"public\".\"paper\".\"conclusion\" "
            + "from \"public\".\"paper\" join \"public\".\"paper_code\" on \"public\".\"paper\".\"id\" = \"public\".\"paper_code\".\"paper_id\" "
            + "join \"public\".\"code\" on \"public\".\"paper_code\".\"code\" = \"public\".\"code\".\"code\" "
            + "group by \"public\".\"paper\".\"id\", \"public\".\"paper\".\"number\", \"public\".\"paper\".\"pm_id\", \"public\".\"paper\".\"authors\", "
            + "\"public\".\"paper\".\"title\", \"public\".\"paper\".\"location\", \"public\".\"paper\".\"publication_year\", \"public\".\"paper\".\"goals\", "
            + "\"public\".\"paper\".\"methods\", \"public\".\"paper\".\"population\", \"public\".\"paper\".\"result\", \"public\".\"paper\".\"comment\", "
            + "\"public\".\"paper\".\"version\", \"public\".\"paper\".\"created\", \"public\".\"paper\".\"last_modified\", "
            + "\"public\".\"paper\".\"method_study_design\", \"public\".\"paper\".\"method_outcome\", \"public\".\"paper\".\"exposure_pollutant\", "
            + "\"public\".\"paper\".\"exposure_assessment\", \"public\".\"paper\".\"method_statistics\", "
            + "\"public\".\"paper\".\"method_confounders\", \"public\".\"paper\".\"population_place\", \"public\".\"paper\".\"population_participants\", "
            + "\"public\".\"paper\".\"population_duration\", \"public\".\"paper\".\"result_exposure_range\", \"public\".\"paper\".\"result_effect_estimate\", "
            + "\"public\".\"paper\".\"result_measured_outcome\", \"public\".\"paper\".\"conclusion\"";
    }

    @Override
    protected TableField<PaperRecord, Timestamp> expectedLastSyncField() {
        return ch.difty.scipamato.publ.db.tables.Paper.PAPER.LAST_SYNCHED;
    }

    @Test
    void makingEntity() throws SQLException {
        when(rs.getLong(Paper.PAPER.ID.getName())).thenReturn(1L);
        when(rs.getLong(Paper.PAPER.NUMBER.getName())).thenReturn(2L);
        when(rs.getInt(Paper.PAPER.PM_ID.getName())).thenReturn(3);
        when(rs.getString(Paper.PAPER.AUTHORS.getName())).thenReturn("a");
        when(rs.getString(Paper.PAPER.TITLE.getName())).thenReturn("t");
        when(rs.getString(Paper.PAPER.LOCATION.getName())).thenReturn("l");
        when(rs.getInt(Paper.PAPER.PUBLICATION_YEAR.getName())).thenReturn(2017);
        when(rs.getString(Paper.PAPER.GOALS.getName())).thenReturn("g");
        when(rs.getString(Paper.PAPER.COMMENT.getName())).thenReturn("c");
        when(rs.getArray("codes")).thenReturn(
            new MockArray<>(SQLDialect.POSTGRES, new String[] { "1A", "2B" }, String[].class));
        when(rs.getInt(Paper.PAPER.VERSION.getName())).thenReturn(4);
        when(rs.getTimestamp(Paper.PAPER.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(Paper.PAPER.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        when(codeAggregator.getCodesPopulation()).thenReturn(new Short[] { 1, 2 });
        when(codeAggregator.getCodesStudyDesign()).thenReturn(new Short[] { 3, 4 });
        when(codeAggregator.getAggregatedCodes()).thenReturn(new String[] { "1A", "2B" });

        when(shortFieldConcatenator.methodsFrom(rs)).thenReturn("mfrs");
        when(shortFieldConcatenator.populationFrom(rs)).thenReturn("pfrs");
        when(shortFieldConcatenator.resultFrom(rs)).thenReturn("rfrs");

        PublicPaper pp = config.makeEntity(rs);

        pp.getId() shouldBeEqualTo 1L;
        pp.getNumber() shouldBeEqualTo 2L;
        pp.getPmId() shouldBeEqualTo 3;
        pp.getAuthors() shouldBeEqualTo "a";
        pp.getTitle() shouldBeEqualTo "t";
        pp.getLocation() shouldBeEqualTo "l";
        pp.getPublicationYear() shouldBeEqualTo 2017;
        pp.getGoals() shouldBeEqualTo "g";
        pp.getMethods() shouldBeEqualTo "mfrs";
        pp.getPopulation() shouldBeEqualTo "pfrs";
        pp.getResult() shouldBeEqualTo "rfrs";
        pp.getComment() shouldBeEqualTo "c";
        pp.getCodes() shouldContainAll listOf("1A", "2B");
        pp.getCodesPopulation() shouldContainAll listOf((short) 1, (short) 2);
        pp.getCodesStudyDesign() shouldContainAll listOf((short) 3, (short) 4);
        pp.getVersion() shouldBeEqualTo 4;
        pp.getCreated() shouldBeEqualTo CREATED;
        pp.getLastModified() shouldBeEqualTo MODIFIED;
        assertThat(pp.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify{ rs.getLong(Paper.PAPER.ID.getName()); }
        verify{ rs.getLong(Paper.PAPER.NUMBER.getName()); }
        verify{ rs.getInt(Paper.PAPER.PM_ID.getName()); }
        verify{ rs.getString(Paper.PAPER.AUTHORS.getName()); }
        verify{ rs.getString(Paper.PAPER.TITLE.getName()); }
        verify{ rs.getString(Paper.PAPER.LOCATION.getName()); }
        verify{ rs.getInt(Paper.PAPER.PUBLICATION_YEAR.getName()); }
        verify{ rs.getString(Paper.PAPER.GOALS.getName()); }
        verify{ rs.getString(Paper.PAPER.COMMENT.getName()); }
        verify{ rs.getArray("codes"); }
        verify{ rs.getInt(Paper.PAPER.VERSION.getName()); }
        verify{ rs.getTimestamp(Paper.PAPER.CREATED.getName()); }
        verify{ rs.getTimestamp(Paper.PAPER.LAST_MODIFIED.getName()); }
        verify(exactly=5) { rs.wasNull(); }

        verifyCodeAggregator();

        verify{ shortFieldConcatenator.methodsFrom(rs); }
        verify{ shortFieldConcatenator.populationFrom(rs); }
        verify{ shortFieldConcatenator.resultFrom(rs); }

        confirmVerified(rs);
    }

    private void verifyCodeAggregator() {
        verify{ codeAggregator.setInternalCodes(internalCodes); }
        verify{ codeAggregator.load(new String[] { "1A", "2B" }); }
        verify{ codeAggregator.getCodesPopulation(); }
        verify{ codeAggregator.getCodesStudyDesign(); }
        verify{ codeAggregator.getAggregatedCodes(); }
    }

    @Test
    void makingEntity_withNullValueInPM_ID() throws SQLException {
        final String fieldName = Paper.PAPER.PM_ID.getName();
        validateNullableInteger(fieldName);
        verify{ rs.getInt(fieldName); }
        verifyCodeAggregator();
    }

    private void validateNullableInteger(String fieldName) throws SQLException {
        validateNullableIntColumn(() -> {
            try {
                return rs.getInt(fieldName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Test
    void makingEntity_withNullValueInPublicationYear() throws SQLException {
        final String fieldName = Paper.PAPER.PUBLICATION_YEAR.getName();
        validateNullableInteger(fieldName);
        verify{ rs.getInt(fieldName); }
        verifyCodeAggregator();
    }

    @Test
    void makingEntity_withNullNumber() throws SQLException {
        final String fieldName = Paper.PAPER.NUMBER.getName();
        validateNullableLongColumn(() -> {
            try {
                return rs.getLong(fieldName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
        verify{ rs.getLong(fieldName); }

        verifyCodeAggregator();
    }

    private void validateNullableIntColumn(Supplier<Integer> suppl) throws SQLException {
        when(suppl.get()).thenReturn(0);
        validateNullableNumberColumn();
    }

    private void validateNullableLongColumn(Supplier<Long> suppl) throws SQLException {
        when(suppl.get()).thenReturn(0L);
        validateNullableNumberColumn();
    }

    private void validateNullableNumberColumn() throws SQLException {
        when(rs.wasNull()).thenReturn(true);
        when(rs.getArray("codes")).thenReturn(
            new MockArray<>(SQLDialect.POSTGRES, new String[] { "1A", "2B" }, String[].class));

        PublicPaper pp = config.makeEntity(rs);

        pp.getNumber().shouldBeNull();
        verify(exactly=5) { rs.wasNull(); }
    }

    @Test
    void assertInternalCodesAreSet() {
        verify{ codeAggregator.setInternalCodes(anyList()); }
    }

    @Test
    @Override
    protected void assertingJobName() {
        verify{ codeAggregator.setInternalCodes(internalCodes); }
    }

    @Test
    @Override
    protected void jobIsRestartable() {
        verify{ codeAggregator.setInternalCodes(internalCodes); }
    }

    @Test
    @Override
    protected void assertingJobIncrementer_toBeRunIdIncrementer() {
        verify{ codeAggregator.setInternalCodes(internalCodes); }
    }

    @Test
    @Override
    protected void assertingSql() {
        selectSql() shouldBeEqualTo expectedSelectSql();
        verify{ codeAggregator.setInternalCodes(internalCodes); }
    }

    @Test
    @Override
    protected void assertingPseudoRefDataEnforcementDdl() {
        final DeleteConditionStep<PaperRecord> dcs = getPseudoFkDcs();
        if (dcs != null)
            dcs.getSQL() shouldBeEqualTo expectedPseudoFkSql();
        else
            expectedPseudoFkSql().shouldBeNull();
        verify{ codeAggregator.setInternalCodes(internalCodes); }
        confirmVerified(jooqPublic);
    }

    @Test
    @Override
    protected void assertingPurgeLastSynchField() {
        lastSynchedField() shouldBeEqualTo expectedLastSyncField();
        verify{ codeAggregator.setInternalCodes(internalCodes); }
    }

}
