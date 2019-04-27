package ch.difty.scipamato.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ModeTest {

    @Test
    void testValues() {
        assertThat(Mode.values()).containsExactly(Mode.EDIT, Mode.VIEW, Mode.SEARCH);
    }
}
