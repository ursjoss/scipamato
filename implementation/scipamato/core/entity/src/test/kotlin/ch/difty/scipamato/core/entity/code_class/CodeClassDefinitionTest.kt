package ch.difty.scipamato.core.entity.code_class

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection", "PrivatePropertyName")
internal class CodeClassDefinitionTest {

    private val cc_de = CodeClassTranslation(10, "de", "codede2", "beschreibung", 1)
    private val cc_de2 = CodeClassTranslation(10, "de", "codede2foo", null, 1)
    private val cc_en = CodeClassTranslation(11, "en", "codeen2", "description", 1)
    private val cc_fr = CodeClassTranslation(12, "fr", "codefr2", "remarc", 1)

    @Test
    fun withNoTranslations_unableToEstablishMainName() {
        val codeClass = CodeClassDefinition(1, "de", 2)
        assertThat(codeClass.id).isEqualTo(1)
        assertThat(codeClass.name).isEqualTo("n.a.")
        assertThat(codeClass.displayValue).isEqualTo("n.a.")
        assertThat(codeClass.translations.asMap()).isEmpty()
    }

    @Test
    fun withTranslations_onePerLanguage() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        assertThat(code.id).isEqualTo(1)
        assertThat(code.name).isEqualTo("codede2")
        assertThat(code.mainLanguageCode).isEqualTo("de")
        assertThat(code.displayValue).isEqualTo("codede2")
        assertThat(code.translations.asMap()).hasSize(3)
        assertThat(code.translations.keySet()).containsExactly("de", "en", "fr")
        val trs = code.translations.values()
        assertThat(trs.map { it.name }).containsOnly("codede2", "codeen2", "codefr2")
        for (tr in trs)
            assertThat(tr.lastModified).isNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        assertThat(code.translationsAsString).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: 'codefr2'")
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en,
                CodeClassTranslation(12, "fr", null, "remarc", 1))
        assertThat(code.translationsAsString).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: n.a.")
    }

    @Test
    fun modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        code.setNameInLanguage("de", "CODE 2")
        assertThat(code.name).isEqualTo("CODE 2")
        assertTranslatedName(code, "de", 0, "CODE 2")
        assertLastModifiedIsNotNull(code, "de", 0)
        assertLastModifiedIsNull(code, "en", 0)
        assertLastModifiedIsNull(code, "fr", 0)
    }

    private fun assertTranslatedName(code: CodeClassDefinition, lc: String, index: Int,
                                     value: String) {
        assertThat(code.translations.get(lc)[index].name).isEqualTo(value)
    }

    @Suppress("SameParameterValue")
    private fun assertLastModifiedIsNotNull(code: CodeClassDefinition, lc: String, index: Int) {
        assertThat(code.translations.get(lc)[index].lastModified).isNotNull()
    }

    private fun assertLastModifiedIsNull(code: CodeClassDefinition, lc: String, index: Int) {
        assertThat(code.translations.get(lc)[index].lastModified).isNull()
    }

    @Test
    fun modifyTranslation_withNonMainLanguageTranslationModified_keepsMainName_changesTranslationName_andSetsModifiedTimestamp() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        code.setNameInLanguage("fr", "bar")
        assertThat(code.name).isEqualTo("codede2")
        assertTranslatedName(code, "fr", 0, "bar")
        assertThat(code.translations.get("de")[0].lastModified).isNull()
        assertThat(code.translations.get("en")[0].lastModified).isNull()
        assertLastModifiedIsNotNull(code, "fr", 0)
    }

    @Test
    fun gettingNameInLanguage_withValidLanguages_returnsNames() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        assertThat(code.getNameInLanguage("de")).isEqualTo("codede2")
        assertThat(code.getNameInLanguage("en")).isEqualTo("codeen2")
        assertThat(code.getNameInLanguage("fr")).isEqualTo("codefr2")
    }

    @Test
    fun gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        val ccd = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        assertThat(ccd.getNameInLanguage("deX")).isNull()
    }

    @Test
    fun withTranslations_moreThanOnePerLanguage() {
        val ccd = CodeClassDefinition(2, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        assertThat(ccd.id).isEqualTo(2)
        assertThat(ccd.name).isEqualTo("codede2")
        assertThat(ccd.displayValue).isEqualTo("codede2")
        assertThat(ccd.translations.asMap()).hasSize(3)
        assertThat(ccd.translations.keySet()).containsExactly("de", "en", "fr")
        val trs = ccd.translations.values()
        assertThat(trs.map { it.name }).containsOnly("codede2", "codede2foo", "codeen2", "codefr2")
        for (tr in trs)
            assertThat(tr.lastModified).isNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        assertThat(code.translationsAsString).isEqualTo("DE: 'codede2','codede2foo'; EN: 'codeen2'; FR: 'codefr2'")
    }

    @Test
    fun modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp_multipleTranslationsPerLanguage() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        code.setNameInLanguage("de", "Code 2")
        assertThat(code.name).isEqualTo("Code 2")
        assertTranslatedName(code, "de", 0, "Code 2")
        assertTranslatedName(code, "de", 1, "codede2foo")
        assertLastModifiedIsNotNull(code, "de", 0)
        assertLastModifiedIsNull(code, "de", 1)
        assertLastModifiedIsNull(code, "en", 0)
        assertLastModifiedIsNull(code, "fr", 0)
    }

    @Test
    fun assertCodeFields() {
        assertThat(CodeClassDefinition.CodeClassDefinitionFields.values().map { it.fieldName }).containsExactly("id", "mainLanguageCode", "name")
    }

    @Test
    fun gettingNulSafeId() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        assertThat(code.nullSafeId).isEqualTo(1)
    }
}