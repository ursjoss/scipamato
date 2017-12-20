package ch.difty.scipamato.public_.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.public_.ScipamatoPublicApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScipamatoPublicApplication.class)
public class ScipamatoPublicPropertiesIntegrationTest {

    @Autowired
    private ScipamatoPublicProperties spp;

    @Test
    public void brand_hasDefaultValue() {
        assertThat(spp.getBrand()).isEqualTo("SciPaMaTo");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(spp.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(spp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

}
