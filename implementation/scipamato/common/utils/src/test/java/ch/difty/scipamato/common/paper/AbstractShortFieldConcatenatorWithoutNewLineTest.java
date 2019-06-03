package ch.difty.scipamato.common.paper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("SpellCheckingInspection")
class AbstractShortFieldConcatenatorWithoutNewLineTest {

    private AbstractShortFieldConcatenator concatenator = new AbstractShortFieldConcatenator(false) {
    };

    @Test
    void method_withMethodPresent_returnsMethod() {
        assertThat(concatenator.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo("m");
    }

    @Test
    void method_withMethodNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
    }

    @Test
    void method_withMethodNullAndSomeShortFieldsNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", null, "pp", "ep", "ea", "ms", "mc")).isEqualTo(
            "Study Design: msd / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
    }

    @Test
    void method_withMethodNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(
            concatenator.methodsFrom(null, newT("msd"), newT("mo"), newT("pp"), newT("ep"), newT("ea"), newT("ms"),
                newT("mc"))).isEqualTo("msdl: msd / mol: mo / ppl: pp / epl: ep / eal: ea / msl: ms / mcl: mc");
    }

    @Test
    void method_withMethodNullAndTupleLabelNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"),
            new AbstractShortFieldConcatenator.Tuple(null, "pp"), newT("ep"), newT("ea"), newT("ms"),
            newT("mc"))).isEqualTo("msdl: msd / mol: mo / pp / epl: ep / eal: ea / msl: ms / mcl: mc");
    }

    @Test
    void method_withMethodNullAndEntireTupleNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"), null, newT("ep"), newT("ea"), newT("ms"),
            newT("mc"))).isEqualTo("msdl: msd / mol: mo / epl: ep / eal: ea / msl: ms / mcl: mc");
    }

    private AbstractShortFieldConcatenator.Tuple newT(final String f) {
        return new AbstractShortFieldConcatenator.Tuple(f + "l", f);
    }

    @Test
    void population_withMethodPresent_returnsMethod() {
        assertThat(concatenator.populationFrom("p", "ppl", "ppa", "pd")).isEqualTo("p");
    }

    @Test
    void population_withPopulationNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.populationFrom(null, "ppl", "ppa", "pd")).isEqualTo(
            "Place: ppl / Participants: ppa / Study Duration: pd");
    }

    @Test
    void population_withPopulationNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.populationFrom(null, newT("ppl"), newT("ppa"), newT("pd"))).isEqualTo(
            "ppll: ppl / ppal: ppa / pdl: pd");
    }

    @Test
    void result_withResultPresent_returnsMethod() {
        assertThat(concatenator.resultFrom("r", "rmo", "rer", "ree", "cc")).isEqualTo("r");
    }

    @Test
    void result_withResultNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, "rmo", "rer", "ree", "cc")).isEqualTo(
            "Measured Outcome: rmo / Exposure (Range): rer / Effect Estimate: ree / Conclusion: cc");
    }

    @Test
    void result_withResultNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, newT("rmo"), newT("rer"), newT("ree"), newT("cc"))).isEqualTo(
            "rmol: rmo / rerl: rer / reel: ree / ccl: cc");
    }

}