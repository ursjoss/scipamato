package ch.difty.scipamato.core.web.paper.jasper

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class JasperEntityCommonTest : JasperEntity() {
    @Test
    fun na_withNullParameter_returnsBlank() {
        na(null) shouldBeEqualTo ""
    }

    @Test
    fun na_withBlankParameter_returnsBlank() {
        na("") shouldBeEqualTo ""
    }

    @Test
    fun na_withNonBlankParameter_returnsThat() {
        na("foo") shouldBeEqualTo "foo"
    }

    @Test
    fun na_withNullParameterAndNullValue_throws() {
        na(null, null) shouldBeEqualTo ""
    }

    @Test
    fun na_withBlankParameterAndNullValue_throws() {
        na("", null) shouldBeEqualTo ""
    }

    @Test
    fun na_withBlankParameterAndNonBlankValue_throws() {
        na("foo", null) shouldBeEqualTo ""
    }

    @Test
    fun na_withNullParameterAndBlankValue_returnsBlank() {
        na(null, "") shouldBeEqualTo ""
    }

    @Test
    fun na_withBlankParameterAndBlankValue_returnsBlank() {
        na("", "") shouldBeEqualTo ""
    }

    @Test
    fun na_withNonBlankParameterAndBlankValue_returnsBlank() {
        na("foo", "") shouldBeEqualTo ""
    }

    @Test
    fun na_withNullParameterAndNonBlankValue_returnsBlank() {
        na(null, "foo") shouldBeEqualTo ""
    }

    @Test
    fun na_withBlankParameterAndNonBlankValue_returnsBlank() {
        na("", "foo") shouldBeEqualTo ""
    }

    @Test
    fun na_withNonBlankParameterAndNonBlankValue_returnsLabel() {
        na("foo", "bar") shouldBeEqualTo "foo"
    }

    @Test
    fun na2_withBlankParameterAndNullValue_throws() {
        na2("", null) shouldBeEqualTo ""
    }

    @Test
    fun na2_withBlankParameterAndNonBlankValue_throws() {
        na2("foo", null) shouldBeEqualTo ""
    }

    @Test
    fun na2_withBlankParameterAndBlankValue_returnsBlank() {
        na2("", "") shouldBeEqualTo ""
    }

    @Test
    fun na2_withNonBlankParameterAndBlankValue_returnsBlank() {
        na2("foo", "") shouldBeEqualTo ""
    }

    @Test
    fun na2_withBlankParameterAndNonBlankValue_returnsBlank() {
        na2("", "foo") shouldBeEqualTo ""
    }

    @Test
    fun na2_withNonBlankParameterAndNonBlankValue_returnsLabel() {
        na2("foo", "bar") shouldBeEqualTo "foo"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
