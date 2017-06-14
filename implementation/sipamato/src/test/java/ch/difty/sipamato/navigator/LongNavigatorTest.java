package ch.difty.sipamato.navigator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LongNavigatorTest {

    private final ItemNavigator<Long> nm = new LongNavigator();

    private final List<Long> single = Arrays.asList(5l);
    private final List<Long> triple = Arrays.asList(5l, 12l, 3l);

    @Test
    public void gettingItemWithFocus_withUninitializedNavigator_returnsNull() {
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    public void settingFocus_withUninitilizedNavigator_ignores() {
        nm.setFocusToItem(5l);
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
        nm.initialize(new ArrayList<Long>());
        assertThat(nm.getItemWithFocus()).isNull();
    }

    @Test
    public void initalizingSingleItemList_setsFocusToSingleItem() {
        nm.initialize(single);
        assertThat(nm.getItemWithFocus()).isEqualTo(5l);
    }

    @Test
    public void singleItemList_cannotMove_andKeepsFocusOnSingleItem() {
        nm.initialize(single);
        assertThat(nm.hasPrevious()).isFalse();
        nm.previous();
        assertThat(nm.getItemWithFocus()).isEqualTo(5l);
        assertThat(nm.hasNext()).isFalse();
        nm.next();
        assertThat(nm.getItemWithFocus()).isEqualTo(5l);
    }

    @Test
    public void initalizingWithTripleItemList_hasFocusOnFirstItem() {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5l);
    }

    @Test
    public void initalizingWithTripleItemList_hasNoPrevious() {
        nm.initialize(triple);
        assertThat(nm.hasPrevious()).isFalse();
    }

    @Test
    public void initalizingWithTripleItemList_hasNextAndCanAdvance() {
        nm.initialize(triple);
        assertThat(nm.getItemWithFocus()).isEqualTo(5l);
        assertThat(nm.hasNext()).isTrue();
        nm.next();
        assertThat(nm.getItemWithFocus()).isEqualTo(12l);
    }

    @Test
    public void canSetFocus_withTripleItemList() {
        nm.initialize(triple);
        nm.setFocusToItem(12l);
        assertThat(nm.getItemWithFocus()).isEqualTo(12l);
    }

    @Test
    public void canRetract_withTripleItemList_withFocusOnSecond() {
        nm.initialize(triple);
        nm.setFocusToItem(12l);
        assertThat(nm.hasPrevious()).isTrue();
        nm.previous();
        assertThat(nm.getItemWithFocus()).isEqualTo(5l);
    }

    @Test
    public void settingFocusToNull_isIgnored() {
        nm.initialize(triple);
        nm.setFocusToItem(null);
        assertThat(nm.getItemWithFocus()).isEqualTo(5l);
    }

    @Test
    public void settingFocusToItemNotInList_throws() {
        nm.initialize(triple);
        assertThat(triple).doesNotContain(100l);
        try {
            nm.setFocusToItem(100l);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("Cannot set focus to item that is not part of the managed list (item 100).");
        }
    }
}
