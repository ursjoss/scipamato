package ch.difty.sipamato.logic.parsing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.config.SipamatoProperties;
import ch.difty.sipamato.config.AuthorParserStrategy;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Default implementation of the {@link AuthorParserFactory} which relies
 * on the enum based{@link SipamatoProperties} bean and the associated
 * {@link AuthorParserStrategy} enum to parse and provide the property.
 *
 * @author u.joss
 */
@Component
public class EnumBasedAuthorParserFactory implements AuthorParserFactory {

    private final AuthorParserStrategy authorParserStrategy;

    @Autowired
    public EnumBasedAuthorParserFactory(SipamatoProperties appProperties) {
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
