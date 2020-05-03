package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.search.PaperFilter
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.junit.jupiter.api.Test

internal class PaperSlimByPaperFilterProviderTest :
    AbstractPaperSlimProviderTest<PaperFilter, PaperSlimByPaperFilterProvider>() {

    @MockK
    override lateinit var filter: PaperFilter

    override fun localFixture() {
        every { paperSlimServiceMock.findPageByFilter(filter, any()) } returns pageOfSlimPapers
    }

    override fun newProvider(): PaperSlimByPaperFilterProvider = PaperSlimByPaperFilterProvider(filter, PAGE_SIZE)

    override fun verifyFilterMock(offset: Int, pageSize: Int, sort: String) {
        verify { paperSlimServiceMock.findPageByFilter(filter, matchPaginationContext(offset, pageSize, sort)) }
    }

    @Test
    fun constructingWithNewFilter_usesEmptyFilter() {
        val p = PaperSlimByPaperFilterProvider(null, 10)
        p.filterState shouldBeEqualTo PaperFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { paperSlimServiceMock.countByFilter(filter) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { paperSlimServiceMock.countByFilter(filter) }
    }

    @Test
    fun settingFilterState() {
        val filterMock2 = mockk<PaperFilter>()
        provider.filterState shouldBeEqualTo filter
        provider.filterState = filterMock2
        provider.filterState shouldBeEqualTo filterMock2
        verify { filter == filter }
    }

    @Test
    fun findingAllPapersByFilter() {
        provider.setSort("title", SortOrder.DESCENDING)
        every {
            paperServiceMock.findPageByFilter(filter, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
        } returns pageOfPapers
        val papers = provider.findAllPapersByFilter()
        papers shouldHaveSize 5
        papers shouldContain paperMock
        verify {
            paperServiceMock.findPageByFilter(filter, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
        }
        verify { paperMock == paperMock }
    }

    @Test
    fun findingAllIdsByFilter() {
        provider.setSort("title", SortOrder.DESCENDING)
        every {
            paperServiceMock.findPageOfIdsByFilter(filter, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
        } returns listOf(5L, 3L, 17L)
        val ids = provider.findAllPaperIdsByFilter()
        ids shouldContainSame listOf(5L, 3L, 17L)
        verify {
            paperServiceMock.findPageOfIdsByFilter(filter, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
        }
    }

    // I know: The Liskov Substitution Principle ... :-(
    @Test
    fun gettingSearchOrderId_alwaysReturnsNull() {
        provider.searchOrderId.shouldBeNull()
    }

    // I know: The Liskov Substitution Principle ... :-(
    @Test
    fun gettingInvertExclusionFlag_alwaysReturnsFalse() {
        provider.isShowExcluded.shouldBeFalse()
        provider.isShowExcluded = true
        provider.isShowExcluded.shouldBeFalse()
    }
}
