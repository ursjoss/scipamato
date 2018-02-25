package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.publ.entity.PopulationCode.ADULTS;
import static ch.difty.scipamato.publ.entity.PopulationCode.CHILDREN;
import static ch.difty.scipamato.publ.entity.PopulationCode.values;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import org.junit.Test;

public class PopulationCodeTest {

    @Test
    public void hasAllvalues() {
        assertThat(values()).containsExactly(CHILDREN, ADULTS);
    }

    @Test
    public void assertIds() {
        assertThat(extractProperty("id").from(values())).containsExactly((short) 1, (short) 2);
    }

    @Test
    public void of_withExistingId() {
        assertThat(PopulationCode.of((short) 1)).hasValue(CHILDREN);
    }

    @Test
    public void of_withNotExistingId_returnsEmptyOptioal() {
        assertThat(PopulationCode.of((short) 0)).isEmpty();
    }

}
