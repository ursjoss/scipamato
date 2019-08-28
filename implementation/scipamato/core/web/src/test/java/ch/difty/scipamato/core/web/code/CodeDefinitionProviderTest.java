package ch.difty.scipamato.core.web.code;

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
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.web.AbstractWicketTest;

class CodeDefinitionProviderTest extends AbstractWicketTest {

    private CodeDefinitionProvider provider;

    @Mock
    private CodeFilter     filterMock;
    @Mock
    private CodeDefinition entityMock;

    private List<CodeDefinition> codes;

    @BeforeEach
    final void setUp() {
        new WicketTester(application);
        provider = new CodeDefinitionProvider(filterMock);
        codes = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(codeServiceMock, entityMock);
    }

    @Test
    void defaultFilterIsNewCodeFilter() {
        provider = new CodeDefinitionProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new CodeFilter());
    }

    @Test
    void nullFilterResultsInNewCodeFilter() {
        CodeDefinitionProvider p = new CodeDefinitionProvider(null);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new CodeFilter());
    }

    @Test
    void size() {
        int size = 5;
        when(codeServiceMock.countByFilter(filterMock)).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(codeServiceMock).countByFilter(filterMock);
    }

    @Test
    void gettingModel_wrapsEntity() {
        IModel<CodeDefinition> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    void settingFilterState() {
        provider = new CodeDefinitionProvider();
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
        codes = Collections.emptyList();
        when(codeServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codes.iterator());
        Iterator<CodeDefinition> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(codeServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new CodeDefinitionProviderTest.PaginationContextMatcher(3, "sort: ASC")));
    }

    @Test
    void iterating_throughFirst() {
        when(codeServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codes.iterator());
        Iterator<CodeDefinition> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(codeServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new CodeDefinitionProviderTest.PaginationContextMatcher(3, "sort: ASC")));
    }

    private void assertRecordsIn(Iterator<CodeDefinition> it) {
        int i = 0;
        while (i++ < 3) {
            assertThat(it.hasNext()).isTrue();
            it.next();
        }
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void iterating_throughSecondPage() {
        when(codeServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codes.iterator());
        Iterator<CodeDefinition> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(codeServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new CodeDefinitionProviderTest.PaginationContextMatcher(3, "sort: ASC")));
    }

    @Test
    void iterating_throughThirdPage() {
        provider.setSort("sort", SortOrder.DESCENDING);
        when(codeServiceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codes.iterator());
        Iterator<CodeDefinition> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(codeServiceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new CodeDefinitionProviderTest.PaginationContextMatcher(3, "sort: DESC")));
    }

}