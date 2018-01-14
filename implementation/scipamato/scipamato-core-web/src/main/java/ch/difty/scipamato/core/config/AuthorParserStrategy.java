package ch.difty.scipamato.core.config;

/**
 * Parses a property value to define the AuthorParserStrategy to be used.
 *
 * @author u.joss
 */
public enum AuthorParserStrategy {

    /**
     * The {@link AuthorParserStrategy} interpreting PubMed Authors string
     */
    PUBMED;

    private static final AuthorParserStrategy[] STRATEGIES = values();

    public static AuthorParserStrategy fromProperty(final String propertyValue) {
        return PropertyUtils.fromProperty(propertyValue, STRATEGIES, PUBMED,
            ApplicationCoreProperties.AUTHOR_PARSER_FACTORY);
    }
}
