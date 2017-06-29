package ch.difty.scipamato.web.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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
}
