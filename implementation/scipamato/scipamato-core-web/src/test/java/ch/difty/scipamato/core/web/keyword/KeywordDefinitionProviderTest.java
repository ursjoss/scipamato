package ch.difty.scipamato.core.web.keyword;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.ScipamatoCoreApplication;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeywordDefinitionProviderTest {

    private KeywordDefinitionProvider provider;

    @Autowired
    private ScipamatoCoreApplication application;

    @MockBean
    private KeywordService serviceMock;

    @Mock
    private KeywordFilter filterMock;

    @Mock
    private KeywordDefinition entityMock;

    private List<KeywordDefinition> papers;

    @Before
    public void setUp() {
        new WicketTester(application);
        provider = new KeywordDefinitionProvider(filterMock);

        papers = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, entityMock);
    }

    @Test
    public void defaultFilterIsNewKeywordFilter() {
        provider = new KeywordDefinitionProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new KeywordFilter());
    }

    @Test
    public void nullFilterResultsInNewKeywordFilter() {
        KeywordDefinitionProvider p = new KeywordDefinitionProvider(null);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new KeywordFilter());
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
        IModel<KeywordDefinition> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    public void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    public void settingFilterState() {
        provider = new KeywordDefinitionProvider();
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
    public void iterating_withNoRecords_returnsNoRecords() {
        papers = Collections.emptyList();
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new KeywordDefinitionProviderTest.PaginationContextMatcher(3, "name: ASC")));
    }

    @Test
    public void iterating_throughFirst() {
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new KeywordDefinitionProviderTest.PaginationContextMatcher(3, "name: ASC")));
    }

    private void assertRecordsIn(Iterator<KeywordDefinition> it) {
        int i = 0;
        while (i++ < 3) {
            assertThat(it.hasNext()).isTrue();
            it.next();
        }
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void iterating_throughSecondPage() {
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new KeywordDefinitionProviderTest.PaginationContextMatcher(3, "name: ASC")));
    }

    @Test
    public void iterating_throughThirdPage() {
        provider.setSort("name", SortOrder.DESCENDING);
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new KeywordDefinitionProviderTest.PaginationContextMatcher(3, "name: DESC")));
    }

}