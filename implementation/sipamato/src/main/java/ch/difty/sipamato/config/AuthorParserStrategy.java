package ch.difty.sipamato.config;

import ch.difty.sipamato.logic.parsing.AuthorParser;

/**
 * Parses a property value to define the {@link AuthorParserStrategy} to be used.
 * @see AuthorParser
 *
 * @author u.joss
 */
public enum AuthorParserStrategy {

    /**
     * The only {@link AuthorParserStrategies}
     */
    DEFAULT;

    public static AuthorParserStrategy fromProperty(final String propertyValue) {
        return PropertyUtils.fromProperty(propertyValue, values(), DEFAULT, ApplicationProperties.AUTHOR_PARSER_FACTORY);
    }
}
