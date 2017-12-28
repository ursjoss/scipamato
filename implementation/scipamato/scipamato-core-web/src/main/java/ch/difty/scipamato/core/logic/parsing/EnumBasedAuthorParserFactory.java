package ch.difty.scipamato.core.logic.parsing;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.config.AuthorParserStrategy;

/**
 * Default implementation of the {@link AuthorParserFactory} which relies on the
 * application config enum defined in {@link ApplicationProperties} and the
 * associated {@link AuthorParserStrategy} enum to parse and provide the
 * property.
 *
 * @author u.joss
 */
@Component
public class EnumBasedAuthorParserFactory implements AuthorParserFactory {

    private final AuthorParserStrategy authorParserStrategy;

    public EnumBasedAuthorParserFactory(final ApplicationCoreProperties appProperties) {
        AssertAs.notNull(appProperties, "appProperties");
        this.authorParserStrategy = appProperties.getAuthorParserStrategy();
    }

    @Override
    public AuthorParser createParser(final String authorString) {
        AssertAs.notNull(authorString, "authorString");

        switch (authorParserStrategy) {
        case PUBMED:
        default:
            return new PubmedAuthorParser(authorString);
        }
    }

}
