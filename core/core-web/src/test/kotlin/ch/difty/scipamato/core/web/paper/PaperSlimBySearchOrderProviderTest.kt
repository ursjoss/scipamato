package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.search.SearchOrder
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.junit.jupiter.api.Test

private const val ID = 55L

internal class PaperSlimBySearchOrderProviderTest : AbstractPaperSlimProviderTest<SearchOrder, PaperSlimBySearchOrderProvider>() {

    override val filterDummy = SearchOrder().apply {
        name = "foo"
        id = ID
    }

    override fun localFixture() {
        every { paperSlimServiceMock.findPageBySearchOrder(filterDummy, any()) } returns pageOfSlimPapers
    }

    override fun newProvider(): PaperSlimBySearchOrderProvider =
        object : PaperSlimBySearchOrderProvider(filterDummy, PAGE_SIZE) {
            override fun getLanguageCode(): String = LC
        }

    override fun localVerify(offset: Int, pageSize: Int, sort: String) {
        verify { paperSlimServiceMock.findPageBySearchOrder(filterDummy, matchPaginationContext(offset, pageSize, sort)) }
    }

    @Test
    fun constructingWithNewFilter_usesEmptyFilter() {
        val p = PaperSlimBySearchOrderProvider(null, 10)
        p.filterState shouldBeEqualTo SearchOrder()
    }

    @Test
    fun size() {
        val size = 5
        every { paperSlimServiceMock.countBySearchOrder(filterDummy) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { paperSlimServiceMock.countBySearchOrder(filterDummy) }
    }

    @Test
    fun settingFilterState() {
        provider.filterState shouldBeEqualTo filterDummy
        val searchOrder2 = SearchOrder().apply { isGlobal = true }
        provider.filterState = searchOrder2
        provider.filterState shouldBeEqualTo searchOrder2
    }

    @Test
    fun gettingAllPapersByFilter() {
        provider.setSort("authors", SortOrder.ASCENDING)
        every {
            paperServiceMock.findPageBySearchOrder(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"), LC)
        } returns pageOfPapers
        val papers = provider.findAllPapersByFilter()
        papers shouldHaveSize 5
        papers shouldContain paperDummy
        verify {
            paperServiceMock.findPageBySearchOrder(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"), LC)
        }
    }

    @Test
    fun findingAllPaperIds() {
        val ids = listOf(3L, 18L, 6L)
        provider.setSort("authors", SortOrder.ASCENDING)
        every {
            paperServiceMock.findPageOfIdsBySearchOrder(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"))
        } returns ids
        val papers = provider.findAllPaperIdsByFilter()
        papers shouldBeEqualTo ids
        verify {
            paperServiceMock.findPageOfIdsBySearchOrder(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"))
        }
    }

    @Test
    fun gettingSearchOrderId_passesModelId() {
        provider.searchOrderId shouldBeEqualTo ID
    }

    @Test
    fun isShowingExclusion_ifTrueInFilter_returnsTrue() {
        assertShowExcluded(true)
    }

    @Test
    fun isShowingExclusion_ifFalseInFilter_returnsFalse() {
        assertShowExcluded(false)
    }

    private fun assertShowExcluded(result: Boolean) {
        val filter2Dummy = SearchOrder().apply {
            isShowExcluded = result
        }
        val provider2 = object : PaperSlimBySearchOrderProvider(filter2Dummy, PAGE_SIZE) {
            override fun getLanguageCode(): String = LC
        }
        provider2.isShowExcluded shouldBeEqualTo result
    }

    companion object {
        private const val LC = "en"
    }
}
