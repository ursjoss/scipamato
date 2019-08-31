package ch.difty.scipamato.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TranslationUtilsTest : FinalClassTest<TranslationUtils> {

    @Test
    fun verifyNotTranslatedString() {
        assertThat(TranslationUtils.NOT_TRANSL).isEqualTo("not translated")
    }

    @Test
    fun trimmingLanguageCode_with2CharCode_returnsInput() {
        assertThat(TranslationUtils.trimLanguageCode("de")).isEqualTo("de")
        assertThat(TranslationUtils.trimLanguageCode("fr")).isEqualTo("fr")
    }

    @Test
    fun trimmingLanguageCode_withMoreThan2CharCode_returnsFirstTwoChars() {
        assertThat(TranslationUtils.trimLanguageCode("de_CH")).isEqualTo("de")
        assertThat(TranslationUtils.trimLanguageCode("en_US")).isEqualTo("en")
    }

    @Test
    fun deCamelCasing_null_returnsNull() {
        assertThat(TranslationUtils.deCamelCase(null)).isNull()
    }

    @Test
    fun deCamelCasing_blank_returnsBlank() {
        assertThat(TranslationUtils.deCamelCase("")).isEqualTo("")
    }

    @Test
    fun deCamelCasing_withCase_converts() {
        assertThat(TranslationUtils.deCamelCase("fooBar")).isEqualTo("foo_bar")
    }

    @Test
    fun deCamelCasing_withoutCase_returnsOriginal() {
        assertThat(TranslationUtils.deCamelCase("foobar")).isEqualTo("foobar")
    }

    @Test
    fun deCamelCasing_withoutCaseAndUnderscore_returnsOriginal() {
        assertThat(TranslationUtils.deCamelCase("foo_bar")).isEqualTo("foo_bar")
    }

}
