package ch.difty.sipamato.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Paper extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String AUTHORS = "authors";
    public static final String FIRST_AUTHOR = "firstAuthor";
    public static final String FIRST_AUTHOR_OVERRIDDEN = "firstAuthorOverridden";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";

    @NotNull
    @Pattern(regexp = "^[\\w-']+(\\s[\\w-']+){0,}(,\\s[\\w-']+(\\s[\\w-']+){0,}){0,}\\.$", message = "{paper.invalidAuthor}")
    private String authors;
    @NotNull
    private String firstAuthor;
    private boolean firstAuthorOverridden;

    @NotNull
    private String title;

    private String location;

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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Paper [authors=");
        builder.append(authors);
        builder.append(", title=");
        builder.append(title);
        builder.append(", location=");
        builder.append(location);
        builder.append(", firstAuthor=");
        builder.append(firstAuthor);
        builder.append("]");
        return builder.toString();
    }

    public boolean isFirstAuthorOverridden() {
        return firstAuthorOverridden;
    }

    public void setFirstAuthorOverridden(boolean firstAuthorOverridden) {
        this.firstAuthorOverridden = firstAuthorOverridden;
    }

}
