package ch.difty.scipamato.core.web.paper;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.entity.search.SearchCondition;

@ExtendWith(MockitoExtension.class)
class SearchConditionProviderTest {

    private SearchConditionProvider provider;

    @Mock
    private SearchCondition mockCondition1, mockCondition2, mockCondition3, mockCondition4;

    private final List<SearchCondition> conditions = new ArrayList<>();

    @BeforeEach
    void setUp() {
        conditions.addAll(Arrays.asList(mockCondition1, mockCondition2, mockCondition3, mockCondition4));

        provider = new SearchConditionProvider(Model.ofList(conditions));
    }

    @Test
    void degenerateConstruction_withNullSearchOrderModel() {
        assertDegenerateSupplierParameter(() -> new SearchConditionProvider(null), "searchConditionsModel");
    }

    @Test
    void degenerateConstruction_withNullSearchOrderModel1() {
        assertDegenerateSupplierParameter(() -> new SearchConditionProvider(Model.ofList(null)), "searchConditions");
    }

    @Test
    void providerSize_equals_conditionSize() {
        assertThat(provider.size()).isEqualTo(conditions.size());
    }

    @Test
    void iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        assertThat(provider.iterator(0, 100)).containsExactly(mockCondition1, mockCondition2, mockCondition3,
            mockCondition4);
    }

    @Test
    void iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        assertThat(provider.iterator(0, 2)).containsExactly(mockCondition1, mockCondition2);
    }

    @Test
    void iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        assertThat(provider.iterator(1, 2)).containsExactly(mockCondition2, mockCondition3);
    }

    @Test
    void gettingModel() {
        IModel<SearchCondition> model = provider.model(mockCondition1);
        assertThat(model.getObject()).isEqualTo(mockCondition1);
    }

}
