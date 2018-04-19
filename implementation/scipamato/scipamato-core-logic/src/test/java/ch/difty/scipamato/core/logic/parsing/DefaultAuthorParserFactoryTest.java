package ch.difty.scipamato.core.logic.parsing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import ch.difty.scipamato.common.NullArgumentException;

public class DefaultAuthorParserFactoryTest {

    private AuthorParserFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultAuthorParserFactory(AuthorParserStrategy.PUBMED);
    }

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction() {
        new DefaultAuthorParserFactory(null);
    }

    @Test(expected = NullArgumentException.class)
    public void creatingParser_withNullAuthorString_throws() {
        factory.createParser(null);
    }

    @Test
    public void cratingParser_withNoSetting_usesDefaultAuthorParser() {
        AuthorParser parser = factory.createParser("Turner MC.");
        assertThat(parser).isInstanceOf(PubmedAuthorParser.class);
    }

}