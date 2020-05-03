package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.common.persistence.paging.matchPaginationContext
import ch.difty.scipamato.core.entity.search.SearchOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.junit.jupiter.api.Test

internal class PaperSlimBySearchOrderProviderTest : AbstractPaperSlimProviderTest<SearchOrder, PaperSlimBySearchOrderProvider>() {

    @MockK
    override lateinit var filter: SearchOrder

    override fun localFixture() {
        every { paperSlimServiceMock.findPageBySearchOrder(filter, any()) } returns pageOfSlimPapers
    }

    override fun newProvider(): PaperSlimBySearchOrderProvider =
        object : PaperSlimBySearchOrderProvider(filter, PAGE_SIZE) {
            override fun getLanguageCode(): String = LC
        }

    override fun verifyFilterMock(offset: Int, pageSize: Int, sort: String) {
        verify { paperSlimServiceMock.findPageBySearchOrder(filter, matchPaginationContext(offset, pageSize, sort)) }
    }

    @Test
    fun constructingWithNewFilter_usesEmptyFilter() {
        val p = PaperSlimBySearchOrderProvider(null, 10)
        p.filterState shouldBeEqualTo SearchOrder()
    }

    @Test
    fun size() {
        val size = 5
        every { paperSlimServiceMock.countBySearchOrder(filter) } returns size
        provider.size() shouldBeEqualTo size.toLong()
        verify { paperSlimServiceMock.countBySearchOrder(filter) }
    }

    @Test
    fun settingFilterState() {
        val searchOrder2 = mockk<SearchOrder>()
        provider.filterState shouldBeEqualTo filter
        provider.filterState = searchOrder2
        provider.filterState shouldBeEqualTo searchOrder2
        verify { filter == filter }
    }

    @Test
    fun gettingAllPapersByFilter() {
        provider.setSort("authors", SortOrder.ASCENDING)
        every {
            paperServiceMock.findPageBySearchOrder(filter, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"), LC)
        } returns pageOfPapers
        val papers = provider.findAllPapersByFilter()
        papers shouldHaveSize 5
        papers shouldContain paperMock
        verify {
            paperServiceMock.findPageBySearchOrder(filter, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"), LC)
        }
        verify { paperMock == paperMock }
    }

    @Test
    fun findingAllPaperIds() {
        val ids = listOf(3L, 18L, 6L)
        provider.setSort("authors", SortOrder.ASCENDING)
        every {
            paperServiceMock.findPageOfIdsBySearchOrder(filter, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"))
        } returns ids
        val papers = provider.findAllPaperIdsByFilter()
        papers shouldBeEqualTo ids
        verify {
            paperServiceMock.findPageOfIdsBySearchOrder(filter, matchPaginationContext(0, Int.MAX_VALUE, "authors: ASC"))
        }
    }

    @Test
    fun gettingSearchOrderId_passesModelId() {
        every { filter.id } returns 55L
        provider.searchOrderId shouldBeEqualTo 55L
        verify { filter.id }
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
        every { filter.isShowExcluded } returns result
        provider.isShowExcluded shouldBeEqualTo result
        verify { filter.isShowExcluded }
    }

    companion object {
        private const val LC = "en"
    }
}
