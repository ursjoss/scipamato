package ch.difty.scipamato.core.pubmed

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Note: This ad-hoc integration test should not run automatically, as it
 * actually issues a call to PubMed over the internet. Thus this test fails if
 * the machine running the test is offline or cannot reach PubMed for some other
 * reason.
 *
 *
 * Also it's not polite to continuously access the public service.
 *
 * @author u.joss
 */
@SpringBootTest
@Suppress("FunctionName", "SpellCheckingInspection", "MagicNumber")
internal class PubmedXmlServiceIntegrationAdHocTest {

    @Autowired
    private val service: PubmedArticleService? = null

    // https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=25395026&retmode=xml&version=2.0
    @Test
    fun gettingArticleFromPubmed_withValidPmId_returnsArticleAndNoMessage() {
        val pmId = 25395026

        val result = service!!.getPubmedArticleWithPmid(pmId)
        assertThat(result.pubmedArticleFacade == null).isFalse()
        assertThat(result.errorMessage).isNull()
        assertArticle239026(result.pubmedArticleFacade)
    }

    private fun assertArticle239026(sa: PubmedArticleFacade) {
        with(sa) {
            assertThat(pmId).isEqualTo("25395026")
            assertThat(authors).isEqualTo(
                "Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM."
            )
            assertThat(firstAuthor).isEqualTo("Turner")
            assertThat(publicationYear).isEqualTo("2014")
            assertThat(location).isEqualTo("Am J Epidemiol. 2014; 180 (12): 1145-1149.")
            assertThat(title).isEqualTo(
                """Interactions between cigarette smoking and fine particulate matter
                    | in the Risk of Lung Cancer Mortality in Cancer Prevention Study II.""".trimMargin()
            )
            assertThat(doi).isEqualTo("10.1093/aje/kwu275")
            assertThat(originalAbstract).startsWith(
                "The International Agency for Research on Cancer recently classified outdoor air pollution"
            )
            assertThat(sa.originalAbstract.trim { it <= ' ' })
                .endsWith("based on reducing exposure to either risk factor alone.")
        }
    }

    @Test
    fun gettingArticleFromPubmed_withNotExistingPmId_returnsNoArticle() {
        val pmId = 999999999
        val result = service!!.getPubmedArticleWithPmid(pmId)
        assertThat(result.pubmedArticleFacade == null).isTrue()
        assertThat(result.errorMessage).isEqualTo("PMID $pmId seems to be undefined in PubMed.")
    }

    // https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?api_key=xxx,db=pubmed&id=25395026&retmode=xml&version=2.0
    @Test
    fun gettingArticleFromPubmed_withValidPmIdButInvalidApiKey_returnsNoting() {
        val pmId = 25395026
        val apiKey = "xxx"

        val result = service!!.getPubmedArticleWithPmidAndApiKey(pmId, apiKey)
        assertThat(result.pubmedArticleFacade == null).isTrue()
        assertThat(result.errorMessage).isEqualTo(
            "Status 400 BAD_REQUEST: status 400 reading PubMed#articleWithId(String,String)"
        )
    }
}
