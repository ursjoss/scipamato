package ch.difty.sipamato.persistance.jooq.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.AbstractServiceTest;
import ch.difty.sipamato.persistance.jooq.Page;
import ch.difty.sipamato.persistance.jooq.Pageable;

public class JooqPaperServiceTest extends AbstractServiceTest<Long, Paper, PaperRepository> {

    private final JooqPaperService service = new JooqPaperService();

    @Mock
    private PaperRepository repoMock;
    @Mock
    private PaperFilter filterMock;
    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<Paper> paperPageMock;
    @Mock
    protected Paper paperMock;

    @Override
    protected Paper getEntity() {
        return paperMock;
    }

    @Override
    protected PaperRepository getRepo() {
        return repoMock;
    }

    private final List<Paper> papers = new ArrayList<>();

    @Override
    public void specificSetUp() {
        service.setRepository(repoMock);
        service.setUserRepository(userRepoMock);

        papers.add(paperMock);
        papers.add(paperMock);

        when(paperMock.getCreatedBy()).thenReturn(10);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, searchOrderMock, pageableMock, paperPageMock, paperMock);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(paperMock);

        Optional<Paper> optPaper = service.findById(id);
        assertThat(optPaper.isPresent()).isTrue();
        assertThat(optPaper.get()).isEqualTo(paperMock);

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
        when(repoMock.findByFilter(filterMock, pageableMock)).thenReturn(paperPageMock);
        when(paperPageMock.getContent()).thenReturn(papers);

        Page<Paper> pageOfPapers = service.findByFilter(filterMock, pageableMock);

        assertThat(pageOfPapers).isEqualTo(paperPageMock);
        assertThat(pageOfPapers.getContent()).isEqualTo(papers);

        verify(repoMock).findByFilter(filterMock, pageableMock);
        verify(paperPageMock).getContent();

        // TODO how to stub this and verify the audit??? Same in JooqPaperSlimServiceTest and JooqSearchOrderServiceTest
        verify(paperPageMock).forEach(isA(Consumer.class));
        //        verifyAudit(2);
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        when(paperMock.getId()).thenReturn(null);
        when(repoMock.add(paperMock)).thenReturn(paperMock);
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).add(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    public void savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        when(paperMock.getId()).thenReturn(17l);
        when(repoMock.update(paperMock)).thenReturn(paperMock);
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).update(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    public void deleting_withNullEntity_doesNothing() {
        service.remove(null);
        verify(repoMock, never()).delete(Mockito.anyLong());
    }

    @Test
    public void deleting_withEntityWithNullId_doesNothing() {
        when(paperMock.getId()).thenReturn(null);

        service.remove(paperMock);

        verify(paperMock).getId();
        verify(repoMock, never()).delete(Mockito.anyLong());
    }

    @Test
    public void deleting_withEntityWithNormald_delegatesToRepo() {
        when(paperMock.getId()).thenReturn(3l);

        service.remove(paperMock);

        verify(paperMock, times(2)).getId();
        verify(repoMock, times(1)).delete(3l);
    }

    @Test
    public void findingBySearchOrder_delegatesToRepo() {
        when(repoMock.findBySearchOrder(searchOrderMock)).thenReturn(papers);
        assertThat(service.findBySearchOrder(searchOrderMock)).containsAll(papers);
        verify(repoMock).findBySearchOrder(searchOrderMock);
    }

    @Test
    public void findingPagedBySearchOrder_delegatesToRepo() {
        when(repoMock.findBySearchOrder(searchOrderMock, pageableMock)).thenReturn(paperPageMock);
        assertThat(service.findBySearchOrder(searchOrderMock, pageableMock)).isEqualTo(paperPageMock);
        verify(repoMock).findBySearchOrder(searchOrderMock, pageableMock);
    }

    @Test
    public void countingBySearchOrder_delegatesToRepo() {
        when(repoMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2);
        verify(repoMock).countBySearchOrder(searchOrderMock);
    }
}
