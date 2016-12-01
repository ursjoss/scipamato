package ch.difty.sipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.ComplexPaperFilter;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class ComplexPaperFilterProviderTest {

    private ComplexPaperFilterProvider provider;

    @Mock
    private SearchOrder mockSearchOrder;

    @Mock
    private ComplexPaperFilter mockFilter1, mockFilter2;

    private final List<ComplexPaperFilter> filters = new ArrayList<>();

    @Before
    public void setUp() {
        filters.addAll(Arrays.asList(mockFilter1, mockFilter2));
        when(mockSearchOrder.getFilters()).thenReturn(filters);

        provider = new ComplexPaperFilterProvider(Model.of(mockSearchOrder));
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel() {
        try {
            new ComplexPaperFilterProvider(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrderModel must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel1() {
        try {
            new ComplexPaperFilterProvider(Model.of((SearchOrder) null));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder.filters must not be null.");
        }
    }

    @Test
    public void providerSize_equals_complexPaperFilterSize() {
        assertThat(provider.size()).isEqualTo(filters.size());
    }

    @Test
    public void iterator_from0ToGreaterThanActualSize_returnsAll() {
        assertThat(provider.iterator(0, 100)).containsExactly(mockFilter1, mockFilter2);
    }

    @Test
    public void iterator_from0ToLessThanActualSize_stillReturnsAll_TODO() {
        // paging not yet implemented
        assertThat(provider.iterator(0, 1)).containsExactly(mockFilter1, mockFilter2);
    }

    @Test
    public void iterator_from1ToActualSize_stillReturnsAll_TODO() {
        // paging not yet implemented
        assertThat(provider.iterator(1, 2)).containsExactly(mockFilter1, mockFilter2);
    }

    @Test
    public void gettingModel() {
        IModel<ComplexPaperFilter> model = provider.model(mockFilter1);
        assertThat(model.getObject()).isEqualTo(mockFilter1);
    }
}
