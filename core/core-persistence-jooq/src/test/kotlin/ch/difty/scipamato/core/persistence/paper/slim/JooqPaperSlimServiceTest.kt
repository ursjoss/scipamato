package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.AbstractServiceTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class JooqPaperSlimServiceTest : AbstractServiceTest<Long, PaperSlim, PaperSlimRepository>() {

    override val repo = mockk<PaperSlimRepository>()
    private val filterMock = mockk<PaperFilter>()
    private val searchOrderMock = mockk<SearchOrder>()
    private val paginationContextMock = mockk<PaginationContext>()
    override val entity = mockk<PaperSlim>(relaxed = true)
    private val paperMock = mockk<Paper>()

    private val papers = listOf(entity, entity)

    private var service = JooqPaperSlimService(repo, userRepoMock)

    public override fun specificTearDown() {
        confirmVerified(repo, filterMock, searchOrderMock, paginationContextMock, entity, paperMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7L
        every { repo.findById(id) } returns entity
        auditFixture()

        val optPaper = service.findById(id)
        optPaper.isPresent.shouldBeTrue()
        optPaper.get() shouldBeEqualTo entity

        verify { repo.findById(id) }
        verify { entity == entity }

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
    fun findingPageByFilter_delegatesToRepo() {
        every { repo.findPageByFilter(filterMock, paginationContextMock) } returns papers
        auditFixture()
        service.findPageByFilter(filterMock, paginationContextMock) shouldBeEqualTo papers
        verify { repo.findPageByFilter(filterMock, paginationContextMock) }
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_withSimpleFilter_delegatesToRepo() {
        every { repo.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repo.countByFilter(filterMock) }
    }

    @Test
    fun findingBySearchOrder_delegatesToRepo() {
        every { repo.findBySearchOrder(searchOrderMock) } returns papers
        service.findBySearchOrder(searchOrderMock) shouldContainSame papers
        verify { repo.findBySearchOrder(searchOrderMock) }
        verify { entity == entity }
    }

    @Test
    fun findingPagedBySearchOrder_delegatesToRepo() {
        every { repo.findPageBySearchOrder(searchOrderMock, paginationContextMock) } returns papers
        service.findPageBySearchOrder(searchOrderMock, paginationContextMock) shouldBeEqualTo papers
        verify { repo.findPageBySearchOrder(searchOrderMock, paginationContextMock) }
    }

    @Test
    fun countingBySearchOrder_delegatesToRepo() {
        every { repo.countBySearchOrder(searchOrderMock) } returns 2
        service.countBySearchOrder(searchOrderMock) shouldBeEqualTo 2
        verify { repo.countBySearchOrder(searchOrderMock) }
    }
}
