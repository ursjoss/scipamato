package ch.difty.scipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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

import ch.difty.scipamato.ScipamatoApplication;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.filter.PaperFilter;
import ch.difty.scipamato.persistance.jooq.paper.JooqPaperService;
import ch.difty.scipamato.persistence.paging.PaginationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperProviderTest {

    private PaperProvider provider;

    @Mock
    private JooqPaperService serviceMock;

    @Mock
    private PaperFilter filterMock;

    @Mock
    private Paper entityMock;

    @Autowired
    private ScipamatoApplication application;

    private List<Paper> papers;

    @Before
    public void setUp() {
        new WicketTester(application);
        provider = new PaperProvider(filterMock);
        provider.setService(serviceMock);

        papers = Arrays.asList(entityMock, entityMock, entityMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, filterMock, entityMock);
    }

    @Test
    public void defaultFilterIsNewPaperFilter() {
        provider = new PaperProvider();
        assertThat(provider.getFilter()).isEqualToComparingFieldByField(new PaperFilter());
    }

    @Test
    public void nullFilterResultsInNewPaperFilter() {
        PaperSlimByPaperFilterProvider p = new PaperSlimByPaperFilterProvider(null, 10);
        assertThat(p.getFilterState()).isEqualToComparingFieldByField(new PaperFilter());
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
        IModel<Paper> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    public void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    public void settingFilterState() {
        provider = new PaperProvider();
        assertThat(provider.getFilterState()).isNotEqualTo(filterMock);
        provider.setFilterState(filterMock);
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    private class PaginationContextMatcher extends ArgumentMatcher<PaginationContext> {

        private final int pageSize;
        private final String sort;

        PaginationContextMatcher(int pageSize, String sort) {
            this.pageSize = pageSize;
            this.sort = sort;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument != null && argument instanceof PaginationContext) {
                PaginationContext p = (PaginationContext) argument;
                return p.getPageSize() == pageSize && sort.equals(p.getSort().toString());
            }
            return false;
        }
    }

    @Test
    public void iterating_withNoRecords_returnsNoRecords() {
        papers = Arrays.asList();
        when(serviceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(serviceMock).findPageByFilter(eq(filterMock), argThat(new PaginationContextMatcher(3, "authors: ASC")));
    }

    @Test
    public void iterating_throughFirst() {
        when(serviceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageByFilter(eq(filterMock), argThat(new PaginationContextMatcher(3, "authors: ASC")));
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
    public void iterating_throughSecondPage() {
        when(serviceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageByFilter(eq(filterMock), argThat(new PaginationContextMatcher(3, "authors: ASC")));
    }

    @Test
    public void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(serviceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
        Iterator<Paper> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(serviceMock).findPageByFilter(eq(filterMock), argThat(new PaginationContextMatcher(3, "title: DESC")));
    }
}
