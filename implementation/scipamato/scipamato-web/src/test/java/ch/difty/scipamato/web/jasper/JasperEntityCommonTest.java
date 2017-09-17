package ch.difty.scipamato.web.jasper;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;

public class JasperEntityCommonTest extends JasperEntity {

    private static final long serialVersionUID = 1L;

    @Test
    public void na_withNullParameter_returnsBlank() {
        assertThat(na(null)).isEqualTo("");
    }

    @Test
    public void na_withBlankParameter_returnsBlank() {
        assertThat(na("")).isEqualTo("");
    }

    @Test
    public void na_withNonBlankParameter_returnsThat() {
        assertThat(na("foo")).isEqualTo("foo");
    }

    @Test
    public void na_withNullParameterAndNullValue_throws() {
        assertThat(na(null, null)).isEqualTo("");
    }

    @Test
    public void na_withBlankParameterAndNullValue_throws() {
        assertThat(na("", null)).isEqualTo("");
    }

    @Test
    public void na_withBlankParameterAndNonBlankValue_throws() {
        assertThat(na("foo", null)).isEqualTo("");
    }

    @Test
    public void na_withNullParameterAndBlankValue_returnsBlank() {
        assertThat(na(null, "")).isEqualTo("");
    }

    @Test
    public void na_withBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na("", "")).isEqualTo("");
    }

    @Test
    public void na_withNonBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na("foo", "")).isEqualTo("");
    }

    @Test
    public void na_withNullParameterAndNonBlankValue_returnsBlank() {
        assertThat(na(null, "foo")).isEqualTo("");
    }

    @Test
    public void na_withBlankParameterAndNonBlankValue_returnsBlank() {
        assertThat(na("", "foo")).isEqualTo("");
    }

    @Test
    public void na_withNonBlankParameterAndNonBlankValue_returnsLabel() {
        assertThat(na("foo", "bar")).isEqualTo("foo");
    }

    @Test(expected = NullArgumentException.class)
    public void na2_withNullParameterAndNullValue_throws() {
        na2(null, null);
    }

    @Test(expected = NullArgumentException.class)
    public void na2_withNullParameterAndBlankValue_returnsBlank() {
        na2(null, "");
    }

    @Test(expected = NullArgumentException.class)
    public void na2_withNullParameterAndNonBlankValue_returnsBlank() {
        na2(null, "foo");
    }

    @Test
    public void na2_withBlankParameterAndNullValue_throws() {
        assertThat(na2("", null)).isEqualTo("");
    }

    @Test
    public void na2_withBlankParameterAndNonBlankValue_throws() {
        assertThat(na2("foo", null)).isEqualTo("");
    }

    @Test
    public void na2_withBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na2("", "")).isEqualTo("");
    }

    @Test
    public void na2_withNonBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na2("foo", "")).isEqualTo("");
    }

    @Test
    public void na2_withBlankParameterAndNonBlankValue_returnsBlank() {
        assertThat(na2("", "foo")).isEqualTo("");
    }

    @Test
    public void na2_withNonBlankParameterAndNonBlankValue_returnsLabel() {
        assertThat(na2("foo", "bar")).isEqualTo("foo");
    }
}
