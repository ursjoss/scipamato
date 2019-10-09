package ch.difty.scipamato.common.navigator

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class LongNavigatorTest {

    private val nm = LongNavigator()

    private val single = listOf(5L)
    private val triple = listOf(5L, 12L, 3L)

    @Test
    fun gettingItemWithFocus_withUninitializedNavigator_returnsNull() {
        assertThat(nm.itemWithFocus == null).isTrue()
    }

    @Test
    fun settingFocus_withUninitializedNavigator_ignores() {
        nm.setFocusToItem(5L)
        assertThat(nm.itemWithFocus == null).isTrue()
    }

    @Test
    fun hasPrevious_withUninitializedNavigator_isFalse() {
        assertThat(nm.hasPrevious()).isFalse()
    }

    @Test
    fun hasNext_withUninitializedNavigator_isFalse() {
        assertThat(nm.hasNext()).isFalse()
    }

    @Test
    fun previousOrNext_withUninitializedNavigator_isIgnored() {
        nm.previous()
        nm.next()
    }

    @Test
    fun initializingWithNull_isIgnored() {
        nm.initialize(null)
        assertThat(nm.itemWithFocus == null).isTrue()
    }

    @Test
    fun initializingEmptyList_isIgnored() {
        nm.initialize(ArrayList())
        assertThat(nm.itemWithFocus == null).isTrue()
    }

    @Test
    fun initializingSingleItemList_setsFocusToSingleItem() {
        nm.initialize(single)
        assertThat(nm.itemWithFocus).isEqualTo(5L)
    }

    @Test
    fun singleItemList_cannotMove_andKeepsFocusOnSingleItem() {
        nm.initialize(single)
        assertThat(nm.hasPrevious()).isFalse()
        nm.previous()
        assertThat(nm.itemWithFocus).isEqualTo(5L)
        assertThat(nm.hasNext()).isFalse()
        nm.next()
        assertThat(nm.itemWithFocus).isEqualTo(5L)
    }

    @Test
    fun initializingWithTripleItemList_hasFocusOnFirstItem() {
        nm.initialize(triple)
        assertThat(nm.itemWithFocus).isEqualTo(5L)
    }

    @Test
    fun initializingWithTripleItemList_hasNoPrevious() {
        nm.initialize(triple)
        assertThat(nm.hasPrevious()).isFalse()
    }

    @Test
    fun initializingWithTripleItemList_hasNextAndCanAdvance() {
        nm.initialize(triple)
        assertThat(nm.itemWithFocus).isEqualTo(5L)
        assertNextIs(12L)
    }

    @Test
    fun canSetFocus_withTripleItemList() {
        nm.initialize(triple)
        nm.setFocusToItem(12L)
        assertThat(nm.itemWithFocus).isEqualTo(12L)
    }

    @Test
    fun canRetract_withTripleItemList_withFocusOnSecond() {
        nm.initialize(triple)
        nm.setFocusToItem(12L)
        assertThat(nm.hasPrevious()).isTrue()
        nm.previous()
        assertThat(nm.itemWithFocus).isEqualTo(5L)
    }

    @Test
    fun settingFocusToNull_isIgnored() {
        nm.initialize(triple)
        nm.setFocusToItem(null)
        assertThat(nm.itemWithFocus).isEqualTo(5L)
    }

    @Test
    fun settingFocusToItemNotInList_throws() {
        nm.initialize(triple)
        assertThat(triple).doesNotContain(100L)
        try {
            nm.setFocusToItem(100L)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot set focus to item that is not part of the managed list (item 100).")
        }
    }

    @Test
    fun settingIdToHeadIfNotPresent_ifPresent_doesNothing() {
        assertAddingDoesNothing(triple[1])
    }

    private fun assertAddingDoesNothing(id: Long?) {
        nm.initialize(triple)
        assertThat(nm.itemWithFocus).isEqualTo(5L)
        assertThat(nm.hasPrevious()).isFalse()
        assertThat(nm.hasNext()).isTrue()

        nm.setIdToHeadIfNotPresent(id)

        assertThat(nm.itemWithFocus).isEqualTo(5L)
        assertThat(nm.hasPrevious()).isFalse()
        assertThat(nm.hasNext()).isTrue()
    }

    @Test
    fun settingIdToHeadIfNotPresent_ifNull_doesNothing() {
        assertAddingDoesNothing(null)
    }

    @Test
    fun settingIdToHeadIfNotPresent_ifNotPresent_addsToHeadAndFocuses() {
        val id = 200L
        assertThat(triple).doesNotContain(id)

        nm.initialize(triple)
        assertThat(nm.itemWithFocus).isEqualTo(5L)
        assertThat(nm.hasPrevious()).isFalse()
        assertThat(nm.hasNext()).isTrue()

        nm.setIdToHeadIfNotPresent(id)

        assertThat(nm.itemWithFocus).isEqualTo(id)
        assertThat(nm.hasPrevious()).isFalse()
        assertThat(nm.hasNext()).isTrue()
    }

    @Test
    fun removeFromManger_ifPresent_removesItAndIsModified() {
        val id = 12L
        assertThat(triple).contains(id)
        nm.initialize(triple)
        assertThat(nm.itemWithFocus).isEqualTo(5L)

        nm.remove(id)
        assertThat(nm.isModified).isTrue()

        assertThat(nm.itemWithFocus).isEqualTo(5L)
        assertThat(nm.hasPrevious()).isFalse()
        assertThat(nm.hasNext()).isTrue()
        assertNextIs(3L)
        assertThat(nm.hasNext()).isFalse()
    }

    @Test
    fun removeFromManager_ifNotPresent_isNotModified() {
        val id = 200L
        assertThat(triple).doesNotContain(id)
        nm.initialize(triple)
        assertThat(nm.itemWithFocus).isEqualTo(5L)

        nm.remove(id)
        assertThat(nm.isModified).isFalse()

        assertThat(nm.itemWithFocus).isEqualTo(5L)
        assertThat(nm.hasPrevious()).isFalse()
        assertNextIs(12L)
        assertNextIs(3L)
        assertThat(nm.hasNext()).isFalse()
    }

    private fun assertNextIs(id: Long) {
        assertThat(nm.hasNext()).isTrue()
        nm.next()
        assertThat(nm.itemWithFocus).isEqualTo(id)
    }

    @Test
    fun removeFromManager_ifPresentAndWithFocusAndIsNotLast_removesItAndSetsFocusToNextItem() {
        val id = 12L
        assertThat(triple).contains(id)
        nm.initialize(triple)

        nm.setFocusToItem(id)
        assertThat(nm.itemWithFocus).isEqualTo(id)

        nm.remove(id)
        assertThat(nm.isModified).isTrue()

        assertThat(nm.itemWithFocus).isEqualTo(3L)
        assertThat(nm.hasPrevious()).isTrue()
        assertThat(nm.hasNext()).isFalse()
    }

    @Test
    fun removeFromManager_ifPresentAndWithFocusAndIsLast_removesItAndSetsFocusToPreviousItem() {
        val id = 3L
        assertThat(triple).contains(id)
        nm.initialize(triple)

        nm.setFocusToItem(id)
        assertThat(nm.itemWithFocus).isEqualTo(id)

        nm.remove(id)

        assertThat(nm.isModified).isTrue()

        assertThat(nm.itemWithFocus).isEqualTo(12L)
        assertThat(nm.hasPrevious()).isTrue()
        assertThat(nm.hasNext()).isFalse()
    }

    @Test
    fun removeFromManager_withNullId_isNotModified() {
        nm.initialize(triple)
        nm.remove(null)
        assertThat(nm.isModified).isFalse()
    }

    @Test
    fun removingFromManager_removingAll() {
        nm.initialize(triple)
        assertThat(nm.itemWithFocus).isEqualTo(5L)

        nm.remove(3L)
        assertThat(nm.itemWithFocus).isEqualTo(5L)

        nm.remove(5L)
        assertThat(nm.itemWithFocus).isEqualTo(12L)
        nm.remove(12L)

        assertThat(nm.isModified).isTrue()
        assertThat(nm.itemWithFocus == null).isTrue()
    }
}
