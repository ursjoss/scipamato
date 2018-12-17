package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.core.db.public_.tables.Paper.PAPER;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class SyncShortFieldWithEmptyMainFieldConcatenatorTest {

    private final SyncShortFieldConcatenator sfc = new SyncShortFieldWithEmptyMainFieldConcatenator();

    @Mock
    private ResultSet resultSet;

    @After
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
    public void methods_withNonNullMethod_returnsMethod() throws SQLException {
        when(resultSet.getString(PAPER.METHODS.getName())).thenReturn("method");
        when(resultSet.getString(PAPER.METHOD_STUDY_DESIGN.getName())).thenReturn("msd");
        when(resultSet.getString(PAPER.METHOD_OUTCOME.getName())).thenReturn("mo");
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("pp");
        when(resultSet.getString(PAPER.EXPOSURE_POLLUTANT.getName())).thenReturn("ep");
        when(resultSet.getString(PAPER.EXPOSURE_ASSESSMENT.getName())).thenReturn("ea");
        when(resultSet.getString(PAPER.METHOD_STATISTICS.getName())).thenReturn("ms");
        when(resultSet.getString(PAPER.METHOD_CONFOUNDERS.getName())).thenReturn("mc");

        assertThat(sfc.methodsFrom(resultSet)).isEqualTo("method");

        verifyCallingMethodsFields();
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
    public void methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() throws SQLException {
        when(resultSet.getString(PAPER.METHODS.getName())).thenReturn(null);
        when(resultSet.getString(PAPER.METHOD_STUDY_DESIGN.getName())).thenReturn("msd");
        when(resultSet.getString(PAPER.METHOD_OUTCOME.getName())).thenReturn("mo");
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("pp");
        when(resultSet.getString(PAPER.EXPOSURE_POLLUTANT.getName())).thenReturn("ep");
        when(resultSet.getString(PAPER.EXPOSURE_ASSESSMENT.getName())).thenReturn("ea");
        when(resultSet.getString(PAPER.METHOD_STATISTICS.getName())).thenReturn("ms");
        when(resultSet.getString(PAPER.METHOD_CONFOUNDERS.getName())).thenReturn("mc");

        assertThat(sfc.methodsFrom(resultSet)).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");

        verifyCallingMethodsFields();
    }

    @Test
    public void population_withNonNullPopulation_returnsPopulation() throws SQLException {
        when(resultSet.getString(PAPER.POPULATION.getName())).thenReturn("population");
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("ppl");
        when(resultSet.getString(PAPER.POPULATION_PARTICIPANTS.getName())).thenReturn("ppa");
        when(resultSet.getString(PAPER.POPULATION_DURATION.getName())).thenReturn("pd");

        assertThat(sfc.populationFrom(resultSet)).isEqualTo("population");

        verifyCallingPopulationFields();
    }

    private void verifyCallingPopulationFields() throws SQLException {
        verify(resultSet).getString(PAPER.POPULATION.getName());
        verify(resultSet).getString(PAPER.POPULATION_PLACE.getName());
        verify(resultSet).getString(PAPER.POPULATION_PARTICIPANTS.getName());
        verify(resultSet).getString(PAPER.POPULATION_DURATION.getName());
    }

    @Test
    public void population_withNullPopulation_returnsPopulationShortFieldsConcatenated() throws SQLException {
        when(resultSet.getString(PAPER.POPULATION.getName())).thenReturn(null);
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("ppl");
        when(resultSet.getString(PAPER.POPULATION_PARTICIPANTS.getName())).thenReturn("ppa");
        when(resultSet.getString(PAPER.POPULATION_DURATION.getName())).thenReturn("pd");

        assertThat(sfc.populationFrom(resultSet)).isEqualTo("Place: ppl / Participants: ppa / Study Duration: pd");

        verifyCallingPopulationFields();
    }

    @Test
    public void result_withNonNullResult_returnsResult() throws SQLException {
        when(resultSet.getString(PAPER.RESULT.getName())).thenReturn("result");
        when(resultSet.getString(PAPER.RESULT_EXPOSURE_RANGE.getName())).thenReturn("rer");
        when(resultSet.getString(PAPER.RESULT_EFFECT_ESTIMATE.getName())).thenReturn("ree");
        when(resultSet.getString(PAPER.RESULT_MEASURED_OUTCOME.getName())).thenReturn("rmo");
        when(resultSet.getString(PAPER.CONCLUSION.getName())).thenReturn("cc");

        assertThat(sfc.resultFrom(resultSet)).isEqualTo("result");

        verifyCallingResultFields();
    }

    private void verifyCallingResultFields() throws SQLException {
        verify(resultSet).getString(PAPER.RESULT.getName());
        verify(resultSet).getString(PAPER.RESULT_EXPOSURE_RANGE.getName());
        verify(resultSet).getString(PAPER.RESULT_EFFECT_ESTIMATE.getName());
        verify(resultSet).getString(PAPER.RESULT_MEASURED_OUTCOME.getName());
        verify(resultSet).getString(PAPER.CONCLUSION.getName());
    }

    @Test
    public void result_withNullResult_returnsResultShortFieldsConcatenated() throws SQLException {
        when(resultSet.getString(PAPER.RESULT.getName())).thenReturn(null);
        when(resultSet.getString(PAPER.RESULT_EXPOSURE_RANGE.getName())).thenReturn("rer");
        when(resultSet.getString(PAPER.RESULT_EFFECT_ESTIMATE.getName())).thenReturn("ree");
        when(resultSet.getString(PAPER.RESULT_MEASURED_OUTCOME.getName())).thenReturn("rmo");
        when(resultSet.getString(PAPER.CONCLUSION.getName())).thenReturn("cc");

        assertThat(sfc.resultFrom(resultSet)).isEqualTo(
            "Exposure (Range): rer / Effect Estimate: ree / Measured Outcome: rmo / Conclusion: cc");

        verifyCallingResultFields();
    }

}