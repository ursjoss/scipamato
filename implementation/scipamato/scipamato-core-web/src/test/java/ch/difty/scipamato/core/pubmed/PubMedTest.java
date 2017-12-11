package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class PubMedTest {

    @Test
    public void assertLink() {
        assertThat(PubMed.URL).isEqualTo("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
    }

}
