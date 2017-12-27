package ch.difty.scipamato.core.logic.parsing;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementations of the {@link AuthorParser} interface accept an
 * {@code author} String (typically as a constructor argument). The string is
 * lexed and parsed and individual authors are maintained. There are various
 * ways to access the parsed authors, especially the {@code First Author} is of
 * importance.
 *
 * @author u.joss
 */
public interface AuthorParser {

    /**
     * @return the {@code first author}, as Optional.
     */
    Optional<String> getFirstAuthor();

    /**
     * @return the original unparsed {@code authors string}
     */
    String getAuthorsString();

    /**
     * @return a stream of {@link Author}s.
     */
    Stream<Author> getAuthors();

}
