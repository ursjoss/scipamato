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

    public static final String AUTHOR_PARSER_FACTORY = "sipamato.author.parser";
    public static final String PAPER_SAVE_MODE = "sipamato.paper.savemode";

    public static final String S = "${", E = ":n.a.}";

    private final AuthorParserStrategy authorParserStrategy;
    private final SaveMode paperSaveMode;

    public SipamatoProperties(@Value(S + AUTHOR_PARSER_FACTORY + E) String authorParserStrategy, @Value(S + PAPER_SAVE_MODE + E) String paperSaveMode) {
        this.authorParserStrategy = AuthorParserStrategy.fromProperty(authorParserStrategy);
        this.paperSaveMode = SaveMode.fromProperty(paperSaveMode);
    }

    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return authorParserStrategy;
    }

    @Override
    public SaveMode getPaperSaveMode() {
        return paperSaveMode;
    }
}
