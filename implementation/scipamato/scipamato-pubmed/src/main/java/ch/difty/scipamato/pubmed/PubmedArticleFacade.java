package ch.difty.scipamato.pubmed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * Instantiate an instance of {@link ScipamatoPubmedArticle} if the provided object is an instance of {@link PubmedArticle}
     * or an instance of {@link ScipamatoPubmedBookArticle} if the provided object is an instance of {@link PubmedBookArticle}.
     *
     * @param pubmedArticleOrPubmedBookArticle
     * @return a derivative of {@link PubmedArticleFacade}
     * @throws IllegalArgumentException - if the parameter is of any other class than one of the two managed ones. 
     */
    public static PubmedArticleFacade of(java.lang.Object pubmedArticleOrPubmedBookArticle) {
        if (pubmedArticleOrPubmedBookArticle instanceof PubmedArticle)
            return new ScipamatoPubmedArticle((PubmedArticle) pubmedArticleOrPubmedBookArticle);
        else if (pubmedArticleOrPubmedBookArticle instanceof PubmedBookArticle)
            return new ScipamatoPubmedBookArticle((PubmedBookArticle) pubmedArticleOrPubmedBookArticle);
        throw new IllegalArgumentException("Cannot instantiate ScipamatoArticle from provided object " + pubmedArticleOrPubmedBookArticle.toString());
    }

    protected String getAuthorsFrom(final AuthorList authorList) {
        final List<String> names = new ArrayList<>();
        for (final Author author : authorList.getAuthor()) {
            final StringBuilder asb = new StringBuilder();
            for (final java.lang.Object o : author.getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()) {
                if (o instanceof ForeName)
                    continue;
                if (asb.length() > 0)
                    asb.append(" ");
                if (o instanceof LastName)
                    asb.append(((LastName) o).getvalue());
                else if (o instanceof Initials)
                    asb.append(((Initials) o).getvalue());
                else if (o instanceof Suffix)
                    asb.append(((Suffix) o).getvalue());
                else if (o instanceof CollectiveName)
                    asb.append(((CollectiveName) o).getvalue());
            }
            names.add(asb.toString());
        }
        return names.stream().collect(Collectors.joining(", ", "", "."));
    }

    protected String getFirstAuthorFrom(final AuthorList authorList) {
        return authorList.getAuthor().stream().map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName).flatMap(List<java.lang.Object>::stream).filter(o -> o instanceof LastName).map(
                lm -> ((LastName) lm).getvalue()).limit(1).findFirst().orElseGet(null);
    }

    protected String getDoiFromArticleIdList(ArticleIdList articleIdList) {
        if (articleIdList != null) {
            return articleIdList.getArticleId().stream().filter(ai -> "doi".equals(ai.getIdType())).map(ArticleId::getvalue).findFirst().orElse(null);
        }
        return null;
    }

    protected String getAbstractFrom(final Abstract abstr) {
        if (abstr == null)
            return null;
        return abstr.getAbstractText().stream().map(this::concatenateAbstract).collect(Collectors.joining("\n"));
    }

    private String concatenateAbstract(AbstractText a) {
        if (a.getLabel() != null)
            return a.getLabel() + ": " + a.getvalue();
        else
            return a.getvalue();
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
