package ch.difty.sipamato.entity.xml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.pubmed.PubmedArticleSet;
import ch.difty.sipamato.service.PubmedService;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class PubmedIntegrationTest {

    @Autowired
    private PubmedService service;

    protected PubmedArticleSet getPubmedArticleSet(String fileName) throws IOException {
        String xml = IOUtils.toString(ClassLoader.getSystemResourceAsStream(fileName), StandardCharsets.UTF_8);
        assertThat(xml).isNotNull();
        return service.unmarshal(xml);
    }
}
