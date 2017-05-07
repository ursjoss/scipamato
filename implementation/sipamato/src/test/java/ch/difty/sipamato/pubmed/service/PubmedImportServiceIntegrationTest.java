package ch.difty.sipamato.pubmed.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.entity.xml.PubmedIntegrationTest;
import ch.difty.sipamato.persistance.jooq.JooqBaseIntegrationTest;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.service.PubmedImporter;
import ch.difty.sipamato.service.ServiceResult;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PubmedImportServiceIntegrationTest extends JooqBaseIntegrationTest {

    private static final String XML = "xml/pubmed_result_3studies.xml";

    private static final String PMID1 = "28454017";
    private static final String PMID2 = "28447797";
    private static final String PMID3 = "28431317";

    @Autowired
    private PubmedImporter importer;

    @Autowired
    private PaperService paperService;

    @Test
    public void canReadXmlFile_whichHas3Studies() throws IOException {
        final String xml = PubmedIntegrationTest.readXmlFile(XML);
        assertThat(xml).startsWith("<?xml version").endsWith("</PubmedArticleSet>\n");
        assertThat(xml).contains("<PMID Version=\"1\">" + PMID1 + "</PMID>");
        assertThat(xml).contains("<PMID Version=\"1\">" + PMID2 + "</PMID>");
        assertThat(xml).contains("<PMID Version=\"1\">" + PMID3 + "</PMID>");
    }

    @Test
    public void test() throws IOException {
        final String xml = PubmedIntegrationTest.readXmlFile(XML);

        final ServiceResult result = importer.persistPubmedArticlesFromXml(xml);

        assertThat(result.getErrorMessages()).isEmpty();
        assertThat(result.getWarnMessages()).isEmpty();
        assertThat(result.getInfoMessages()).hasSize(3);
        final List<String> messagesWithoutId = result.getInfoMessages().stream().map((m) -> m.substring(0, m.indexOf("(") - 1)).collect(Collectors.toList());
        assertThat(messagesWithoutId).contains("PMID " + PMID1, "PMID " + PMID2, "PMID " + PMID3);

        // Delete created records
        final List<Long> ids = result.getInfoMessages().stream().map((m) -> m.substring(m.indexOf("(") + 4, m.length() - 1)).map((id) -> Long.valueOf(id)).collect(Collectors.toList());
        paperService.deleteByIds(ids);
    }

}
