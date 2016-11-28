package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompositeComplexPaperFilterTest {

    private final CompositeComplexPaperFilter f = new CompositeComplexPaperFilter(null);

    @Mock
    public ComplexPaperFilter mockFilter1, mockFilter2;

    private List<ComplexPaperFilter> filters = new ArrayList<>();

    @Test
    public void whenInstantiating_withNullList_noFiltersArePresent() {
        assertThat(f.getFilters()).isEmpty();
    }

    @Test
    public void whenInstantiating_withEmptyList_noFiltersArePresent() {
        assertThat(new CompositeComplexPaperFilter(filters).getFilters()).isEmpty();
    }

    @Test
    public void whenInstantiating_withNonEmptyList_handedOverFiltersArePresent() {
        filters.addAll(Arrays.asList(mockFilter1, mockFilter2));
        assertThat(new CompositeComplexPaperFilter(filters).getFilters()).containsExactly(mockFilter1, mockFilter2);
    }

    @Test
    public void whenAddingNullFilter_itIsNotAdded() {
        f.add(null);
        assertThat(f.getFilters()).isEmpty();
    }

    @Test
    public void whenAddingFilter_itIsGettingAdded() {
        f.add(mockFilter1);
        assertThat(f.getFilters()).containsExactly(mockFilter1);
    }

    @Test
    public void whenMergingCompositeFilterWithFilters_theResultIsMerged() {
        assertThat(f.getFilters()).isEmpty();
        f.add(new ComplexPaperFilter());

        filters.addAll(Arrays.asList(mockFilter1, mockFilter2));
        CompositeComplexPaperFilter other = new CompositeComplexPaperFilter(filters);
        assertThat(other.getFilters()).hasSize(2);

        f.merge(other);

        assertThat(f.getFilters()).hasSize(3);
    }

    @Test
    public void whenRemovingFilter_withNullParameter_doesNothing() {
        f.add(mockFilter1);
        f.add(mockFilter2);
        assertThat(f.getFilters()).containsExactly(mockFilter1, mockFilter2);

        f.remove(null);

        assertThat(f.getFilters()).containsExactly(mockFilter1, mockFilter2);
    }

    @Test
    public void whenRemovingFilter_withFilterWhichIsPresent_doesRemoveIt() {
        f.add(mockFilter1);
        f.add(mockFilter2);
        assertThat(f.getFilters()).containsExactly(mockFilter1, mockFilter2);

        f.remove(mockFilter2);

        assertThat(f.getFilters()).containsExactly(mockFilter1);
    }

    @Test
    public void whenRemovingFilter_withFilterWhichIsNotPresent_doesNothing() {
        f.add(mockFilter2);
        assertThat(f.getFilters()).containsExactly(mockFilter2);

        f.remove(mockFilter1);

        assertThat(f.getFilters()).containsExactly(mockFilter2);
    }

    @Test
    public void testingToString_withNoFilters_returnsBlank() {
        assertThat(f.getFilters()).hasSize(0);
        assertThat(f.toString()).isEqualTo("");
    }

    @Test
    public void testingToString_withSingleFilter_returnsIt() {
        f.add(new ComplexPaperFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "f1ToString";
            }
        });

        assertThat(f.toString()).isEqualTo("f1ToString");
    }

    @Test
    public void testingToString_withTwoFilters_joinsThemUsingOR() {
        f.add(new ComplexPaperFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "f1ToString";
            }
        });
        f.add(new ComplexPaperFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "f2ToString";
            }
        });

        assertThat(f.toString()).isEqualTo("f1ToString; OR f2ToString");
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

        assertThat(f.getFilters()).hasSize(0);
        f.add(f1);
        assertThat(f.getFilters()).hasSize(1);
        f.add(f2);
        assertThat(f.getFilters()).hasSize(1);
    }
}