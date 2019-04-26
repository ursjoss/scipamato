package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.publ.entity.PopulationCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import org.junit.jupiter.api.Test;

class PopulationCodeTest {

    @Test
    void hasAllValues() {
        assertThat(values()).containsExactly(CHILDREN, ADULTS);
    }

    @Test
    void assertIds() {
        assertThat(extractProperty("id").from(values())).containsExactly((short) 1, (short) 2);
    }

    @Test
    void of_withExistingId() {
        assertThat(PopulationCode.of((short) 1)).hasValue(CHILDREN);
    }

    @Test
    void of_withNotExistingId_returnsEmptyOptional() {
        assertThat(PopulationCode.of((short) 0)).isEmpty();
    }

}
