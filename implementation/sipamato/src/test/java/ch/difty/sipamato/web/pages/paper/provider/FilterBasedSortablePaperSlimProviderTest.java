package ch.difty.sipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

public class FilterBasedSortablePaperSlimProviderTest extends SortablePaperSlimProviderTest<PaperFilter, FilterBasedSortablePaperSlimProvider> {

    @Mock
    private PaperFilter filterMock;

    @Override
    protected void localFixture() {
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(pageOfSlimPapers);
    }

    @Override
    protected PaperFilter getFilter() {
        return filterMock;
    }

    @Override
    protected FilterBasedSortablePaperSlimProvider newProvider() {
        return new FilterBasedSortablePaperSlimProvider(getFilter(), PAGE_SIZE);
    }

    @Override
    protected void verifyFilterMock(PageableMatcher matcher) {
        verify(serviceMock).findByFilter(eq(filterMock), argThat(matcher));
    }

    @Test
    public void size() {
        int size = 5;
        when(serviceMock.countByFilter(getFilter())).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(serviceMock).countByFilter(getFilter());
    }

    @Test
    public void settingFilterState() {
        PaperFilter filterMock2 = Mockito.mock(PaperFilter.class);
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
        provider.setFilterState(filterMock2);
        assertThat(provider.getFilterState()).isEqualTo(filterMock2);
    }

    @Test
    public void gettingAllPapersByFilter() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(paperServiceMock.findByFilter(eq(getFilter()), argThat(new PageableMatcher(0, Integer.MAX_VALUE, "title: DESC")))).thenReturn(pageOfPapers);
        List<Paper> papers = provider.findAllPapersByFilter();
        assertThat(papers).hasSize(5);
        assertThat(papers).containsOnly(paperMock);
        verify(paperServiceMock).findByFilter(eq(getFilter()), argThat(new PageableMatcher(0, Integer.MAX_VALUE, "title: DESC")));
    }

}
