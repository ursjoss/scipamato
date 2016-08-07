package ch.difty.sipamato.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import org.hibernate.validator.constraints.Range;

public class Paper extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";
    public static final String PMID = "pmid";
    public static final String DOI = "doi";
    public static final String AUTHORS = "authors";
    public static final String FIRST_AUTHOR = "firstAuthor";
    public static final String FIRST_AUTHOR_OVERRIDDEN = "firstAuthorOverridden";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String PUBL_YEAR = "publicationYear";

    public static final String GOALS = "goals";
    public static final String POPULATION = "population";
    public static final String EXPOSITION = "exposition";
    public static final String METHODS = "methods";

    public static final String RESULT = "result";
    public static final String COMMENT = "comment";
    public static final String INTERN = "intern";

    private Integer id;

    /*
     * Digital Object Identifier (see http://www.doi.org
     *
     * The validation pattern is simplified and seems to catch roughly 74.4M out of 74.9M DOIs.
     * The uncaught ones seem to be old and hopefully don't turn up within Sipamato. Otherwise additional
     * regex patterns catching more of the remaining ones can be found here:
     *
     * http://blog.crossref.org/2015/08/doi-regular-expressions.html (thx to Andrew Gilmartin)
     *
     * /^10.\d{4,9}/[-._;()/:A-Z0-9]+$/i
     */
    @Pattern(regexp = "^10\\.\\d{4,9}/[-._;()/:A-Z0-9]+$", flags = { Flag.CASE_INSENSITIVE }, message = "{paper.invalidDOI}")
    private String doi;

    private Integer pmid;

    @NotNull
    @Pattern(regexp = "^[\\w-']+(\\s[\\w-']+){0,}(,\\s[\\w-']+(\\s[\\w-']+){0,}){0,}\\.$", message = "{paper.invalidAuthor}")
    private String authors;

    @NotNull
    private String firstAuthor;
    private boolean firstAuthorOverridden;
    @NotNull
    private String title;
    @NotNull
    private String location;

    @Range(min = 1500, max = 2100, message = "{paper.invalidPublicationYear}")
    private int publicationYear;

    @NotNull
    private String goals;
    private String population;
    private String exposition;
    private String methods;

    private String result;
    private String comment;
    private String intern;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public Integer getPmid() {
        return pmid;
    }

    public void setPmid(Integer pmid) {
        this.pmid = pmid;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public void setFirstAuthor(String firstAuthor) {
        this.firstAuthor = firstAuthor;
    }

    public boolean isFirstAuthorOverridden() {
        return firstAuthorOverridden;
    }

    public void setFirstAuthorOverridden(boolean firstAuthorOverridden) {
        this.firstAuthorOverridden = firstAuthorOverridden;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getExposition() {
        return exposition;
    }

    public void setExposition(String exposition) {
        this.exposition = exposition;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIntern() {
        return intern;
    }

    public void setIntern(String intern) {
        this.intern = intern;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Paper [id=");
        sb.append(id);
        sb.append(", doi=");
        sb.append(doi);
        sb.append(", pmid=");
        sb.append(pmid);
        sb.append(", authors=");
        sb.append(authors);
        sb.append(", title=");
        sb.append(title);
        sb.append(", location=");
        sb.append(location);
        sb.append(", publicationYear=");
        sb.append(publicationYear);
        sb.append(", firstAuthor=");
        sb.append(firstAuthor);
        sb.append(", goals=");
        sb.append(goals);
        sb.append(", population=");
        sb.append(population);
        sb.append(", exposition=");
        sb.append(getExposition());
        sb.append(", methods=");
        sb.append(methods);
        sb.append(", result=");
        sb.append(result);
        sb.append(", comment=");
        sb.append(comment);
        sb.append(", intern=");
        sb.append(intern);
        sb.append("]");
        return sb.toString();
    }

}
