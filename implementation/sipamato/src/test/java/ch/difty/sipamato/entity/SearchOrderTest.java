package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.ComplexPaperFilter;

@RunWith(MockitoJUnitRunner.class)
public class SearchOrderTest {

    private final SearchOrder so = new SearchOrder(10l, 1, false, null);

    @Mock
    public ComplexPaperFilter mockFilter1, mockFilter2;

    private List<ComplexPaperFilter> filters = new ArrayList<>();

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
    public void whenInstantiating_withNullList_noFiltersArePresent() {
        assertThat(so.getFilters()).isEmpty();
    }

    @Test
    public void whenInstantiating_withEmptyList_noFiltersArePresent() {
        assertThat(new SearchOrder(filters).getFilters()).isEmpty();
    }

    @Test
    public void whenInstantiating_withNonEmptyList_handedOverFiltersArePresent() {
        filters.addAll(Arrays.asList(mockFilter1, mockFilter2));
        assertThat(new SearchOrder(filters).getFilters()).containsExactly(mockFilter1, mockFilter2);
    }

    @Test
    public void whenAddingNullFilter_itIsNotAdded() {
        so.add(null);
        assertThat(so.getFilters()).isEmpty();
    }

    @Test
    public void whenAddingFilter_itIsGettingAdded() {
        so.add(mockFilter1);
        assertThat(so.getFilters()).containsExactly(mockFilter1);
    }

    @Test
    public void whenMergingNullSearchOrder_doNothing() {
        so.add(mockFilter1);
        assertThat(so.getFilters()).containsOnly(mockFilter1);

        so.merge(null);

        assertThat(so.getFilters()).containsOnly(mockFilter1);
    }

    @Test
    public void whenMergingCompositeFilterWithFilters_theResultIsMerged() {
        assertThat(so.getFilters()).isEmpty();
        so.add(new ComplexPaperFilter());

        filters.addAll(Arrays.asList(mockFilter1, mockFilter2));
        SearchOrder other = new SearchOrder(filters);
        assertThat(other.getFilters()).hasSize(2);

        so.merge(other);

        assertThat(so.getFilters()).hasSize(3);
    }

    @Test
    public void whenRemovingFilter_withNullParameter_doesNothing() {
        so.add(mockFilter1);
        so.add(mockFilter2);
        assertThat(so.getFilters()).containsExactly(mockFilter1, mockFilter2);

        so.remove(null);

        assertThat(so.getFilters()).containsExactly(mockFilter1, mockFilter2);
    }

    @Test
    public void whenRemovingFilter_withFilterWhichIsPresent_doesRemoveIt() {
        so.add(mockFilter1);
        so.add(mockFilter2);
        assertThat(so.getFilters()).containsExactly(mockFilter1, mockFilter2);

        so.remove(mockFilter2);

        assertThat(so.getFilters()).containsExactly(mockFilter1);
    }

    @Test
    public void whenRemovingFilter_withFilterWhichIsNotPresent_doesNothing() {
        so.add(mockFilter2);
        assertThat(so.getFilters()).containsExactly(mockFilter2);

        so.remove(mockFilter1);

        assertThat(so.getFilters()).containsExactly(mockFilter2);
    }

    @Test
    public void testingToString_withNoFilters() {
        assertThat(so.getFilters()).hasSize(0);
        assertThat(so.toString()).isEqualTo("SearchOrder[owner=1,global=false,filters=[],id=10]");
    }

    @Test
    public void testingDisplayValue_withNoFilters_returnsBlank() {
        assertThat(so.getFilters()).hasSize(0);
        assertThat(so.getDisplayValue()).isEqualTo("");
    }

    @Test
    public void testingDisplayValue_withSingleFilter_returnsIt() {
        so.add(new ComplexPaperFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "f1ToString";
            }
        });

        assertThat(so.getDisplayValue()).isEqualTo("f1ToString");
    }

    @Test
    public void testingDisplayValue_withTwoFilters_joinsThemUsingOR() {
        so.add(new ComplexPaperFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "f1ToString";
            }
        });
        so.add(new ComplexPaperFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "f2ToString";
            }
        });

        assertThat(so.getDisplayValue()).isEqualTo("f1ToString; OR f2ToString");
    }

    @Test
    public void cannotAddTheSameSearchTermTwice() {
        ComplexPaperFilter f1 = new ComplexPaperFilter();
        f1.setAuthors("baz");
        f1.setTitle("foo");
        f1.setPublicationYear("2016");
        f1.setFirstAuthorOverridden(true);
        ComplexPaperFilter f2 = new ComplexPaperFilter();
        f2.setAuthors("baz");
        f2.setTitle("foo");
        f2.setPublicationYear("2016");
        f2.setFirstAuthorOverridden(true);

        assertThat(so.getFilters()).hasSize(0);
        so.add(f1);
        assertThat(so.getFilters()).hasSize(1);
        so.add(f2);
        assertThat(so.getFilters()).hasSize(1);
    }
}