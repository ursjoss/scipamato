package ch.difty.scipamato.core.entity.keyword

import org.assertj.core.api.Assertions.assertThat

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
        assertThat(kd.id).isEqualTo(1)
        assertThat(kd.name).isEqualTo("n.a.")
        assertThat(kd.searchOverride).isNull()
        assertThat(kd.displayValue).isEqualTo("n.a.")
        assertThat(kd.translations.asMap()).isEmpty()
    }

    @Test
    fun withSearchOverride() {
        val kd = KeywordDefinition(1, "de", "so", 1)
        assertThat(kd.id).isEqualTo(1)
        assertThat(kd.name).isEqualTo("n.a.")
        assertThat(kd.searchOverride).isEqualTo("so")
        assertThat(kd.displayValue).isEqualTo("n.a.")
        assertThat(kd.translations.asMap()).isEmpty()
    }

    @Test
    fun withTranslations_onePerLanguage() {
        val kd = KeywordDefinition(2, "de", "sooo", 1, kw_de, kw_en, kw_fr)
        assertThat(kd.id).isEqualTo(2)
        assertThat(kd.name).isEqualTo("stichwort2")
        assertThat(kd.searchOverride).isEqualTo("sooo")
        assertThat(kd.displayValue).isEqualTo("stichwort2")
        assertThat(kd.translations.asMap()).hasSize(3)
        assertThat(kd.translations.keySet()).containsExactly("de", "en", "fr")
        val trs = kd.translations.values()
        assertThat(trs.map { it.name }).containsOnly("stichwort2", "keyword2", "motdeclef2")
        for (tr in trs)
            assertThat(tr.lastModified).isNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        assertThat(kd.translationsAsString).isEqualTo("DE: 'stichwort2'; EN: 'keyword2'; FR: 'motdeclef2'")
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en,
                KeywordTranslation(12, "fr", null, 1))
        assertThat(kd.translationsAsString).isEqualTo("DE: 'stichwort2'; EN: 'keyword2'; FR: n.a.")
    }

    @Test
    fun canGetTranslationsAsString_withNoTranslations() {
        val kd = KeywordDefinition(2, "de", 1)
        assertThat(kd.translationsAsString).isNull()
    }

    @Test
    fun modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        kd.setNameInLanguage("de", "KEYWORD 2")
        assertThat(kd.name).isEqualTo("KEYWORD 2")
        assertTranslatedName(kd, "de", 0, "KEYWORD 2")
        assertLastModifiedIsNotNull(kd, "de", 0)
        assertLastModifiedIsNull(kd, "en", 0)
        assertLastModifiedIsNull(kd, "fr", 0)
    }

    private fun assertTranslatedName(kd: KeywordDefinition, lc: String, index: Int, value: String) {
        assertThat(kd.translations.get(lc)[index].name).isEqualTo(value)
    }

    @Suppress("SameParameterValue")
    private fun assertLastModifiedIsNotNull(kd: KeywordDefinition, lc: String, index: Int) {
        assertThat(kd.translations.get(lc)[index].lastModified).isNotNull()
    }

    private fun assertLastModifiedIsNull(kd: KeywordDefinition, lc: String, index: Int) {
        assertThat(kd.translations.get(lc)[index].lastModified).isNull()
    }

    @Test
    fun modifyTranslation_withNonMainLanguageTranslationModified_keepsMainName_changesTranslationName_andSetsModifiedTimestamp() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        kd.setNameInLanguage("fr", "bar")
        assertThat(kd.name).isEqualTo("stichwort2")
        assertTranslatedName(kd, "fr", 0, "bar")
        assertThat(kd.translations.get("de")[0].lastModified).isNull()
        assertThat(kd.translations.get("en")[0].lastModified).isNull()
        assertLastModifiedIsNotNull(kd, "fr", 0)
    }

    @Test
    fun gettingNameInLanguage_withValidLanguages_returnsNames() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr)
        assertThat(kd.getNameInLanguage("de")).isEqualTo("stichwort2")
        assertThat(kd.getNameInLanguage("en")).isEqualTo("keyword2")
        assertThat(kd.getNameInLanguage("fr")).isEqualTo("motdeclef2")
    }

    @Test
    fun gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        assertThat(KeywordDefinition(2, "de", 1).getNameInLanguage("de")).isNull()
    }

    @Test
    fun withTranslations_moreThanOnePerLanguage() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr)
        assertThat(kd.id).isEqualTo(2)
        assertThat(kd.name).isEqualTo("stichwort2")
        assertThat(kd.displayValue).isEqualTo("stichwort2")
        assertThat(kd.translations.asMap()).hasSize(3)
        assertThat(kd.translations.keySet()).containsExactly("de", "en", "fr")
        val trs = kd.translations.values()
        assertThat(trs.map { it.name }).containsOnly("stichwort2", "stichwort2foo", "keyword2", "motdeclef2")
        for (tr in trs)
            assertThat(tr.lastModified).isNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr)
        assertThat(kd.translationsAsString).isEqualTo(
                "DE: 'stichwort2','stichwort2foo'; EN: 'keyword2'; FR: 'motdeclef2'")
    }

    @Test
    fun modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp_multipleTranslationsPerLanguage() {
        val kd = KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr, kw_de2)
        kd.setNameInLanguage("de", "Stichwort 2")
        assertThat(kd.name).isEqualTo("Stichwort 2")
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
        assertThat(kd.nullSafeId).isEqualTo(2)
    }

    @Test
    fun gettingNullSafeId_withNoIdPresent() {
        val kd = KeywordDefinition(null, "de", 1, kw_de, kw_en, kw_fr, kw_de2)
        assertThat(kd.nullSafeId).isEqualTo(0)
    }
}