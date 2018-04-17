package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.publ.entity.StudyDesignCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import org.junit.Test;

public class StudyDesignCodeTest {

    @Test
    public void hasAllValues() {
        assertThat(values()).containsExactly(EXPERIMENTAL, EPIODEMIOLOGICAL, OVERVIEW_METHODOLOGY);
    }

    @Test
    public void assertIds() {
        assertThat(extractProperty("id").from(values())).containsExactly((short) 1, (short) 2, (short) 3);
    }

    @Test
    public void of_withExistingId() {
        assertThat(StudyDesignCode.of((short) 1)).hasValue(EXPERIMENTAL);
    }

    @Test
    public void of_withNotExistingId_returnsEmptyOptional() {
        assertThat(StudyDesignCode.of((short) 0)).isEmpty();
    }
}
