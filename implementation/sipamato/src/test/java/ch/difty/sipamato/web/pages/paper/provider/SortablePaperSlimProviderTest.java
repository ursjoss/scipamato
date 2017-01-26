package ch.difty.sipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.paper.slim.JooqPaperSlimService;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class SortablePaperSlimProviderTest<F extends PaperSlimFilter, P extends SortablePaperSlimProvider<F>> {

    protected static final int PAGE_SIZE = 3;

    protected P provider;

    @Mock
    protected JooqPaperSlimService serviceMock;

    @Mock
    protected PaperSlim entityMock;

    @Autowired
    protected SipamatoApplication application;

    protected abstract F getFilter();

    protected Page<PaperSlim> pageOfPapers;

    @Before
    public final void setUp() {
        new WicketTester(application);
        provider = newProvider();
        provider.setService(serviceMock);

        pageOfPapers = new PageImpl<PaperSlim>(Arrays.asList(entityMock, entityMock, entityMock));

        localFixture();
    }

    protected abstract void localFixture();

    @After
    public final void tearDown() {
        verifyNoMoreInteractions(serviceMock, getFilter(), entityMock);
    }

    protected abstract P newProvider();

    @Test
    public void gettingModel_wrapsEntity() {
        IModel<PaperSlim> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    public void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(getFilter());
    }

    class PageableMatcher extends ArgumentMatcher<Pageable> {

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
        // reset the service mock
        pageOfPapers = new PageImpl<PaperSlim>(Arrays.asList());
        localFixture();

        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verifyFilterMock(new PageableMatcher(0, 3, "authors: ASC"));
    }

    protected abstract void verifyFilterMock(PageableMatcher matcher);

    @Test
    public void iterating_throughFirstFullPage() {
        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PageableMatcher(0, 3, "authors: ASC"));
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
    public void iterating_throughSecondFullPage() {
        Iterator<PaperSlim> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PageableMatcher(1, 3, "authors: ASC"));
    }

    @Test
    public void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        Iterator<PaperSlim> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PageableMatcher(2, 3, "title: DESC"));
    }

    @Test
    public void instantiationWithPageSize_returnsGivenPageSize() {
        assertThat(provider.getRowsPerPage()).isEqualTo(PAGE_SIZE);
    }

    @Test
    public void iterating_withDefaultPageSize_throughFirstFullPage_withPageSizeMatchingActualSize() {
        int actualSize = 3;
        assertThat(actualSize).isEqualTo(PAGE_SIZE);

        Iterator<PaperSlim> it = provider.iterator(0, actualSize);
        assertRecordsIn(it);
        verifyFilterMock(new PageableMatcher(0, PAGE_SIZE, "authors: ASC"));
    }

    @Test
    public void iterating_withDefaultPageSize_throughThirdNotFullPage_withPageSizeHigherThanActualSize() {
        int actualSize = 2;
        assertThat(actualSize).isLessThan(PAGE_SIZE);

        provider.setSort("title", SortOrder.DESCENDING);

        Iterator<PaperSlim> it = provider.iterator(6, actualSize);
        assertRecordsIn(it);
        verifyFilterMock(new PageableMatcher(2, PAGE_SIZE, "title: DESC"));
    }

}
