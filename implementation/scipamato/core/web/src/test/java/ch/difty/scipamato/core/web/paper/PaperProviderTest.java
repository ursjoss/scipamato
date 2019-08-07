package ch.difty.scipamato.core.web.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.web.AbstractWicketTest;

class PaperProviderTest extends AbstractWicketTest {

    private PaperProvider provider;

    @Mock
    private PaperFilter filterMock;
    @Mock
    private Paper       entityMock;

    private List<Paper> papers;

    @BeforeEach
    void setUp() {
        new WicketTester(application);
        provider = new PaperProvider(filterMock);
        provider.setService(paperServiceMock);

        papers = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(paperServiceMock, entityMock);
    }

    @Test
    void defaultFilterIsNewPaperFilter() {
        provider = new PaperProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new PaperFilter());
    }

    @Test
    void nullFilterResultsInNewPaperFilter() {
        PaperSlimByPaperFilterProvider p = new PaperSlimByPaperFilterProvider(null, 10);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new PaperFilter());
    }

    @Test
    void size() {
        int size = 5;
        when(paperServiceMock.countByFilter(filterMock)).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(paperServiceMock).countByFilter(filterMock);
    }

    @Test
    void gettingModel_wrapsEntity() {
        IModel<Paper> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    void settingFilterState() {
        provider = new PaperProvider();
        assertThat(provider.getFilterState()).isNotEqualTo(filterMock);
        provider.setFilterState(filterMock);
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    private class PaginationContextMatcher implements ArgumentMatcher<PaginationContext> {

        private final int    pageSize;
        private final String sort;

        @SuppressWarnings("SameParameterValue")
        PaginationContextMatcher(int pageSize, String sort) {
            this.pageSize = pageSize;
            this.sort = sort;
        }

        @Override
        public boolean matches(PaginationContext p) {
            return p != null && p.getPageSize() == pageSize && sort.equals(p
                .getSort()
                .toString());
        }
    }

    @Test
    void iterating_withNoRecords_returnsNoRecords() {
        papers = Collections.emptyList();
        when(paperServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(paperServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "authors: ASC")));
    }

    @Test
    void iterating_throughFirst() {
        when(paperServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(paperServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "authors: ASC")));
    }

    private void assertRecordsIn(Iterator<Paper> it) {
        int i = 0;
        while (i++ < 3) {
            assertThat(it.hasNext()).isTrue();
            it.next();
        }
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void iterating_throughSecondPage() {
        when(paperServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(paperServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "authors: ASC")));
    }

    @Test
    void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(paperServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(paperServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "title: DESC")));
    }
}
