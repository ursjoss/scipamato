package ch.difty.sipamato.web.pages.paper.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.persistance.jooq.paper.JooqPaperService;
import ch.difty.sipamato.persistance.jooq.paper.slim.JooqPaperSlimService;

@RunWith(SpringRunner.class)
@SpringBootTest
//uncomment the profile with which the jooq db was compiled for - but don't commit it as it may fail in maven, where the respective profile is present
//@ActiveProfiles({ "DB_JOOQ", "test", "postgres" })
//@ActiveProfiles({ "DB_JOOQ", "test", "h2" })
public abstract class AbstractPaperSlimProviderTest<F extends PaperSlimFilter, P extends AbstractPaperSlimProvider<F>> {

    protected static final int PAGE_SIZE = 3;

    @Autowired
    protected SipamatoApplication application;

    @Mock
    protected JooqPaperSlimService serviceMock;
    @Mock
    protected JooqPaperService paperServiceMock;
    @Mock
    protected PaperSlim entityMock;
    @Mock
    protected Paper paperMock;

    protected P provider;
    protected List<PaperSlim> pageOfSlimPapers;
    protected List<Paper> pageOfPapers;

    protected abstract F getFilter();

    @Before
    public final void setUp() {
        new WicketTester(application);
        provider = newProvider();
        provider.setService(serviceMock);
        provider.setPaperService(paperServiceMock);

        pageOfSlimPapers = Arrays.asList(entityMock, entityMock, entityMock);
        pageOfPapers = Arrays.asList(paperMock, paperMock, paperMock, paperMock, paperMock);

        localFixture();
    }

    protected abstract void localFixture();

    @After
    public final void tearDown() {
        verifyNoMoreInteractions(serviceMock, getFilter(), entityMock, paperServiceMock, paperMock);
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

    class PaginationContextMatcher extends ArgumentMatcher<PaginationContext> {

        private final int offset;
        private final int pageSize;
        private final String sort;

        PaginationContextMatcher(int offset, int pageSize, String sort) {
            this.offset = offset;
            this.pageSize = pageSize;
            this.sort = sort;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument != null && argument instanceof PaginationContext) {
                PaginationContext p = (PaginationContext) argument;
                return p.getOffset() == offset && p.getPageSize() == pageSize && sort.equals(p.getSort().toString());
            }
            return false;
        }
    }

    @Test
    public void iterating_withNoRecords_returnsNoRecords() {
        // reset the service mock
        pageOfSlimPapers = Arrays.asList();
        localFixture();

        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verifyFilterMock(new PaginationContextMatcher(0, 3, "authors: ASC"));
    }

    protected abstract void verifyFilterMock(PaginationContextMatcher matcher);

    @Test
    public void iterating_throughFirstFullPage() {
        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(0, 3, "authors: ASC"));
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
        verifyFilterMock(new PaginationContextMatcher(3, 3, "authors: ASC"));
    }

    @Test
    public void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.DESCENDING);
        Iterator<PaperSlim> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(6, 3, "title: DESC"));
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
        verifyFilterMock(new PaginationContextMatcher(0, actualSize, "authors: ASC"));
    }

    @Test
    public void iterating_withDefaultPageSize_throughThirdNotFullPage_withPageSizeHigherThanActualSize() {
        int actualSize = 2;
        assertThat(actualSize).isLessThan(PAGE_SIZE);

        provider.setSort("title", SortOrder.DESCENDING);

        Iterator<PaperSlim> it = provider.iterator(6, actualSize);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(6, actualSize, "title: DESC"));
    }

}
