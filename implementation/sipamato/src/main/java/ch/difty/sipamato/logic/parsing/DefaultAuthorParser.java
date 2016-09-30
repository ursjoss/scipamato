package ch.difty.sipamato.logic.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.wicket.util.string.Strings;

import ch.difty.sipamato.lib.AssertAs;

/**
 * Utility class to lex and parse Author strings. From the list of parsed authors it can return the first author.<p/>
 *
 * An example of a typical author string is:<p/>
 *
 * <literal>Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.</literal><p/>
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