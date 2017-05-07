package ch.difty.sipamato.web.service;

import ch.difty.sipamato.service.ServiceResult;

public interface PubmedImporter {

    /**
     * Extracts the relevant PubMed information from the xml string and saves the articles to DB.
     * @param xml, must not be null.
     * @return {@link ServiceResult}
     */
    ServiceResult persistPubmedArticlesFromXml(String xml);

}
