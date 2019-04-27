package ch.difty.scipamato.core.web.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationContextMatcher;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.search.SearchOrder;

@SuppressWarnings("ResultOfMethodCallIgnored")
class PaperSlimBySearchOrderProviderTest
    extends AbstractPaperSlimProviderTest<SearchOrder, PaperSlimBySearchOrderProvider> {

    private static final String LC = "en";

    @Mock
    private SearchOrder searchOrder;

    @Override
    protected void localFixture() {
        when(serviceMock.findPageBySearchOrder(eq(searchOrder), isA(PaginationContext.class))).thenReturn(
            pageOfSlimPapers);
    }

    @Override
    SearchOrder getFilter() {
        return searchOrder;
    }

    @Override
    protected PaperSlimBySearchOrderProvider newProvider() {
        return new PaperSlimBySearchOrderProvider(getFilter(), PAGE_SIZE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected String getLanguageCode() {
                return LC;
            }
        };
    }

    @Override
    protected void verifyFilterMock(PaginationContextMatcher matcher) {
        verify(serviceMock).findPageBySearchOrder(eq(searchOrder), Mockito.argThat(matcher));
    }

    @Test
    void constructingWithNewFilter_usesEmptyFilter() {
        PaperSlimBySearchOrderProvider p = new PaperSlimBySearchOrderProvider(null, 10);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new SearchOrder());
    }

    @Test
    void size() {
        int size = 5;
        when(serviceMock.countBySearchOrder(getFilter())).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(serviceMock).countBySearchOrder(getFilter());
    }

    @Test
    void settingFilterState() {
        SearchOrder searchOrder2 = mock(SearchOrder.class);
        assertThat(provider.getFilterState()).isEqualTo(searchOrder);
        provider.setFilterState(searchOrder2);
        assertThat(provider.getFilterState()).isEqualTo(searchOrder2);
    }

    @Test
    void gettingAllPapersByFilter() {
        provider.setSort("authors", SortOrder.ASCENDING);
        when(paperServiceMock.findPageBySearchOrder(eq(getFilter()),
            Mockito.argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, "authors: ASC")), eq(LC))).thenReturn(
            pageOfPapers);
        List<Paper> papers = provider.findAllPapersByFilter();
        assertThat(papers).hasSize(5);
        assertThat(papers).containsOnly(paperMock);
        verify(paperServiceMock).findPageBySearchOrder(eq(getFilter()),
            Mockito.argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, "authors: ASC")), eq(LC));
    }

    @Test
    void findingAllPaperIds() {
        final List<Long> ids = Arrays.asList(3L, 18L, 6L);
        provider.setSort("authors", SortOrder.ASCENDING);
        when(paperServiceMock.findPageOfIdsBySearchOrder(eq(getFilter()),
            Mockito.argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, "authors: ASC")))).thenReturn(ids);
        List<Long> papers = provider.findAllPaperIdsByFilter();
        assertThat(papers).isEqualTo(ids);
        verify(paperServiceMock).findPageOfIdsBySearchOrder(eq(getFilter()),
            Mockito.argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, "authors: ASC")));
    }

    @Test
    void gettingSearchOrderId_passesModelId() {
        when(searchOrder.getId()).thenReturn(55L);
        assertThat(provider.getSearchOrderId()).isEqualTo(55L);
        verify(searchOrder).getId();
    }

    @Test
    void isShowingExclusion_ifTrueInFilter_returnsTrue() {
        assertShowExcluded(true);
    }

    @Test
    void isShowingExclusion_ifFalseInFilter_returnsFalse() {
        assertShowExcluded(false);
    }

    private void assertShowExcluded(boolean result) {
        when(searchOrder.isShowExcluded()).thenReturn(result);
        assertThat(provider.isShowExcluded()).isEqualTo(result);
        verify(searchOrder).isShowExcluded();
    }
}
