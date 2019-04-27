package ch.difty.scipamato.core.entity.code_class;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.TestUtils;

class CodeClassDefinitionTest {

    private final CodeClassTranslation cc_de  = new CodeClassTranslation(10, "de", "codede2", "beschreibung", 1);
    private final CodeClassTranslation cc_de2 = new CodeClassTranslation(10, "de", "codede2foo", null, 1);
    private final CodeClassTranslation cc_en  = new CodeClassTranslation(11, "en", "codeen2", "description", 1);
    private final CodeClassTranslation cc_fr  = new CodeClassTranslation(12, "fr", "codefr2", "remarc", 1);

    @Test
    void withNoTranslations_unableToEstablishMainName() {
        CodeClassDefinition codeClass = new CodeClassDefinition(1, "de", 2);
        assertThat(codeClass.getId()).isEqualTo(1);
        assertThat(codeClass.getName()).isEqualTo("n.a.");
        assertThat(codeClass.getDisplayValue()).isEqualTo("n.a.");
        assertThat(codeClass
            .getTranslations()
            .asMap()).isEmpty();
    }

    @Test
    void withTranslations_onePerLanguage() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr);
        assertThat(code.getId()).isEqualTo(1);
        assertThat(code.getName()).isEqualTo("codede2");
        assertThat(code.getMainLanguageCode()).isEqualTo("de");
        assertThat(code.getDisplayValue()).isEqualTo("codede2");
        assertThat(code
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(code
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<CodeClassTranslation> trs = code
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(CodeClassTranslation.DefinitionTranslationFields.NAME.getName())
            .containsOnly("codede2", "codeen2", "codefr2");
        for (final CodeClassTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    void canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr);
        assertThat(code.getTranslationsAsString()).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: 'codefr2'");
    }

    @Test
    void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_en,
            new CodeClassTranslation(12, "fr", null, "remarc", 1));
        assertThat(code.getTranslationsAsString()).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: n.a.");
    }

    @Test
    void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr);
        code.setNameInLanguage("de", "CODE 2");
        assertThat(code.getName()).isEqualTo("CODE 2");
        assertTranslatedName(code, "de", 0, "CODE 2");
        assertLastModifiedIsNotNull(code, "de", 0);
        assertLastModifiedIsNull(code, "en", 0);
        assertLastModifiedIsNull(code, "fr", 0);
    }

    private void assertTranslatedName(final CodeClassDefinition code, final String lc, final int index,
        final String value) {
        assertThat(code
            .getTranslations()
            .get(lc)
            .get(index)
            .getName()).isEqualTo(value);
    }

    private void assertLastModifiedIsNotNull(final CodeClassDefinition code, final String lc, final int index) {
        assertThat(code
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNotNull();
    }

    private void assertLastModifiedIsNull(final CodeClassDefinition code, final String lc, final int index) {
        assertThat(code
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNull();
    }

    @Test
    void modifyTranslation_withNonMainLanguageTranslationModified_keepsMainName_changesTranslationName_andSetsModifiedTimestamp() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr);
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
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr);
        assertThat(code.getNameInLanguage("de")).isEqualTo("codede2");
        assertThat(code.getNameInLanguage("en")).isEqualTo("codeen2");
        assertThat(code.getNameInLanguage("fr")).isEqualTo("codefr2");
    }

    @Test
    void gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        CodeClassDefinition ccd = new CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr);
        assertThat(ccd.getNameInLanguage("deX")).isNull();
    }

    @Test
    void gettingNameInLanguage_withNullLanguage_throws() {
        CodeClassDefinition ccd = new CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr);
        TestUtils.assertDegenerateSupplierParameter(() -> ccd.getNameInLanguage(null), "langCode");
    }

    @Test
    void withTranslations_moreThanOnePerLanguage() {
        CodeClassDefinition ccd = new CodeClassDefinition(2, "de", 1, cc_de, cc_de2, cc_en, cc_fr);
        assertThat(ccd.getId()).isEqualTo(2);
        assertThat(ccd.getName()).isEqualTo("codede2");
        assertThat(ccd.getDisplayValue()).isEqualTo("codede2");
        assertThat(ccd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(ccd
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<CodeClassTranslation> trs = ccd
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(CodeClassTranslation.DefinitionTranslationFields.NAME.getName())
            .containsOnly("codede2", "codede2foo", "codeen2", "codefr2");
        for (final CodeClassTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr);
        assertThat(code.getTranslationsAsString()).isEqualTo(
            "DE: 'codede2','codede2foo'; EN: 'codeen2'; FR: 'codefr2'");
    }

    @Test
    void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp_multipleTranslationsPerLanguage() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr);
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
        assertThat(CodeClassDefinition.CodeClassDefinitionFields.values())
            .extracting("name")
            .containsExactly("id", "mainLanguageCode", "name");
    }

    @Test
    void gettingNulSafeId() {
        CodeClassDefinition code = new CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr);
        assertThat(code.getNullSafeId()).isEqualTo(1);
    }
}