package ch.difty.scipamato.navigator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.difty.scipamato.lib.NullArgumentException;

public class NavigatedListTest {

    private final List<Long> ids = new ArrayList<>(Arrays.asList(13l, 2l, 5l, 27l, 7l, 3l, 30l));
    private final NavigatedList<Long> navigatedList = new NavigatedList<Long>(ids);

    @Test(expected = NullArgumentException.class)
    public void passingNull_throws() {
        new NavigatedList<Long>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passingEmptyList_throws() {
        new NavigatedList<Long>(new ArrayList<Long>());
    }

    public void passingSingleItemList_accepts() {
        NavigatedList<Boolean> rs = new NavigatedList<>(Arrays.asList(true));
        assertThat(rs.size()).isEqualTo(1);
    }

    @Test
    public void size_ofNonEmptyResultSet_isEqualToSizeOfPassedInList() {
        assertThat(navigatedList.size()).isEqualTo(ids.size());
    }

    @Test
    public void doesNotAcceptNullValues() {
        NavigatedList<Long> nav = new NavigatedList<Long>(Arrays.asList(13l, 2l, null, 5l));
        assertThat(nav.getItems()).containsExactly(13l, 2l, 5l).doesNotContain((Long) null);
    }

    @Test
    public void doesNotAcceptDuplicateValues() {
        NavigatedList<Long> nav = new NavigatedList<Long>(Arrays.asList(13l, 2l, 2l, 5l));
        assertThat(nav.getItems()).hasSize(3).containsExactly(13l, 2l, 5l);
    }

    @Test
    public void nonEmptyLongResultSet_returnsAllUniqueNonNullItemsPassedIn() {
        assertThat(navigatedList.getItems()).containsExactlyElementsOf(ids);
    }

    @Test
    public void nonEmptyStringResultSet_returnsAllItemsPassedIn() {
        NavigatedList<String> stringNav = new NavigatedList<String>(Arrays.asList("baz", "foo", "bar"));
        assertThat(stringNav.getItems()).containsExactly("baz", "foo", "bar");
    }

    @Test
    public void cannotModifyItemsOfResultSet() {
        navigatedList.getItems().add(100l);
        assertThat(navigatedList.getItems()).containsExactlyElementsOf(ids);
    }

    @Test
    public void indexOfNewResultSet_isOnFirstItem() {
        assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void settingCurrentItem_withNullParamter_throws() {
        navigatedList.setFocusToItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingCurrentItem_withItemNotContained_throws() {
        long idNotContained = 300l;
        assertThat(ids).doesNotContain(idNotContained);
        navigatedList.setFocusToItem(idNotContained);
    }

    @Test
    public void canSetIndexWithinRangeOfList() {
        navigatedList.setFocusToItem(27l);
        assertThat(navigatedList.getItemWithFocus()).isEqualTo(27l);
    }

    @Test
    public void canGoToNext() {
        int idx = 0;
        while (idx < ids.size() - 1) {
            assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(idx++));
            navigatedList.next();
            if (idx < ids.size() - 2)
                assertThat(navigatedList.hasNext()).isTrue();
        }
        assertThat(navigatedList.hasNext()).isFalse();
        assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(ids.size() - 1));
        assertThat(navigatedList.hasNext()).isFalse();
    }

    @Test
    public void cannotAdvanceBeyondLastItem() {
        navigatedList.setFocusToItem(ids.get(ids.size() - 1));
        for (int i = 0; i < 10; i++) {
            assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(ids.size() - 1));
            navigatedList.next();
        }
        assertThat(navigatedList.hasNext()).isFalse();
    }

    @Test
    public void cannotRetreatBeyondFirstItem() {
        for (int i = 0; i < 10; i++) {
            assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(0));
            navigatedList.previous();
        }
        assertThat(navigatedList.hasPrevious()).isFalse();
    }

    @Test
    public void canRetreatToPrevious() {
        int idx = ids.size() - 1;
        navigatedList.setFocusToItem(ids.get(idx));
        while (idx > 0) {
            assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(idx--));
            navigatedList.previous();
            if (idx > 1)
                assertThat(navigatedList.hasPrevious()).isTrue();
        }
        assertThat(navigatedList.hasPrevious()).isFalse();
        assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(0));
        assertThat(navigatedList.hasPrevious()).isFalse();
    }

}
