package ch.difty.scipamato.core.web.paper.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.NullArgumentException;

class JasperEntityCommonTest extends JasperEntity {

    private static final long serialVersionUID = 1L;

    @Test
    void na_withNullParameter_returnsBlank() {
        assertThat(na(null)).isEqualTo("");
    }

    @Test
    void na_withBlankParameter_returnsBlank() {
        assertThat(na("")).isEqualTo("");
    }

    @Test
    void na_withNonBlankParameter_returnsThat() {
        assertThat(na("foo")).isEqualTo("foo");
    }

    @Test
    void na_withNullParameterAndNullValue_throws() {
        assertThat(na(null, null)).isEqualTo("");
    }

    @Test
    void na_withBlankParameterAndNullValue_throws() {
        assertThat(na("", null)).isEqualTo("");
    }

    @Test
    void na_withBlankParameterAndNonBlankValue_throws() {
        assertThat(na("foo", null)).isEqualTo("");
    }

    @Test
    void na_withNullParameterAndBlankValue_returnsBlank() {
        assertThat(na(null, "")).isEqualTo("");
    }

    @Test
    void na_withBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na("", "")).isEqualTo("");
    }

    @Test
    void na_withNonBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na("foo", "")).isEqualTo("");
    }

    @Test
    void na_withNullParameterAndNonBlankValue_returnsBlank() {
        assertThat(na(null, "foo")).isEqualTo("");
    }

    @Test
    void na_withBlankParameterAndNonBlankValue_returnsBlank() {
        assertThat(na("", "foo")).isEqualTo("");
    }

    @Test
    void na_withNonBlankParameterAndNonBlankValue_returnsLabel() {
        assertThat(na("foo", "bar")).isEqualTo("foo");
    }

    @Test
    void na2_withNullParameterAndNullValue_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> na2(null, null));
    }

    @Test
    void na2_withNullParameterAndBlankValue_returnsBlank() {
        Assertions.assertThrows(NullArgumentException.class, () -> na2(null, ""));
    }

    @Test
    void na2_withNullParameterAndNonBlankValue_returnsBlank() {
        Assertions.assertThrows(NullArgumentException.class, () -> na2(null, "foo"));
    }

    @Test
    void na2_withBlankParameterAndNullValue_throws() {
        assertThat(na2("", null)).isEqualTo("");
    }

    @Test
    void na2_withBlankParameterAndNonBlankValue_throws() {
        assertThat(na2("foo", null)).isEqualTo("");
    }

    @Test
    void na2_withBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na2("", "")).isEqualTo("");
    }

    @Test
    void na2_withNonBlankParameterAndBlankValue_returnsBlank() {
        assertThat(na2("foo", "")).isEqualTo("");
    }

    @Test
    void na2_withBlankParameterAndNonBlankValue_returnsBlank() {
        assertThat(na2("", "foo")).isEqualTo("");
    }

    @Test
    void na2_withNonBlankParameterAndNonBlankValue_returnsLabel() {
        assertThat(na2("foo", "bar")).isEqualTo("foo");
    }
}
