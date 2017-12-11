package ch.difty.scipamato.core.logic.parsing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

/**
 * Default implementation of the {@link AuthorParserFactory} which relies on the application config enum
 * defined in {@link ApplicationProperties} and the associated {@link AuthorParserStrategy} enum to parse
 * and provide the property.
 *
 * @author u.joss
 */
@Component
public class EnumBasedAuthorParserFactory implements AuthorParserFactory {

    private final AuthorParserStrategy authorParserStrategy;

    @Autowired
    public EnumBasedAuthorParserFactory(ApplicationProperties appProperties) {
        AssertAs.notNull(appProperties, "appProperties");
        this.authorParserStrategy = appProperties.getAuthorParserStrategy();
    }

    /** {@inheritDoc} */
    @Override
    public AuthorParser createParser(String authorString) {
        AssertAs.notNull(authorString, "authorString");

        switch (authorParserStrategy) {
        case DEFAULT:
        default:
            return new DefaultAuthorParser(authorString);
        }
    }

}
