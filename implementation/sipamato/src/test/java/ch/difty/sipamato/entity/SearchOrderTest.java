package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.SearchCondition;

@RunWith(MockitoJUnitRunner.class)
public class SearchOrderTest {

    private final SearchOrder so = new SearchOrder(10l, 1, false, null);

    @Mock
    public SearchCondition mockCondition1, mockCondition2;

    private List<SearchCondition> searchConditions = new ArrayList<>();

    @Test
    public void testGetters() {
        assertThat(so.getId()).isEqualTo(10);
        assertThat(so.getOwner()).isEqualTo(1);
        assertThat(so.isGlobal()).isEqualTo(false);
    }

    @Test
    public void testSetters() {
        so.setId(11l);
        so.setOwner(2);
        so.setGlobal(true);

        assertThat(so.getId()).isEqualTo(11);
        assertThat(so.getOwner()).isEqualTo(2);
        assertThat(so.isGlobal()).isEqualTo(true);
    }

    @Test
    public void whenInstantiating_withNullList_hasNoConditions() {
        assertThat(so.getSearchConditions()).isEmpty();
    }

    @Test
    public void whenInstantiating_withEmptyList_hasNoConditions() {
        assertThat(new SearchOrder(searchConditions).getSearchConditions()).isEmpty();
    }

    @Test
    public void whenInstantiating_withNonEmptyList_hasHandedOverConditions() {
        searchConditions.addAll(Arrays.asList(mockCondition1, mockCondition2));
        assertThat(new SearchOrder(searchConditions).getSearchConditions()).containsExactly(mockCondition1, mockCondition2);
    }

    @Test
    public void whenAddingNullCondition_itIsNotAdded() {
        so.add(null);
        assertThat(so.getSearchConditions()).isEmpty();
    }

    @Test
    public void whenAddingCondition_itIsGettingAdded() {
        so.add(mockCondition1);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition1);
    }

    @Test
    public void whenMergingNullSearchOrder_doNothing() {
        so.add(mockCondition1);
        assertThat(so.getSearchConditions()).containsOnly(mockCondition1);

        so.merge(null);

        assertThat(so.getSearchConditions()).containsOnly(mockCondition1);
    }

    @Test
    public void whenMergingSearchOrderWithConditions_theResultIsMerged() {
        assertThat(so.getSearchConditions()).isEmpty();
        so.add(new SearchCondition());

        searchConditions.addAll(Arrays.asList(mockCondition1, mockCondition2));
        SearchOrder other = new SearchOrder(searchConditions);
        assertThat(other.getSearchConditions()).hasSize(2);

        so.merge(other);

        assertThat(so.getSearchConditions()).hasSize(3);
    }

    @Test
    public void whenRemovingSearchCondition_withNullParameter_doesNothing() {
        so.add(mockCondition1);
        so.add(mockCondition2);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition1, mockCondition2);

        so.remove(null);

        assertThat(so.getSearchConditions()).containsExactly(mockCondition1, mockCondition2);
    }

    @Test
    public void whenRemovingSearchCondition_withConditionWhichIsPresent_doesRemoveIt() {
        so.add(mockCondition1);
        so.add(mockCondition2);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition1, mockCondition2);

        so.remove(mockCondition2);

        assertThat(so.getSearchConditions()).containsExactly(mockCondition1);
    }

    @Test
    public void whenRemovingCondition_withConditionWhichIsNotPresent_doesNothing() {
        so.add(mockCondition2);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition2);

        so.remove(mockCondition1);

        assertThat(so.getSearchConditions()).containsExactly(mockCondition2);
    }

    @Test
    public void testingToString_withNoConditions() {
        assertThat(so.getSearchConditions()).hasSize(0);
        assertThat(so.toString()).isEqualTo("SearchOrder[owner=1,global=false,searchConditions=[],id=10]");
    }

    @Test
    public void testingDisplayValue_withNoConditions_returnsIDOnly() {
        assertThat(so.getSearchConditions()).hasSize(0);
        assertThat(so.getDisplayValue()).isEqualTo("(10)");
    }

    @Test
    public void testingDisplayValue_forGlobalCSearchOrderWithNoConditions_returnsIdPlusGlobalIndicator() {
        assertThat(so.getSearchConditions()).hasSize(0);
        so.setGlobal(true);
        assertThat(so.getDisplayValue()).isEqualTo("(10)*");
    }

    @Test
    public void testingDisplayValue_withSingleCondition_returnsIt() {
        so.add(new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "f1ToString";
            }
        });

        assertThat(so.getDisplayValue()).isEqualTo("f1ToString (10)");
    }

    @Test
    public void testingDisplayValue_withTwoConditions_joinsThemUsingOR() {
        so.add(new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "c1ToString";
            }
        });
        so.add(new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "c2ToString";
            }
        });

        assertThat(so.getDisplayValue()).isEqualTo("c1ToString; OR c2ToString (10)");
    }

    @Test
    public void cannotAddTheSameSearchTermTwice() {
        SearchCondition c1 = new SearchCondition();
        c1.setAuthors("baz");
        c1.setTitle("foo");
        c1.setPublicationYear("2016");
        c1.setFirstAuthorOverridden(true);
        SearchCondition c2 = new SearchCondition();
        c2.setAuthors("baz");
        c2.setTitle("foo");
        c2.setPublicationYear("2016");
        c2.setFirstAuthorOverridden(true);

        assertThat(so.getSearchConditions()).hasSize(0);
        so.add(c1);
        assertThat(so.getSearchConditions()).hasSize(1);
        so.add(c2);
        assertThat(so.getSearchConditions()).hasSize(1);
    }
}