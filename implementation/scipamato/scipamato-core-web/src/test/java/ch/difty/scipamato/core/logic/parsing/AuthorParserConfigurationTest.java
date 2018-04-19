package ch.difty.scipamato.core.logic.parsing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;

@RunWith(MockitoJUnitRunner.class)
public class AuthorParserConfigurationTest {

    private AuthorParserConfiguration conf;

    @Mock
    private ApplicationCoreProperties appProperties;

    @Before
    public void setUp() {
        conf = new AuthorParserConfiguration();
        when(appProperties.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.PUBMED);
    }

    @Test
    public void canRetrieveAuthorParserFactory() {
        AuthorParserFactory factory = conf.authorParserFactory(appProperties);

        assertThat(factory)
            .isNotNull()
            .isInstanceOf(AuthorParserFactory.class)
            .isInstanceOf(DefaultAuthorParserFactory.class);

        verify(appProperties).getAuthorParserStrategy();
    }
}