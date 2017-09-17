package ch.difty.scipamato.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This bean is used to evaluate all environment properties used in the application in one place and serve those as bean to wherever they are used.
 *
 * @see <a href="https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/">https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/</a>
 *
 * @author u.joss
 */
@Component
public class ScipamatoProperties implements ApplicationProperties {

    private final String defaultLocalization;
    private final AuthorParserStrategy authorParserStrategy;
    private final String brand;
    private final long minimumPaperNumberToBeRecycled;

    private static final String S = "${", E = ":n.a.}";

    public ScipamatoProperties(@Value(S + LOCALIZATION_DEFAULT + ":en}") String defaultLocalization, @Value(S + AUTHOR_PARSER_FACTORY + E) String authorParserStrategy,
            @Value(S + BRAND + E) String brand, @Value(S + PAPER_NUMBER_MIN_TO_RECYCLE + E) Long minimumPaperNumberToBeRecycled) {
        this.defaultLocalization = defaultLocalization;
        this.authorParserStrategy = AuthorParserStrategy.fromProperty(authorParserStrategy);
        this.brand = brand;
        this.minimumPaperNumberToBeRecycled = minimumPaperNumberToBeRecycled != null ? minimumPaperNumberToBeRecycled.longValue() : 0;
    }

    /** {@inheritDoc} */
    @Override
    public String getDefaultLocalization() {
        return defaultLocalization;
    }

    /** {@inheritDoc} */
    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return authorParserStrategy;
    }

    /** {@inheritDoc} */
    @Override
    public String getBrand() {
        return brand;
    }

    /** {@inheritDoc} */
    @Override
    public long getMinimumPaperNumberToBeRecycled() {
        return minimumPaperNumberToBeRecycled;
    }

}
