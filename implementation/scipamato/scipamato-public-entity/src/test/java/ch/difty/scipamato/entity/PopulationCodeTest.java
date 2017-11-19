package ch.difty.scipamato.entity;

import static ch.difty.scipamato.entity.PopulationCode.*;
import static org.assertj.core.api.Assertions.*;

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
        assertThat(PopulationCode.of((short) 1)).isEqualTo(CHILDREN);
    }

    @Test
    public void of_withNotExistingId_throws() {
        try {
            PopulationCode.of((short) 0);
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("No matching type for id 0");
        }
    }

}
