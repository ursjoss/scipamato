package ch.difty.sipamato.service;

public interface PubmedImporter {

    /**
     * Extracts the relevant PubMed information from the xml string and saves the articles to DB.
     * @param xml, must not be null.
     * @return {@link ServiceResult}
     */
    ServiceResult persistPubmedArticlesFromXml(String xml);

}
