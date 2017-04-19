package ch.difty.sipamato.persistance.jooq.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.paging.Page;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.AbstractServiceTest;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

@RunWith(MockitoJUnitRunner.class)
public class JooqPaperSlimServiceTest extends AbstractServiceTest<Long, PaperSlim, PaperSlimRepository> {

    private final JooqPaperSlimService service = new JooqPaperSlimService();

    @Mock
    private PaperSlimRepository repoMock;
    @Mock
    private PaperFilter filterMock;
    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<PaperSlim> paperSlimPageMock;
    @Mock
    private PaperSlim paperSlimMock;
    @Mock
    private Paper paperMock;

    private final List<PaperSlim> papers = new ArrayList<>();

    @Override
    protected PaperSlimRepository getRepo() {
        return repoMock;
    }

    @Override
    protected PaperSlim getEntity() {
        return paperSlimMock;
    }

    @Override
    public void specificSetUp() {
        service.setRepository(repoMock);
        service.setUserRepository(userRepoMock);

        papers.add(paperSlimMock);
        papers.add(paperSlimMock);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, searchOrderMock, pageableMock, paperSlimPageMock, paperSlimMock, paperMock);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(paperSlimMock);

        Optional<PaperSlim> optPaper = service.findById(id);
        assertThat(optPaper.isPresent()).isTrue();
        assertThat(optPaper.get()).isEqualTo(paperSlimMock);

        verify(repoMock).findById(id);

        verifyAudit(1);
    }

    @Test
    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service.findById(id).isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findByFilter(filterMock, pageableMock)).thenReturn(paperSlimPageMock);
        when(paperSlimPageMock.getContent()).thenReturn(papers);

        Page<PaperSlim> pageOfPapers = service.findByFilter(filterMock, pageableMock);

        assertThat(pageOfPapers).isEqualTo(paperSlimPageMock);
        assertThat(pageOfPapers.getContent()).isEqualTo(papers);

        verify(repoMock).findByFilter(filterMock, pageableMock);
        verify(paperSlimPageMock).getContent();

        verify(paperSlimPageMock).forEach(isA(Consumer.class));
        //        verifyAudit(2);
    }

    @Test
    public void countingByFilter_withSimpleFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void findingBySearchOrder_delegatesToRepo() {
        when(repoMock.findBySearchOrder(searchOrderMock)).thenReturn(papers);
        assertThat(service.findBySearchOrder(searchOrderMock)).containsAll(papers);
        verify(repoMock).findBySearchOrder(searchOrderMock);
    }

    @Test
    public void findingPagedBySearchOrder_delegatesToRepo() {
        when(repoMock.findBySearchOrder(searchOrderMock, pageableMock)).thenReturn(paperSlimPageMock);
        assertThat(service.findBySearchOrder(searchOrderMock, pageableMock)).isEqualTo(paperSlimPageMock);
        verify(repoMock).findBySearchOrder(searchOrderMock, pageableMock);
    }

    @Test
    public void countingBySearchOrder_delegatesToRepo() {
        when(repoMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2);
        verify(repoMock).countBySearchOrder(searchOrderMock);
    }

}
