package ch.difty.scipamato.core.pubmed;

import static ch.difty.scipamato.common.TestUtilsKt.readFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet;

@SpringBootTest
@SuppressWarnings("SameParameterValue")
abstract class PubmedIntegrationTest {

    @Autowired
    private PubmedXmlService service;

    PubmedArticleSet getPubmedArticleSet(String fileName) throws IOException {
        String xml = readFileAsString(fileName);
        assertThat(xml).isNotNull();
        return service.unmarshal(xml);
    }

    List<PubmedArticleFacade> getPubmedArticles(String fileName) throws IOException {
        String xml = readFileAsString(fileName);
        assertThat(xml).isNotNull();
        return service.extractArticlesFrom(xml);
    }

}
