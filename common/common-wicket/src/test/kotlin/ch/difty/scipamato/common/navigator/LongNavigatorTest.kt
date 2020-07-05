package ch.difty.scipamato.common.navigator

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

internal class LongNavigatorTest {

    private val nm = LongNavigator()

    private val single = listOf(5L)
    private val triple = listOf(5L, 12L, 3L)

    @Test
    fun gettingItemWithFocus_withUninitializedNavigator_returnsNull() {
        nm.itemWithFocus.shouldBeNull()
    }

    @Test
    fun settingFocus_withUninitializedNavigator_ignores() {
        nm.setFocusToItem(5L)
        nm.itemWithFocus.shouldBeNull()
    }

    @Test
    fun hasPrevious_withUninitializedNavigator_isFalse() {
        nm.hasPrevious().shouldBeFalse()
    }

    @Test
    fun hasNext_withUninitializedNavigator_isFalse() {
        nm.hasNext().shouldBeFalse()
    }

    @Test
    fun previousOrNext_withUninitializedNavigator_isIgnored() {
        nm.previous()
        nm.next()
    }

    @Test
    fun initializingEmptyList_isIgnored() {
        nm.initialize(ArrayList())
        nm.itemWithFocus.shouldBeNull()
    }

    @Test
    fun initializingSingleItemList_setsFocusToSingleItem() {
        nm.initialize(single)
        nm.itemWithFocus shouldBeEqualTo 5L
    }

    @Test
    fun singleItemList_cannotMove_andKeepsFocusOnSingleItem() {
        nm.initialize(single)
        nm.hasPrevious().shouldBeFalse()
        nm.previous()
        nm.itemWithFocus shouldBeEqualTo 5L
        nm.hasNext().shouldBeFalse()
        nm.next()
        nm.itemWithFocus shouldBeEqualTo 5L
    }

    @Test
    fun initializingWithTripleItemList_hasFocusOnFirstItem() {
        nm.initialize(triple)
        nm.itemWithFocus shouldBeEqualTo 5L
    }

    @Test
    fun initializingWithTripleItemList_hasNoPrevious() {
        nm.initialize(triple)
        nm.hasPrevious().shouldBeFalse()
    }

    @Test
    fun initializingWithTripleItemList_hasNextAndCanAdvance() {
        nm.initialize(triple)
        nm.itemWithFocus shouldBeEqualTo 5L
        assertNextIs(12L)
    }

    @Test
    fun canSetFocus_withTripleItemList() {
        nm.initialize(triple)
        nm.setFocusToItem(12L)
        nm.itemWithFocus shouldBeEqualTo 12L
    }

    @Test
    fun canRetract_withTripleItemList_withFocusOnSecond() {
        nm.initialize(triple)
        nm.setFocusToItem(12L)
        nm.hasPrevious().shouldBeTrue()
        nm.previous()
        nm.itemWithFocus shouldBeEqualTo 5L
    }

    @Test
    fun settingFocusToNull_isIgnored() {
        nm.initialize(triple)
        nm.setFocusToItem(null)
        nm.itemWithFocus shouldBeEqualTo 5L
    }

    @Test
    fun settingFocusToItemNotInList_throws() {
        nm.initialize(triple)
        triple shouldNotContain 100L
        invoking { nm.setFocusToItem(100L) } shouldThrow IllegalArgumentException::class withMessage
            "Cannot set focus to item that is not part of the managed list (item 100)."
    }

    @Test
    fun settingIdToHeadIfNotPresent_ifPresent_doesNothing() {
        assertAddingDoesNothing(triple[1])
    }

    private fun assertAddingDoesNothing(id: Long) {
        nm.initialize(triple)
        nm.itemWithFocus shouldBeEqualTo 5L
        nm.hasPrevious().shouldBeFalse()
        nm.hasNext().shouldBeTrue()

        nm.setIdToHeadIfNotPresent(id)

        nm.itemWithFocus shouldBeEqualTo 5L
        nm.hasPrevious().shouldBeFalse()
        nm.hasNext().shouldBeTrue()
    }

    @Test
    fun settingIdToHeadIfNotPresent_ifNotPresent_addsToHeadAndFocuses() {
        val id = 200L
        triple shouldNotContain id

        nm.initialize(triple)
        nm.itemWithFocus shouldBeEqualTo 5L
        nm.hasPrevious().shouldBeFalse()
        nm.hasNext().shouldBeTrue()

        nm.setIdToHeadIfNotPresent(id)

        nm.itemWithFocus shouldBeEqualTo id
        nm.hasPrevious().shouldBeFalse()
        nm.hasNext().shouldBeTrue()
    }

    @Test
    fun removeFromManger_ifPresent_removesItAndIsModified() {
        val id = 12L
        triple shouldContain id
        nm.initialize(triple)
        nm.itemWithFocus shouldBeEqualTo 5L

        nm.remove(id)
        nm.isModified.shouldBeTrue()

        nm.itemWithFocus shouldBeEqualTo 5L
        nm.hasPrevious().shouldBeFalse()
        nm.hasNext().shouldBeTrue()
        assertNextIs(3L)
        nm.hasNext().shouldBeFalse()
    }

    @Test
    fun removeFromManager_ifNotPresent_isNotModified() {
        val id = 200L
        triple shouldNotContain id
        nm.initialize(triple)
        nm.itemWithFocus shouldBeEqualTo 5L

        nm.remove(id)
        nm.isModified.shouldBeFalse()

        nm.itemWithFocus shouldBeEqualTo 5L
        nm.hasPrevious().shouldBeFalse()
        assertNextIs(12L)
        assertNextIs(3L)
        nm.hasNext().shouldBeFalse()
    }

    private fun assertNextIs(id: Long) {
        nm.hasNext().shouldBeTrue()
        nm.next()
        nm.itemWithFocus shouldBeEqualTo id
    }

    @Test
    fun removeFromManager_ifPresentAndWithFocusAndIsNotLast_removesItAndSetsFocusToNextItem() {
        val id = 12L
        triple shouldContain id
        nm.initialize(triple)

        nm.setFocusToItem(id)
        nm.itemWithFocus shouldBeEqualTo id

        nm.remove(id)
        nm.isModified.shouldBeTrue()

        nm.itemWithFocus shouldBeEqualTo 3L
        nm.hasPrevious().shouldBeTrue()
        nm.hasNext().shouldBeFalse()
    }

    @Test
    fun removeFromManager_ifPresentAndWithFocusAndIsLast_removesItAndSetsFocusToPreviousItem() {
        val id = 3L
        triple shouldContain id
        nm.initialize(triple)

        nm.setFocusToItem(id)
        nm.itemWithFocus shouldBeEqualTo id

        nm.remove(id)

        nm.isModified.shouldBeTrue()

        nm.itemWithFocus shouldBeEqualTo 12L
        nm.hasPrevious().shouldBeTrue()
        nm.hasNext().shouldBeFalse()
    }

    @Test
    fun removingFromManager_removingAll() {
        nm.initialize(triple)
        nm.itemWithFocus shouldBeEqualTo 5L

        nm.remove(3L)
        nm.itemWithFocus shouldBeEqualTo 5L

        nm.remove(5L)
        nm.itemWithFocus shouldBeEqualTo 12L
        nm.remove(12L)

        nm.isModified.shouldBeTrue()
        nm.itemWithFocus.shouldBeNull()
    }
}
