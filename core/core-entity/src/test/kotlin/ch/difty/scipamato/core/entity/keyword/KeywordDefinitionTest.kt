package ch.difty.scipamato.core.entity.keyword

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("PrivatePropertyName", "PrivatePropertyName", "SpellCheckingInspection")
internal class KeywordDefinitionTest {

    private val kw_de = KeywordTranslation(10, "de", "stichwort2", 1)
    private val kw_de2 = KeywordTranslation(10, "de", "stichwort2foo", 1)
    private val kw_en = KeywordTranslation(11, "en", "keyword2", 1)
    private val kw_fr = KeywordTranslation(12, "fr", "motdeclef2", 1)

    @Test
    fun withNoTranslations_unableToEstablishMainName() {
        val kd = KeywordDefinition(1, "de", 1)
        kd.id shouldBeEqualTo 1
        kd.name shouldBeEqualTo "n.a."
        kd.searchOverride.shouldBeNull()
        kd.displayValue shouldBeEqualTo "n.a."
        kd.getTranslations().shouldBeEmpty()
    }

    @Test
    fun withSearchOverride() {
        val kd = KeywordDefinition(1, "de", "so", 1)
        kd.id shouldBeEqualTo 1
        kd.name shouldBeEqualTo "n.a."
        kd.searchOverride shouldBeEqualTo "so"
        kd.displayValue shouldBeEqualTo "n.a."
        kd.getTranslations().shouldBeEmpty()
    }

    @Test
    fun withTranslations_onePerLanguage() {
        val kd = KeywordDefinition(2, "de", "sooo", 1, kw_de, kw_en, kw_fr)
        kd.id shouldBeEqualTo 2
        kd.name shouldBeEqualTo "stichwort2"
        kd.searchOverride shouldBeEqualTo "sooo"
        kd.displayValue shouldBeEqualTo "stichwort2"
        val trs = kd.getTranslations()
        trs.map { it.name } shouldContainSame listOf("stichwort2", "keyword2", "motdeclef2")
        for (tr in trs)
            tr.lastModified.shouldBeNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        kd.translationsAsString shouldBeEqualTo "DE: 'stichwort2'; EN: 'keyword2'; FR: 'motdeclef2'"
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en,
            KeywordTranslation(12, "fr", null, 1))
        kd.translationsAsString shouldBeEqualTo "DE: 'stichwort2'; EN: 'keyword2'; FR: n.a."
    }

    @Test
    fun canGetTranslationsAsString_withNoTranslations() {
        val kd = KeywordDefinition(2, "de", 1)
        kd.translationsAsString.shouldBeNull()
    }

    @Test
    fun modifyTransl_withMainLangTranslModified_changesMainName_translName_andSetsModifiedTimestamp() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        kd.setNameInLanguage("de", "KEYWORD 2")
        kd.name shouldBeEqualTo "KEYWORD 2"
        assertTranslatedName(kd, "de", 0, "KEYWORD 2")
        assertLastModifiedIsNotNull(kd, "de", 0)
        assertLastModifiedIsNull(kd, "en", 0)
        assertLastModifiedIsNull(kd, "fr", 0)
    }

    private fun assertTranslatedName(kd: KeywordDefinition, lc: String, index: Int, value: String) {
        kd.getTranslations(lc)[index]?.name shouldBeEqualTo value
    }

    @Suppress("SameParameterValue")
    private fun assertLastModifiedIsNotNull(kd: KeywordDefinition, lc: String, index: Int) {
        kd.getTranslations(lc)[index]?.lastModified.shouldNotBeNull()
    }

    private fun assertLastModifiedIsNull(kd: KeywordDefinition, lc: String, index: Int) {
        kd.getTranslations(lc)[index]?.lastModified.shouldBeNull()
    }

    @Test
    fun modifyTransl_withNonMainLangTranslModified_keepsMainName_changesTranslationName_andSetsModifiedTimestamp() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        kd.setNameInLanguage("fr", "bar")
        kd.name shouldBeEqualTo "stichwort2"
        assertTranslatedName(kd, "fr", 0, "bar")
        kd.getTranslations("de")[0]?.lastModified.shouldBeNull()
        kd.getTranslations("en")[0]?.lastModified.shouldBeNull()
        assertLastModifiedIsNotNull(kd, "fr", 0)
    }

    @Test
    fun gettingNameInLanguage_withValidLanguages_returnsNames() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        kd.getNameInLanguage("de") shouldBeEqualTo "stichwort2"
        kd.getNameInLanguage("en") shouldBeEqualTo "keyword2"
        kd.getNameInLanguage("fr") shouldBeEqualTo "motdeclef2"
    }

    @Test
    fun gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        KeywordDefinition(2, "de", 1).getNameInLanguage("de").shouldBeNull()
    }

    @Test
    fun withTranslations_moreThanOnePerLanguage() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr)
        kd.id shouldBeEqualTo 2
        kd.name shouldBeEqualTo "stichwort2"
        kd.displayValue shouldBeEqualTo "stichwort2"
        val trs = kd.getTranslations()
        trs.map { it.name } shouldContainSame listOf("stichwort2", "stichwort2foo", "keyword2", "motdeclef2")
        for (tr in trs)
            tr.lastModified.shouldBeNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr)
        kd.translationsAsString shouldBeEqualTo "DE: 'stichwort2','stichwort2foo'; EN: 'keyword2'; FR: 'motdeclef2'"
    }

    @Test
    fun modifyTransl_withMainLangTranslModified_changesMainName_translName_andSetsModTimestamp_multipleTranslPerLang() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr, kw_de2)
        kd.setNameInLanguage("de", "Stichwort 2")
        kd.name shouldBeEqualTo "Stichwort 2"
        assertTranslatedName(kd, "de", 0, "Stichwort 2")
        assertTranslatedName(kd, "de", 1, "stichwort2foo")
        assertLastModifiedIsNotNull(kd, "de", 0)
        assertLastModifiedIsNull(kd, "de", 1)
        assertLastModifiedIsNull(kd, "en", 0)
        assertLastModifiedIsNull(kd, "fr", 0)
    }

    @Test
    fun gettingNullSafeId_withIdPresent() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr, kw_de2)
        kd.nullSafeId shouldBeEqualTo 2
    }

    @Test
    fun gettingNullSafeId_withNoIdPresent() {
        val kd = KeywordDefinition(null, "de", 1, kw_de, kw_en, kw_fr, kw_de2)
        kd.nullSafeId shouldBeEqualTo 0
    }
}
