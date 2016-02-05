package ch.difty.sipamato;

import java.io.Serializable;

public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String AUTHOR = "author";
    public static final String FIRST_AUTHOR = "firstAuthor";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";

    private String author;
    private String title;
    private String location;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFirstAuthor() {
        return author == null ? "" : author.split(" ", 2)[0];
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
        StringBuilder builder = new StringBuilder();
        builder.append("Paper [author=");
        builder.append(author);
        builder.append(", title=");
        builder.append(title);
        builder.append(", location=");
        builder.append(location);
        builder.append("]");
        return builder.toString();
    }

}
