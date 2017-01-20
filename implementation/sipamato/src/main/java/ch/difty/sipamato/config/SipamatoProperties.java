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
public class SipamatoProperties implements ApplicationProperties {

    private final String defaultLocalization;
    private final AuthorParserStrategy authorParserStrategy;
    private final int autoSaveIntervalInSeconds;
    private final String brand;

    private static final String S = "${", E = ":n.a.}";

    public SipamatoProperties(@Value(S + LOCALIZATION_DEFAULT + ":en}") String defaultLocalization, @Value(S + AUTHOR_PARSER_FACTORY + E) String authorParserStrategy,
            @Value(S + AUTO_SAVE_INTERVAL + E) String paperAutoSaveInterval, @Value(S + BRAND + E) String brand) {
        this.defaultLocalization = defaultLocalization;
        this.authorParserStrategy = AuthorParserStrategy.fromProperty(authorParserStrategy);
        this.autoSaveIntervalInSeconds = PropertyUtils.parseInt(paperAutoSaveInterval, AUTO_SAVE_HINT, DEFAULT_AUTO_SAVE_INTERVAL_IN_SECONDS, AUTO_SAVE_INTERVAL);
        this.brand = brand;
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
    public int getAutoSaveIntervalInSeconds() {
        return autoSaveIntervalInSeconds;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAutoSavingEnabled() {
        return getAutoSaveIntervalInSeconds() > 0;
    }

    @Override
    public String getBrand() {
        return brand;
    }

}
