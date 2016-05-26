package ch.difty.sipamato.logic.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.wicket.util.string.Strings;

import ch.difty.sipamato.lib.NullArgumentException;

public class AuthorParser {

    private final String authorsString;
    private final List<Author> authors;

    public AuthorParser(final String authorsString) {
        if (authorsString == null) {
            throw new NullArgumentException("authorsString");
        }
        this.authorsString = authorsString.trim();

        final String as = preprocess();
        final List<String> authorStrings = lexAuthors(as);
        this.authors = parseAuthors(authorStrings);
    }

    private String preprocess() {
        if (authorsString.endsWith(".")) {
            return authorsString.substring(0, authorsString.length() - 1);
        } else {
            return authorsString;
        }
    }

    private List<String> lexAuthors(String authors) {
        return Arrays.asList(authors.split(" *, *"));
    }

    private List<Author> parseAuthors(List<String> authorStrings) {
        final List<Author> authors = new ArrayList<>();
        for (final String as : authorStrings) {
            authors.add(parseAuthor(as));
        }
        return authors;
    }

    private Author parseAuthor(final String authorString) {
        String lastName = authorString;
        String firstName = "";

        final List<String> tokens = Arrays.asList(authorString.split(" +"));
        if (tokens.size() > 1) {
            int i = tokens.size() - 1;
            firstName = tokens.get(i).trim();
            lastName = Strings.join(" ", tokens.subList(0, i));
        }
        return new Author(authorString, lastName, firstName);
    }

    public Optional<String> getFirstAuthor() {
        return authors.stream().findFirst().map(Author::getLastName);
    }

    public String getAuthorsString() {
        return authorsString;
    }

    public Stream<Author> getAuthors() {
        return authors.stream();
    }

}