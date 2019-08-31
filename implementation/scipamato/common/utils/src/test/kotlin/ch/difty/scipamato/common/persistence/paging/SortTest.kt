package ch.difty.scipamato.common.persistence.paging

import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class SortTest {

    private val sortProperties = ArrayList<SortProperty>(4)

    private var sort: Sort? = null

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
        try {
            Sort(emptyList())
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("sortProperties can't be empty.")
        }

    }

    @Test
    fun degenerateConstruction_withEmptyPropertyNames_throws() {
        try {
            Sort(Direction.ASC)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("propertyNames can't be empty.")
        }

    }

    private fun assertSortProperty(dir: Direction, propertyNames: Array<String>) {
        val sort = Sort(dir, *propertyNames)

        assertThat(sort.iterator()).hasSize(propertyNames.size)

        for (sp in sort)
            assertThat(sp.direction).isEqualTo(dir)
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
        val it = sort!!.iterator()
        assertSortProperty(it, Direction.ASC, "a")
        assertSortProperty(it, Direction.DESC, "b")
        assertSortProperty(it, Direction.DESC, "c")
        assertSortProperty(it, Direction.ASC, "d")
        assertThat(it.hasNext()).isFalse()
    }

    private fun assertSortProperty(it: Iterator<SortProperty>, dir: Direction, p: String) {
        val sp = it.next()
        assertThat(sp.direction).isEqualTo(dir)
        assertThat(sp.name).isEqualTo(p)
    }

    @Test
    fun gettingSortPropertyFor_withNullName_returnsNull() {
        assertThat(sort!!.getSortPropertyFor(null)).isNull()
    }

    @Test
    fun gettingSortPropertyFor_nonExistingName_returnsNull() {
        val p = "x"
        assertThat(sortProperties)
                .extracting("name")
                .doesNotContain(p)
        assertThat(sort!!.getSortPropertyFor(p)).isNull()
    }

    @Test
    fun gettingSortPropertyFor_existingName_returnsRespectiveSortProperty() {
        val p = "c"
        assertThat(sortProperties)
                .extracting("name")
                .contains(p)
        assertThat(sort!!
                .getSortPropertyFor(p)!!
                .name).isEqualTo(p)
    }

    @Test
    fun directionAsc_isAscending() {
        assertThat(Direction.ASC.isAscending).isTrue()
    }

    @Test
    fun directionDesc_isNotAscending() {
        assertThat(Direction.DESC.isAscending).isFalse()
    }

    @Test
    fun sortPropertyWithNullDirection_isAscending() {
        assertThat(SortProperty("foo", null).direction).isEqualTo(Direction.ASC)
    }

    @Test
    fun testingToString() {
        assertThat(sort!!.toString()).isEqualTo("a: ASC,b: DESC,c: DESC,d: ASC")
    }

    @Test
    fun sortEqualityTests() {
        assertThat(sort == null).isFalse()
        assertThat(sort == sort).isTrue()
        assertThat(sort == Sort(sortProperties)).isTrue()

        val sortProperties2 = ArrayList<SortProperty>()
        sortProperties2.add(SortProperty("a", Direction.ASC))
        sortProperties2.add(SortProperty("b", Direction.DESC))
        sortProperties2.add(SortProperty("c", Direction.DESC))
        assertThat(sort == Sort(sortProperties2)).isFalse()
        assertThat(sort!!.hashCode()).isNotEqualTo(Sort(sortProperties2).hashCode())

        sortProperties2.add(SortProperty("d", Direction.ASC))
        assertThat(sort == Sort(sortProperties2)).isTrue()
        assertThat(sort!!.hashCode()).isEqualTo(Sort(sortProperties2).hashCode())
    }

    @Test
    fun sortPropertyEqualityTests() {
        val sf1: SortProperty? = SortProperty("foo", Direction.DESC)

        assertThat(sf1 == null).isFalse()
        assertThat(sf1 == sf1).isTrue()
        assertThat(sf1 == SortProperty("foo", Direction.DESC)).isTrue()
        assertThat(sf1 == SortProperty("foo", Direction.ASC)).isFalse()
        assertThat(sf1 == SortProperty("bar", Direction.DESC)).isFalse()

    }
}
