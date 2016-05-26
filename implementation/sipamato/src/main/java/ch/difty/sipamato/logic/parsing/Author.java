package ch.difty.sipamato.logic.parsing;

public class Author {

    private final String authorString;
    private final String lastName;
    private final String firstName;

    public Author(final String authorString, final String lastName, final String firstName) {
        this.authorString = authorString;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getAuthorString() {
        return authorString;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

}