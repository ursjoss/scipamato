package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.web.AbstractWicketTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeLessThan
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.Locale

@SpringBootTest
@ActiveProfiles("wickettest")
internal abstract class AbstractPaperSlimProviderTest<F : PaperSlimFilter, P : AbstractPaperSlimProvider<F>> : AbstractWicketTest() {

    @MockK
    protected lateinit var entityMock: PaperSlim

    @MockK
    protected lateinit var paperMock: Paper

    lateinit var provider: P

    lateinit var pageOfSlimPapers: List<PaperSlim>
    lateinit var pageOfPapers: List<Paper>

    abstract val filter: F

    @BeforeEach
    fun setUp() {
        val tester = WicketTester(application)
        tester.session.locale = Locale("en")
        provider = newProvider()
        provider.service = paperSlimServiceMock
        provider.paperService = paperServiceMock
        pageOfSlimPapers = listOf(entityMock, entityMock, entityMock)
        pageOfPapers = listOf(paperMock, paperMock, paperMock, paperMock, paperMock)
        every { sessionFacadeMock.languageCode } returns "en"
        localFixture()
    }

    protected abstract fun localFixture()

    @AfterEach
    fun tearDown() {
        confirmVerified(paperSlimServiceMock, filter, entityMock, paperServiceMock, paperMock)
    }

    protected abstract fun newProvider(): P

    @Test
    fun gettingModel_wrapsEntity() {
        val model = provider.model(entityMock)
        model.getObject() shouldBeEqualTo entityMock
        verify { entityMock == entityMock }
    }

    @Test
    fun gettingFilterState_returnsFilter() {
        provider.filterState shouldBeEqualTo filter
        verify { filter == filter }
    }

    @Test
    fun iterating_withNoRecords_returnsNoRecords() {
        // reset the service mock
        pageOfSlimPapers = emptyList()
        localFixture()
        val it = provider.iterator(0, 3)
        it.hasNext().shouldBeFalse()
        verifyFilterMock(0, 3, "id: DESC")
    }

    protected abstract fun verifyFilterMock(offset: Int, pageSize: Int, sort: String)

    @Test
    fun iterating_throughFirstFullPage() {
        val it = provider.iterator(0, 3)
        assertRecordsIn(it)
        verifyFilterMock(0, 3, "id: DESC")
    }

    private fun assertRecordsIn(it: Iterator<PaperSlim>) {
        repeat(3) { _ ->
            it.hasNext().shouldBeTrue()
            it.next()
        }
        it.hasNext().shouldBeFalse()
    }

    @Test
    fun iterating_throughSecondFullPage() {
        val it = provider.iterator(3, 3)
        assertRecordsIn(it)
        verifyFilterMock(3, 3, "id: DESC")
    }

    @Test
    fun iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.ASCENDING)
        val it = provider.iterator(6, 3)
        assertRecordsIn(it)
        verifyFilterMock(6, 3, "title: ASC")
    }

    @Test
    fun instantiationWithPageSize_returnsGivenPageSize() {
        provider.rowsPerPage shouldBeEqualTo PAGE_SIZE
    }

    @Test
    fun iterating_withDefaultPageSize_throughFirstFullPage_withPageSizeMatchingActualSize() {
        val actualSize = 3
        actualSize shouldBeEqualTo PAGE_SIZE
        val it = provider.iterator(0, actualSize.toLong())
        assertRecordsIn(it)
        verifyFilterMock(0, actualSize, "id: DESC")
    }

    @Test
    fun iterating_withDefaultPageSize_throughThirdNotFullPage_withPageSizeHigherThanActualSize() {
        val actualSize = 2
        actualSize shouldBeLessThan PAGE_SIZE
        provider.setSort("title", SortOrder.DESCENDING)
        val it = provider.iterator(6, actualSize.toLong())
        assertRecordsIn(it)
        verifyFilterMock(6, actualSize, "title: DESC")
    }

    @Test
    fun gettingLanguageCode() {
        provider.languageCode shouldBeEqualTo "en"
    }

    companion object {
        const val PAGE_SIZE = 3
    }
}
