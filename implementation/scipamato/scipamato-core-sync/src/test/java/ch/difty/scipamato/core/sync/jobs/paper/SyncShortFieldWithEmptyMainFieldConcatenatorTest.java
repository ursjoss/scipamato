package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.core.db.public_.tables.Paper.PAPER;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jooq.TableField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.core.db.public_.tables.records.PaperRecord;

@ExtendWith(MockitoExtension.class)
public class SyncShortFieldWithEmptyMainFieldConcatenatorTest {

    private final SyncShortFieldConcatenator sfc = new SyncShortFieldWithEmptyMainFieldConcatenator();

    @Mock
    private ResultSet resultSet;

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void methods_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.methodsFrom(null), "rs");
    }

    @Test
    public void population_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.populationFrom(null), "rs");
    }

    @Test
    public void result_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.resultFrom(null), "rs");
    }

    @Test
    void methods_withNonNullMethod_returnsMethod() throws SQLException {
        stubMethodFieldsWithMainFieldReturning("method");
        assertThat(sfc.methodsFrom(resultSet)).isEqualTo("method");
        verifyCallingMethodsFields();
    }

    @Test
    void methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() throws SQLException {
        stubMethodFieldsWithMainFieldReturning(null);
        assertThat(sfc.methodsFrom(resultSet)).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
        verifyCallingMethodsFields();
    }

    private void stubMethodFieldsWithMainFieldReturning(final String mainFixture) throws SQLException {
        when(resultSet.getString(anyString())).thenAnswer(invocationsOnMock -> {
            String fieldName = (String) invocationsOnMock.getArguments()[0];
            if (fieldName.equals(PAPER.METHODS.getName()))
                return mainFixture;
            else if (fieldName.equals(PAPER.METHOD_STUDY_DESIGN.getName()))
                return "msd";
            else if (fieldName.equals(PAPER.METHOD_OUTCOME.getName()))
                return "mo";
            else if (fieldName.equals(PAPER.POPULATION_PLACE.getName()))
                return "pp";
            else if (fieldName.equals(PAPER.EXPOSURE_POLLUTANT.getName()))
                return "ep";
            else if (fieldName.equals(PAPER.EXPOSURE_ASSESSMENT.getName()))
                return "ea";
            else if (fieldName.equals(PAPER.METHOD_STATISTICS.getName()))
                return "ms";
            else if (fieldName.equals(PAPER.METHOD_CONFOUNDERS.getName()))
                return "mc";
            else
                throw new IllegalArgumentException("unhandled stub");
        });
    }

    private void verifyCallingMethodsFields() throws SQLException {
        verify(resultSet).getString(PAPER.METHODS.getName());
        verify(resultSet).getString(PAPER.METHOD_STUDY_DESIGN.getName());
        verify(resultSet).getString(PAPER.METHOD_OUTCOME.getName());
        verify(resultSet).getString(PAPER.POPULATION_PLACE.getName());
        verify(resultSet).getString(PAPER.EXPOSURE_POLLUTANT.getName());
        verify(resultSet).getString(PAPER.EXPOSURE_ASSESSMENT.getName());
        verify(resultSet).getString(PAPER.METHOD_STATISTICS.getName());
        verify(resultSet).getString(PAPER.METHOD_CONFOUNDERS.getName());
    }

    @Test
    void population_withNonNullPopulation_returnsPopulation() throws SQLException {
        stubPopulationFieldsWithMainFieldReturning("population");
        assertThat(sfc.populationFrom(resultSet)).isEqualTo("population");
        verifyCallingPopulationFields();
    }

    @Test
    void population_withNullPopulation_returnsPopulationShortFieldsConcatenated() throws SQLException {
        stubPopulationFieldsWithMainFieldReturning(null);
        assertThat(sfc.populationFrom(resultSet)).isEqualTo("Place: ppl / Participants: ppa / Study Duration: pd");
        verifyCallingPopulationFields();
    }

    private void stubPopulationFieldsWithMainFieldReturning(final String mainFixture) throws SQLException {
        when(resultSet.getString(anyString())).thenAnswer(invocationsOnMock -> {
            String fieldName = (String) invocationsOnMock.getArguments()[0];
            if (fieldName.equals(PAPER.POPULATION.getName()))
                return mainFixture;
            else if (fieldName.equals(PAPER.POPULATION_PLACE.getName()))
                return "ppl";
            else if (fieldName.equals(PAPER.POPULATION_PARTICIPANTS.getName()))
                return "ppa";
            else if (fieldName.equals(PAPER.POPULATION_DURATION.getName()))
                return "pd";
            else
                throw new IllegalArgumentException("unhandled stub");
        });
    }

    private void verifyCallingPopulationFields() throws SQLException {
        verify(resultSet).getString(PAPER.POPULATION.getName());
        verify(resultSet).getString(PAPER.POPULATION_PLACE.getName());
        verify(resultSet).getString(PAPER.POPULATION_PARTICIPANTS.getName());
        verify(resultSet).getString(PAPER.POPULATION_DURATION.getName());
    }

    @Test
    void result_withNonNullResult_returnsResult() throws SQLException {
        stubResultFieldsWithMainFieldReturning("result");
        assertThat(sfc.resultFrom(resultSet)).isEqualTo("result");
        verifyCallingResultFields();
    }

    @Test
    void result_withNullResult_returnsResultShortFieldsConcatenated() throws SQLException {
        stubResultFieldsWithMainFieldReturning(null);
        assertThat(sfc.resultFrom(resultSet)).isEqualTo(
            "Measured Outcome: rmo / Exposure (Range): rer / Effect Estimate: ree / Conclusion: cc");
        verifyCallingResultFields();
    }

    private void stubResultFieldsWithMainFieldReturning(final String mainFixture) throws SQLException {
        when(resultSet.getString(anyString())).thenAnswer(invocationsOnMock -> {
            String fieldName = (String) invocationsOnMock.getArguments()[0];
            if (fieldName.equals(PAPER.RESULT.getName()))
                return mainFixture;
            else if (fieldName.equals(PAPER.RESULT_MEASURED_OUTCOME.getName()))
                return "rmo";
            else if (fieldName.equals(PAPER.RESULT_EXPOSURE_RANGE.getName()))
                return "rer";
            else if (fieldName.equals(PAPER.RESULT_EFFECT_ESTIMATE.getName()))
                return "ree";
            else if (fieldName.equals(PAPER.CONCLUSION.getName()))
                return "cc";
            else
                throw new IllegalArgumentException("unhandled stub");
        });
    }

    private void verifyCallingResultFields() throws SQLException {
        verify(resultSet).getString(PAPER.RESULT.getName());
        verify(resultSet).getString(PAPER.RESULT_MEASURED_OUTCOME.getName());
        verify(resultSet).getString(PAPER.RESULT_EXPOSURE_RANGE.getName());
        verify(resultSet).getString(PAPER.RESULT_EFFECT_ESTIMATE.getName());
        verify(resultSet).getString(PAPER.CONCLUSION.getName());
    }

    private final SyncShortFieldConcatenator throwingConcatenator = new SyncShortFieldWithEmptyMainFieldConcatenator() {
        @Override
        String methodsFrom(final ResultSet rs, final TableField<PaperRecord, String> methodField,
            final TableField<PaperRecord, String> methodStudyDesignField,
            final TableField<PaperRecord, String> methodOutcomeField,
            final TableField<PaperRecord, String> populationPlaceField,
            final TableField<PaperRecord, String> exposurePollutantField,
            final TableField<PaperRecord, String> exposureAssessmentField,
            final TableField<PaperRecord, String> methodStatisticsField,
            final TableField<PaperRecord, String> methodConfoundersField) throws SQLException {
            throw new SQLException("boom");
        }

        @Override
        String populationFrom(final ResultSet rs, final TableField<PaperRecord, String> populationField,
            final TableField<PaperRecord, String> populationPlaceField,
            final TableField<PaperRecord, String> populationParticipantsField,
            final TableField<PaperRecord, String> populationDurationField) throws SQLException {
            throw new SQLException("boom");
        }

        @Override
        String resultFrom(final ResultSet rs, final TableField<PaperRecord, String> resultField,
            final TableField<PaperRecord, String> resultMeasuredOutcomeField,
            final TableField<PaperRecord, String> resultExposureRangeField,
            final TableField<PaperRecord, String> resultEffectEstimateField,
            final TableField<PaperRecord, String> conclusionField) throws SQLException {
            throw new SQLException("boom");
        }
    };

    @Test
    public void methodsFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.methodsFrom(mock(ResultSet.class))).isNull();
    }

    @Test
    public void polulationFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.populationFrom(mock(ResultSet.class))).isNull();
    }

    @Test
    public void resultFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.resultFrom(mock(ResultSet.class))).isNull();
    }

}