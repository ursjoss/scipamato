package ch.difty.sipamato.persistance.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.persistance.repository.PaperRepository;

@RunWith(MockitoJUnitRunner.class)
public class JooqPaperServiceTest {

    private final JooqPaperService service = new JooqPaperService();

    @Mock
    private PaperRepository repoMock;

    @Before
    public void setUp() {
        service.setRepository(repoMock);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void findingByFilter() {
        // TODO implement and test
    }

    @Test
    public void countingByFilter() {
        // TODO implement and test
    }
}
