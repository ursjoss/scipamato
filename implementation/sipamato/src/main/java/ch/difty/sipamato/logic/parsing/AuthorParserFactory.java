package ch.difty.sipamato.logic.parsing;

/**
 * The implementation of the {@link AuthorParserFactory} provides a configured {@link AuthorParser} able parse the provided
 * property String <literal>authorString</literal>.
 *
 * @author u.joss
 */
public interface AuthorParserFactory {

    /**
     * Creates an implementation of an {@link AuthorParser} depending on the provided <literal>author string</literal>.
     * Returns a <literal>DEFAULT</literal> {@link AuthorParser} if the <literal>authorString</literal> is null or undefined.
     *
     * @param authorString - valid, invalid or null
     * @return {@link AuthorParser}
     */
    AuthorParser createParser(String authorString);

}
