package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet;

public class PubMedTest {

    @Test
    public void assertLink() {
        assertThat(PubMed.URL).isEqualTo("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
    }

    @Test
    public void test() {
        assertThat(new MyPubmed().getUrl()).isEqualTo(PubMed.URL);
    }

    private static class MyPubmed implements PubMed {

        @Override
        public PubmedArticleSet articleWithId(final String pmid) {
            return null;
        }

        @Override
        public PubmedArticleSet articleWithId(final String pmid, final String apiKey) {
            return null;
        }

        String getUrl() {
            return URL;
        }
    }
}
