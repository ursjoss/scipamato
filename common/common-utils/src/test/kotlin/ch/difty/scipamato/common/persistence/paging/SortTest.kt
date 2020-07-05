package ch.difty.scipamato.common.persistence.paging

import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SortTest {

    private val sortProperties = ArrayList<SortProperty>(4)

    private lateinit var sort: Sort

    @BeforeEach
    fun setUp() {
        sortProperties.add(SortProperty("a", Direction.ASC))
        sortProperties.add(SortProperty("b", Direction.DESC))
        sortProperties.add(SortProperty("c", Direction.DESC))
        sortProperties.add(SortProperty("d", Direction.ASC))

        sort = Sort(sortProperties)
    }

    @Test
    fun degenerateConstruction_withNoSortProperties_throws() {
        invoking { Sort(emptyList()) } shouldThrow
            IllegalArgumentException::class withMessage "sortProperties can't be empty."
    }

    @Test
    fun degenerateConstruction_withEmptyPropertyNames_throws() {

        invoking { Sort(Direction.ASC) } shouldThrow
            IllegalArgumentException::class withMessage "propertyNames can't be empty."
    }

    private fun assertSortProperty(dir: Direction, propertyNames: Array<String>) {
        val sort = Sort(dir, *propertyNames)

        sort.iterator().asSequence() shouldHaveSize propertyNames.size

        for (sp in sort)
            sp.direction shouldBeEqualTo dir
    }

    @Test
    fun creatingSortForThreeAscendingProperties_returnsIteratorForAllThreePropertiesInAscendingOrder() {
        assertSortProperty(Direction.ASC, arrayOf("a", "b", "c"))
    }

    @Test
    fun cratingSortForFiveDescendingProperties_returnsIteratorForAllFiveElementsWithDescendingOrder() {
        assertSortProperty(Direction.DESC, arrayOf("a", "b", "c", "d", "e"))
    }

    @Test
    fun creatingSortForFourSortPropertiesWithDifferentSortDirections() {
        val it = sort.iterator()
        assertSortProperty(it, Direction.ASC, "a")
        assertSortProperty(it, Direction.DESC, "b")
        assertSortProperty(it, Direction.DESC, "c")
        assertSortProperty(it, Direction.ASC, "d")
        it.hasNext().shouldBeFalse()
    }

    private fun assertSortProperty(it: Iterator<SortProperty>, dir: Direction, p: String) {
        val sp = it.next()
        sp.direction shouldBeEqualTo dir
        sp.name shouldBeEqualTo p
    }

    @Test
    fun gettingSortPropertyFor_nonExistingName_returnsNull() {
        val p = "x"
        sortProperties.map { it.name } shouldNotContain p
        sort.getSortPropertyFor(p).shouldBeNull()
    }

    @Test
    fun gettingSortPropertyFor_existingName_returnsRespectiveSortProperty() {
        val p = "c"
        sortProperties.map { it.name }.shouldContain(p)
        sort.getSortPropertyFor(p)?.name shouldBeEqualTo p
    }

    @Test
    fun directionAsc_isAscending() {
        Direction.ASC.isAscending().shouldBeTrue()
    }

    @Test
    fun directionDesc_isNotAscending() {
        Direction.DESC.isAscending().shouldBeFalse()
    }

    @Test
    fun testingToString() {
        sort.toString() shouldBeEqualTo "a: ASC,b: DESC,c: DESC,d: ASC"
    }

    @Test
    fun sortEqualityTests() {
        (sort == sort).shouldBeTrue()
        (sort == Sort(sortProperties)).shouldBeTrue()

        val sortProperties2 = ArrayList<SortProperty>()
        sortProperties2.add(SortProperty("a", Direction.ASC))
        sortProperties2.add(SortProperty("b", Direction.DESC))
        sortProperties2.add(SortProperty("c", Direction.DESC))
        (sort == Sort(sortProperties2)).shouldBeFalse()
        sort.hashCode() shouldNotBeEqualTo Sort(sortProperties2).hashCode()

        sortProperties2.add(SortProperty("d", Direction.ASC))
        (sort == Sort(sortProperties2)).shouldBeTrue()
        sort.hashCode() shouldBeEqualTo Sort(sortProperties2).hashCode()
    }

    @Test
    fun sortPropertyEqualityTests() {
        val sf1: SortProperty? = SortProperty("foo", Direction.DESC)

        (sf1 == null).shouldBeFalse()
        (sf1 == sf1).shouldBeTrue()
        (sf1 == SortProperty("foo", Direction.DESC)).shouldBeTrue()
        (sf1 == SortProperty("foo", Direction.ASC)).shouldBeFalse()
        (sf1 == SortProperty("bar", Direction.DESC)).shouldBeFalse()
    }
}
