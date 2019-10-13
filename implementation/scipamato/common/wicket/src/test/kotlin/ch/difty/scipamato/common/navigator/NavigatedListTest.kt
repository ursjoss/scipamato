package ch.difty.scipamato.common.navigator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class NavigatedListTest {

    private val ids = listOf(13L, 2L, 5L, 27L, 7L, 3L, 30L)
    private val navigatedList = NavigatedList(ids)

    @Test
    fun passingEmptyList_throws() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { NavigatedList(ArrayList<Long>()) }
    }

    @Test
    fun passingSingleItemList_accepts() {
        val rs = NavigatedList(listOf(true))
        assertThat(rs.size()).isEqualTo(1)
    }

    @Test
    fun size_ofNonEmptyResultSet_isEqualToSizeOfPassedInList() {
        assertThat(navigatedList.size()).isEqualTo(ids.size)
    }

    @Test
    fun doesNotAcceptDuplicateValues() {
        val nav = NavigatedList(listOf(13L, 2L, 2L, 5L))
        assertThat(nav.items).containsExactly(13L, 2L, 5L)
    }

    @Test
    fun nonEmptyLongResultSet_returnsAllUniqueNonNullItemsPassedIn() {
        assertThat(navigatedList.items).containsExactlyElementsOf(ids)
    }

    @Test
    fun nonEmptyStringResultSet_returnsAllItemsPassedIn() {
        val stringNav = NavigatedList(listOf("baz", "foo", "bar"))
        assertThat(stringNav.items).containsExactly("baz", "foo", "bar")
    }

    @Test
    fun cannotModifyItemsOfResultSet() {
        navigatedList
            .items
            .add(100L)
        assertThat(navigatedList.items).containsExactlyElementsOf(ids)
    }

    @Test
    fun indexOfNewResultSet_isOnFirstItem() {
        assertThat(navigatedList.itemWithFocus).isEqualTo(ids[0])
    }

    @Test
    fun settingCurrentItem_withItemNotContained_throws() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            val idNotContained = 300L
            assertThat(ids).doesNotContain(idNotContained)
            navigatedList.setFocusToItem(idNotContained)
        }
    }

    @Test
    fun canSetIndexWithinRangeOfList() {
        navigatedList.setFocusToItem(27L)
        assertThat(navigatedList.itemWithFocus).isEqualTo(27L)
    }

    @Test
    fun canGoToNext() {
        var idx = 0
        while (idx < ids.size - 1) {
            assertThat(navigatedList.itemWithFocus).isEqualTo(ids[idx++])
            navigatedList.next()
            if (idx < ids.size - 2)
                assertThat(navigatedList.hasNext()).isTrue()
        }
        assertThat(navigatedList.hasNext()).isFalse()
        assertThat(navigatedList.itemWithFocus).isEqualTo(ids[ids.size - 1])
        assertThat(navigatedList.hasNext()).isFalse()
    }

    @Test
    fun cannotAdvanceBeyondLastItem() {
        navigatedList.setFocusToItem(ids[ids.size - 1])
        for (i in 0..9) {
            assertThat(navigatedList.itemWithFocus).isEqualTo(ids[ids.size - 1])
            navigatedList.next()
        }
        assertThat(navigatedList.hasNext()).isFalse()
    }

    @Test
    fun cannotRetreatBeyondFirstItem() {
        for (i in 0..9) {
            assertThat(navigatedList.itemWithFocus).isEqualTo(ids[0])
            navigatedList.previous()
        }
        assertThat(navigatedList.hasPrevious()).isFalse()
    }

    @Test
    fun canRetreatToPrevious() {
        var idx = ids.size - 1
        navigatedList.setFocusToItem(ids[idx])
        while (idx > 0) {
            assertThat(navigatedList.itemWithFocus).isEqualTo(ids[idx--])
            navigatedList.previous()
            if (idx > 1)
                assertThat(navigatedList.hasPrevious()).isTrue()
        }
        assertThat(navigatedList.hasPrevious()).isFalse()
        assertThat(navigatedList.itemWithFocus).isEqualTo(ids[0])
        assertThat(navigatedList.hasPrevious()).isFalse()
    }

    @Test
    fun contains_withIdInList_returnsTrue() {
        assertThat(navigatedList.containsId(2L)).isTrue()
    }

    @Test
    fun contains_withIdNotInList_returnsFalse() {
        assertThat(navigatedList.containsId(-1L)).isFalse()
    }

    @Test
    fun without_withIdInOriginalList() {
        assertThat(navigatedList.without(5L)).containsExactly(13L, 2L, 27L, 7L, 3L, 30L)
    }

    @Test
    fun without_withIdNotInOriginalList_returnsFullList() {
        assertThat(navigatedList.without(50L)).containsExactly(13L, 2L, 5L, 27L, 7L, 3L, 30L)
    }
}
