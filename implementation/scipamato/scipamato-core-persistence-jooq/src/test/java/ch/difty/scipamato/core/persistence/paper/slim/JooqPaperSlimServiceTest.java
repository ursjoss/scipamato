package ch.difty.scipamato.core.persistence.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.AbstractServiceTest;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@ExtendWith(MockitoExtension.class)
public class JooqPaperSlimServiceTest extends AbstractServiceTest<Long, PaperSlim, PaperSlimRepository> {

    private JooqPaperSlimService service;

    @Mock
    private PaperSlimRepository repoMock;
    @Mock
    private PaperFilter         filterMock;
    @Mock
    private SearchOrder         searchOrderMock;
    @Mock
    private PaginationContext   paginationContextMock;
    @Mock
    private PaperSlim           paperSlimMock;
    @Mock
    private Paper               paperMock;

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
        service = new JooqPaperSlimService(repoMock, userRepoMock);

        papers.add(paperSlimMock);
        papers.add(paperSlimMock);

        // when(newsletterMock.getCreatedBy()).thenReturn(10);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, searchOrderMock, paginationContextMock, paperSlimMock,
            paperMock);
    }

    @Test
    void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7L;
        when(repoMock.findById(id)).thenReturn(paperSlimMock);
        auditFixture();

        Optional<PaperSlim> optPaper = service.findById(id);
        assertThat(optPaper.isPresent()).isTrue();
        assertThat(optPaper.get()).isEqualTo(paperSlimMock);

        verify(repoMock).findById(id);

        verifyAudit(1);
    }

    @Test
    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Long id = 7L;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service
            .findById(id)
            .isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    public void findingPageByFilter_delegatesToRepo() {
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        auditFixture();
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verifyAudit(2);
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
        when(repoMock.findPageBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageBySearchOrder(searchOrderMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    public void countingBySearchOrder_delegatesToRepo() {
        when(repoMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2);
        verify(repoMock).countBySearchOrder(searchOrderMock);
    }

}
