package ch.difty.scipamato.web;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class ModeTest {

    @Test
    public void testValues() {
        assertThat(Mode.values()).containsExactly(Mode.EDIT, Mode.VIEW, Mode.SEARCH);
    }
}
