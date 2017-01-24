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
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class SearchConditionProviderTest {

    private SearchConditionProvider provider;

    @Mock
    private SearchOrder mockSearchOrder;

    @Mock
    private SearchCondition mockCondition1, mockCondition2, mockCondition3, mockCondition4;

    private final List<SearchCondition> conditions = new ArrayList<>();

    @Before
    public void setUp() {
        conditions.addAll(Arrays.asList(mockCondition1, mockCondition2, mockCondition3, mockCondition4));
        when(mockSearchOrder.getSearchConditions()).thenReturn(conditions);

        provider = new SearchConditionProvider(Model.of(mockSearchOrder));
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel() {
        try {
            new SearchConditionProvider(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrderModel must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel1() {
        try {
            new SearchConditionProvider(Model.of((SearchOrder) null));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder must not be null.");
        }
    }

    @Test
    public void providerSize_equals_conditionSize() {
        assertThat(provider.size()).isEqualTo(conditions.size());
    }

    @Test
    public void iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        assertThat(provider.iterator(0, 100)).containsExactly(mockCondition1, mockCondition2, mockCondition3, mockCondition4);
    }

    @Test
    public void iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        assertThat(provider.iterator(0, 2)).containsExactly(mockCondition1, mockCondition2);
    }

    @Test
    public void iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        assertThat(provider.iterator(1, 2)).containsExactly(mockCondition2, mockCondition3);
    }

    @Test
    public void gettingModel() {
        IModel<SearchCondition> model = provider.model(mockCondition1);
        assertThat(model.getObject()).isEqualTo(mockCondition1);
    }

    @Test
    public void settingModelObjectToNullAfterInstantiation_doesNotThrow() {
        Model<SearchOrder> searchOrderModel = Model.of(mockSearchOrder);
        assertThat(searchOrderModel.getObject()).isNotNull();
        SearchConditionProvider p = new SearchConditionProvider(searchOrderModel);
        assertThat(p.size()).isEqualTo(4l);

        searchOrderModel.setObject(null);

        assertThat(p.size()).isEqualTo(0l);
    }
}
