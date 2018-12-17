package ch.difty.scipamato.common.paper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;

public class AbstractShortFieldConcatenatorTest {

    private AbstractShortFieldConcatenator concWithLabels    = new AbstractShortFieldConcatenator(true) {
    };
    private AbstractShortFieldConcatenator concWithoutLabels = new AbstractShortFieldConcatenator(false) {
    };

    @Test
    public void method_withLabels_withMethodPresent_returnsMethod() {
        assertThat(concWithLabels.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo("m");
    }

    @Test
    public void method_withoutLabels_withMethodPresent_returnsMethod() {
        assertThat(concWithoutLabels.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo("m");
    }

    @Test
    public void method_withLabels_withMethodNull_returnsConcatenadShortfields() {
        assertThat(concWithLabels.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");
    }

    @Test
    public void method_withoutLabels_withMethodNull_returnsConcatenadShortfields() {
        assertThat(concWithoutLabels.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc")).isEqualTo(
            "msd / mo / pp / ep / ea / ms / mc");
    }

    @Test
    public void population_withLabels_withMethodPresent_returnsMethod() {
        assertThat(concWithLabels.populationFrom("p", "ppl", "ppa", "pd")).isEqualTo("p");
    }

    @Test
    public void population_withoutLabels_withMethodPresent_returnsMethod() {
        assertThat(concWithoutLabels.populationFrom("p", "ppl", "ppa", "pd")).isEqualTo("p");
    }

    @Test
    public void population_withLabels_withPopulationNull_returnsConcatenadShortfields() {
        assertThat(concWithLabels.populationFrom(null, "ppl", "ppa", "pd")).isEqualTo(
            "Place: ppl / Participants: ppa / Study Duration: pd");
    }

    @Test
    public void population_withoutLabels_withPopulationNull_returnsConcatenadShortfields() {
        assertThat(concWithoutLabels.populationFrom(null, "ppl", "ppa", "pd")).isEqualTo("ppl / ppa / pd");
    }

    @Test
    public void result_withLabels_withResultPresent_returnsMethod() {
        assertThat(concWithLabels.resultFrom("r", "rer", "ree", "rmo", "cc")).isEqualTo("r");
    }

    @Test
    public void result_withoutLabels_withResultPresent_returnsMethod() {
        assertThat(concWithoutLabels.resultFrom("r", "rer", "ree", "rmo", "cc")).isEqualTo("r");
    }

    @Test
    public void result_withLabels_withResultNull_returnsConcatenadShortfields() {
        assertThat(concWithLabels.resultFrom(null, "rer", "ree", "rmo", "cc")).isEqualTo(
            "Exposure (Range): rer / Effect Estimate: ree / Measured Outcome: rmo / Conclusion: cc");
    }

    @Test
    public void result_withoutLabels_withResultNull_returnsConcatenadShortfields() {
        assertThat(concWithoutLabels.resultFrom(null, "rer", "ree", "rmo", "cc")).isEqualTo("rer / ree / rmo / cc");
    }

}