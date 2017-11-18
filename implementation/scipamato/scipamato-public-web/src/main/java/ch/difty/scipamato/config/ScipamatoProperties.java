package ch.difty.scipamato.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.config.core.AuthorParserStrategy;

/**
 * This bean is used to evaluate all environment properties used in the application in one place and serve those as bean to wherever they are used.
 *
 * @see <a href="https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/">https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/</a>
 *
 * @author u.joss
 */
@Component
public class ScipamatoProperties implements ApplicationProperties {

    private final String buildVersion;
    private final String defaultLocalization;
    private final String brand;

    private static final String S = "${", E = ":n.a.}";

    public ScipamatoProperties(@Value(S + BUILD_VERSION + E) String buildVersion, @Value(S + LOCALIZATION_DEFAULT + ":en}") String defaultLocalization, @Value(S + BRAND + E) String brand) {
        this.buildVersion = buildVersion;
        this.defaultLocalization = defaultLocalization;
        this.brand = brand;
    }

    @Override
    public String getBuildVersion() {
        return buildVersion;
    }

    @Override
    public String getDefaultLocalization() {
        return defaultLocalization;
    }

    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return null;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public long getMinimumPaperNumberToBeRecycled() {
        return 0;
    }

}
