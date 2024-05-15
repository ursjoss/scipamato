package ch.difty.scipamato.core.pubmed;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

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

    public PubmedXmlService(@NotNull final Unmarshaller unmarshaller, @NotNull final PubMed pubMed) {
        this.unmarshaller = unmarshaller;
        this.pubMed = pubMed;
    }

    @NotNull
    @Override
    public PubmedArticleResult getPubmedArticleWithPmid(final int pmId) {
        return processArticles(retrievePubmedArticleSet(pmId), pmId);
    }

    @NotNull
    @Override
    public PubmedArticleResult getPubmedArticleWithPmidAndApiKey(final int pmId, @NotNull final String apiKey) {
        return processArticles(retrievePubmedArticleSet(pmId, apiKey), pmId);
    }

    private PubmedArticleResult processArticles(@NotNull final PubmedArticleFeignResult result, final int pmId) {
        if (result.pubmedArticleSet != null) {
            final List<Object> articles = result.pubmedArticleSet.getPubmedArticleOrPubmedBookArticle();
            if (articles != null) {
                final Optional<PubmedArticleFacade> facade = articles
                    .stream()
                    .map(PubmedArticleFacade::newPubmedArticleFrom)
                    .findFirst();
                return facade
                    .map(pubmedArticleFacade -> new PubmedArticleResult(pubmedArticleFacade, result.httpStatus, result.errorMessage))
                    .orElseGet(() -> new PubmedArticleResult(null, null, "PMID " + pmId + " seems to be undefined in PubMed."));
            }
        }
        return new PubmedArticleResult(null, result.httpStatus, result.errorMessage);
    }

    private PubmedArticleFeignResult retrievePubmedArticleSet(final int pmId) {
        return doRetrieve(() -> pubMed.articleWithId(String.valueOf(pmId)));
    }

    private PubmedArticleFeignResult retrievePubmedArticleSet(final int pmId, @NotNull final String apiKey) {
        return doRetrieve(() -> pubMed.articleWithId(String.valueOf(pmId), apiKey));
    }

    private PubmedArticleFeignResult doRetrieve(@NotNull final Supplier<PubmedArticleSet> articleSetSupplier) {
        try {
            return new PubmedArticleFeignResult(articleSetSupplier.get());
        } catch (final FeignException fex) {
            log.error("feign exception: {}", fex.getMessage());
            return new PubmedArticleFeignResult(fex);
        } catch (final Exception ex) {
            log.error("Unexpected error: {}", ex.getMessage());
            return new PubmedArticleFeignResult(ex.getMessage());
        }
    }

    /**
     * Unmarshal the XML content of the pubmed XML export. Returns a
     * {@link PubmedArticleSet}.
     *
     * @param xmlString
     *     the raw xml string to unmarshal
     * @return {@link PubmedArticleSet}
     */
    @Nullable
    public PubmedArticleSet unmarshal(@NotNull final String xmlString) throws IOException {
        final StringReader reader = new StringReader(xmlString);
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
     * <li>through the Web UI (e.g. <a href="https://www.ncbi.nlm.nih.gov/pubmed/25395026">...</a>)
     * when sending to {@code file} in format {@code XML}</li>
     * </ul>
     *
     * @param xmlString
     *     pubmed content in XML format, as String. Must not be null.
     * @return List of {@link PubmedArticleFacade} entries. Never null. Will be
     *     empty if there are issues with the XML.
     */
    @NotNull
    @Override
    public List<PubmedArticleFacade> extractArticlesFrom(@NotNull final String xmlString) {
        final List<PubmedArticleFacade> articles = new ArrayList<>();
        try {
            final PubmedArticleSet set = Objects.requireNonNull(unmarshal(xmlString));
            final List<java.lang.Object> article = set.getPubmedArticleOrPubmedBookArticle();
            articles.addAll(article
                .stream()
                .map(PubmedArticleFacade::newPubmedArticleFrom)
                .toList());
        } catch (final Exception e) {
            log.info("Unable to parse xmlString '{}': {}", xmlString, e.getMessage());
        }
        return articles;
    }

    /**
     * Data class that will either hold a {@link PubmedArticleSet} or
     * an error message (optionally with an {@link HttpStatus} if available.
     */
    private static class PubmedArticleFeignResult {
        @Nullable
        final PubmedArticleSet pubmedArticleSet;
        @Nullable
        final String           errorMessage;
        @Nullable
        final HttpStatus       httpStatus;

        PubmedArticleFeignResult(@NotNull final PubmedArticleSet articleSet) {
            pubmedArticleSet = articleSet;
            errorMessage = null;
            httpStatus = null;
        }

        PubmedArticleFeignResult(@NotNull final FeignException ex) {
            pubmedArticleSet = null;
            errorMessage = ex.getLocalizedMessage();
            httpStatus = HttpStatus.resolve(ex.status());
        }

        PubmedArticleFeignResult(@NotNull final String exceptionMessage) {
            pubmedArticleSet = null;
            errorMessage = exceptionMessage;
            httpStatus = null;
        }
    }
}
