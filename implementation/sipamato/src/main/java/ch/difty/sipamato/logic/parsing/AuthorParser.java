package ch.difty.sipamato.logic.parsing;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementations of the {@link AuthorParser} interface accept an <literal>author</literal> String
 * (typically as a constructor argument). The string is lexed and parsed and individual authors are
 * maintained. There are various ways to access the parsed authors, especially the <literal>First Author</literal
 * is of importance.
 *
 * @author u.joss
 */
public interface AuthorParser {

    /**
     * @return the <literal>first author</literal>, as Optional.
     */
    Optional<String> getFirstAuthor();

    /**
     * @return the original unparsed <literal>authors string</literal>
     */
    String getAuthorsString();

    /**
     * A stream of {@link Author}s.
     * @return
     */
    Stream<Author> getAuthors();

}
