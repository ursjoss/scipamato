package ch.difty.sipamato.pubmed.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.pubmed.PubMed;
import ch.difty.sipamato.pubmed.PubmedArticleSet;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.sipamato.service.PubmedArticleService;

/**
 * Service handling pubmed content.
 *
 * @author u.joss
 */
@Service
public class PubmedXmlService implements PubmedArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubmedXmlService.class);

    private final Unmarshaller unmarshaller;

    private final PubMed pubMed;

    @Autowired
    public PubmedXmlService(final Unmarshaller unmarshaller, final PubMed pubMed) {
        this.unmarshaller = AssertAs.notNull(unmarshaller, "unmarshaller");
        this.pubMed = AssertAs.notNull(pubMed, "pubMed");
    }

    /** {@inheritDoc} */
    @Override
    public Optional<PubmedArticleFacade> getPubmedArticleWithPmid(int pmId) {
        final PubmedArticleSet set = pubMed.articleWithId(String.valueOf(pmId));
        final List<Object> aoba = set.getPubmedArticleOrPubmedBookArticle();
        if (aoba != null && !aoba.isEmpty()) {
            return aoba.stream().map(PubmedArticleFacade::of).findFirst();
        }
        return Optional.empty();
    }

    /**
     * Unmarshal the XML content of the pubmed XML export. Returns a {@link  PubmedArticleSet}.
     *
     * @param xmlString
     * @return {@link PubmedArticleSet}
     * @throws IOException
     */
    public PubmedArticleSet unmarshal(final String xmlString) throws IOException {
        final StringReader reader = new StringReader(AssertAs.notNull(xmlString, "xmlString"));
        return (PubmedArticleSet) unmarshaller.unmarshal(new StreamSource(reader));
    }

    /**
     * Extracts pubmed articles and pubmed book articles from the source string representing XML exported from Pubmed.
     *
     * <p>The XML string could be derived e.g. from
     * <ul>
     * <li> via API, e.g. {@code https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=25395026&retmode=xml}</li>
     * <li> through the Web UI (e.g. https://www.ncbi.nlm.nih.gov/pubmed/25395026) when sending to {@code file} in format {@code XML}</li>
     * </ul>
     *
     * @param content pubmed content in XML format, as String. Must not be null.
     * @return List of {@link PubmedArticleFacade} entries. Never null. Will be empty if there are issues with the XML.
     * @throws NullArgumentException in case of null xmlString.
     */
    @Override
    public List<PubmedArticleFacade> extractArticlesFrom(final String xmlString) {
        final List<PubmedArticleFacade> articles = new ArrayList<>();
        try {
            final PubmedArticleSet set = unmarshal(xmlString);
            final List<Object> aoba = set.getPubmedArticleOrPubmedBookArticle();
            articles.addAll(aoba.stream().map(PubmedArticleFacade::of).collect(Collectors.toList()));
        } catch (final Exception e) {
            LOGGER.info("Unable to parse xmlString '{}': {}", xmlString, e.getMessage());
        }
        return articles;
    }

}
