package ch.difty.sipamato.logic.parsing;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class AuthorTest {

    private static final String AUTHOR_STRING = "authorString";
    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";

    @Test
    public void canGetValues() {
        Author a = new Author(AUTHOR_STRING, LAST_NAME, FIRST_NAME);
        assertThat(a.getAuthorString()).isEqualTo(AUTHOR_STRING);
        assertThat(a.getLastName()).isEqualTo(LAST_NAME);
        assertThat(a.getFirstName()).isEqualTo(FIRST_NAME);
    }
}
