package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class JooqPublicPaperServiceTest {

    private val publicPaper: PublicPaper = PublicPaper.builder().id(ID).build()

    private val mockRepo = mockk<PublicPaperRepository>()
    private val filterMock = mockk<PublicPaperFilter>()
    private val paginationContextMock = mockk<PaginationContext>()

    private val papers = listOf(publicPaper, PublicPaper.builder().id(ID + 1).build())

    private val service = JooqPublicPaperService(mockRepo)

    @AfterEach
    fun tearDown() {
        confirmVerified(mockRepo)
    }

    @Test
    fun findingByNumber_withRepoFindingRecord_returnsItWrappedAsOptional() {
        every { mockRepo.findByNumber(NUMBER) } returns publicPaper

        val paperOp = service.findByNumber(NUMBER)

        paperOp.isPresent.shouldBeTrue()
        paperOp.get() shouldBeEqualTo publicPaper
        verify { mockRepo.findByNumber(NUMBER) }
    }

    @Test
    fun findingByNumber_withRepoNotFindingRecord_returnsEmptyOptional() {
        every { mockRepo.findByNumber(NUMBER) } returns null

        val paperOp = service.findByNumber(NUMBER)

        paperOp.isPresent.shouldBeFalse()
        verify { mockRepo.findByNumber(NUMBER) }
    }

    @Test
    fun findingPageByFilter_delegatesToRepo() {
        every { mockRepo.findPageByFilter(filterMock, paginationContextMock) } returns papers
        service.findPageByFilter(filterMock, paginationContextMock) shouldHaveSize 2
        verify { mockRepo.findPageByFilter(filterMock, paginationContextMock) }
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        every { mockRepo.countByFilter(filterMock) } returns 2
        service.countByFilter(filterMock) shouldBeEqualTo 2
        verify { mockRepo.countByFilter(filterMock) }
    }

    @Test
    fun findingPageOfIdsByFilter_delegatesToRepo() {
        val idList = listOf(3L, 5L)
        every { mockRepo.findPageOfNumbersByFilter(filterMock, paginationContextMock) } returns idList
        service.findPageOfNumbersByFilter(filterMock, paginationContextMock) shouldBeEqualTo idList
        verify { mockRepo.findPageOfNumbersByFilter(filterMock, paginationContextMock) }
    }

    companion object {
        private const val ID: Long = 5
        private const val NUMBER: Long = 15
    }
}
