package ch.difty.scipamato.core.web.newsletter;

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
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.web.AbstractWicketTest;

class NewsletterProviderTest extends AbstractWicketTest {

    private NewsletterProvider provider;

    @Mock
    private NewsletterFilter filterMock;
    @Mock
    private Newsletter       entityMock;

    private List<Newsletter> papers;

    @BeforeEach
    void setUp() {
        new WicketTester(application);
        provider = new NewsletterProvider(filterMock);
        provider.setService(newsletterServiceMock);

        papers = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(newsletterServiceMock, entityMock);
    }

    @Test
    void defaultFilterIsNewNewsletterFilter() {
        provider = new NewsletterProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new NewsletterFilter());
    }

    @Test
    void nullFilterResultsInNewNewsletterFilter() {
        NewsletterProvider p = new NewsletterProvider(null);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new NewsletterFilter());
    }

    @Test
    void size() {
        int size = 5;
        when(newsletterServiceMock.countByFilter(filterMock)).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(newsletterServiceMock).countByFilter(filterMock);
    }

    @Test
    void gettingModel_wrapsEntity() {
        IModel<Newsletter> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    void settingFilterState() {
        provider = new NewsletterProvider();
        assertThat(provider.getFilterState()).isNotEqualTo(filterMock);
        provider.setFilterState(filterMock);
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    private static class PaginationContextMatcher implements ArgumentMatcher<PaginationContext> {

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
        when(newsletterServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Newsletter> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(newsletterServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "issue: DESC")));
    }

    @Test
    void iterating_throughFirst() {
        when(newsletterServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Newsletter> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(newsletterServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "issue: DESC")));
    }

    private void assertRecordsIn(Iterator<Newsletter> it) {
        int i = 0;
        while (i++ < 3) {
            assertThat(it.hasNext()).isTrue();
            it.next();
        }
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void iterating_throughSecondPage() {
        when(newsletterServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Newsletter> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(newsletterServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "issue: DESC")));
    }

    @Test
    void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(newsletterServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Newsletter> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(newsletterServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "title: DESC")));
    }

    @Test
    void iterating_throughThirdPage_ascendingly() {
        provider.setSort("title", SortOrder.ASCENDING);
        when(newsletterServiceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Newsletter> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(newsletterServiceMock).findPageByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "title: ASC")));
    }

}