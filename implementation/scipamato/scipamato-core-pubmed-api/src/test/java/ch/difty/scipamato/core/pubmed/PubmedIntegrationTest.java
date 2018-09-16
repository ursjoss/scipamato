package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("SameParameterValue")
public abstract class PubmedIntegrationTest {

    @Autowired
    private PubmedXmlService service;

    PubmedArticleSet getPubmedArticleSet(String fileName) throws IOException {
        String xml = TestUtils.readFileAsString(fileName);
        assertThat(xml).isNotNull();
        return service.unmarshal(xml);
    }

    List<PubmedArticleFacade> getPubmedArticles(String fileName) throws IOException {
        String xml = TestUtils.readFileAsString(fileName);
        assertThat(xml).isNotNull();
        return service.extractArticlesFrom(xml);
    }

}
