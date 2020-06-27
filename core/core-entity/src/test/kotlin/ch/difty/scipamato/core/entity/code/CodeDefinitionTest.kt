package ch.difty.scipamato.core.entity.code

import ch.difty.scipamato.core.entity.CodeClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("PrivatePropertyName", "SpellCheckingInspection", "SameParameterValue")
internal class CodeDefinitionTest {

    private val c_de = CodeTranslation(10, "de", "codede2", "kommentar", 1)
    private val c_de2 = CodeTranslation(10, "de", "codede2foo", null, 1)
    private val c_en = CodeTranslation(11, "en", "codeen2", "comment", 1)
    private val c_fr = CodeTranslation(12, "fr", "codefr2", "remarc", 1)

    private val codeClass = CodeClass(1, "cc1", "foo")

    @Test
    fun withNoTranslations_unableToEstablishMainName() {
        val code = CodeDefinition("1A", "de", codeClass, 2, false, 1)
        assertThat(code.code).isEqualTo("1A")
        assertThat(code.name).isEqualTo("n.a.")
        assertThat(code.codeClass).isEqualTo(codeClass)
        assertThat(code.sort).isEqualTo(2)
        assertThat(code.isInternal).isFalse()
        assertThat(code.displayValue).isEqualTo("n.a.")
    }

    @Test
    fun withTranslations_onePerLanguage() {
        val code = CodeDefinition("1A", "de", codeClass, 1, true, 1, c_de, c_en, c_fr)
        assertThat(code.code).isEqualTo("1A")
        assertThat(code.name).isEqualTo("codede2")
        assertThat(code.isInternal).isTrue()
        assertThat(code.mainLanguageCode).isEqualTo("de")
        assertThat(code.displayValue).isEqualTo("codede2")
        assertThat(code.getTranslations()).hasSize(3)
        val trs = code.getTranslations()
        assertThat(trs.map { it.name }).containsOnly("codede2", "codeen2", "codefr2")
        for (tr in trs)
            assertThat(tr.lastModified).isNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr)
        assertThat(code.translationsAsString).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: 'codefr2'")
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en,
            CodeTranslation(12, "fr", null, "remarc", 1)
        )
        assertThat(code.translationsAsString).isEqualTo("DE: 'codede2'; EN: 'codeen2'; FR: n.a.")
    }

    @Test
    fun modifyTranslation_withMainLanguTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr
        )
        code.setNameInLanguage("de", "CODE 2")
        assertThat(code.name).isEqualTo("CODE 2")
        assertTranslatedName(code, "de", 0, "CODE 2")
        assertLastModifiedIsNotNull(code, "de", 0)
        assertLastModifiedIsNull(code, "en", 0)
        assertLastModifiedIsNull(code, "fr", 0)
    }

    private fun assertTranslatedName(code: CodeDefinition, lc: String, index: Int, value: String) {
        assertThat(code.getTranslations(lc)[index]?.name).isEqualTo(value)
    }

    private fun assertLastModifiedIsNotNull(code: CodeDefinition, lc: String, index: Int) {
        assertThat(code.getTranslations(lc)[index]?.lastModified).isNotNull()
    }

    private fun assertLastModifiedIsNull(code: CodeDefinition, lc: String, index: Int) {
        assertThat(code.getTranslations(lc)[index]?.lastModified).isNull()
    }

    @Test
    fun modifyTranslation_withNonMainLangTranslModified_keepsMainName_changesTranslName_andSetsModifiedTimestamp() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr
        )
        code.setNameInLanguage("fr", "bar")
        assertThat(code.name).isEqualTo("codede2")
        assertTranslatedName(code, "fr", 0, "bar")
        assertThat(code.getTranslations("de")[0]?.lastModified).isNull()
        assertThat(code.getTranslations("en")[0].lastModified).isNull()
        assertLastModifiedIsNotNull(code, "fr", 0)
    }

    @Test
    fun gettingNameInLanguage_withValidLanguages_returnsNames() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr
        )
        assertThat(code.getNameInLanguage("de")).isEqualTo("codede2")
        assertThat(code.getNameInLanguage("en")).isEqualTo("codeen2")
        assertThat(code.getNameInLanguage("fr")).isEqualTo("codefr2")
    }

    @Test
    fun gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr)
        assertThat(code.getNameInLanguage("deX")).isNull()
    }

    @Test
    fun withTranslations_moreThanOnePerLanguage() {
        val code = CodeDefinition("1B", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr)
        assertThat(code.code).isEqualTo("1B")
        assertThat(code.name).isEqualTo("codede2")
        assertThat(code.displayValue).isEqualTo("codede2")
        val trs = code.getTranslations()
        assertThat(trs.map { it.name }).containsOnly("codede2", "codede2foo", "codeen2", "codefr2")
        for (tr in trs)
            assertThat(tr.lastModified).isNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr)
        assertThat(code.translationsAsString).isEqualTo("DE: 'codede2','codede2foo'; EN: 'codeen2'; FR: 'codefr2'")
    }

    @Test
    fun modifyTransl_withMainLangTranslMod_changesMainName_translName_andSetsModTimestamp_multipleTranslsPerLanguage() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr
        )
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
        assertThat(CodeDefinition.CodeDefinitionFields.values().map { it.fieldName })
            .containsExactly("code", "mainLanguageCode", "codeClass", "sort", "internal", "name")
    }

    @Test
    fun gettingNulSafeId() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr)
        assertThat(code.nullSafeId).isEqualTo("1A")
    }
}
