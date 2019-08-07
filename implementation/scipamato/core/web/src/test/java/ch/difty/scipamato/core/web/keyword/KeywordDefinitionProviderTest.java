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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.web.AbstractWicketTest;

class KeywordDefinitionProviderTest extends AbstractWicketTest {

    private KeywordDefinitionProvider provider;

    @Mock
    private KeywordFilter     filterMock;
    @Mock
    private KeywordDefinition entityMock;

    private List<KeywordDefinition> papers;

    @BeforeEach
    void setUp() {
        new WicketTester(application);
        provider = new KeywordDefinitionProvider(filterMock);

        papers = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(keywordServiceMock, entityMock);
    }

    @Test
    void defaultFilterIsNewKeywordFilter() {
        provider = new KeywordDefinitionProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new KeywordFilter());
    }

    @Test
    void nullFilterResultsInNewKeywordFilter() {
        KeywordDefinitionProvider p = new KeywordDefinitionProvider(null);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new KeywordFilter());
    }

    @Test
    void size() {
        int size = 5;
        when(keywordServiceMock.countByFilter(filterMock)).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(keywordServiceMock).countByFilter(filterMock);
    }

    @Test
    void gettingModel_wrapsEntity() {
        IModel<KeywordDefinition> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    void settingFilterState() {
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
    void iterating_withNoRecords_returnsNoRecords() {
        papers = Collections.emptyList();
        when(keywordServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(keywordServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new KeywordDefinitionProviderTest.PaginationContextMatcher(3, "name: ASC")));
    }

    @Test
    void iterating_throughFirst() {
        when(keywordServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(keywordServiceMock).findPageOfEntityDefinitions(eq(filterMock),
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
    void iterating_throughSecondPage() {
        when(keywordServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(keywordServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new KeywordDefinitionProviderTest.PaginationContextMatcher(3, "name: ASC")));
    }

    @Test
    void iterating_throughThirdPage() {
        provider.setSort("name", SortOrder.DESCENDING);
        when(keywordServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            papers.iterator());
        Iterator<KeywordDefinition> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(keywordServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new KeywordDefinitionProviderTest.PaginationContextMatcher(3, "name: DESC")));
    }

}