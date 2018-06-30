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

    String URL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";

    /**
     * Retrieve a pubmed article with the given PMID.
     *
     * @param pmid
     *     the pubmed id identifiying the article
     * @return pubmedArticleSet
     */
    @RequestLine("GET efetch.fcgi?db=pubmed&id={pmid}&retmode=xml&version=2.0")
    PubmedArticleSet articleWithId(@Param("pmid") String pmid);
}