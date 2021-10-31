package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.publ.ScipamatoPublicApplication
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import ch.difty.scipamato.publ.persistence.api.PublicPaperService
import ch.difty.scipamato.publ.web.matchPaginationContext
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PublicPaperProviderTest {

    private lateinit var provider: PublicPaperProvider

    @MockK
    private lateinit var serviceMock: PublicPaperService

    @Autowired
    private lateinit var application: ScipamatoPublicApplication

    private val filterDummy = PublicPaperFilter(number = 1L)

    private val papers: MutableList<PublicPaper> = ArrayList()

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = PublicPaperProvider(filterDummy, 20)
        provider.setService(serviceMock)
        papers.add(
            PublicPaper(
                1L, 1L, 1000, "authors1", "auths", "title1", "location1", "journal1", 2016, "goals1",
                "methods1", "population1", "result1", "comment1"
            )
        )
        papers.add(
            PublicPaper(
                2L, 2L, 1002, "authors2", "auths", "title2", "location2", "journal2", 2017, "goals2",
                "methods2", "population2", "result2", "comment2"
            )
        )
        every { serviceMock.countByFilter(filterDummy) } returns 2
        every { serviceMock.findPageByFilter(filterDummy, any()) } returns papers
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(serviceMock)
    }

    @Test
    fun construct() {
        provider.filterState shouldBeEqualTo filterDummy
        provider.rowsPerPage shouldBeEqualTo 20
    }

    @Test
    fun construct_withNullFilter_instantiatesNewFilter() {
        val provider2 = PublicPaperProvider(null, 10)
        with(provider2.filterState) {
            shouldNotBeNull()
            this shouldBeInstanceOf PublicPaperFilter::class
            this shouldNotBeEqualTo filterDummy
        }
        provider2.rowsPerPage shouldBeEqualTo 10
    }

    @Test
    fun canSetFilterState() {
        provider.filterState shouldBeEqualTo filterDummy
        provider.filterState = PublicPaperFilter()
        provider.filterState shouldNotBeEqualTo filterDummy
        provider.filterState shouldBeInstanceOf PublicPaperFilter::class
    }

    @Test
    fun newModel() {
        val pp = PublicPaper(id = 5L)
        val model = provider.model(pp)

        model.shouldNotBeNull()
        model.`object` shouldBeInstanceOf PublicPaper::class
        model.`object`.id shouldBeEqualTo 5L
    }

    @Test
    fun gettingIterator_withAscendingSort() {
        provider.setSort("title", SortOrder.ASCENDING)
        provider.iterator(0L, 10L).asSequence().map { it.id } shouldContainSame listOf(1L, 2L).asSequence()
        verify { serviceMock.findPageByFilter(filterDummy, any()) }
    }

    @Test
    fun gettingIterator_withDescendingSort() {
        provider.setSort("title", SortOrder.DESCENDING)
        provider.iterator(0L, 10L).asSequence().map { it.id } shouldContainSame listOf(2L, 1L).asSequence()
        verify { serviceMock.findPageByFilter(filterDummy, any()) }
    }

    @Test
    fun gettingSize() {
        provider.size() shouldBeEqualTo 2
        verify { serviceMock.countByFilter(filterDummy) }
    }

    @Test
    fun findingAllNumbersByFilter_descendingSort() {
        findingAllNumbersByFilter(SortOrder.DESCENDING)
    }

    @Test
    fun findingAllNumbersByFilter_ascendingSort() {
        findingAllNumbersByFilter(SortOrder.ASCENDING)
    }

    private fun findingAllNumbersByFilter(srt: SortOrder) {
        val sortDescription = "title: " + if (SortOrder.DESCENDING == srt) "DESC" else "ASC"
        provider.setSort("title", srt)

        every {
            serviceMock.findPageOfNumbersByFilter(filterDummy, matchPaginationContext(0, Int.MAX_VALUE, sortDescription))
        } returns listOf(5L, 3L, 17L)
        provider.findAllPaperNumbersByFilter() shouldContainSame listOf(5L, 3L, 17L)
        verify { serviceMock.findPageOfNumbersByFilter(filterDummy, any()) }
    }
}
