package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;

@RunWith(MockitoJUnitRunner.class)
public class JooqPaperSlimServiceTest {

    private final JooqPaperSlimService service = new JooqPaperSlimService();

    @Mock
    private PaperSlimRepository repoMock;
    @Mock
    private SimplePaperFilter simplefilterMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<PaperSlim> paperSlimPageMock;
    @Mock
    private PaperSlim paperSlimMock;
    @Mock
    private Paper paperMock;

    private final List<PaperSlim> papers = new ArrayList<>();

    @Before
    public void setUp() {
        service.setRepository(repoMock);

        papers.add(paperSlimMock);
        papers.add(paperSlimMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(repoMock, simplefilterMock, pageableMock, paperSlimPageMock, paperSlimMock, paperMock);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(paperSlimMock);

        Optional<PaperSlim> optPaper = service.findById(id);
        assertThat(optPaper.isPresent()).isTrue();
        assertThat(optPaper.get()).isEqualTo(paperSlimMock);

        verify(repoMock).findById(id);
    }

    @Test
    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service.findById(id).isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findByFilter(simplefilterMock, pageableMock)).thenReturn(paperSlimPageMock);
        when(paperSlimPageMock.getContent()).thenReturn(papers);

        assertThat(service.findByFilter(simplefilterMock, pageableMock)).isEqualTo(papers);

        verify(repoMock).findByFilter(simplefilterMock, pageableMock);
        verify(paperSlimPageMock).getContent();
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(simplefilterMock)).thenReturn(3);
        assertThat(service.countByFilter(simplefilterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(simplefilterMock);
    }

}
