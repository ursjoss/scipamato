package ch.difty.scipamato.config;

/**
 * Parses a property value to define the AuthorParserStrategy to be used.
 *
 * @author u.joss
 */
public enum AuthorParserStrategy {

    /**
     * The only {@link AuthorParserStrategy}
     */
    DEFAULT;

    public static AuthorParserStrategy fromProperty(final String propertyValue) {
        return PropertyUtils.fromProperty(propertyValue, values(), DEFAULT, ApplicationProperties.AUTHOR_PARSER_FACTORY);
    }
}
