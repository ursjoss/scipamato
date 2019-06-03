package ch.difty.scipamato.core.web.codeclass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.ScipamatoCoreApplication;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CodeClassDefinitionProviderTest {
    private CodeClassDefinitionProvider provider;

    @MockBean
    private CodeClassService serviceMock;

    @Mock
    private CodeClassFilter filterMock;

    @Mock
    private CodeClassDefinition entityMock;

    private List<CodeClassDefinition> codeClasses;

    @Autowired
    private ScipamatoCoreApplication application;

    @BeforeEach
    final void setUp() {
        new WicketTester(application);
        provider = new CodeClassDefinitionProvider(filterMock);
        codeClasses = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(serviceMock, entityMock);
    }

    @Test
    void defaultFilterIsNewCodeClassFilter() {
        provider = new CodeClassDefinitionProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new CodeClassFilter());
    }

    @Test
    void nullFilterResultsInNewCodeClassFilter() {
        CodeClassDefinitionProvider p = new CodeClassDefinitionProvider(null);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new CodeClassFilter());
    }

    @Test
    void size() {
        int size = 5;
        when(serviceMock.countByFilter(filterMock)).thenReturn(size);
        assertThat(provider.size()).isEqualTo(size);
        verify(serviceMock).countByFilter(filterMock);
    }

    @Test
    void gettingModel_wrapsEntity() {
        IModel<CodeClassDefinition> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    void settingFilterState() {
        provider = new CodeClassDefinitionProvider();
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
        codeClasses = Collections.emptyList();
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codeClasses.iterator());
        Iterator<CodeClassDefinition> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "id: ASC")));
    }

    @Test
    void iterating_throughFirst() {
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codeClasses.iterator());
        Iterator<CodeClassDefinition> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "id: ASC")));
    }

    private void assertRecordsIn(Iterator<CodeClassDefinition> it) {
        int i = 0;
        while (i++ < 3) {
            assertThat(it.hasNext()).isTrue();
            it.next();
        }
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void iterating_throughSecondPage() {
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codeClasses.iterator());
        Iterator<CodeClassDefinition> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new PaginationContextMatcher(3, "id: ASC")));
    }

    @Test
    void iterating_throughThirdPage() {
        provider.setSort("id", SortOrder.DESCENDING);
        when(serviceMock.findPageOfEntityDefinitions(eq(filterMock), isA(PaginationContext.class))).thenReturn(
            codeClasses.iterator());
        Iterator<CodeClassDefinition> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageOfEntityDefinitions(eq(filterMock),
            argThat(new CodeClassDefinitionProviderTest.PaginationContextMatcher(3, "id: DESC")));
    }
}