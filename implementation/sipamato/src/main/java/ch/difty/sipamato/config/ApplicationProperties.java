package ch.difty.sipamato.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This bean is used to evaluate all environment properties used in the application in one place and serve those as bean to wherever they are used.
 *
 * @see https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/
 *
 * @author u.joss
 */
@Component
public class ApplicationProperties {

    public static final String AUTHOR_PARSER_FACTORY = "sipamato.author.parser";

    private final AuthorParserStrategies authorParserStrategy;

    public ApplicationProperties(@Value("${sipamato.author.parser}") String authorParserStrategy) {
        this.authorParserStrategy = AuthorParserStrategies.fromProperty(authorParserStrategy);
    }

    public AuthorParserStrategies getAuthorParserStrategy() {
        return authorParserStrategy;
    }
}
