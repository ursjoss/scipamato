package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.search.PaperFilter
import io.mockk.every
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

    override val filterDummy = PaperFilter().apply { authorMask = "foo" }

    override fun localFixture() {
        every { paperSlimServiceMock.findPageByFilter(filterDummy, any()) } returns pageOfSlimPapers
    }

    override fun newProvider(): PaperSlimByPaperFilterProvider = PaperSlimByPaperFilterProvider(filterDummy, PAGE_SIZE)

    override fun localVerify(offset: Int, pageSize: Int, sort: String) {
        verify { paperSlimServiceMock.findPageByFilter(filterDummy, matchPaginationContext(offset, pageSize, sort)) }
    }

    @Test
    fun constructingWithNewFilter_usesEmptyFilter() {
        val p = PaperSlimByPaperFilterProvider(null, 10)
        p.filterState shouldBeEqualTo PaperFilter()
    }

    @Test
    fun size() {
        val size = 5
        every { paperSlimServiceMock.countByFilter(filterDummy) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { paperSlimServiceMock.countByFilter(filterDummy) }
    }

    @Test
    fun settingFilterState() {
        provider.filterState shouldBeEqualTo filterDummy
        val filter2Dummy = PaperFilter().apply { newsletterId = 10 }
        provider.filterState = filter2Dummy
        provider.filterState shouldBeEqualTo filter2Dummy
    }

    @Test
    fun findingAllPapersByFilter() {
        provider.setSort("title", SortOrder.DESCENDING)
        every {
            paperServiceMock.findPageByFilter(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
        } returns pageOfPapers
        val papers = provider.findAllPapersByFilter()
        papers shouldHaveSize 5
        papers shouldContain paperDummy
        verify {
            paperServiceMock.findPageByFilter(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
        }
    }

    @Test
    fun findingAllIdsByFilter() {
        provider.setSort("title", SortOrder.DESCENDING)
        every {
            paperServiceMock.findPageOfIdsByFilter(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
        } returns listOf(5L, 3L, 17L)
        val ids = provider.findAllPaperIdsByFilter()
        ids shouldContainSame listOf(5L, 3L, 17L)
        verify {
            paperServiceMock.findPageOfIdsByFilter(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "title: DESC"))
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
