package ch.difty.scipamato.core.web.paper.jasper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.sql.SQLException;

import org.junit.Test;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.core.entity.Paper;

public class CoreShortFieldWithEmptyMainFieldConcatenatorTest {

    private final CoreShortFieldConcatenator sfc = new CoreShortFieldWithEmptyMainFieldConcatenator();

    private final Paper p = new Paper();

    @Test
    public void methods_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.methodsFrom(null), "paper");
    }

    @Test
    public void population_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.populationFrom(null), "paper");
    }

    @Test
    public void result_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.resultFrom(null), "paper");
    }

    @Test
    public void methods_withNonNullMethod_returnsMethod() throws SQLException {
        p.setMethods("method");
        p.setMethodStudyDesign("msd");
        p.setMethodOutcome("mo");
        p.setPopulationPlace("pp");
        p.setExposurePollutant("ep");
        p.setExposureAssessment("ea");
        p.setMethodStatistics("ms");
        p.setMethodConfounders("mc");

        assertThat(sfc.methodsFrom(p)).isEqualTo("method");
    }

    @Test
    public void methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() throws SQLException {
        p.setMethods(null);
        p.setMethodStudyDesign("msd");
        p.setMethodOutcome("mo");
        p.setPopulationPlace("pp");
        p.setExposurePollutant("ep");
        p.setExposureAssessment("ea");
        p.setMethodStatistics("ms");
        p.setMethodConfounders("mc");

        assertThat(sfc.methodsFrom(p)).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
    }

    @Test
    public void population_withNonNullPopulation_returnsPopulation() throws SQLException {
        p.setPopulation("population");
        p.setPopulationPlace("ppl");
        p.setPopulationParticipants("ppa");
        p.setPopulationDuration("pd");

        assertThat(sfc.populationFrom(p)).isEqualTo("population");
    }

    @Test
    public void population_withNullPopulation_returnsPopulationShortFieldsConcatenated() throws SQLException {
        p.setPopulation(null);
        p.setPopulationPlace("ppl");
        p.setPopulationParticipants("ppa");
        p.setPopulationDuration("pd");

        assertThat(sfc.populationFrom(p)).isEqualTo("Place: ppl / Participants: ppa / Study Duration: pd");
    }

    @Test
    public void result_withNonNullResult_returnsResult() throws SQLException {
        p.setResult("result");
        p.setResultExposureRange("rer");
        p.setResultEffectEstimate("ree");
        p.setResultMeasuredOutcome("rmo");
        p.setConclusion("cc");

        assertThat(sfc.resultFrom(p)).isEqualTo("result");
    }

    @Test
    public void result_withNullResult_returnsResultShortFieldsConcatenated() throws SQLException {
        p.setResult(null);
        p.setResultExposureRange("rer");
        p.setResultEffectEstimate("ree");
        p.setResultMeasuredOutcome("rmo");
        p.setConclusion("cc");

        assertThat(sfc.resultFrom(p)).isEqualTo(
            "Exposure (Range): rer / Effect Estimate: ree / Measured Outcome: rmo / Conclusion: cc");
    }

}