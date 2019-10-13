package ch.difty.scipamato.core.pubmed;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.persistence.ServiceResult;

public interface PubmedImporter {

    /**
     * Extracts the relevant PubMed information from the xml string and saves the
     * articles to DB.
     *
     * @param xml
     *     XML as string
     * @return {@link ServiceResult}
     */
    @NotNull
    ServiceResult persistPubmedArticlesFromXml(@Nullable String xml);
}
