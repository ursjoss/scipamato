package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

internal class TranslationUtilsTest {

    @Test
    fun verifyNotTranslatedString() {
        TranslationUtils.NOT_TRANSL shouldBeEqualTo "not translated"
    }

    @Test
    fun trimmingLanguageCode_with2CharCode_returnsInput() {
        TranslationUtils.trimLanguageCode("de") shouldBeEqualTo "de"
        TranslationUtils.trimLanguageCode("fr") shouldBeEqualTo "fr"
    }

    @Test
    fun trimmingLanguageCode_withMoreThan2CharCode_returnsFirstTwoChars() {
        TranslationUtils.trimLanguageCode("de_CH") shouldBeEqualTo "de"
        TranslationUtils.trimLanguageCode("en_US") shouldBeEqualTo "en"
    }

    @Test
    fun deCamelCasing_null_returnsNull() {
        TranslationUtils.deCamelCase(null).shouldBeNull()
    }

    @Test
    fun deCamelCasing_blank_returnsBlank() {
        TranslationUtils.deCamelCase("") shouldBeEqualTo ""
    }

    @Test
    fun deCamelCasing_withCase_converts() {
        TranslationUtils.deCamelCase("fooBar") shouldBeEqualTo "foo_bar"
    }

    @Test
    fun deCamelCasing_withoutCase_returnsOriginal() {
        TranslationUtils.deCamelCase("foobar") shouldBeEqualTo "foobar"
    }

    @Test
    fun deCamelCasing_withoutCaseAndUnderscore_returnsOriginal() {
        TranslationUtils.deCamelCase("foo_bar") shouldBeEqualTo "foo_bar"
    }
}
