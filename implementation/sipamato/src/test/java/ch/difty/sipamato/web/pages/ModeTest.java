package ch.difty.sipamato.web.pages;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ModeTest {

    @Test
    public void testValues() {
        assertThat(Mode.values()).containsExactly(Mode.EDIT, Mode.VIEW, Mode.SEARCH);
    }
}
