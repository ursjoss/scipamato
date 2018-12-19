package ch.difty.scipamato.common.paper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;

@SuppressWarnings("SpellCheckingInspection")
public class AbstractShortFieldConcatenatorWithoutNewLineTest {

    private AbstractShortFieldConcatenator concatenator = new AbstractShortFieldConcatenator(false) {
    };

    @Test
    public void method_withMethodPresent_returnsMethod() {
        assertThat(concatenator.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo("m");
    }

    @Test
    public void method_withMethodNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
    }

    @Test
    public void method_withMethodNullAndSomeShortFieldsNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, "msd", null, "pp", "ep", "ea", "ms", "mc")).isEqualTo(
            "Study Design: msd / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
    }

    @Test
    public void method_withMethodNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(
            concatenator.methodsFrom(null, newT("msd"), newT("mo"), newT("pp"), newT("ep"), newT("ea"), newT("ms"),
                newT("mc"))).isEqualTo("msdl: msd / mol: mo / ppl: pp / epl: ep / eal: ea / msl: ms / mcl: mc");
    }

    @Test
    public void method_withMethodNullAndTupleLabelNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"),
            new AbstractShortFieldConcatenator.Tuple(null, "pp"), newT("ep"), newT("ea"), newT("ms"),
            newT("mc"))).isEqualTo("msdl: msd / mol: mo / pp / epl: ep / eal: ea / msl: ms / mcl: mc");
    }

    @Test
    public void method_withMethodNullAndEntireTupleNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.methodsFrom(null, newT("msd"), newT("mo"), null, newT("ep"), newT("ea"), newT("ms"),
            newT("mc"))).isEqualTo("msdl: msd / mol: mo / epl: ep / eal: ea / msl: ms / mcl: mc");
    }

    private AbstractShortFieldConcatenator.Tuple newT(final String f) {
        return new AbstractShortFieldConcatenator.Tuple(f + "l", f);
    }

    @Test
    public void population_withMethodPresent_returnsMethod() {
        assertThat(concatenator.populationFrom("p", "ppl", "ppa", "pd")).isEqualTo("p");
    }

    @Test
    public void population_withPopulationNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.populationFrom(null, "ppl", "ppa", "pd")).isEqualTo(
            "Place: ppl / Participants: ppa / Study Duration: pd");
    }

    @Test
    public void population_withPopulationNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.populationFrom(null, newT("ppl"), newT("ppa"), newT("pd"))).isEqualTo(
            "ppll: ppl / ppal: ppa / pdl: pd");
    }

    @Test
    public void result_withResultPresent_returnsMethod() {
        assertThat(concatenator.resultFrom("r", "rer", "ree", "rmo", "cc")).isEqualTo("r");
    }

    @Test
    public void result_withResultNull_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, "rer", "ree", "rmo", "cc")).isEqualTo(
            "Exposure (Range): rer / Effect Estimate: ree / Measured Outcome: rmo / Conclusion: cc");
    }

    @Test
    public void result_withResultNull_withExplicitLabels_returnsConcatenatedShortFields() {
        assertThat(concatenator.resultFrom(null, newT("rer"), newT("ree"), newT("rmo"), newT("cc"))).isEqualTo(
            "rerl: rer / reel: ree / rmol: rmo / ccl: cc");
    }

}