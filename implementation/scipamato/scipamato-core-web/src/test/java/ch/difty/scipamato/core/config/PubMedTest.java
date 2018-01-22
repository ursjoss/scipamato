package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.config.PubMed;

public class PubMedTest {

    @Test
    public void assertLink() {
        assertThat(PubMed.URL).isEqualTo("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
    }

}
