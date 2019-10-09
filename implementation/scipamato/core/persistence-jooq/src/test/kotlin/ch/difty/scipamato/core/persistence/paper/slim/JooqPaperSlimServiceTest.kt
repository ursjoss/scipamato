package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.AbstractServiceTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqPaperSlimServiceTest : AbstractServiceTest<Long, PaperSlim, PaperSlimRepository>() {

    override val repo = mock<PaperSlimRepository>()
    private val filterMock = mock<PaperFilter>()
    private val searchOrderMock = mock<SearchOrder>()
    private val paginationContextMock = mock<PaginationContext>()
    override val entity = mock<PaperSlim>()
    private val paperMock = mock<Paper>()

    private val papers = listOf(entity, entity)

    private var service = JooqPaperSlimService(repo, userRepoMock)

    public override fun specificTearDown() {
        verifyNoMoreInteractions(repo, filterMock, searchOrderMock, paginationContextMock, entity, paperMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7L
        whenever(repo.findById(id)).thenReturn(entity)
        auditFixture()

        val optPaper = service.findById(id)
        assertThat(optPaper.isPresent).isTrue()
        assertThat(optPaper.get()).isEqualTo(entity)

        verify(repo).findById(id)

        verifyAudit(1)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7L
        whenever(repo.findById(id)).thenReturn(null)
        assertThat(service.findById(id).isPresent).isFalse()
        verify(repo).findById(id)
    }

    @Test
    fun findingPageByFilter_delegatesToRepo() {
        whenever(repo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers)
        auditFixture()
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers)
        verify(repo).findPageByFilter(filterMock, paginationContextMock)
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_withSimpleFilter_delegatesToRepo() {
        whenever(repo.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repo).countByFilter(filterMock)
    }

    @Test
    fun findingBySearchOrder_delegatesToRepo() {
        whenever(repo.findBySearchOrder(searchOrderMock)).thenReturn(papers)
        assertThat(service.findBySearchOrder(searchOrderMock)).containsAll(papers)
        verify(repo).findBySearchOrder(searchOrderMock)
    }

    @Test
    fun findingPagedBySearchOrder_delegatesToRepo() {
        whenever(repo.findPageBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(papers)
        assertThat(service.findPageBySearchOrder(searchOrderMock, paginationContextMock)).isEqualTo(papers)
        verify(repo).findPageBySearchOrder(searchOrderMock, paginationContextMock)
    }

    @Test
    fun countingBySearchOrder_delegatesToRepo() {
        whenever(repo.countBySearchOrder(searchOrderMock)).thenReturn(2)
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2)
        verify(repo).countBySearchOrder(searchOrderMock)
    }
}
