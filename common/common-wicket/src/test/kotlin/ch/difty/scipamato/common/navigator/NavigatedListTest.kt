package ch.difty.scipamato.common.navigator

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class NavigatedListTest {

    private val ids = listOf(13L, 2L, 5L, 27L, 7L, 3L, 30L)
    private val navigatedList = NavigatedList(ids)

    @Test
    fun passingEmptyList_throws() {
        invoking { NavigatedList(ArrayList<Long>()) } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun passingSingleItemList_accepts() {
        val rs = NavigatedList(listOf(true))
        rs.size() shouldBeEqualTo 1
    }

    @Test
    fun size_ofNonEmptyResultSet_isEqualToSizeOfPassedInList() {
        navigatedList.size() shouldBeEqualTo ids.size
    }

    @Test
    fun doesNotAcceptDuplicateValues() {
        val nav = NavigatedList(listOf(13L, 2L, 2L, 5L))
        nav.items shouldContainAll listOf(13L, 2L, 5L)
    }

    @Test
    fun nonEmptyLongResultSet_returnsAllUniqueNonNullItemsPassedIn() {
        navigatedList.items shouldContainAll ids
    }

    @Test
    fun nonEmptyStringResultSet_returnsAllItemsPassedIn() {
        val stringNav = NavigatedList(listOf("baz", "foo", "bar"))
        stringNav.items shouldContainAll listOf("baz", "foo", "bar")
    }

    @Test
    fun indexOfNewResultSet_isOnFirstItem() {
        navigatedList.itemWithFocus shouldBeEqualTo ids[0]
    }

    @Test
    fun settingCurrentItem_withItemNotContained_throws() {
        invoking {
            val idNotContained = 300L
            ids shouldNotContain idNotContained
            navigatedList.setFocusToItem(idNotContained)
        } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun canSetIndexWithinRangeOfList() {
        navigatedList.setFocusToItem(27L)
        navigatedList.itemWithFocus shouldBeEqualTo 27L
    }

    @Test
    fun canGoToNext() {
        var idx = 0
        while (idx < ids.size - 1) {
            navigatedList.itemWithFocus shouldBeEqualTo ids[idx++]
            navigatedList.next()
            if (idx < ids.size - 2)
                navigatedList.hasNext().shouldBeTrue()
        }
        navigatedList.hasNext().shouldBeFalse()
        navigatedList.itemWithFocus shouldBeEqualTo ids[ids.size - 1]
        navigatedList.hasNext().shouldBeFalse()
    }

    @Test
    fun cannotAdvanceBeyondLastItem() {
        navigatedList.setFocusToItem(ids[ids.size - 1])
        for (i in 0..9) {
            navigatedList.itemWithFocus shouldBeEqualTo ids[ids.size - 1]
            navigatedList.next()
        }
        navigatedList.hasNext().shouldBeFalse()
    }

    @Test
    fun cannotRetreatBeyondFirstItem() {
        for (i in 0..9) {
            navigatedList.itemWithFocus shouldBeEqualTo ids[0]
            navigatedList.previous()
        }
        navigatedList.hasPrevious().shouldBeFalse()
    }

    @Test
    fun canRetreatToPrevious() {
        var idx = ids.size - 1
        navigatedList.setFocusToItem(ids[idx])
        while (idx > 0) {
            navigatedList.itemWithFocus shouldBeEqualTo ids[idx--]
            navigatedList.previous()
            if (idx > 1)
                navigatedList.hasPrevious().shouldBeTrue()
        }
        navigatedList.hasPrevious().shouldBeFalse()
        navigatedList.itemWithFocus shouldBeEqualTo ids[0]
        navigatedList.hasPrevious().shouldBeFalse()
    }

    @Test
    fun contains_withIdInList_returnsTrue() {
        navigatedList.containsId(2L).shouldBeTrue()
    }

    @Test
    fun contains_withIdNotInList_returnsFalse() {
        navigatedList.containsId(-1L).shouldBeFalse()
    }

    @Test
    fun without_withIdInOriginalList() {
        navigatedList.without(5L) shouldContainAll listOf(13L, 2L, 27L, 7L, 3L, 30L)
    }

    @Test
    fun without_withIdNotInOriginalList_returnsFullList() {
        navigatedList.without(50L) shouldContainAll listOf(13L, 2L, 5L, 27L, 7L, 3L, 30L)
    }
}
