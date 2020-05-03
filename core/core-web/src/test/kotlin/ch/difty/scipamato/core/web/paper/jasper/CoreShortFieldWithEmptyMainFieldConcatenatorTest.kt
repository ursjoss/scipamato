package ch.difty.scipamato.core.web.paper.jasper

import ch.difty.scipamato.core.entity.Paper
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class CoreShortFieldWithEmptyMainFieldConcatenatorTest {

    private val sfc: CoreShortFieldConcatenator = CoreShortFieldWithEmptyMainFieldConcatenator()
    private val p = Paper()
    private val rhf = ReportHeaderFields
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
        .build()

    @Test
    fun methods_withNonNullMethod_returnsMethod() {
        p.methods = "method"
        p.methodStudyDesign = "msd"
        p.methodOutcome = "mo"
        p.populationPlace = "pp"
        p.exposurePollutant = "ep"
        p.exposureAssessment = "ea"
        p.methodStatistics = "ms"
        p.methodConfounders = "mc"
        sfc.methodsFrom(p, rhf) shouldBeEqualTo "method"
    }

    @Test
    fun methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() {
        p.methods = null
        p.methodStudyDesign = "msd"
        p.methodOutcome = "mo"
        p.populationPlace = "pp"
        p.exposurePollutant = "ep"
        p.exposureAssessment = "ea"
        p.methodStatistics = "ms"
        p.methodConfounders = "mc"
        sfc.methodsFrom(p, rhf) shouldBeEqualTo
            "msdl: msd\nmol: mo\nppll: pp\nepl: ep\neal: ea\nmsl: ms\nmcl: mc"
    }

    @Test
    fun population_withNonNullPopulation_returnsPopulation() {
        p.population = "population"
        p.populationPlace = "ppl"
        p.populationParticipants = "ppa"
        p.populationDuration = "pd"
        sfc.populationFrom(p, rhf) shouldBeEqualTo "population"
    }

    @Test
    fun population_withNullPopulation_returnsPopulationShortFieldsConcatenated() {
        p.population = null
        p.populationPlace = "ppl"
        p.populationParticipants = "ppa"
        p.populationDuration = "pd"
        sfc.populationFrom(p, rhf) shouldBeEqualTo "ppll: ppl\nppal: ppa\npdl: pd"
    }

    @Test
    fun result_withNonNullResult_returnsResult() {
        p.result = "result"
        p.resultMeasuredOutcome = "rmo"
        p.resultExposureRange = "rer"
        p.resultEffectEstimate = "ree"
        p.conclusion = "cc"
        sfc.resultFrom(p, rhf) shouldBeEqualTo "result"
    }

    @Test
    fun result_withNullResult_returnsResultShortFieldsConcatenated() {
        p.result = null
        p.resultMeasuredOutcome = "rmo"
        p.resultExposureRange = "rer"
        p.resultEffectEstimate = "ree"
        p.conclusion = "cc"
        sfc.resultFrom(p, rhf) shouldBeEqualTo "rmol: rmo\nrerl: rer\nreel: ree\nccl: cc"
    }
}
