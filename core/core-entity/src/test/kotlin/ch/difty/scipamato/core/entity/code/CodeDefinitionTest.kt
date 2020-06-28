package ch.difty.scipamato.core.entity.code

import ch.difty.scipamato.core.entity.CodeClass
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
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
        code.code shouldBeEqualTo "1A"
        code.name shouldBeEqualTo "n.a."
        code.codeClass shouldBeEqualTo codeClass
        code.sort shouldBeEqualTo 2
        code.isInternal.shouldBeFalse()
        code.displayValue shouldBeEqualTo "n.a."
    }

    @Test
    fun withTranslations_onePerLanguage() {
        val code = CodeDefinition("1A", "de", codeClass, 1, true, 1, c_de, c_en, c_fr)
        code.code shouldBeEqualTo "1A"
        code.name shouldBeEqualTo "codede2"
        code.isInternal.shouldBeTrue()
        code.mainLanguageCode shouldBeEqualTo "de"
        code.displayValue shouldBeEqualTo "codede2"
        code.getTranslations() shouldHaveSize 3
        val trs = code.getTranslations()
        trs.map { it.name } shouldContainSame listOf("codede2", "codeen2", "codefr2")
        for (tr in trs)
            tr.lastModified.shouldBeNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr)
        code.translationsAsString shouldBeEqualTo "DE: 'codede2'; EN: 'codeen2'; FR: 'codefr2'"
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en,
            CodeTranslation(12, "fr", null, "remarc", 1)
        )
        code.translationsAsString shouldBeEqualTo "DE: 'codede2'; EN: 'codeen2'; FR: n.a."
    }

    @Test
    fun modifyTranslation_withMainLanguTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr
        )
        code.setNameInLanguage("de", "CODE 2")
        code.name shouldBeEqualTo "CODE 2"
        assertTranslatedName(code, "de", 0, "CODE 2")
        assertLastModifiedIsNotNull(code, "de", 0)
        assertLastModifiedIsNull(code, "en", 0)
        assertLastModifiedIsNull(code, "fr", 0)
    }

    private fun assertTranslatedName(code: CodeDefinition, lc: String, index: Int, value: String) {
        code.getTranslations(lc)[index]?.name shouldBeEqualTo value
    }

    private fun assertLastModifiedIsNotNull(code: CodeDefinition, lc: String, index: Int) {
        code.getTranslations(lc)[index]?.lastModified.shouldNotBeNull()
    }

    private fun assertLastModifiedIsNull(code: CodeDefinition, lc: String, index: Int) {
        code.getTranslations(lc)[index]?.lastModified.shouldBeNull()
    }

    @Test
    fun modifyTranslation_withNonMainLangTranslModified_keepsMainName_changesTranslName_andSetsModifiedTimestamp() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr
        )
        code.setNameInLanguage("fr", "bar")
        code.name shouldBeEqualTo "codede2"
        assertTranslatedName(code, "fr", 0, "bar")
        code.getTranslations("de")[0]?.lastModified.shouldBeNull()
        code.getTranslations("en")[0].lastModified.shouldBeNull()
        assertLastModifiedIsNotNull(code, "fr", 0)
    }

    @Test
    fun gettingNameInLanguage_withValidLanguages_returnsNames() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr
        )
        code.getNameInLanguage("de") shouldBeEqualTo "codede2"
        code.getNameInLanguage("en") shouldBeEqualTo "codeen2"
        code.getNameInLanguage("fr") shouldBeEqualTo "codefr2"
    }

    @Test
    fun gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_en, c_fr)
        code.getNameInLanguage("deX").shouldBeNull()
    }

    @Test
    fun withTranslations_moreThanOnePerLanguage() {
        val code = CodeDefinition("1B", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr)
        code.code shouldBeEqualTo "1B"
        code.name shouldBeEqualTo "codede2"
        code.displayValue shouldBeEqualTo "codede2"
        val trs = code.getTranslations()
        trs.map { it.name } shouldContainSame listOf("codede2", "codede2foo", "codeen2", "codefr2")
        for (tr in trs)
            tr.lastModified.shouldBeNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr)
        code.translationsAsString shouldBeEqualTo "DE: 'codede2','codede2foo'; EN: 'codeen2'; FR: 'codefr2'"
    }

    @Test
    fun modifyTransl_withMainLangTranslMod_changesMainName_translName_andSetsModTimestamp_multipleTranslsPerLanguage() {
        val code = CodeDefinition(
            "1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr
        )
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
        CodeDefinition.CodeDefinitionFields.values().map { it.fieldName } shouldContainSame
            listOf("code", "mainLanguageCode", "codeClass", "sort", "internal", "name")
    }

    @Test
    fun gettingNulSafeId() {
        val code = CodeDefinition("1A", "de", codeClass, 1, false, 1, c_de, c_de2, c_en, c_fr)
        code.nullSafeId shouldBeEqualTo "1A"
    }
}
