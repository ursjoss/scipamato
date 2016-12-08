package ch.difty.sipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.persistance.jooq.paper.slim.JooqPaperSlimService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterBasedSortablePaperSlimProviderTest {

    private FilterBasedSortablePaperSlimProvider provider;

    @Mock
    private JooqPaperSlimService serviceMock;

    @Mock
    private PaperFilter filterMock;

    @Mock
    private PaperSlim entityMock;

    @Autowired
    private SipamatoApplication application;

    private final List<PaperSlim> papers = new ArrayList<>();

    @Before
    public void setUp() {
        new WicketTester(application);
        provider = new FilterBasedSortablePaperSlimProvider(filterMock);
        provider.setService(serviceMock);

        papers.addAll(Arrays.asList(entityMock, entityMock, entityMock));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, filterMock, entityMock);
    }

    @Test
    public void defaultFilterIsNewPaperFilter() {
        provider = new FilterBasedSortablePaperSlimProvider();
        assertThat(provider.getFilterState()).isEqualToComparingFieldByField(new PaperFilter());
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
        IModel<PaperSlim> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    public void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
    }

    @Test
    public void settingFilterState() {
        provider = new FilterBasedSortablePaperSlimProvider();
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
        papers.clear();
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(papers);
        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(0, 3, "authors: ASC")));
    }

    @Test
    public void iterating_throughFirst() {
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(papers);
        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(0, 3, "authors: ASC")));
    }

    private void assertRecordsIn(Iterator<PaperSlim> it) {
        int i = 0;
        while (i++ < 3) {
            assertThat(it.hasNext()).isTrue();
            it.next();
        }
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void iterating_throughSeconPage() {
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(papers);
        Iterator<PaperSlim> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(1, 3, "authors: ASC")));
    }

    @Test
    public void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(serviceMock.findByFilter(eq(filterMock), isA(Pageable.class))).thenReturn(papers);
        Iterator<PaperSlim> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verify(serviceMock).findByFilter(eq(filterMock), argThat(new PageableMatcher(2, 3, "title: DESC")));
    }
}
