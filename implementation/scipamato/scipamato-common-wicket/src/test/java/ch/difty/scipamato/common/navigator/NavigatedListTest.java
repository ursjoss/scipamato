package ch.difty.scipamato.common.navigator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.NullArgumentException;

public class NavigatedListTest {

    private final List<Long>          ids           = new ArrayList<>(Arrays.asList(13L, 2L, 5L, 27L, 7L, 3L, 30L));
    private final NavigatedList<Long> navigatedList = new NavigatedList<>(ids);

    @Test
    public void passingNull_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> new NavigatedList<Long>(null));
    }

    @Test
    public void passingEmptyList_throws() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NavigatedList<>(new ArrayList<Long>()));
    }

    @Test
    public void passingSingleItemList_accepts() {
        NavigatedList<Boolean> rs = new NavigatedList<>(Collections.singletonList(true));
        assertThat(rs.size()).isEqualTo(1);
    }

    @Test
    public void size_ofNonEmptyResultSet_isEqualToSizeOfPassedInList() {
        assertThat(navigatedList.size()).isEqualTo(ids.size());
    }

    @Test
    public void doesNotAcceptNullValues() {
        NavigatedList<Long> nav = new NavigatedList<>(Arrays.asList(13L, 2L, null, 5L));
        assertThat(nav.getItems())
            .containsExactly(13L, 2L, 5L)
            .doesNotContain((Long) null);
    }

    @Test
    public void doesNotAcceptDuplicateValues() {
        NavigatedList<Long> nav = new NavigatedList<>(Arrays.asList(13L, 2L, 2L, 5L));
        assertThat(nav.getItems())
            .hasSize(3)
            .containsExactly(13L, 2L, 5L);
    }

    @Test
    public void nonEmptyLongResultSet_returnsAllUniqueNonNullItemsPassedIn() {
        assertThat(navigatedList.getItems()).containsExactlyElementsOf(ids);
    }

    @Test
    public void nonEmptyStringResultSet_returnsAllItemsPassedIn() {
        NavigatedList<String> stringNav = new NavigatedList<>(Arrays.asList("baz", "foo", "bar"));
        assertThat(stringNav.getItems()).containsExactly("baz", "foo", "bar");
    }

    @Test
    public void cannotModifyItemsOfResultSet() {
        navigatedList
            .getItems()
            .add(100L);
        assertThat(navigatedList.getItems()).containsExactlyElementsOf(ids);
    }

    @Test
    public void indexOfNewResultSet_isOnFirstItem() {
        assertThat(navigatedList.getItemWithFocus()).isEqualTo(ids.get(0));
    }

    @Test
    public void settingCurrentItem_withNullParameter_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> navigatedList.setFocusToItem(null));
    }

    @Test
    public void settingCurrentItem_withItemNotContained_throws() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            long idNotContained = 300L;
            assertThat(ids).doesNotContain(idNotContained);
            navigatedList.setFocusToItem(idNotContained);
        });
    }

    @Test
    public void canSetIndexWithinRangeOfList() {
        navigatedList.setFocusToItem(27L);
        assertThat(navigatedList.getItemWithFocus()).isEqualTo(27L);
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

    @Test
    public void contains_withIdNull_returnsFalse() {
        assertThat(navigatedList.containsId(null)).isFalse();
    }

    @Test
    public void contains_withIdInList_returnsTrue() {
        assertThat(navigatedList.containsId(2L)).isTrue();
    }

    @Test
    public void contains_withIdNotInList_returnsFalse() {
        assertThat(navigatedList.containsId(-1L)).isFalse();
    }

    @Test
    public void without_withIdInOriginalList() {
        assertThat(navigatedList.without(5L)).containsExactly(13L, 2L, 27L, 7L, 3L, 30L);
    }

    @Test
    public void without_withIdNotInOriginalList_returnsFullList() {
        assertThat(navigatedList.without(50L)).containsExactly(13L, 2L, 5L, 27L, 7L, 3L, 30L);
    }
}
