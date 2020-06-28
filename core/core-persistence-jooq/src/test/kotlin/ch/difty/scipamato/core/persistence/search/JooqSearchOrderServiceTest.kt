package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchOrderFilter
import ch.difty.scipamato.core.persistence.AbstractServiceTest
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong

@Suppress("UsePropertyAccessSyntax")
internal class JooqSearchOrderServiceTest : AbstractServiceTest<Long, SearchOrder, SearchOrderRepository>() {

    override val repo = mock<SearchOrderRepository>()
    private val filterMock = mock<SearchOrderFilter>()
    private val paginationContextMock = mock<PaginationContext>()
    override val entity = mock<SearchOrder>()
    private val searchConditionMock = mock<SearchCondition>()

    private var service = JooqSearchOrderService(repo, userRepoMock)

    private val searchOrders = listOf(entity, entity)

    public override fun specificTearDown() {
        verifyNoMoreInteractions(repo, filterMock, paginationContextMock, entity, searchConditionMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7L
        whenever(repo.findById(id)).thenReturn(entity)
        auditFixture()

        val optSearchOrder = service.findById(id)
        assertThat(optSearchOrder.isPresent).isTrue()
        assertThat(optSearchOrder.get()).isEqualTo(entity)

        verify(repo).findById(id)
        verifyAudit(1)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7L
        doReturn(null).whenever(repo).findById(id)
        assertThat(service.findById(id).isPresent).isFalse()
        verify(repo).findById(id)
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        whenever(repo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(searchOrders)
        auditFixture()
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(searchOrders)
        verify(repo).findPageByFilter(filterMock, paginationContextMock)
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        whenever(repo.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repo).countByFilter(filterMock)
    }

    @Test
    fun savingOrUpdating_withSearchOrderWithNullId_hasRepoAddTheSearchOrder() {
        whenever(entity.id).thenReturn(null)
        whenever(repo.add(entity)).thenReturn(entity)
        auditFixture()
        assertThat(service.saveOrUpdate(entity)).isEqualTo(entity)
        verify(repo).add(entity)
        verify(entity).id
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_withSearchOrderWithNonNullId_hasRepoUpdateTheSearchOrder() {
        whenever(entity.id).thenReturn(17L)
        whenever(repo.update(entity)).thenReturn(entity)
        auditFixture()
        assertThat(service.saveOrUpdate(entity)).isEqualTo(entity)
        verify(repo).update(entity)
        verify(entity).id
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdatingSearchCondition_withConditionWithNullId_delegatesAddingToRepo() {
        val searchOrderId: Long = 3
        whenever(searchConditionMock.searchConditionId).thenReturn(null)
        whenever(repo.addSearchCondition(searchConditionMock, searchOrderId, LC)).thenReturn(searchConditionMock)
        assertThat(service.saveOrUpdateSearchCondition(searchConditionMock, searchOrderId, LC))
            .isEqualTo(searchConditionMock)
        verify(repo).addSearchCondition(searchConditionMock, searchOrderId, LC)
        verify(searchConditionMock).searchConditionId
    }

    @Test
    fun savingOrUpdatingSearchCondition_withConditionWithId_delegatesUpdatingToRepo() {
        val searchOrderId: Long = 3
        whenever(searchConditionMock.searchConditionId).thenReturn(17L)
        whenever(repo.updateSearchCondition(searchConditionMock, searchOrderId, LC)).thenReturn(searchConditionMock)
        assertThat(service.saveOrUpdateSearchCondition(searchConditionMock, searchOrderId, LC))
            .isEqualTo(searchConditionMock)
        verify(repo).updateSearchCondition(searchConditionMock, searchOrderId, LC)
        verify(searchConditionMock).searchConditionId
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify(repo, never()).delete(anyLong(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        whenever(entity.id).thenReturn(null)
        service.remove(entity)
        verify(entity).id
        verify(repo, never()).delete(anyLong(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        whenever(entity.id).thenReturn(3L)
        whenever(entity.version).thenReturn(33)

        service.remove(entity)

        verify(entity, times(2)).id
        verify(entity, times(1)).version
        verify(repo, times(1)).delete(3L, 33)
    }

    @Test
    fun removingSearchConditionWithId_withNullId_doesNothing() {
        service.removeSearchConditionWithId(null)
        verify(repo, never()).deleteSearchConditionWithId(anyLong())
    }

    @Test
    fun removingSearchConditionWithId_delegatesToRepo() {
        val id = 3L
        service.removeSearchConditionWithId(id)
        verify(repo, times(1)).deleteSearchConditionWithId(id)
    }

    companion object {
        private const val LC = "de"
    }
}
