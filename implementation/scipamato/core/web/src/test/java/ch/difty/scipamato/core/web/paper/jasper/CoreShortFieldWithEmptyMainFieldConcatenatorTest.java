package ch.difty.scipamato.core.web.paper.jasper;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.Paper;

@SuppressWarnings("SpellCheckingInspection")
class CoreShortFieldWithEmptyMainFieldConcatenatorTest {

    private final CoreShortFieldConcatenator sfc = new CoreShortFieldWithEmptyMainFieldConcatenator();

    private final Paper p = new Paper();

    @SuppressWarnings("SpellCheckingInspection")
    private final ReportHeaderFields rhf = ReportHeaderFields
        .builder("hp", "b")
        .methodStudyDesignLabel("msdl")
        .methodOutcomeLabel("mol")
        .populationPlaceLabel("ppll")
        .exposurePollutantLabel("epl")
        .exposureAssessmentLabel("eal")
        .methodStatisticsLabel("msl")
        .methodConfoundersLabel("mcl")
        .populationDurationLabel("pdl")
        .populationParticipantsLabel("ppal")
        .resultMeasuredOutcomeLabel("rmol")
        .resultEffectEstimateLabel("reel")
        .resultExposureRangeLabel("rerl")
        .conclusionLabel("ccl")
        .build();

    @Test
    void methods_withNullRecordset_throws() {
        assertDegenerateSupplierParameter(() -> sfc.methodsFrom(null, rhf), "paper");
    }

    @Test
    void population_withNullRecordset_throws() {
        assertDegenerateSupplierParameter(() -> sfc.populationFrom(null, rhf), "paper");
    }

    @Test
    void result_withNullRecordset_throws() {
        assertDegenerateSupplierParameter(() -> sfc.resultFrom(null, rhf), "paper");
    }

    @Test
    void methods_withNonNullMethod_returnsMethod() {
        p.setMethods("method");
        p.setMethodStudyDesign("msd");
        p.setMethodOutcome("mo");
        p.setPopulationPlace("pp");
        p.setExposurePollutant("ep");
        p.setExposureAssessment("ea");
        p.setMethodStatistics("ms");
        p.setMethodConfounders("mc");

        assertThat(sfc.methodsFrom(p, rhf)).isEqualTo("method");
    }

    @Test
    void methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() {
        p.setMethods(null);
        p.setMethodStudyDesign("msd");
        p.setMethodOutcome("mo");
        p.setPopulationPlace("pp");
        p.setExposurePollutant("ep");
        p.setExposureAssessment("ea");
        p.setMethodStatistics("ms");
        p.setMethodConfounders("mc");

        assertThat(sfc.methodsFrom(p, rhf)).isEqualTo(
            "msdl: msd\nmol: mo\nppll: pp\nepl: ep\neal: ea\nmsl: ms\nmcl: mc");
    }

    @Test
    void population_withNonNullPopulation_returnsPopulation() {
        p.setPopulation("population");
        p.setPopulationPlace("ppl");
        p.setPopulationParticipants("ppa");
        p.setPopulationDuration("pd");

        assertThat(sfc.populationFrom(p, rhf)).isEqualTo("population");
    }

    @Test
    void population_withNullPopulation_returnsPopulationShortFieldsConcatenated() {
        p.setPopulation(null);
        p.setPopulationPlace("ppl");
        p.setPopulationParticipants("ppa");
        p.setPopulationDuration("pd");

        assertThat(sfc.populationFrom(p, rhf)).isEqualTo("ppll: ppl\nppal: ppa\npdl: pd");
    }

    @Test
    void result_withNonNullResult_returnsResult() {
        p.setResult("result");
        p.setResultMeasuredOutcome("rmo");
        p.setResultExposureRange("rer");
        p.setResultEffectEstimate("ree");
        p.setConclusion("cc");

        assertThat(sfc.resultFrom(p, rhf)).isEqualTo("result");
    }

    @Test
    void result_withNullResult_returnsResultShortFieldsConcatenated() {
        p.setResult(null);
        p.setResultMeasuredOutcome("rmo");
        p.setResultExposureRange("rer");
        p.setResultEffectEstimate("ree");
        p.setConclusion("cc");

        assertThat(sfc.resultFrom(p, rhf)).isEqualTo("rmol: rmo\nrerl: rer\nreel: ree\nccl: cc");
    }

}