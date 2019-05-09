package ch.difty.scipamato.core.pubmed;

import feign.Param;
import feign.RequestLine;

import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet;

/**
 * Interface to PubMed via efetch
 *
 * @author u.joss
 */
public interface PubMed {

    /**
     * Retrieve a pubmed article with the given PMID.
     *
     * @param pmid
     *     the pubmed id identifying the article
     * @return pubmedArticleSet
     */
    @RequestLine("GET efetch.fcgi?db=pubmed&id={pmid}&retmode=xml&version=2.0")
    PubmedArticleSet articleWithId(@Param("pmid") String pmid);

    /**
     * Retrieve a pubmed article with the given PMID.
     *
     * @param pmid
     *     the pubmed id identifying the article
     * @param apiKey
     *     the api key used to retrieve the article from pubmed
     * @return pubmedArticleSet
     */
    @RequestLine("GET efetch.fcgi?db=pubmed&id={pmid}&api_key={apiKey}&retmode=xml&version=2.0")
    PubmedArticleSet articleWithId(@Param("pmid") String pmid, @Param("apiKey") String apiKey);
}