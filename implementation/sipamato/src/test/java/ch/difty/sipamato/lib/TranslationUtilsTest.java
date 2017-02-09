package ch.difty.sipamato.lib;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.sipamato.FinalClassTest;

public class TranslationUtilsTest extends FinalClassTest<TranslationUtils> {

    @Test
    public void verifyNotTranslatedString() {
        assertThat(TranslationUtils.NOT_TRANSL).isEqualTo("not translated");
    }

    @Test
    public void trimmingLanguageCode_withNullCode_throws() {
        try {
            TranslationUtils.trimLanguageCode(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
        }
    }

    @Test
    public void trimmingLanguageCode_with2CharCode_returnsInput() {
        assertThat(TranslationUtils.trimLanguageCode("de")).isEqualTo("de");
        assertThat(TranslationUtils.trimLanguageCode("fr")).isEqualTo("fr");
    }

    @Test
    public void trimmingLanguageCode_withMoreThan2CharCode_returnsFirstTwoChars() {
        assertThat(TranslationUtils.trimLanguageCode("de_CH")).isEqualTo("de");
        assertThat(TranslationUtils.trimLanguageCode("en_US")).isEqualTo("en");
    }

}
