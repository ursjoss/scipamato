package ch.difty.scipamato.common.navigator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class LongNavigatorTest {

    private final ItemNavigator<Long> nm = new LongNavigator();

    private final List<Long> single = Collections.singletonList(5L);
    private final List<Long> triple = Arrays.asList(5L, 12L, 3L);

    @Test
    public void gettingItemWithFocus_withUninitializedNavigator_returnsNull() {
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    public void settingFocus_withUninitializedNavigator_ignores() {
        nm.setFocusToItem(5L);
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    public void hasPrevious_withUninitializedNavigator_isFalse() {
        assertThat(nm.hasPrevious()).isFalse();
    }

    @Test
    public void hasNext_withUninitializedNavigator_isFalse() {
        assertThat(nm.hasNext()).isFalse();
    }

    @Test
    public void previousOrNext_withUninitializedNavigator_isIgnored() {
        nm.previous();
        nm.next();
    }

    @Test
    public void initializingWithNull_isIgnored() {
        nm.initialize(null);
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    public void initializingEmptyList_isIgnored() {
        nm.initialize(new ArrayList<>());
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    public void initializingSingleItemList_setsFocusToSingleItem() {
        nm.initialize(single);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    public void singleItemList_cannotMove_andKeepsFocusOnSingleItem() {
        nm.initialize(single);
        assertThat(nm.hasPrevious()).isFalse();
        nm.previous();
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasNext()).isFalse();
        nm.next();
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    public void initializingWithTripleItemList_hasFocusOnFirstItem() {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    public void initializingWithTripleItemList_hasNoPrevious() {
        nm.initialize(triple);
        assertThat(nm.hasPrevious()).isFalse();
    }

    @Test
    public void initializingWithTripleItemList_hasNextAndCanAdvance() {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
        assertThat(nm.hasNext()).isTrue();
        nm.next();
        assertThat(nm.getItemWithFocus()).isEqualTo(12L);
    }

    @Test
    public void canSetFocus_withTripleItemList() {
        nm.initialize(triple);
        nm.setFocusToItem(12L);
        assertThat(nm.getItemWithFocus()).isEqualTo(12L);
    }

    @Test
    public void canRetract_withTripleItemList_withFocusOnSecond() {
        nm.initialize(triple);
        nm.setFocusToItem(12L);
        assertThat(nm.hasPrevious()).isTrue();
        nm.previous();
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    public void settingFocusToNull_isIgnored() {
        nm.initialize(triple);
        nm.setFocusToItem(null);
        assertThat(nm.getItemWithFocus()).isEqualTo(5L);
    }

    @Test
    public void settingFocusToItemNotInList_throws() {
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
    public void settingIdToHeadIfNotPresent_ifPresent_doesNothing() {
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
    public void settingIdToHeadIfNotPresent_ifNull_doesNothing() {
        assertAddingDoesNothing(null);
    }

    @Test
    public void settingIdToHeadIfNotPresent_ifNotPresent_addsToHeadAndFocuses() {
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
}
