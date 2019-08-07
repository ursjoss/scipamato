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
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.web.AbstractWicketTest;

class NewsletterTopicDefinitionProviderTest extends AbstractWicketTest {

    private NewsletterTopicDefinitionProvider provider;

    @Mock
    private NewsletterTopicFilter     filterMock;
    @Mock
    private NewsletterTopicDefinition entityMock;

    private List<NewsletterTopicDefinition> papers;

    @BeforeEach
    void setUp() {
        new WicketTester(application);
        provider = new NewsletterTopicDefinitionProvider(filterMock);

        papers = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(newsletterTopicServiceMock, entityMock);
    }

    @Test
    void defaultFilterIsNewNewsletterTopicFilter() {
        provider = new NewsletterTopicDefinitionProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new NewsletterTopicFilter());
    }

    @Test
    void nullFilterResultsInNewNewsletterTopicFilter() {
        NewsletterTopicDefinitionProvider p = new NewsletterTopicDefinitionProvider(null);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new NewsletterTopicFilter());
    }

    @Test
    void size() {
        int size = 5;
        when(newsletterTopicServiceMock.countByFilter(filterMock)).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(newsletterTopicServiceMock).countByFilter(filterMock);
    }

    @Test
    void gettingModel_wrapsEntity() {
        IModel<NewsletterTopicDefinition> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    void settingFilterState() {
        provider = new NewsletterTopicDefinitionProvider();
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
        when(newsletterTopicServiceMock.findPageOfEntityDefinitions(eq(filterMock),
            isA(PaginationContext.class))).thenReturn(papers.iterator());
        Iterator<NewsletterTopicDefinition> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(newsletterTopicServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new NewsletterTopicDefinitionProviderTest.PaginationContextMatcher(3, "title: ASC")));
    }

    @Test
    void iterating_throughFirst() {
        when(newsletterTopicServiceMock.findPageOfEntityDefinitions(eq(filterMock),
            isA(PaginationContext.class))).thenReturn(papers.iterator());
        Iterator<NewsletterTopicDefinition> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(newsletterTopicServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new NewsletterTopicDefinitionProviderTest.PaginationContextMatcher(3, "title: ASC")));
    }

    private void assertRecordsIn(Iterator<NewsletterTopicDefinition> it) {
        int i = 0;
        while (i++ < 3) {
            assertThat(it.hasNext()).isTrue();
            it.next();
        }
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void iterating_throughSecondPage() {
        when(newsletterTopicServiceMock.findPageOfEntityDefinitions(eq(filterMock),
            isA(PaginationContext.class))).thenReturn(papers.iterator());
        Iterator<NewsletterTopicDefinition> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(newsletterTopicServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new NewsletterTopicDefinitionProviderTest.PaginationContextMatcher(3, "title: ASC")));
    }

    @Test
    void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(newsletterTopicServiceMock.findPageOfEntityDefinitions(eq(filterMock),
            isA(PaginationContext.class))).thenReturn(papers.iterator());
        Iterator<NewsletterTopicDefinition> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(newsletterTopicServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new NewsletterTopicDefinitionProviderTest.PaginationContextMatcher(3, "title: DESC")));
    }

}