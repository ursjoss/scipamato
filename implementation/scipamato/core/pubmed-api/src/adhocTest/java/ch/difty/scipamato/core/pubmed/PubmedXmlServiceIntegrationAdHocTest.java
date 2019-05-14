package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Note: This ad-hoc integration test should not run automatically, as it
 * actually issues a call to PubMed over the internet. Thus this test fails if
 * the machine running the test is offline or cannot reach PubMed for some other
 * reason.
 * <p>
 * Also it's not polite to continuously access the public service.
 *
 * @author u.joss
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class PubmedXmlServiceIntegrationAdHocTest {

    @Autowired
    private PubmedArticleService service;

    // https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=25395026&retmode=xml&version=2.0
    @Test
    void gettingArticleFromPubmed_withValidPmId_returnsArticleAndNoMessage() {
        final int pmId = 25395026;

        final PubmedArticleResult result = service.getPubmedArticleWithPmid(pmId);
        assertThat(result.getPubmedArticleFacade()).isNotNull();
        assertThat(result.getErrorMessage()).isNull();
        assertArticle239026(result.getPubmedArticleFacade());
    }

    private void assertArticle239026(final PubmedArticleFacade sa) {
        assertThat(sa.getPmId()).isEqualTo("25395026");
        assertThat(sa.getAuthors()).isEqualTo(
            "Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.");
        assertThat(sa.getFirstAuthor()).isEqualTo("Turner");
        assertThat(sa.getPublicationYear()).isEqualTo("2014");
        assertThat(sa.getLocation()).isEqualTo("Am J Epidemiol. 2014; 180 (12): 1145-1149.");
        assertThat(sa.getTitle()).isEqualTo(
            "Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.");
        assertThat(sa.getDoi()).isEqualTo("10.1093/aje/kwu275");
        assertThat(sa.getOriginalAbstract()).startsWith(
            "The International Agency for Research on Cancer recently classified outdoor air pollution");
        assertThat(sa
            .getOriginalAbstract()
            .trim()).endsWith("based on reducing exposure to either risk factor alone.");
    }

    @Test
    void gettingArticleFromPubmed_withNotExistingPmId_returnsNoArticle() {
        final int pmId = 999999999;

        final PubmedArticleResult result = service.getPubmedArticleWithPmid(pmId);
        assertThat(result.getPubmedArticleFacade()).isNull();
        assertThat(result.getErrorMessage()).isEqualTo("PMID " + pmId + " seems to be undefined in PubMed.");
    }

    // https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?api_key=xxx,db=pubmed&id=25395026&retmode=xml&version=2.0
    @Test
    void gettingArticleFromPubmed_withValidPmIdButInvalidApiKey_returnsNoting() {
        final int pmId = 25395026;
        final String apiKey = "xxx";

        final PubmedArticleResult result = service.getPubmedArticleWithPmidAndApiKey(pmId, apiKey);
        assertThat(result.getPubmedArticleFacade()).isNull();
        assertThat(result.getErrorMessage()).isEqualTo(
            "Status 400 BAD_REQUEST: status 400 reading PubMed#articleWithId(String,String)");
    }
}
