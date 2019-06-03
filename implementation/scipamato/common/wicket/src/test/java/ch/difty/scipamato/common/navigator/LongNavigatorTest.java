package ch.difty.scipamato.common.navigator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class LongNavigatorTest {

    private final ItemNavigator<Long> nm = new LongNavigator();

    private final List<Long> single = Collections.singletonList(5L);
    private final List<Long> triple = Arrays.asList(5L, 12L, 3L);

    @Test
    void gettingItemWithFocus_withUninitializedNavigator_returnsNull() {
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    void settingFocus_withUninitializedNavigator_ignores() {
        nm.setFocusToItem(5L);
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    void hasPrevious_withUninitializedNavigator_isFalse() {
        assertThat(nm.hasPrevious()).isFalse();
    }

    @Test
    void hasNext_withUninitializedNavigator_isFalse() {
        assertThat(nm.hasNext()).isFalse();
    }

    @Test
    void previousOrNext_withUninitializedNavigator_isIgnored() {
        nm.previous();
        nm.next();
    }

    @Test
    void initializingWithNull_isIgnored() {
        nm.initialize(null);
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    void initializingEmptyList_isIgnored() {
        nm.initialize(new ArrayList<>());
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    void initializingSingleItemList_setsFocusToSingleItem() {
        nm.initialize(single);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    void singleItemList_cannotMove_andKeepsFocusOnSingleItem() {
        nm.initialize(single);
        assertThat(nm.hasPrevious()).isFalse();
        nm.previous();
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasNext()).isFalse();
        nm.next();
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    void initializingWithTripleItemList_hasFocusOnFirstItem() {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    void initializingWithTripleItemList_hasNoPrevious() {
        nm.initialize(triple);
        assertThat(nm.hasPrevious()).isFalse();
    }

    @Test
    void initializingWithTripleItemList_hasNextAndCanAdvance() {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertNextIs(12L);
    }

    @Test
    void canSetFocus_withTripleItemList() {
        nm.initialize(triple);
        nm.setFocusToItem(12L);
        assertThat(nm.getItemWithFocus()).isEqualTo(12L);
    }

    @Test
    void canRetract_withTripleItemList_withFocusOnSecond() {
        nm.initialize(triple);
        nm.setFocusToItem(12L);
        assertThat(nm.hasPrevious()).isTrue();
        nm.previous();
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    void settingFocusToNull_isIgnored() {
        nm.initialize(triple);
        nm.setFocusToItem(null);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    void settingFocusToItemNotInList_throws() {
        nm.initialize(triple);
        assertThat(triple).doesNotContain(100L);
        try {
            nm.setFocusToItem(100L);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot set focus to item that is not part of the managed list (item 100).");
        }
    }

    @Test
    void settingIdToHeadIfNotPresent_ifPresent_doesNothing() {
        assertAddingDoesNothing(triple.get(1));
    }

    private void assertAddingDoesNothing(final Long id) {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasPrevious()).isFalse();
        assertThat(nm.hasNext()).isTrue();

        nm.setIdToHeadIfNotPresent(id);

        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasPrevious()).isFalse();
        assertThat(nm.hasNext()).isTrue();
    }

    @Test
    void settingIdToHeadIfNotPresent_ifNull_doesNothing() {
        assertAddingDoesNothing(null);
    }

    @Test
    void settingIdToHeadIfNotPresent_ifNotPresent_addsToHeadAndFocuses() {
        Long id = 200L;
        assertThat(triple).doesNotContain(id);

        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasPrevious()).isFalse();
        assertThat(nm.hasNext()).isTrue();

        nm.setIdToHeadIfNotPresent(id);

        assertThat(nm.getItemWithFocus()).isEqualTo(id);
        assertThat(nm.hasPrevious()).isFalse();
        assertThat(nm.hasNext()).isTrue();
    }

    @Test
    void removeFromManger_ifPresent_removesItAndIsModified() {
        Long id = 12L;
        assertThat(triple).contains(id);
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);

        nm.remove(id);
        assertThat(nm.isModified()).isTrue();

        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasPrevious()).isFalse();
        assertThat(nm.hasNext()).isTrue();
        assertNextIs(3L);
        assertThat(nm.hasNext()).isFalse();
    }

    @Test
    void removeFromManager_ifNotPresent_isNotModified() {
        Long id = 200L;
        assertThat(triple).doesNotContain(id);
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);

        nm.remove(id);
        assertThat(nm.isModified()).isFalse();

        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasPrevious()).isFalse();
        assertNextIs(12L);
        assertNextIs(3L);
        assertThat(nm.hasNext()).isFalse();
    }

    private void assertNextIs(final long id) {
        assertThat(nm.hasNext()).isTrue();
        nm.next();
        assertThat(nm.getItemWithFocus()).isEqualTo(id);
    }

    @Test
    void removeFromManager_ifPresentAndWithFocusAndIsNotLast_removesItAndSetsFocusToNextItem() {
        Long id = 12L;
        assertThat(triple).contains(id);
        nm.initialize(triple);

        nm.setFocusToItem(id);
        assertThat(nm.getItemWithFocus()).isEqualTo(id);

        nm.remove(id);
        assertThat(nm.isModified()).isTrue();

        assertThat(nm.getItemWithFocus()).isEqualTo(3L);
        assertThat(nm.hasPrevious()).isTrue();
        assertThat(nm.hasNext()).isFalse();
    }

    @Test
    void removeFromManager_ifPresentAndWithFocusAndIsLast_removesItAndSetsFocusToPreviousItem() {
        Long id = 3L;
        assertThat(triple).contains(id);
        nm.initialize(triple);

        nm.setFocusToItem(id);
        assertThat(nm.getItemWithFocus()).isEqualTo(id);

        nm.remove(id);

        assertThat(nm.isModified()).isTrue();

        assertThat(nm.getItemWithFocus()).isEqualTo(12L);
        assertThat(nm.hasPrevious()).isTrue();
        assertThat(nm.hasNext()).isFalse();
    }

    @Test
    void removeFromManager_withNullId_isNotModified() {
        nm.initialize(triple);
        nm.remove(null);
        assertThat(nm.isModified()).isFalse();
    }

    @Test
    void removingFromManager_removingAll() {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);

        nm.remove(3L);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);

        nm.remove(5L);
        assertThat(nm.getItemWithFocus()).isEqualTo(12L);
        nm.remove(12L);

        assertThat(nm.isModified()).isTrue();
        assertThat(nm.getItemWithFocus()).isNull();
    }

}
