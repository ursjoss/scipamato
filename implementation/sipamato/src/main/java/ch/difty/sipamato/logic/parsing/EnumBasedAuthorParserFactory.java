package ch.difty.sipamato.logic.parsing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.config.AuthorParserStrategies;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Default implementation of the {@link AuthorParserFactory} which relies
 * on the enum based{@link ApplicationProperties} bean and the associated 
 * {@link AuthorParserStrategies} enum to parse and provide the property. 
 *
 * @author u.joss
 */
@Component
public class EnumBasedAuthorParserFactory implements AuthorParserFactory {

    private final AuthorParserStrategies authorParserStrategy;

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
