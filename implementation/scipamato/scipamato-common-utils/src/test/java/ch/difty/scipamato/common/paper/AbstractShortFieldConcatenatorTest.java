package ch.difty.scipamato.common.paper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;

public class AbstractShortFieldConcatenatorTest {

    private AbstractShortFieldConcatenator concWithLabels = new AbstractShortFieldConcatenator() {
    };

    @Test
    public void method_withMethodPresent_returnsMethod() {
        assertThat(concWithLabels.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo("m");
    }

    @Test
    public void method_withMethodNull_returnsConcatenadShortfields() {
        assertThat(concWithLabels.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
    }

    @Test
    public void method_withMethodNull_withExplicitLabels_returnsConcatenadShortfields() {
        assertThat(
            concWithLabels.methodsFrom(null, newT("msd"), newT("mo"), newT("pp"), newT("ep"), newT("ea"), newT("ms"),
                newT("mc"))).isEqualTo("msdl: msd / mol: mo / ppl: pp / epl: ep / eal: ea / msl: ms / mcl: mc");
    }

    private AbstractShortFieldConcatenator.Tuple newT(final String f) {
        return new AbstractShortFieldConcatenator.Tuple(f + "l", f);
    }

    @Test
    public void population_withMethodPresent_returnsMethod() {
        assertThat(concWithLabels.populationFrom("p", "ppl", "ppa", "pd")).isEqualTo("p");
    }

    @Test
    public void population_withPopulationNull_returnsConcatenadShortfields() {
        assertThat(concWithLabels.populationFrom(null, "ppl", "ppa", "pd")).isEqualTo(
            "Place: ppl / Participants: ppa / Study Duration: pd");
    }

    @Test
    public void population_withPopulationNull_withExplicitLabels_returnsConcatenadShortfields() {
        assertThat(concWithLabels.populationFrom(null, newT("ppl"), newT("ppa"), newT("pd"))).isEqualTo(
            "ppll: ppl / ppal: ppa / pdl: pd");
    }

    @Test
    public void result_withResultPresent_returnsMethod() {
        assertThat(concWithLabels.resultFrom("r", "rer", "ree", "rmo", "cc")).isEqualTo("r");
    }

    @Test
    public void result_withResultNull_returnsConcatenadShortfields() {
        assertThat(concWithLabels.resultFrom(null, "rer", "ree", "rmo", "cc")).isEqualTo(
            "Exposure (Range): rer / Effect Estimate: ree / Measured Outcome: rmo / Conclusion: cc");
    }

    @Test
    public void result_withResultNull_withExplicitLabels_returnsConcatenadShortfields() {
        assertThat(concWithLabels.resultFrom(null, newT("rer"), newT("ree"), newT("rmo"), newT("cc"))).isEqualTo(
            "rerl: rer / reel: ree / rmol: rmo / ccl: cc");
    }

}