package ch.difty.scipamato.core.pubmed;

import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet;

/**
 * Interface to PubMed via efetch
 *
 * @author u.joss
 */
@SuppressWarnings("ALL")
public interface PubMed {

    /**
     * Retrieve a pubmed article with the given PMID.
     *
     * @param pmid
     *     the pubmed id identifying the article
     * @return pubmedArticleSet
     */
    @NotNull
    @RequestLine("GET efetch.fcgi?db=pubmed&id={pmid}&retmode=xml&version=2.0")
    PubmedArticleSet articleWithId(@Param("pmid") @NotNull String pmid);

    /**
     * Retrieve a pubmed article with the given PMID.
     *
     * @param pmid
     *     the pubmed id identifying the article
     * @param apiKey
     *     the api key used to retrieve the article from pubmed
     * @return pubmedArticleSet
     */
    @NotNull
    @RequestLine("GET efetch.fcgi?db=pubmed&id={pmid}&api_key={apiKey}&retmode=xml&version=2.0")
    PubmedArticleSet articleWithId(@Param("pmid") @NotNull String pmid, @Param("apiKey") @NotNull String apiKey);
}