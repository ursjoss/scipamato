package ch.difty.scipamato.persistance.jooq.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.persistance.jooq.AbstractServiceTest;

@RunWith(MockitoJUnitRunner.class)
public class JooqSearchOrderServiceTest extends AbstractServiceTest<Long, SearchOrder, SearchOrderRepository> {

    private final JooqSearchOrderService service = new JooqSearchOrderService();

    @Mock
    private SearchOrderRepository repoMock;
    @Mock
    private SearchOrderFilter filterMock;
    @Mock
    private PaginationContext paginationContextMock;
    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private SearchCondition searchConditionMock;

    private final List<SearchOrder> searchorders = new ArrayList<>();

    @Override
    protected SearchOrderRepository getRepo() {
        return repoMock;
    }

    @Override
    protected SearchOrder getEntity() {
        return searchOrderMock;
    }

    @Override
    public void specificSetUp() {
        service.setRepository(repoMock);
        service.setUserRepository(userRepoMock);

        searchorders.add(searchOrderMock);
        searchorders.add(searchOrderMock);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, paginationContextMock, searchOrderMock, searchConditionMock);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(searchOrderMock);

        Optional<SearchOrder> optSearchOrder = service.findById(id);
        assertThat(optSearchOrder.isPresent()).isTrue();
        assertThat(optSearchOrder.get()).isEqualTo(searchOrderMock);

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

    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(searchorders);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(searchorders);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verifyAudit(2);
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void savingOrUpdating_withSearchOrderWithNullId_hasRepoAddTheSearchOrder() {
        when(searchOrderMock.getId()).thenReturn(null);
        when(repoMock.add(searchOrderMock)).thenReturn(searchOrderMock);
        assertThat(service.saveOrUpdate(searchOrderMock)).isEqualTo(searchOrderMock);
        verify(repoMock).add(searchOrderMock);
        verify(searchOrderMock).getId();
        verifyAudit(1);
    }

    @Test
    public void savingOrUpdating_withSearchOrderWithNonNullId_hasRepoUpdateTheSearchOrder() {
        when(searchOrderMock.getId()).thenReturn(17l);
        when(repoMock.update(searchOrderMock)).thenReturn(searchOrderMock);
        assertThat(service.saveOrUpdate(searchOrderMock)).isEqualTo(searchOrderMock);
        verify(repoMock).update(searchOrderMock);
        verify(searchOrderMock).getId();
        verifyAudit(1);
    }

    @Test
    public void savingOrUpdatingSearchCondition_withConditionWithNullId_delegatesAddingToRepo() {
        final long searchOrderId = 3;
        when(searchConditionMock.getSearchConditionId()).thenReturn(null);
        when(repoMock.addSearchCondition(searchConditionMock, searchOrderId)).thenReturn(searchConditionMock);
        assertThat(service.saveOrUpdateSearchCondition(searchConditionMock, searchOrderId)).isEqualTo(searchConditionMock);
        verify(repoMock).addSearchCondition(searchConditionMock, searchOrderId);
        verify(searchConditionMock).getSearchConditionId();
    }

    @Test
    public void savingOrUpdatingSearchCondition_withConditionWithId_delegatesUpdatingToRepo() {
        final long searchOrderId = 3;
        when(searchConditionMock.getSearchConditionId()).thenReturn(17l);
        when(repoMock.updateSearchCondition(searchConditionMock, searchOrderId)).thenReturn(searchConditionMock);
        assertThat(service.saveOrUpdateSearchCondition(searchConditionMock, searchOrderId)).isEqualTo(searchConditionMock);
        verify(repoMock).updateSearchCondition(searchConditionMock, searchOrderId);
        verify(searchConditionMock).getSearchConditionId();
    }

    @Test
    public void deleting_withNullEntity_doesNothing() {
        service.remove(null);
        verify(repoMock, never()).delete(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    public void deleting_withEntityWithNullId_doesNothing() {
        when(searchOrderMock.getId()).thenReturn(null);

        service.remove(searchOrderMock);

        verify(searchOrderMock).getId();
        verify(repoMock, never()).delete(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    public void deleting_withEntityWithNormalId_delegatesToRepo() {
        when(searchOrderMock.getId()).thenReturn(3l);
        when(searchOrderMock.getVersion()).thenReturn(33);

        service.remove(searchOrderMock);

        verify(searchOrderMock, times(2)).getId();
        verify(searchOrderMock, times(1)).getVersion();
        verify(repoMock, times(1)).delete(3l, 33);
    }

    @Test
    public void removingSearchConditionWithId_withNullId_doesNothing() {
        service.removeSearchConditionWithId(null);
        verify(repoMock, never()).deleteSearchConditionWithId(Mockito.anyLong());
    }

    @Test
    public void removingSearchConditionWithId_delegatesToRepo() {
        long id = 3l;
        service.removeSearchConditionWithId(id);
        verify(repoMock, times(1)).deleteSearchConditionWithId(id);
    }

}
