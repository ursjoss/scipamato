package ch.difty.scipamato.core.entity.keyword;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.TestUtils;

@SuppressWarnings("SameParameterValue")
public class KeywordDefinitionTest {

    private final KeywordTranslation kw_de  = new KeywordTranslation(10, "de", "stichwort2", 1);
    private final KeywordTranslation kw_de2 = new KeywordTranslation(10, "de", "stichwort2foo", 1);
    private final KeywordTranslation kw_en  = new KeywordTranslation(11, "en", "keyword2", 1);
    private final KeywordTranslation kw_fr  = new KeywordTranslation(12, "fr", "motdeclef2", 1);

    @Test
    public void withNoTranslations_unableToEstablishMainName() {
        KeywordDefinition kd = new KeywordDefinition(1, "de", 1);
        assertThat(kd.getId()).isEqualTo(1);
        assertThat(kd.getName()).isEqualTo("n.a.");
        assertThat(kd.getSearchOverride()).isNull();
        assertThat(kd.getDisplayValue()).isEqualTo("n.a.");
        assertThat(kd
            .getTranslations()
            .asMap()).isEmpty();
    }

    @Test
    public void withSearchOverride() {
        KeywordDefinition kd = new KeywordDefinition(1, "de", "so", 1);
        assertThat(kd.getId()).isEqualTo(1);
        assertThat(kd.getName()).isEqualTo("n.a.");
        assertThat(kd.getSearchOverride()).isEqualTo("so");
        assertThat(kd.getDisplayValue()).isEqualTo("n.a.");
        assertThat(kd
            .getTranslations()
            .asMap()).isEmpty();
    }

    @Test
    public void withTranslations_onePerLanguage() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", "sooo", 1, kw_de, kw_en, kw_fr);
        assertThat(kd.getId()).isEqualTo(2);
        assertThat(kd.getName()).isEqualTo("stichwort2");
        assertThat(kd.getSearchOverride()).isEqualTo("sooo");
        assertThat(kd.getDisplayValue()).isEqualTo("stichwort2");
        assertThat(kd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(kd
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<KeywordTranslation> trs = kd
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(KeywordTranslation.DefinitionTranslationFields.NAME.getName())
            .containsOnly("stichwort2", "keyword2", "motdeclef2");
        for (final KeywordTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        assertThat(kd.getTranslationsAsString()).isEqualTo("DE: 'stichwort2'; EN: 'keyword2'; FR: 'motdeclef2'");
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_en,
            new KeywordTranslation(12, "fr", null, 1));
        assertThat(kd.getTranslationsAsString()).isEqualTo("DE: 'stichwort2'; EN: 'keyword2'; FR: n.a.");
    }

    @Test
    public void canGetTranslationsAsString_withNoTranslations() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1);
        assertThat(kd.getTranslationsAsString()).isNull();
    }

    @Test
    public void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        kd.setNameInLanguage("de", "KEYWORD 2");
        assertThat(kd.getName()).isEqualTo("KEYWORD 2");
        assertTranslatedName(kd, "de", 0, "KEYWORD 2");
        assertLastModifiedIsNotNull(kd, "de", 0);
        assertLastModifiedIsNull(kd, "en", 0);
        assertLastModifiedIsNull(kd, "fr", 0);
    }

    private void assertTranslatedName(final KeywordDefinition kd, final String lc, final int index,
        final String value) {
        assertThat(kd
            .getTranslations()
            .get(lc)
            .get(index)
            .getName()).isEqualTo(value);
    }

    private void assertLastModifiedIsNotNull(final KeywordDefinition kd, final String lc, final int index) {
        assertThat(kd
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNotNull();
    }

    private void assertLastModifiedIsNull(final KeywordDefinition kd, final String lc, final int index) {
        assertThat(kd
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNull();
    }

    @Test
    public void modifyTranslation_withNonMainLanguageTranslationModified_keepsMainName_changesTranslationName_andSetsModifiedTimestamp() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        kd.setNameInLanguage("fr", "bar");
        assertThat(kd.getName()).isEqualTo("stichwort2");
        assertTranslatedName(kd, "fr", 0, "bar");
        assertThat(kd
            .getTranslations()
            .get("de")
            .get(0)
            .getLastModified()).isNull();
        assertThat(kd
            .getTranslations()
            .get("en")
            .get(0)
            .getLastModified()).isNull();
        assertLastModifiedIsNotNull(kd, "fr", 0);
    }

    @Test
    public void gettingNameInLanguage_withValidLanguages_returnsNames() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        assertThat(kd.getNameInLanguage("de")).isEqualTo("stichwort2");
        assertThat(kd.getNameInLanguage("en")).isEqualTo("keyword2");
        assertThat(kd.getNameInLanguage("fr")).isEqualTo("motdeclef2");
    }

    @Test
    public void gettingNameInLanguage_withInvalidLanguage_returnsNames() {
        assertThat(new KeywordDefinition(2, "de", 1).getNameInLanguage("de")).isNull();
    }

    @Test
    public void gettingNameInLanguage_withNullLanguage_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new KeywordDefinition(2, "de", 1).getNameInLanguage(null),
            "langCode");
    }

    @Test
    public void withTranslations_moreThanOnePerLanguage() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr);
        assertThat(kd.getId()).isEqualTo(2);
        assertThat(kd.getName()).isEqualTo("stichwort2");
        assertThat(kd.getDisplayValue()).isEqualTo("stichwort2");
        assertThat(kd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(kd
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<KeywordTranslation> trs = kd
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(KeywordTranslation.DefinitionTranslationFields.NAME.getName())
            .containsOnly("stichwort2", "stichwort2foo", "keyword2", "motdeclef2");
        for (final KeywordTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr);
        assertThat(kd.getTranslationsAsString()).isEqualTo(
            "DE: 'stichwort2','stichwort2foo'; EN: 'keyword2'; FR: 'motdeclef2'");
    }

    @Test
    public void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp_multipleTranslationsPerLanguage() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr, kw_de2);
        kd.setNameInLanguage("de", "Stichwort 2");
        assertThat(kd.getName()).isEqualTo("Stichwort 2");
        assertTranslatedName(kd, "de", 0, "Stichwort 2");
        assertTranslatedName(kd, "de", 1, "stichwort2foo");
        assertLastModifiedIsNotNull(kd, "de", 0);
        assertLastModifiedIsNull(kd, "de", 1);
        assertLastModifiedIsNull(kd, "en", 0);
        assertLastModifiedIsNull(kd, "fr", 0);
    }

    @Test
    public void gettingNullSafeId_withIdPresent() {
        KeywordDefinition kd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr, kw_de2);
        assertThat(kd.getNullSafeId()).isEqualTo(2);
    }

    @Test
    public void gettingNullSafeId_withNoIdPresent() {
        KeywordDefinition kd = new KeywordDefinition(null, "de", 1, kw_de, kw_en, kw_fr, kw_de2);
        assertThat(kd.getNullSafeId()).isEqualTo(0);
    }
}