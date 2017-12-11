package ch.difty.scipamato.core.pubmed;

import ch.difty.scipamato.core.persistence.ServiceResult;

public interface PubmedImporter {

    /**
     * Extracts the relevant PubMed information from the xml string and saves the articles to DB.
     * @param xml XML as string must not be null.
     * @return {@link ServiceResult}
     */
    ServiceResult persistPubmedArticlesFromXml(String xml);

}
