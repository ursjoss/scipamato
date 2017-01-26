package ch.difty.sipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SearchOrder;

public class SearchOrderBasedSortablePaperSlimProviderTest extends SortablePaperSlimProviderTest<SearchOrder, SearchOrderBasedSortablePaperSlimProvider> {

    @Mock
    private SearchOrder searchOrder;

    @Override
    protected void localFixture() {
        when(serviceMock.findBySearchOrder(eq(searchOrder), isA(Pageable.class))).thenReturn(pageOfPapers);
    }

    @Override
    protected SearchOrder getFilter() {
        return searchOrder;
    }

    @Override
    protected SearchOrderBasedSortablePaperSlimProvider newProvider() {
        return new SearchOrderBasedSortablePaperSlimProvider(getFilter(), PAGE_SIZE);
    }

    @Override
    protected void verifyFilterMock(PageableMatcher matcher) {
        verify(serviceMock).findBySearchOrder(eq(searchOrder), argThat(matcher));
    }

    @Test
    public void size() {
        int size = 5;
        when(serviceMock.countBySearchOrder(getFilter())).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(serviceMock).countBySearchOrder(getFilter());
    }

    @Test
    public void settingFilterState() {
        SearchOrder searchOrder2 = Mockito.mock(SearchOrder.class);
        assertThat(provider.getFilterState()).isEqualTo(searchOrder);
        provider.setFilterState(searchOrder2);
        assertThat(provider.getFilterState()).isEqualTo(searchOrder2);
    }
}
