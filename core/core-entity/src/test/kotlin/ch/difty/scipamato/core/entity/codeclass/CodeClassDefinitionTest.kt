package ch.difty.scipamato.core.entity.codeclass

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection", "PrivatePropertyName", "VariableNaming")
internal class CodeClassDefinitionTest {

    private val cc_de = CodeClassTranslation(10, "de", "codede2", "beschreibung", 1)
    private val cc_de2 = CodeClassTranslation(10, "de", "codede2foo", null, 1)
    private val cc_en = CodeClassTranslation(11, "en", "codeen2", "description", 1)
    private val cc_fr = CodeClassTranslation(12, "fr", "codefr2", "remarc", 1)

    @Test
    fun withNoTranslations_unableToEstablishMainName() {
        val codeClass = CodeClassDefinition(1, "de", 2)
        codeClass.id shouldBeEqualTo 1
        codeClass.name shouldBeEqualTo "n.a."
        codeClass.displayValue shouldBeEqualTo "n.a."
        codeClass.getTranslations().shouldBeEmpty()
    }

    @Test
    fun withTranslations_onePerLanguage() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        code.id shouldBeEqualTo 1
        code.name shouldBeEqualTo "codede2"
        code.mainLanguageCode shouldBeEqualTo "de"
        code.displayValue shouldBeEqualTo "codede2"
        val trs = code.getTranslations()
        trs.map { it.name } shouldContainSame listOf("codede2", "codeen2", "codefr2")
        for (tr in trs)
            tr.lastModified.shouldBeNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        code.translationsAsString shouldBeEqualTo "DE: 'codede2'; EN: 'codeen2'; FR: 'codefr2'"
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val code = CodeClassDefinition(
            1, "de", 1, cc_de, cc_en,
            CodeClassTranslation(12, "fr", null, "remarc", 1)
        )
        code.translationsAsString shouldBeEqualTo "DE: 'codede2'; EN: 'codeen2'; FR: n.a."
    }

    @Test
    fun modifyTransl_withMainLangTranslModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        code.setNameInLanguage("de", "CODE 2")
        code.name shouldBeEqualTo "CODE 2"
        assertTranslatedName(code, "de", 0, "CODE 2")
        assertLastModifiedIsNotNull(code, "de", 0)
        assertLastModifiedIsNull(code, "en", 0)
        assertLastModifiedIsNull(code, "fr", 0)
    }

    private fun assertTranslatedName(code: CodeClassDefinition, lc: String, index: Int, value: String) {
        code.getTranslations(lc)[index]?.name shouldBeEqualTo value
    }

    @Suppress("SameParameterValue")
    private fun assertLastModifiedIsNotNull(code: CodeClassDefinition, lc: String, index: Int) {
        code.getTranslations(lc)[index]?.lastModified.shouldNotBeNull()
    }

    private fun assertLastModifiedIsNull(code: CodeClassDefinition, lc: String, index: Int) {
        code.getTranslations(lc)[index]?.lastModified.shouldBeNull()
    }

    @Test
    fun modifyTransl_withNonMainLangTranslModified_keepsMainName_changesTranslName_andSetsModifiedTimestamp() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        code.setNameInLanguage("fr", "bar")
        code.name shouldBeEqualTo "codede2"
        assertTranslatedName(code, "fr", 0, "bar")
        code.getTranslations("de")[0]?.lastModified.shouldBeNull()
        code.getTranslations("en")[0]?.lastModified.shouldBeNull()
        assertLastModifiedIsNotNull(code, "fr", 0)
    }

    @Test
    fun gettingNameInLanguage_withValidLanguages_returnsNames() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        code.getNameInLanguage("de") shouldBeEqualTo "codede2"
        code.getNameInLanguage("en") shouldBeEqualTo "codeen2"
        code.getNameInLanguage("fr") shouldBeEqualTo "codefr2"
    }

    @Test
    fun gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        val ccd = CodeClassDefinition(1, "de", 1, cc_de, cc_en, cc_fr)
        ccd.getNameInLanguage("deX").shouldBeNull()
    }

    @Test
    fun withTranslations_moreThanOnePerLanguage() {
        val ccd = CodeClassDefinition(2, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        ccd.id shouldBeEqualTo 2
        ccd.name shouldBeEqualTo "codede2"
        ccd.displayValue shouldBeEqualTo "codede2"
        val trs = ccd.getTranslations()
        trs.map { it.name } shouldContainSame listOf("codede2", "codede2foo", "codeen2", "codefr2")
        for (tr in trs)
            tr.lastModified.shouldBeNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        code.translationsAsString shouldBeEqualTo "DE: 'codede2','codede2foo'; EN: 'codeen2'; FR: 'codefr2'"
    }

    @Test
    fun modifyTransl_withMainLangTranslModified_changesMainName_translName_andSetsModTimestamp_multipleTranslPerLang() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        code.setNameInLanguage("de", "Code 2")
        code.name shouldBeEqualTo "Code 2"
        assertTranslatedName(code, "de", 0, "Code 2")
        assertTranslatedName(code, "de", 1, "codede2foo")
        assertLastModifiedIsNotNull(code, "de", 0)
        assertLastModifiedIsNull(code, "de", 1)
        assertLastModifiedIsNull(code, "en", 0)
        assertLastModifiedIsNull(code, "fr", 0)
    }

    @Test
    fun assertCodeFields() {
        CodeClassDefinition.CodeClassDefinitionFields.values().map { it.fieldName } shouldContainSame
            listOf("id", "mainLanguageCode", "name")
    }

    @Test
    fun gettingNulSafeId() {
        val code = CodeClassDefinition(1, "de", 1, cc_de, cc_de2, cc_en, cc_fr)
        code.nullSafeId shouldBeEqualTo 1
    }
}
