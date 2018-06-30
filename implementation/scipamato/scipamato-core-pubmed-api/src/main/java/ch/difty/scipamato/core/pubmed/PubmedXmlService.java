package ch.difty.scipamato.core.pubmed;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet;

/**
 * Service handling PubMed content.
 *
 * @author u.joss
 */
@Service
@Slf4j
public class PubmedXmlService implements PubmedArticleService {

    private final Unmarshaller unmarshaller;
    private final PubMed       pubMed;

    public PubmedXmlService(final Unmarshaller unmarshaller, final PubMed pubMed) {
        this.unmarshaller = AssertAs.notNull(unmarshaller, "unmarshaller");
        this.pubMed = AssertAs.notNull(pubMed, "pubMed");
    }

    @Override
    public Optional<PubmedArticleFacade> getPubmedArticleWithPmid(final int pmId) {
        try {
            final PubmedArticleSet set = pubMed.articleWithId(String.valueOf(pmId));
            final List<java.lang.Object> articles = set.getPubmedArticleOrPubmedBookArticle();
            return articles
                .stream()
                .map(PubmedArticleFacade::newPubmedArticleFrom)
                .findFirst();
        } catch (final Exception ex) {
            log.error("Unexpected error", ex);
            return Optional.empty();
        }
    }

    /**
     * Unmarshal the XML content of the pubmed XML export. Returns a
     * {@link PubmedArticleSet}.
     *
     * @param xmlString
     *     the raw xml string to unmarshal
     * @return {@link PubmedArticleSet}
     * @throws IOException
     *     TODO describe conditions
     * @throws org.springframework.oxm.XmlMappingException
     *     TODO describe conditions
     */
    public PubmedArticleSet unmarshal(final String xmlString) throws IOException {
        final StringReader reader = new StringReader(AssertAs.notNull(xmlString, "xmlString"));
        return (PubmedArticleSet) unmarshaller.unmarshal(new StreamSource(reader));
    }

    /**
     * Extracts PubMed articles and PubMed book articles from the source string
     * representing XML exported from PubMed.
     * <p>
     * The XML string could be derived e.g. from
     * <ul>
     * <li>via API, e.g.
     * {@code https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=25395026&retmode=xml}</li>
     * <li>through the Web UI (e.g. https://www.ncbi.nlm.nih.gov/pubmed/25395026)
     * when sending to {@code file} in format {@code XML}</li>
     * </ul>
     *
     * @param xmlString
     *     pubmed content in XML format, as String. Must not be null.
     * @return List of {@link PubmedArticleFacade} entries. Never null. Will be
     *     empty if there are issues with the XML.
     * @throws NullArgumentException
     *     in case of null xmlString.
     */
    @Override
    public List<PubmedArticleFacade> extractArticlesFrom(final String xmlString) {
        final List<PubmedArticleFacade> articles = new ArrayList<>();
        try {
            final PubmedArticleSet set = unmarshal(xmlString);
            final List<java.lang.Object> aoba = set.getPubmedArticleOrPubmedBookArticle();
            articles.addAll(aoba
                .stream()
                .map(PubmedArticleFacade::newPubmedArticleFrom)
                .collect(Collectors.toList()));
        } catch (final Exception e) {
            log.info("Unable to parse xmlString '{}': {}", xmlString, e.getMessage());
        }
        return articles;
    }

}
