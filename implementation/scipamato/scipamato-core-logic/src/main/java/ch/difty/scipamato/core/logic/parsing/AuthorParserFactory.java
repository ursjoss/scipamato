package ch.difty.scipamato.core.logic.parsing;

/**
 * The implementation of the {@link AuthorParserFactory} provides a configured {@link AuthorParser} able parse the provided
 * property String {@code authorString}.
 *
 * @author u.joss
 */
@FunctionalInterface
public interface AuthorParserFactory {

    /**
     * Creates an implementation of an {@link AuthorParser} depending on the provided {@code author string}.
     * Returns a {@code DEFAULT} {@link AuthorParser} if the {@code authorString} is null or undefined.
     *
     * @param authorString - valid, invalid or null
     * @return {@link AuthorParser}
     */
    AuthorParser createParser(String authorString);

}
