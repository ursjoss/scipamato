package ch.difty.scipamato.common.paper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class AbstractShortFieldConcatenatorWithNewLineTest {

    private val concatenator = object : AbstractShortFieldConcatenator(true) {
    }

    @Test
    fun method_withMethodPresent_returnsMethod() {
        assertThat(concatenator.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo("m")
    }

    @Test
    fun method_withMethodNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo(
                "Study Design: msd\nOutcome: mo\nPlace: pp\nPollutant: ep\nExposure Assessment: ea\nStatistical Method: ms\nConfounders: mc")
    }

    @Test
    fun method_withMethodNullAndSomeShortFieldsNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", null, "pp", "ep", "ea", "ms", "mc")).isEqualTo(
                "Study Design: msd\nPlace: pp\nPollutant: ep\nExposure Assessment: ea\nStatistical Method: ms\nConfounders: mc")
    }

    @Test
    fun method_withMethodNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(
                concatenator.methodsFrom(null, newT("msd"), newT("mo"), newT("pp"), newT("ep"), newT("ea"), newT("ms"),
                        newT("mc"))).isEqualTo("msdl: msd\nmol: mo\nppl: pp\nepl: ep\neal: ea\nmsl: ms\nmcl: mc")
    }

    @Test
    fun method_withMethodNullAndTupleLabelNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"),
                AbstractShortFieldConcatenator.Tuple(null, "pp"), newT("ep"), newT("ea"), newT("ms"),
                newT("mc"))).isEqualTo("msdl: msd\nmol: mo\npp\nepl: ep\neal: ea\nmsl: ms\nmcl: mc")
    }

    @Test
    fun method_withMethodNullAndEntireTupleNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"), null, newT("ep"), newT("ea"), newT("ms"),
                newT("mc"))).isEqualTo("msdl: msd\nmol: mo\nepl: ep\neal: ea\nmsl: ms\nmcl: mc")
    }

    private fun newT(f: String): AbstractShortFieldConcatenator.Tuple {
        return AbstractShortFieldConcatenator.Tuple(f + "l", f)
    }

    @Test
    fun population_withMethodPresent_returnsMethod() {
        assertThat(concatenator.populationFrom("p", "ppl", "ppa", "pd")).isEqualTo("p")
    }

    @Test
    fun population_withPopulationNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.populationFrom(null, "ppl", "ppa", "pd")).isEqualTo(
                "Place: ppl\nParticipants: ppa\nStudy Duration: pd")
    }

    @Test
    fun population_withPopulationNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.populationFrom(null, newT("ppl"), newT("ppa"), newT("pd"))).isEqualTo(
                "ppll: ppl\nppal: ppa\npdl: pd")
    }

    @Test
    fun result_withResultPresent_returnsMethod() {
        assertThat(concatenator.resultFrom("r", "rmo", "rer", "ree", "cc")).isEqualTo("r")
    }

    @Test
    fun result_withResultNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, "rmo", "rer", "ree", "cc")).isEqualTo(
                "Measured Outcome: rmo\nExposure (Range): rer\nEffect Estimate: ree\nConclusion: cc")
    }

    @Test
    fun result_withResultNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, newT("rmo"), newT("rer"), newT("ree"), newT("cc"))).isEqualTo(
                "rmol: rmo\nrerl: rer\nreel: ree\nccl: cc")
    }

}