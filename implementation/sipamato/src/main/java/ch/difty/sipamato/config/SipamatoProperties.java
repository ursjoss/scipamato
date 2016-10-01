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

    private static final String S = "${", E = ":n.a.}";

    private final AuthorParserStrategy authorParserStrategy;
    private final int paperAutoSaveInterval;

    public SipamatoProperties(@Value(S + AUTHOR_PARSER_FACTORY + E) String authorParserStrategy, @Value(S + PAPER_AUTO_SAVE_INTERVAL + E) String paperAutoSaveInterval) {
        this.authorParserStrategy = AuthorParserStrategy.fromProperty(authorParserStrategy);
        this.paperAutoSaveInterval = PropertyUtils.parseInt(paperAutoSaveInterval, "0: Submit based saving, >= 1 refresh interval", DEFAULT_PAPER_AUTO_SAVE_INTERVAL_IN_SECONDS,
                PAPER_AUTO_SAVE_INTERVAL);
    }

    /** {@inheritDoc} */
    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return authorParserStrategy;
    }

    /** {@inheritDoc} */
    @Override
    public int getPaperAutoSaveInterval() {
        return paperAutoSaveInterval;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPaperAutoSaveMode() {
        return getPaperAutoSaveInterval() > 0;
    }
}
