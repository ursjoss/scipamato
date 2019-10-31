package ch.difty.scipamato.common.paper

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class AbstractShortFieldConcatenatorWithoutNewLineTest {

    private val concatenator = object : AbstractShortFieldConcatenator(false) {
    }

    @Test
    fun method_withMethodPresent_returnsMethod() {
        assertThat(concatenator.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo("m")
    }

    @Test
    fun method_withMethodNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc"))
            .isEqualTo(
                "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / " +
                    "Exposure Assessment: ea / Statistical Method: ms / Confounders: mc"
            )
    }

    @Test
    fun method_withMethodNullAndSomeShortFieldsNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", null, "pp", "ep", "ea", "ms", "mc"))
            .isEqualTo(
                "Study Design: msd / Place: pp / Pollutant: ep / Exposure Assessment: " +
                    "ea / Statistical Method: ms / Confounders: mc"
            )
    }

    @Test
    fun method_withMethodNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(
            concatenator.methodsFrom(null, newT("msd"), newT("mo"), newT("pp"), newT("ep"), newT("ea"), newT("ms"),
                newT("mc"))).isEqualTo("msdl: msd / mol: mo / ppl: pp / epl: ep / eal: ea / msl: ms / mcl: mc")
    }

    @Test
    fun method_withMethodNullAndTupleLabelNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"),
            AbstractShortFieldConcatenator.Tuple(null, "pp"), newT("ep"), newT("ea"), newT("ms"),
            newT("mc"))).isEqualTo("msdl: msd / mol: mo / pp / epl: ep / eal: ea / msl: ms / mcl: mc")
    }

    @Test
    fun method_withMethodNullAndEntireTupleNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"), null, newT("ep"), newT("ea"), newT("ms"),
            newT("mc"))).isEqualTo("msdl: msd / mol: mo / epl: ep / eal: ea / msl: ms / mcl: mc")
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
            "Place: ppl / Participants: ppa / Study Duration: pd")
    }

    @Test
    fun population_withPopulationNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.populationFrom(null, newT("ppl"), newT("ppa"), newT("pd"))).isEqualTo(
            "ppll: ppl / ppal: ppa / pdl: pd")
    }

    @Test
    fun result_withResultPresent_returnsMethod() {
        assertThat(concatenator.resultFrom("r", "rmo", "rer", "ree", "cc")).isEqualTo("r")
    }

    @Test
    fun result_withResultNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, "rmo", "rer", "ree", "cc")).isEqualTo(
            "Measured Outcome: rmo / Exposure (Range): rer / Effect Estimate: ree / Conclusion: cc")
    }

    @Test
    fun result_withResultNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, newT("rmo"), newT("rer"), newT("ree"), newT("cc"))).isEqualTo(
            "rmol: rmo / rerl: rer / reel: ree / ccl: cc")
    }
}
