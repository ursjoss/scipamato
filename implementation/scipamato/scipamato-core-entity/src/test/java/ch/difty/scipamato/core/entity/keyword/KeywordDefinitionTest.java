package ch.difty.scipamato.core.entity.keyword;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Collection;

import org.junit.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.TestUtils;

public class KeywordDefinitionTest {

    private final KeywordTranslation kw_de  = new KeywordTranslation(10, "de", "stichwort2", "stich", 1);
    private final KeywordTranslation kw_de2 = new KeywordTranslation(10, "de", "stichwort2foo", null, 1);
    private final KeywordTranslation kw_en  = new KeywordTranslation(11, "en", "keyword2", null, 1);
    private final KeywordTranslation kw_fr  = new KeywordTranslation(12, "fr", "motdeclef2", null, 1);

    @Test
    public void degenerateConstruction_withNullLanguageCode_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new KeywordDefinition(1, null, 1), "mainLanguageCode");
    }

    @Test
    public void withNoTranslations_unableToEstablishMainName() {
        KeywordDefinition ntd = new KeywordDefinition(1, "de", 1);
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getName()).isEqualTo("n.a.");
        assertThat(ntd.getDisplayValue()).isEqualTo("n.a.");
        assertThat(ntd
            .getTranslations()
            .asMap()).isEmpty();
    }

    @Test
    public void withTranslations_onePerLanguage() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        assertThat(ntd.getId()).isEqualTo(2);
        assertThat(ntd.getName()).isEqualTo("stichwort2");
        assertThat(ntd.getDisplayValue()).isEqualTo("stichwort2");
        assertThat(ntd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(ntd
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<KeywordTranslation> trs = ntd
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(KeywordTranslation.KeywordTranslationFields.NAME.getName())
            .containsOnly("stichwort2", "keyword2", "motdeclef2");
        for (final KeywordTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        assertThat(ntd.getTranslationsAsString()).isEqualTo("DE: 'stichwort2'; EN: 'keyword2'; FR: 'motdeclef2'");
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en,
            new KeywordTranslation(12, "fr", null, null, 1));
        assertThat(ntd.getTranslationsAsString()).isEqualTo("DE: 'stichwort2'; EN: 'keyword2'; FR: n.a.");
    }

    @Test
    public void canGetTranslationsAsString_withNoTranslations() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1);
        assertThat(ntd.getTranslationsAsString()).isNull();
    }

    @Test
    public void modifyingTranslation_withNullLanguageCode_throws() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        try {
            ntd.setNameInLanguage(null, "foo");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("langCode must not be null.");
        }
    }

    @Test
    public void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        ntd.setNameInLanguage("de", "KEYWORD 2");
        assertThat(ntd.getName()).isEqualTo("KEYWORD 2");
        assertTranslatedName(ntd, "de", 0, "KEYWORD 2");
        assertLastModifiedIsNotNull(ntd, "de", 0);
        assertLastModifiedIsNull(ntd, "en", 0);
        assertLastModifiedIsNull(ntd, "fr", 0);
    }

    private void assertTranslatedName(final KeywordDefinition ntd, final String lc, final int index,
        final String value) {
        assertThat(ntd
            .getTranslations()
            .get(lc)
            .get(index)
            .getName()).isEqualTo(value);
    }

    private void assertLastModifiedIsNotNull(final KeywordDefinition ntd, final String lc, final int index) {
        assertThat(ntd
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNotNull();
    }

    private void assertLastModifiedIsNull(final KeywordDefinition ntd, final String lc, final int index) {
        assertThat(ntd
            .getTranslations()
            .get(lc)
            .get(index)
            .getLastModified()).isNull();
    }

    @Test
    public void modifyTranslation_withNonMainLanguageTranslationModified_keepsMainName_changesTranslationName_andSetsModifiedTimestamp() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        ntd.setNameInLanguage("fr", "bar");
        assertThat(ntd.getName()).isEqualTo("stichwort2");
        assertTranslatedName(ntd, "fr", 0, "bar");
        assertThat(ntd
            .getTranslations()
            .get("de")
            .get(0)
            .getLastModified()).isNull();
        assertThat(ntd
            .getTranslations()
            .get("en")
            .get(0)
            .getLastModified()).isNull();
        assertLastModifiedIsNotNull(ntd, "fr", 0);
    }

    @Test
    public void gettingNameInLanguage_withValidLanguages_returnsNames() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr);
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("stichwort2");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("keyword2");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("motdeclef2");
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
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr);
        assertThat(ntd.getId()).isEqualTo(2);
        assertThat(ntd.getName()).isEqualTo("stichwort2");
        assertThat(ntd.getDisplayValue()).isEqualTo("stichwort2");
        assertThat(ntd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(ntd
            .getTranslations()
            .keySet()).containsExactly("de", "en", "fr");
        Collection<KeywordTranslation> trs = ntd
            .getTranslations()
            .values();
        assertThat(trs)
            .extracting(KeywordTranslation.KeywordTranslationFields.NAME.getName())
            .containsOnly("stichwort2", "stichwort2foo", "keyword2", "motdeclef2");
        for (final KeywordTranslation tr : trs)
            assertThat(tr.getLastModified()).isNull();
    }

    @Test
    public void canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withMultipleTranslations() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_de2, kw_en, kw_fr);
        assertThat(ntd.getTranslationsAsString()).isEqualTo(
            "DE: 'stichwort2','stichwort2foo'; EN: 'keyword2'; FR: 'motdeclef2'");
    }

    @Test
    public void modifyTranslation_withMainLanguageTranslationModified_changesMainName_translationName_andSetsModifiedTimestamp_multipleTranslationsPerLanguage() {
        KeywordDefinition ntd = new KeywordDefinition(2, "de", 1, kw_de, kw_en, kw_fr, kw_de2);
        ntd.setNameInLanguage("de", "Stichwort 2");
        assertThat(ntd.getName()).isEqualTo("Stichwort 2");
        assertTranslatedName(ntd, "de", 0, "Stichwort 2");
        assertTranslatedName(ntd, "de", 1, "stichwort2foo");
        assertLastModifiedIsNotNull(ntd, "de", 0);
        assertLastModifiedIsNull(ntd, "de", 1);
        assertLastModifiedIsNull(ntd, "en", 0);
        assertLastModifiedIsNull(ntd, "fr", 0);
    }

}