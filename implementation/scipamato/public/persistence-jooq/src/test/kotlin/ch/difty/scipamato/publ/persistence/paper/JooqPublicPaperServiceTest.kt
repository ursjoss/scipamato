package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqPublicPaperServiceTest {

    private val publicPaper: PublicPaper = PublicPaper.builder().id(ID).build()

    private val mockRepo = mock<PublicPaperRepository>()
    private val filterMock = mock<PublicPaperFilter>()
    private val paginationContextMock = mock<PaginationContext>()

    private val papers = listOf(publicPaper, PublicPaper.builder().id(ID + 1).build())

    private val service = JooqPublicPaperService(mockRepo)

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(mockRepo)
    }

    @Test
    fun findingByNumber_withRepoFindingRecord_returnsItWrappedAsOptional() {
        whenever(mockRepo.findByNumber(NUMBER)).thenReturn(publicPaper)

        val paperOp = service.findByNumber(NUMBER)

        assertThat(paperOp).isPresent
        assertThat(paperOp).hasValue(publicPaper)
        verify(mockRepo).findByNumber(NUMBER)
    }

    @Test
    fun findingByNumber_withRepoNotFindingRecord_returnsEmptyOptional() {
        whenever(mockRepo.findByNumber(NUMBER)).thenReturn(null)

        val paperOp = service.findByNumber(NUMBER)

        assertThat(paperOp).isNotPresent
        verify(mockRepo).findByNumber(NUMBER)
    }

    @Test
    fun findingPageByFilter_delegatesToRepo() {
        whenever(mockRepo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers)
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).hasSize(2)
        verify(mockRepo).findPageByFilter(filterMock, paginationContextMock)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        whenever(mockRepo.countByFilter(filterMock)).thenReturn(2)
        assertThat(service.countByFilter(filterMock)).isEqualTo(2)
        verify(mockRepo).countByFilter(filterMock)
    }

    @Test
    fun findingPageOfIdsByFilter_delegatesToRepo() {
        val idList = listOf(3L, 5L)
        whenever(mockRepo.findPageOfNumbersByFilter(filterMock, paginationContextMock)).thenReturn(idList)
        assertThat(service.findPageOfNumbersByFilter(filterMock, paginationContextMock)).isEqualTo(idList)
        verify(mockRepo).findPageOfNumbersByFilter(filterMock, paginationContextMock)
    }

    companion object {
        private const val ID: Long = 5
        private const val NUMBER: Long = 15
    }
}
