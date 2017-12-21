package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

public class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties();

    @Test
    public void brand_hasDefaultValue() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo-Core");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("en");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    public void authorParser_isDefault() {
        assertThat(sp.getAuthorParser()).isEqualTo("DEFAULT");
    }

    @Test
    public void authorParserStrategy_isDefault() {
        assertThat(sp.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.DEFAULT);
    }

    @Test
    public void paperMinimumToBeRecycled_hasDefaultValue() {
        assertThat(sp.getPaperNumberMinimumToBeRecycled()).isEqualTo(0);
    }

    @Test
    public void dbSchema_hasDefaultValuePublic() {
        assertThat(sp.getDbSchema()).isEqualTo("public");
    }

}
