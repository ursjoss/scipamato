package ch.difty.sipamato.service;

import java.util.List;

import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;

/**
 * Service to handle artifacts from PubMed and present it in a Sipamato specific representation.
 *
 * @author u.joss
 */
public interface PubmedService {

    /**
     * Extracts pubmed articles and pubmed book articles from the source string provided by Pubmed.
     *
     * @param content pubmed content, as String. Must not be null.
     * @return List of {@link PubmedArticleFacade} entries. Never null. Will be empty if there are issues parsing the content.
     * @throws NullArgumentException in case of null content.
     */
    List<PubmedArticleFacade> extractArticlesFrom(String content);

}
