package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;

@RunWith(MockitoJUnitRunner.class)
public class JooqPaperSlimServiceTest {

    private final JooqPaperSlimService service = new JooqPaperSlimService();

    @Mock
    private PaperSlimRepository repoMock;
    @Mock
    private PaperFilter filterMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<PaperSlim> paperSlimPageMock;
    @Mock
    private PaperSlim paperSlimMock;

    private final List<PaperSlim> papers = new ArrayList<>();

    @Before
    public void setUp() {
        service.setRepository(repoMock);

        papers.add(paperSlimMock);
        papers.add(paperSlimMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, pageableMock, paperSlimPageMock, paperSlimMock);
    }

    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findByFilter(filterMock, pageableMock)).thenReturn(paperSlimPageMock);
        when(paperSlimPageMock.getContent()).thenReturn(papers);

        assertThat(service.findByFilter(filterMock, pageableMock)).isEqualTo(papers);

        verify(repoMock).findByFilter(filterMock, pageableMock);
        verify(paperSlimPageMock).getContent();
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

}
