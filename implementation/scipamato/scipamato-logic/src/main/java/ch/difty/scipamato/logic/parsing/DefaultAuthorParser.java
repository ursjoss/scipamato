package ch.difty.scipamato.logic.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import ch.difty.scipamato.AssertAs;


/**
 * Utility class to lex and parse Author strings. From the list of parsed authors it can return the first author.<p>
 *
 * An example of a typical author string is:<p>
 *
 * {@code Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.}<p>
 *
 * <ul>
 * <li> An author can have one or more names + initials.
 * <li> One or more authors appear in a comma delimited list.
 * <li> The last character is a period.
 * </ul>
 *
 * @author u.joss
 */
public class DefaultAuthorParser implements AuthorParser {

    private static final Pattern CARDINALITY_PATTERN = Pattern.compile("(?:1st|2nd|3rd)|(?:\\d+th)|(?:Jr)");

    private final String authorsString;
    private final List<Author> authors;

    public DefaultAuthorParser(final String authorsString) {
        this.authorsString = AssertAs.notNull(authorsString, "authorsString").trim();

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

    private List<String> lexAuthors(final String authors) {
        return Arrays.asList(authors.split(" *, *"));
    }

    private List<Author> parseAuthors(final List<String> authorStrings) {
        final List<Author> parsedAuthors = new ArrayList<>();
        for (final String as : authorStrings) {
            parsedAuthors.add(parseAuthor(as));
        }
        return parsedAuthors;
    }

    private Author parseAuthor(final String authorString) {
        String lastName = authorString;
        String firstName = "";

        final List<String> tokens = Arrays.asList(authorString.split(" +"));
        if (tokens.size() > 1) {
            final int i = getIndexOfFirstName(tokens);
            firstName = String.join(" ", tokens.subList(i, tokens.size())).trim();
            lastName = String.join(" ", tokens.subList(0, i)).trim();
        }
        return new Author(authorString, lastName, firstName);
    }

    private int getIndexOfFirstName(final List<String> tokens) {
        final int i = tokens.size() - 1;
        final Matcher m = CARDINALITY_PATTERN.matcher(tokens.get(i));
        if (i > 0 && m.find())
            return i - 1;
        else
            return i;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<String> getFirstAuthor() {
        return authors.stream().findFirst().map(Author::getLastName);
    }

    /** {@inheritDoc} */
    @Override
    public String getAuthorsString() {
        return authorsString;
    }

    /** {@inheritDoc} */
    @Override
    public Stream<Author> getAuthors() {
        return authors.stream();
    }

}
