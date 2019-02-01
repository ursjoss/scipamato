package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class JaxbConfigurationTest {

    @Test
    public void assertLink() {
        assertThat(JaxbConfiguration.PUBMED_URL).isEqualTo("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
    }
}