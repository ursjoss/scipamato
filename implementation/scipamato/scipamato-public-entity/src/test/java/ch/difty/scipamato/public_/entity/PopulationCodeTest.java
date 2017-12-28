package ch.difty.scipamato.public_.entity;

import static ch.difty.scipamato.public_.entity.PopulationCode.ADULTS;
import static ch.difty.scipamato.public_.entity.PopulationCode.CHILDREN;
import static ch.difty.scipamato.public_.entity.PopulationCode.values;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.assertj.core.api.Assertions.fail;

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
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No matching type for id 0");
        }
    }

}
