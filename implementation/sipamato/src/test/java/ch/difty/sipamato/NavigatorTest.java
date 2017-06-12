package ch.difty.sipamato;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;

public class NavigatorTest {

    private final List<Long> ids = new ArrayList<>(Arrays.asList(13l, 2l, 5l, 27l, 7l, 3l, 30l));
    private final Navigator<Long> navigator = new Navigator<Long>(ids);

    @Test(expected = NullArgumentException.class)
    public void passingNull_throws() {
        new Navigator<Long>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passingEmptyList_throws() {
        new Navigator<Long>(new ArrayList<Long>());
    }

    public void passingSingleItemList_accepts() {
        Navigator<Boolean> rs = new Navigator<>(Arrays.asList(true));
        assertThat(rs.size()).isEqualTo(1);
    }

    @Test
    public void size_ofNonEmptyResultSet_isEqualToSizeOfPassedInList() {
        assertThat(navigator.size()).isEqualTo(ids.size());
    }

    @Test
    public void doesNotAcceptNullValues() {
        Navigator<Long> nav = new Navigator<Long>(Arrays.asList(13l, 2l, null, 5l));
        assertThat(nav.getItems()).containsExactly(13l, 2l, 5l).doesNotContain((Long) null);
    }

    @Test
    public void doesNotAcceptDuplicateValues() {
        Navigator<Long> nav = new Navigator<Long>(Arrays.asList(13l, 2l, 2l, 5l));
        assertThat(nav.getItems()).hasSize(3).containsExactly(13l, 2l, 5l);
    }

    @Test
    public void nonEmptyLongResultSet_returnsAllUniqueNonNullItemsPassedIn() {
        assertThat(navigator.getItems()).containsExactlyElementsOf(ids);
    }

    @Test
    public void nonEmptyStringResultSet_returnsAllItemsPassedIn() {
        Navigator<String> stringNav = new Navigator<String>(Arrays.asList("baz", "foo", "bar"));
        assertThat(stringNav.getItems()).containsExactly("baz", "foo", "bar");
    }

    @Test
    public void cannotModifyItemsOfResultSet() {
        navigator.getItems().add(100l);
        assertThat(navigator.getItems()).containsExactlyElementsOf(ids);
    }

    @Test
    public void indexOfNewResultSet_isOnFirstItem() {
        assertThat(navigator.getCurrentItem()).isEqualTo(ids.get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void settingCurrentItem_withNullParamter_throws() {
        navigator.setCurrentItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingCurrentItem_withItemNotContained_throws() {
        long idNotContained = 300l;
        assertThat(ids).doesNotContain(idNotContained);
        navigator.setCurrentItem(idNotContained);
    }

    @Test
    public void canSetIndexWithinRangeOfList() {
        navigator.setCurrentItem(27l);
        assertThat(navigator.getCurrentItem()).isEqualTo(27l);
    }

    @Test
    public void canAdvance() {
        int idx = 0;
        while (idx < ids.size() - 1) {
            assertThat(navigator.getCurrentItem()).isEqualTo(ids.get(idx++));
            navigator.advance();
        }
        assertThat(navigator.getCurrentItem()).isEqualTo(ids.get(ids.size() - 1));
    }

    @Test
    public void cannotAdvanceBeyondLastItem() {
        navigator.setCurrentItem(ids.get(ids.size() - 1));
        for (int i = 0; i < 10; i++) {
            assertThat(navigator.getCurrentItem()).isEqualTo(ids.get(ids.size() - 1));
            navigator.advance();
        }
    }

    @Test
    public void cannotRetreatBeyondFirstItem() {
        for (int i = 0; i < 10; i++) {
            assertThat(navigator.getCurrentItem()).isEqualTo(ids.get(0));
            navigator.retreat();
        }
    }

    @Test
    public void canRetreat() {
        int idx = ids.size() - 1;
        navigator.setCurrentItem(ids.get(idx));
        while (idx > 0) {
            assertThat(navigator.getCurrentItem()).isEqualTo(ids.get(idx--));
            navigator.retreat();
        }
        assertThat(navigator.getCurrentItem()).isEqualTo(ids.get(0));
    }

}
