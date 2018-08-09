package ch.difty.scipamato.core.web.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.ScipamatoCoreApplication;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsletterTopicDefinitionProviderTest {

    private NewsletterTopicDefinitionProvider provider;

    @Mock
    private NewsletterTopicService serviceMock;

    @Mock
    private NewsletterTopicFilter filterMock;

    @Mock
    private NewsletterTopicDefinition entityMock;

    @Autowired
    private ScipamatoCoreApplication application;

    private List<NewsletterTopicDefinition> papers;

    @Before
    public void setUp() {
        new WicketTester(application);
        provider = new NewsletterTopicDefinitionProvider(filterMock);
        provider.setService(serviceMock);

        papers = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, entityMock);
    }

    @Test
    public void defaultFilterIsNewNewsletterTopicFilter() {
        provider = new NewsletterTopicDefinitionProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new NewsletterTopicFilter());
    }

    @Test
    public void nullFilterResultsInNewNewsletterTopicFilter() {
        NewsletterTopicDefinitionProvider p = new NewsletterTopicDefinitionProvider(null);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new NewsletterTopicFilter());
    }

    @Test
    public void size() {
        int size = 5;
        when(serviceMock.countByFilter(filterMock)).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(serviceMock).countByFilter(filterMock);
    }

    @Test
    public void gettingModel_wrapsEntity() {
        IModel<NewsletterTopicDefinition> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    public void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    public void settingFilterState() {
        provider = new NewsletterTopicDefinitionProvider();
        assertThat(provider.getFilterState()).isNotEqualTo(filterMock);
        provider.setFilterState(filterMock);
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    private class PaginationContextMatcher implements ArgumentMatcher<PaginationContext> {

        private final int    pageSize;
        private final String sort;

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
    public void iterating_withNoRecords_returnsNoRecords() {
        papers = Collections.emptyList();
        when(serviceMock.findPageOfNewsletterTopicDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers);
        Iterator<NewsletterTopicDefinition> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(serviceMock).findPageOfNewsletterTopicDefinitions(eq(filterMock),
            argThat(new NewsletterTopicDefinitionProviderTest.PaginationContextMatcher(3, "title: ASC")));
    }

    @Test
    public void iterating_throughFirst() {
        when(serviceMock.findPageOfNewsletterTopicDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers);
        Iterator<NewsletterTopicDefinition> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfNewsletterTopicDefinitions(eq(filterMock),
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
    public void iterating_throughSecondPage() {
        when(serviceMock.findPageOfNewsletterTopicDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers);
        Iterator<NewsletterTopicDefinition> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfNewsletterTopicDefinitions(eq(filterMock),
            argThat(new NewsletterTopicDefinitionProviderTest.PaginationContextMatcher(3, "title: ASC")));
    }

    @Test
    public void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(serviceMock.findPageOfNewsletterTopicDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers);
        Iterator<NewsletterTopicDefinition> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfNewsletterTopicDefinitions(eq(filterMock),
            argThat(new NewsletterTopicDefinitionProviderTest.PaginationContextMatcher(3, "title: DESC")));
    }

}