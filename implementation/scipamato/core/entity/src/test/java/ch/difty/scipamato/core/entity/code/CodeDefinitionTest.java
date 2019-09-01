package ch.difty.scipamato.core.entity.code;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.CodeClass;

class CodeDefinitionTest {

    private final CodeTranslation c_de  = new CodeTranslation(10, "de", "codede2", "kommentar", 1);
    private final CodeTranslation c_de2 = new CodeTranslation(10, "de", "codede2foo", null, 1);
    private final CodeTranslation c_en  = new CodeTranslation(11, "en", "codeen2", "comment", 1);
    private final CodeTranslation c_fr  = new CodeTranslation(12, "fr", "codefr2", "remarc", 1);

    private CodeClass codeClass = new CodeClass(1, "cc1", "foo");

    @Test
    void withNoTranslations_unableToEstablishMainName() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 2, false, 1);
        assertThat(code.getCode()).isEqualTo("1A");
        assertThat(code.getName()).isEqualTo("n.a.");
        assertThat(code.getCodeClass()).isEqualTo(codeClass);
        assertThat(code.getSort()).isEqualTo(2);
        assertThat(code.isInternal()).isFalse();
        assertThat(code.getDisplayValue()).isEqualTo("n.a.");
        assertThat(code
            .getTranslations()
            .asMap()).isEmpty();
    }

    @Test
    void withTranslations_onePerLanguage() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, true, 1, c_de, c_en, c_fr);
        assertThat(code.getCode()).isEqualTo("1A");
        assertThat(code.getName()).isEqualTo("codede2");
        assertThat(code.isInternal()).isTrue();
        assertThat(code.getMainLanguageCode()).isEqualTo("de");
        assertThat(code.getDisplayValue()).isEqualTo("codede2");
        assertThat(code
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(code
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<CodeTranslation> trs = code
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(CodeTranslation.DefinitionTranslationFields.NAME.getName())
            .containsOnly("codede2", "codeen2", "codefr2");
        for (final CodeTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    void canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr);
        assertThat(code.getTranslationsAsString()).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: 'codefr2'");
    }

    @Test
    void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en,
            new CodeTranslation(12, "fr", null, "remarc", 1));
        assertThat(code.getTranslationsAsString()).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: n.a.");
    }

    @Test
    void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr);
        code.setNameInLanguage("de", "CODE 2");
        assertThat(code.getName()).isEqualTo("CODE 2");
        assertTranslatedName(code, "de", 0, "CODE 2");
        assertLastModifiedIsNotNull(code, "de", 0);
        assertLastModifiedIsNull(code, "en", 0);
        assertLastModifiedIsNull(code, "fr", 0);
    }

    private void assertTranslatedName(final CodeDefinition code, final String lc, final int index, final String value) {
        assertThat(code
            .getTranslations()
            .get(lc)
            .get(index)
            .getName()).isEqualTo(value);
    }

    private void assertLastModifiedIsNotNull(final CodeDefinition code, final String lc, final int index) {
        assertThat(code
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNotNull();
    }

    private void assertLastModifiedIsNull(final CodeDefinition code, final String lc, final int index) {
        assertThat(code
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNull();
    }

    @Test
    void modifyTranslation_withNonMainLanguageTranslationModified_keepsMainName_changesTranslationName_andSetsModifiedTimestamp() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr);
        code.setNameInLanguage("fr", "bar");
        assertThat(code.getName()).isEqualTo("codede2");
        assertTranslatedName(code, "fr", 0, "bar");
        assertThat(code
            .getTranslations()
            .get("de")
            .get(0)
            .getLastModified()).isNull();
        assertThat(code
            .getTranslations()
            .get("en")
            .get(0)
            .getLastModified()).isNull();
        assertLastModifiedIsNotNull(code, "fr", 0);
    }

    @Test
    void gettingNameInLanguage_withValidLanguages_returnsNames() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr);
        assertThat(code.getNameInLanguage("de")).isEqualTo("codede2");
        assertThat(code.getNameInLanguage("en")).isEqualTo("codeen2");
        assertThat(code.getNameInLanguage("fr")).isEqualTo("codefr2");
    }

    @Test
    void gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr);
        assertThat(code.getNameInLanguage("deX")).isNull();
    }

    @Test
    void gettingNameInLanguage_withNullLanguage_throws() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr);
        assertDegenerateSupplierParameter(() -> code.getNameInLanguage(null), "langCode");
    }

    @Test
    void withTranslations_moreThanOnePerLanguage() {
        CodeDefinition code = new CodeDefinition("1B", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr);
        assertThat(code.getCode()).isEqualTo("1B");
        assertThat(code.getName()).isEqualTo("codede2");
        assertThat(code.getDisplayValue()).isEqualTo("codede2");
        assertThat(code
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(code
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<CodeTranslation> trs = code
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(CodeTranslation.DefinitionTranslationFields.NAME.getName())
            .containsOnly("codede2", "codede2foo", "codeen2", "codefr2");
        for (final CodeTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr);
        assertThat(code.getTranslationsAsString()).isEqualTo(
            "DE: 'codede2','codede2foo'; EN: 'codeen2'; FR: 'codefr2'");
    }

    @Test
    void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp_multipleTranslationsPerLanguage() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr);
        code.setNameInLanguage("de", "Code 2");
        assertThat(code.getName()).isEqualTo("Code 2");
        assertTranslatedName(code, "de", 0, "Code 2");
        assertTranslatedName(code, "de", 1, "codede2foo");
        assertLastModifiedIsNotNull(code, "de", 0);
        assertLastModifiedIsNull(code, "de", 1);
        assertLastModifiedIsNull(code, "en", 0);
        assertLastModifiedIsNull(code, "fr", 0);
    }

    @Test
    void assertCodeFields() {
        assertThat(CodeDefinition.CodeDefinitionFields.values())
            .extracting("name")
            .containsExactly("code", "mainLanguageCode", "codeClass", "sort", "internal", "name");
    }

    @Test
    void gettingNulSafeId() {
        CodeDefinition code = new CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr);
        assertThat(code.getNullSafeId()).isEqualTo("1A");
    }
}