package ch.difty.sipamato.web.pages.paper.provider;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.persistance.jooq.paper.JooqPaperService;

@Ignore // TODO wicket application needs to be attached
@RunWith(MockitoJUnitRunner.class)
public class SortablePaperProviderTest {

    private SortablePaperProvider provider = new SortablePaperProvider();

    @Mock
    private JooqPaperService serviceMock;

    @Before
    public void setUp() {
        provider.setService(serviceMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void filterIsNewPaperFilter() {
        assertThat(provider.getFilter()).isEqualTo(new PaperFilter());
    }
}
