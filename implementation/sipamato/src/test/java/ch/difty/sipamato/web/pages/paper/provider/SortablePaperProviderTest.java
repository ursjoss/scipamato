package ch.difty.sipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;

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

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.paging.Page;
import ch.difty.sipamato.paging.PageImpl;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.paper.JooqPaperService;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SortablePaperProviderTest {

    private SortablePaperProvider provider;

    @Mock
    private JooqPaperService serviceMock;

    @Mock
    private PaperFilter filterMock;

    @Mock
    private Paper entityMock;

    @Autowired
    private SipamatoApplication application;

    private Page<Paper> pageOfPapers;

    @Before
    public void setUp() {
        new WicketTester(application);
        provider = new SortablePaperProvider(filterMock);
        provider.setService(serviceMock);

        pageOfPapers = new PageImpl<Paper>(Arrays.asList(entityMock, entityMock, entityMock));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, filterMock, entityMock);
    }

    @Test
    public void defaultFilterIsNewPaperFilter() {
        provider = new SortablePaperProvider();
        assertThat(provider.getFilter()).isEqualToComparingFieldByField(new PaperFilter());
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
        provider = new SortablePaperProvider();
        assertThat(provider.getFilterState()).isNotEqualTo(filterMock);
        provider.setFilterState(filterMock);
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    private class PageableMatcher extends ArgumentMatcher<Pageable> {

        private final int pageNumber, pageSize;
        private final String sort;

        PageableMatcher(int pageNumber, int pageSize, String sort) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.sort = sort;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument != null && argument instanceof Pageable) {
                Pageable p = (Pageable) argument;
                return p.getPageNumber() == pageNumber && p.getPageSize() == pageSize && sort.equals(p.getSort().toString());
            }
            return false;
        }
    }

    @Test
    public void iterating_withNoRecords_returnsNoRecords() {
        pageOfPapers = new PageImpl<Paper>(Arrays.asList());
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(pageOfPapers);
        Iterator<Paper> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(0, 3, "authors: ASC")));
    }

    @Test
    public void iterating_throughFirst() {
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(pageOfPapers);
        Iterator<Paper> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(0, 3, "authors: ASC")));
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
    public void iterating_throughSeconPage() {
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(pageOfPapers);
        Iterator<Paper> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(1, 3, "authors: ASC")));
    }

    @Test
    public void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(pageOfPapers);
        Iterator<Paper> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(2, 3, "title: DESC")));
    }
}
