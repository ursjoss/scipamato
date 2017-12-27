package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class PubmedIntegrationTest {

    @Autowired
    private PubmedXmlService service;

    protected PubmedArticleSet getPubmedArticleSet(String fileName) throws IOException {
        String xml = readXmlFile(fileName);
        assertThat(xml).isNotNull();
        return service.unmarshal(xml);
    }

    protected List<PubmedArticleFacade> getPubmedArticles(String fileName) throws IOException {
        String xml = readXmlFile(fileName);
        assertThat(xml).isNotNull();
        return service.extractArticlesFrom(xml);
    }

    public static String readXmlFile(String fileName) throws IOException {
        return IOUtils.toString(ClassLoader.getSystemResourceAsStream(fileName), StandardCharsets.UTF_8);
    }
}
