package ch.difty.scipamato.core.web.paper;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.core.entity.search.SearchCondition;

@RunWith(MockitoJUnitRunner.class)
public class SearchConditionProviderTest {

    private SearchConditionProvider provider;

    @Mock
    private SearchCondition mockCondition1, mockCondition2, mockCondition3, mockCondition4;

    private final List<SearchCondition> conditions = new ArrayList<>();

    @Before
    public void setUp() {
        conditions.addAll(Arrays.asList(mockCondition1, mockCondition2, mockCondition3, mockCondition4));

        provider = new SearchConditionProvider(Model.ofList(conditions));
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel() {
        assertDegenerateSupplierParameter(() -> new SearchConditionProvider(null), "searchConditionsModel");
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel1() {
        List<SearchCondition> conditions = null;
        assertDegenerateSupplierParameter(() -> new SearchConditionProvider(Model.ofList(conditions)),
            "searchConditions");
    }

    @Test
    public void providerSize_equals_conditionSize() {
        assertThat(provider.size()).isEqualTo(conditions.size());
    }

    @Test
    public void iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        assertThat(provider.iterator(0, 100)).containsExactly(mockCondition1, mockCondition2, mockCondition3,
            mockCondition4);
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

}
