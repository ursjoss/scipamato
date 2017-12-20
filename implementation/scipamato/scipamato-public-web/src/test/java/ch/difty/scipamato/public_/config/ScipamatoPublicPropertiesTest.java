package ch.difty.scipamato.public_.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ScipamatoPublicPropertiesTest {

    private final ScipamatoPublicProperties spp = new ScipamatoPublicProperties();

    @Test
    public void brand_hasDefaultValue() {
        assertThat(spp.getBrand()).isEqualTo("SciPaMaTo-Public");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(spp.getDefaultLocalization()).isEqualTo("en");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(spp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

}
