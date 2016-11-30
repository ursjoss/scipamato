package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BooleanSearchTermTest {

    private static final String KEY = "k";

    private BooleanSearchTerm st;

    private void assertTerm(boolean value, String raw) {
        assertThat(st.getKey()).isEqualTo(KEY);
        assertThat(st.getValue()).isEqualTo(value);
        assertThat(st.getRawValue()).isEqualTo(raw);
        assertThat(st.toString()).isEqualTo((!value ? "-" : "") + KEY);
    }

    @Test
    public void ifTrue() {
        final String raw = "true";
        st = new BooleanSearchTerm(KEY, raw);
        assertTerm(true, raw);
    }

    @Test
    public void ifTrue_withSpaces() {
        final String raw = " true   ";
        st = new BooleanSearchTerm(KEY, raw);
        assertTerm(true, raw);
    }

    @Test
    public void ifFalse() {
        final String raw = "false";
        st = new BooleanSearchTerm(KEY, raw);
        assertTerm(false, raw);
    }

    @Test
    public void ifFalse_withSpaces() {
        final String raw = " false  ";
        st = new BooleanSearchTerm(KEY, raw);
        assertTerm(false, raw);
    }
}
