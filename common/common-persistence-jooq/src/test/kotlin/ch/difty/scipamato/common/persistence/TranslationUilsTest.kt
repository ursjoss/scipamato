package ch.difty.scipamato.common.persistence

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class TranslationUtilsTest {

    @Test
    fun verifyNotTranslatedString() {
        NOT_TRANSL shouldBeEqualTo "not translated"
    }

    @Test
    fun trimmingLanguageCode_with2CharCode_returnsInput() {
        trimLanguageCode("de") shouldBeEqualTo "de"
        trimLanguageCode("fr") shouldBeEqualTo "fr"
    }

    @Test
    fun trimmingLanguageCode_withMoreThan2CharCode_returnsFirstTwoChars() {
        trimLanguageCode("de_CH") shouldBeEqualTo "de"
        trimLanguageCode("en_US") shouldBeEqualTo "en"
    }

    @Test
    fun deCamelCasing_blank_returnsBlank() {
        deCamelCase("") shouldBeEqualTo ""
    }

    @Test
    fun deCamelCasing_withCase_converts() {
        deCamelCase("fooBar") shouldBeEqualTo "foo_bar"
    }

    @Test
    fun deCamelCasing_withoutCase_returnsOriginal() {
        deCamelCase("foobar") shouldBeEqualTo "foobar"
    }

    @Test
    fun deCamelCasing_withoutCaseAndUnderscore_returnsOriginal() {
        deCamelCase("foo_bar") shouldBeEqualTo "foo_bar"
    }
}
