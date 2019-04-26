package ch.difty.scipamato.core.logic.parsing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.NullArgumentException;

class DefaultAuthorParserFactoryTest {

    private AuthorParserFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DefaultAuthorParserFactory(AuthorParserStrategy.PUBMED);
    }

    @Test
    void degenerateConstruction() {
        Assertions.assertThrows(NullArgumentException.class, () -> new DefaultAuthorParserFactory(null));
    }

    @Test
    void creatingParser_withNullAuthorString_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> factory.createParser(null));
    }

    @Test
    void cratingParser_withNoSetting_usesDefaultAuthorParser() {
        AuthorParser parser = factory.createParser("Turner MC.");
        assertThat(parser).isInstanceOf(PubmedAuthorParser.class);
    }

}