package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.publ.entity.StudyDesignCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import org.junit.jupiter.api.Test;

class StudyDesignCodeTest {

    @Test
    void hasAllValues() {
        assertThat(values()).containsExactly(EXPERIMENTAL, EPIDEMIOLOGICAL, OVERVIEW_METHODOLOGY);
    }

    @Test
    void assertIds() {
        assertThat(extractProperty("id").from(values())).containsExactly((short) 1, (short) 2, (short) 3);
    }

    @Test
    void of_withExistingId() {
        assertThat(StudyDesignCode.of((short) 1)).hasValue(EXPERIMENTAL);
    }

    @Test
    void of_withNotExistingId_returnsEmptyOptional() {
        assertThat(StudyDesignCode.of((short) 0)).isEmpty();
    }
}
