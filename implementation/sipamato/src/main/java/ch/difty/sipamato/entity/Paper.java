package ch.difty.sipamato.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

public class Paper extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";
    public static final String PMID = "pmid";
    public static final String DOI = "digitalObjectIdentifier";
    public static final String AUTHORS = "authors";
    public static final String FIRST_AUTHOR = "firstAuthor";
    public static final String FIRST_AUTHOR_OVERRIDDEN = "firstAuthorOverridden";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String PUBL_YEAR = "publicationYear";

    private Integer id;

    // TODO can it be null? Range validation?
    private Integer pmid;

    // TODO can it be null? Pattern validation?
    private String digitalObjectIdentifier;

    @NotNull
    @Pattern(regexp = "^[\\w-']+(\\s[\\w-']+){0,}(,\\s[\\w-']+(\\s[\\w-']+){0,}){0,}\\.$", message = "{paper.invalidAuthor}")
    private String authors;

    @NotNull
    private String firstAuthor;
    private boolean firstAuthorOverridden;

    @NotNull
    private String title;

    // TODO validation non null?
    private String location;

    @Range(min = 1500, max = 2100, message = "{paper.invalidPublicationYear}")
    private int publicationYear;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPmid() {
        return pmid;
    }

    public void setPmid(Integer pmid) {
        this.pmid = pmid;
    }

    public String getDigitalObjectIdentifier() {
        return digitalObjectIdentifier;
    }

    public void setDigitalObjectIdentifier(String digitalObjectIdentifier) {
        this.digitalObjectIdentifier = digitalObjectIdentifier;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Paper [id=");
        sb.append(id);
        sb.append(", pmid=");
        sb.append(pmid);
        sb.append(", doi=");
        sb.append(digitalObjectIdentifier);
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
        sb.append("]");
        return sb.toString();
    }

}
