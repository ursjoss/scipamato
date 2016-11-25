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
}