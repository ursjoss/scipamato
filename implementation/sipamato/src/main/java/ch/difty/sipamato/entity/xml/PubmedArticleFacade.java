package ch.difty.sipamato.entity.xml;

import ch.difty.sipamato.pubmed.PubmedArticle;
import ch.difty.sipamato.pubmed.PubmedBookArticle;

/**
 * Facade encapsulating both {@link PubmedArticle}s and {@link PubmedBookArticle}s, making the
 * relevant information accessible through simple getters.
 *
 * @author u.joss
 */
public abstract class PubmedArticleFacade {

    protected static final String PAGE_SEPARATOR = "-";

    private String pmId;
    private String authors;
    private String firstAuthor;
    private String publicationYear;
    private String location;
    private String title;
    private String doi;
    private String originalAbstract;

    /**
     * Instantiate an instance of {@link SipamatoPubmedArticle} if the provided object is an instance of {@link PubmedArticle}
     * or an instance of {@link SipamatoPubmedBookArticle} if the provided object is an instance of {@link PubmedBookArticle}.
     *
     * @param pubmedArticleOrPubmedBookArticle
     * @return a derivative of {@link PubmedArticleFacade}
     * @throws IllegalArgumentException - if the parameter is of any other class than one of the two managed ones. 
     */
    public static PubmedArticleFacade of(Object pubmedArticleOrPubmedBookArticle) {
        if (pubmedArticleOrPubmedBookArticle instanceof PubmedArticle)
            return new SipamatoPubmedArticle((PubmedArticle) pubmedArticleOrPubmedBookArticle);
        else if (pubmedArticleOrPubmedBookArticle instanceof PubmedBookArticle)
            return new SipamatoPubmedBookArticle((PubmedBookArticle) pubmedArticleOrPubmedBookArticle);
        throw new IllegalArgumentException("Cannot instantiate SipamatoArticle from provided object " + pubmedArticleOrPubmedBookArticle.toString());
    }

    public String getPmId() {
        return pmId;
    }

    protected void setPmId(String pmId) {
        this.pmId = pmId;
    }

    public String getAuthors() {
        return authors;
    }

    protected void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    protected void setFirstAuthor(String firstAuthor) {
        this.firstAuthor = firstAuthor;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    protected void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getLocation() {
        return location;
    }

    protected void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public String getDoi() {
        return doi;
    }

    protected void setDoi(String doi) {
        this.doi = doi;
    }

    public String getOriginalAbstract() {
        return originalAbstract;
    }

    protected void setOriginalAbstract(String originalAbstract) {
        this.originalAbstract = originalAbstract;
    }

}