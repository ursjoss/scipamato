package ch.difty.scipamato.pubmed.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.entity.xml.ScipamatoPubmedArticleIntegrationTest;
import ch.difty.scipamato.persistence.PubmedArticleService;
import ch.difty.scipamato.pubmed.PubmedArticleFacade;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PubmedXmlServiceIntegrationTest {

    @Autowired
    private PubmedArticleService service;

    // https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=25395026&retmode=xml&version=2.0
    @Test
    public void gettingArticleFromPubmed_withValidPmId_returnsArticle() {
        final int pmId = 25395026;

        final Optional<PubmedArticleFacade> article = service.getPubmedArticleWithPmid(pmId);
        assertThat(article.isPresent()).isTrue();
        ScipamatoPubmedArticleIntegrationTest.assertArticle239026(article.get());
    }

    @Test
    public void gettingArticleFromPubmed_withNotExistingPmId_returnsEmptyOptional() {
        final int pmId = 999999999;

        final Optional<PubmedArticleFacade> article = service.getPubmedArticleWithPmid(pmId);
        assertThat(article.isPresent()).isFalse();
    }

}
