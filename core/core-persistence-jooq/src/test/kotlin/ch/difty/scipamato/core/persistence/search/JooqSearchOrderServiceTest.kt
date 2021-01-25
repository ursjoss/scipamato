package ch.difty.scipamato.core.persistence.search

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchOrderFilter
import ch.difty.scipamato.core.persistence.AbstractServiceTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class JooqSearchOrderServiceTest : AbstractServiceTest<Long, SearchOrder, SearchOrderRepository>() {

    override val repo = mockk<SearchOrderRepository>(relaxed = true) {
        every { delete(any(), any()) } returns entity
    }
    private val filterMock = mockk<SearchOrderFilter>()
    private val paginationContextMock = mockk<PaginationContext>()
    override val entity = mockk<SearchOrder>(relaxed = true)
    private val searchConditionMock = mockk<SearchCondition>()

    private var service = JooqSearchOrderService(repo, userRepoMock)

    private val searchOrders = listOf(entity, entity)

    public override fun specificTearDown() {
        confirmVerified(repo, filterMock, paginationContextMock, entity, searchConditionMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7L
        every { repo.findById(id) } returns entity
        auditFixture()

        val optSearchOrder = service.findById(id)
        optSearchOrder.isPresent.shouldBeTrue()
        optSearchOrder.get() shouldBeEqualTo entity

        verify { repo.findById(id) }
        verify { entity == entity }
        verify { entity.toString() }
        verifyAudit(1)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7L
        every { repo.findById(id) } returns null
        service.findById(id).isPresent.shouldBeFalse()
        verify { repo.findById(id) }
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        every { repo.findPageByFilter(filterMock, paginationContextMock) } returns searchOrders
        auditFixture()
        service.findPageByFilter(filterMock, paginationContextMock) shouldBeEqualTo searchOrders
        verify { repo.findPageByFilter(filterMock, paginationContextMock) }
        verify { entity.toString() }
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        every { repo.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repo.countByFilter(filterMock) }
    }

    @Test
    fun savingOrUpdating_withSearchOrderWithNullId_hasRepoAddTheSearchOrder() {
        every { entity.id } returns null
        every { repo.add(entity) } returns entity
        auditFixture()
        service.saveOrUpdate(entity) shouldBeEqualTo entity
        verify { repo.add(entity) }
        verify { entity.id }
        verify { entity == entity }
        verify { entity.toString() }
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_withSearchOrderWithNonNullId_hasRepoUpdateTheSearchOrder() {
        every { entity.id } returns 17L
        every { repo.update(entity) } returns entity
        auditFixture()
        service.saveOrUpdate(entity) shouldBeEqualTo entity
        verify { repo.update(entity) }
        verify { entity.id }
        verify { entity == entity }
        verify { entity.toString() }
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdatingSearchCondition_withConditionWithNullId_delegatesAddingToRepo() {
        val searchOrderId: Long = 3
        every { searchConditionMock.searchConditionId } returns null
        every { repo.addSearchCondition(searchConditionMock, searchOrderId, LC) } returns searchConditionMock
        service.saveOrUpdateSearchCondition(searchConditionMock, searchOrderId, LC) shouldBeEqualTo searchConditionMock
        verify { repo.addSearchCondition(searchConditionMock, searchOrderId, LC) }
        verify { searchConditionMock.searchConditionId }
        verify { searchConditionMock == searchConditionMock }
        verify { searchConditionMock.toString() }
    }

    @Test
    fun savingOrUpdatingSearchCondition_withConditionWithId_delegatesUpdatingToRepo() {
        val searchOrderId: Long = 3
        every { searchConditionMock.searchConditionId } returns 17L
        every { repo.updateSearchCondition(searchConditionMock, searchOrderId, LC) } returns searchConditionMock
        service.saveOrUpdateSearchCondition(searchConditionMock, searchOrderId, LC) shouldBeEqualTo searchConditionMock
        verify { repo.updateSearchCondition(searchConditionMock, searchOrderId, LC) }
        verify { searchConditionMock.searchConditionId }
        verify { searchConditionMock == searchConditionMock }
        verify { searchConditionMock.toString() }
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify(exactly = 0) { repo.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        every { entity.id } returns null
        service.remove(entity)
        verify { entity.id }
        verify(exactly = 0) { repo.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        every { entity.id } returns 3L
        every { entity.version } returns 33

        service.remove(entity)

        verify(exactly = 2) { entity.id }
        verify(exactly = 1) { entity.version }
        verify(exactly = 1) { repo.delete(3L, 33) }
    }

    @Test
    fun removingSearchConditionWithId_withNullId_doesNothing() {
        service.removeSearchConditionWithId(null)
        verify(exactly = 0) { repo.deleteSearchConditionWithId(any()) }
    }

    @Test
    fun removingSearchConditionWithId_delegatesToRepo() {
        val id = 3L
        service.removeSearchConditionWithId(id)
        verify(exactly = 1) { repo.deleteSearchConditionWithId(id) }
    }

    companion object {
        private const val LC = "de"
    }
}
