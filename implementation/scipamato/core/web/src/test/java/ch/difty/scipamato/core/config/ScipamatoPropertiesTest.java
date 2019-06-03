package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties();

    @Test
    void brand_hasDefaultValue() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo-Core");
    }

    @Test
    void defaultLocalization_hasDefaultEnglish() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("en");
    }

    @Test
    void pubmedBaseUrl_hasDefaultValue() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    void authorParser_isDefault() {
        assertThat(sp.getAuthorParser()).isEqualTo("DEFAULT");
    }

    @Test
    void authorParserStrategy_isDefault() {
        assertThat(sp.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.PUBMED);
    }

    @Test
    void paperMinimumToBeRecycled_hasDefaultValue() {
        assertThat(sp.getPaperNumberMinimumToBeRecycled()).isEqualTo(0);
    }

    @Test
    void dbSchema_hasDefaultValuePublic() {
        assertThat(sp.getDbSchema()).isEqualTo("public");
    }

    @Test
    void gettingRedirectPort_hasNoDefaultValue() {
        assertThat(sp.getRedirectFromPort()).isNull();
    }

    @Test
    void gettingMultiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue() {
        assertThat(sp.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
    }

    @Test
    void gettingPubmedApiKey_hasNoDefaultValue() {
        assertThat(sp.getPubmedApiKey()).isNull();
    }
}
