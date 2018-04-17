package ch.difty.scipamato.core.pubmed;

import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.common.NullArgumentException;

/**
 * Service to handle articles from PubMed and present it in a SciPaMaTo specific
 * representation.
 *
 * @author u.joss
 */
public interface PubmedArticleService {

    /**
     * Retrieve the pubmed article with the provided pmID directly from PubMed.
     *
     * @param pmId
     *     pubmedId
     * @return optional of {@link PubmedArticleFacade}
     */
    Optional<PubmedArticleFacade> getPubmedArticleWithPmid(int pmId);

    /**
     * Extracts pubmed articles and pubmed book articles from a a source string. It
     * is the implementation that determines the type and format of the content.
     *
     * @param content
     *     pubmed content, as String. Must not be null.
     * @return List of {@link PubmedArticleFacade} entries. Never null. Will be
     *     empty if there are issues parsing the content.
     * @throws NullArgumentException
     *     in case of null content.
     */
    List<PubmedArticleFacade> extractArticlesFrom(String content);

}
