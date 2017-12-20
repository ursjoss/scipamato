package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

public class ScipamatoCorePropertiesTest {

    private final ScipamatoCoreProperties scp = new ScipamatoCoreProperties();

    @Test
    public void brand_hasDefaultValue() {
        assertThat(scp.getBrand()).isEqualTo("SciPaMaTo-Core");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(scp.getDefaultLocalization()).isEqualTo("en");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(scp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    public void authorParser_isDefault() {
        assertThat(scp.getAuthorParser()).isEqualTo("DEFAULT");
    }

    @Test
    public void authorParserStrategy_isDefault() {
        assertThat(scp.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.DEFAULT);
    }

    @Test
    public void paperMinimumToBeRecycled_hasDefaultValue() {
        assertThat(scp.getPaperNumberMinimumToBeRecycled()).isEqualTo(0);
    }

    @Test
    public void dbSchema_hasDefaultValuePublic() {
        assertThat(scp.getDbSchema()).isEqualTo("public");
    }

}
