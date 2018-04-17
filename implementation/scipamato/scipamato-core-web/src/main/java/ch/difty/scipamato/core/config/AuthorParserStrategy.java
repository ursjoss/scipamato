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

    /**
     * Converts the string based property value into the proper
     * {@link AuthorParserStrategy} enum value.
     *
     * @param propertyValue
     *     the property value as string
     * @param propertyKey
     *     the key name, used for log output only
     * @return the property as enum value
     */
    public static AuthorParserStrategy fromProperty(final String propertyValue, final String propertyKey) {
        return PropertyUtils.fromProperty(propertyValue, STRATEGIES, PUBMED, propertyKey);
    }
}
