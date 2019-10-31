package ch.difty.scipamato.core.web.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.*;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ch.difty.scipamato.common.persistence.paging.PaginationContextMatcher;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.web.AbstractWicketTest;

@SpringBootTest
@ActiveProfiles("wickettest")
abstract class AbstractPaperSlimProviderTest<F extends PaperSlimFilter, P extends AbstractPaperSlimProvider<F>>
    extends AbstractWicketTest {

    static final int PAGE_SIZE = 3;

    @Mock
    private   PaperSlim entityMock;
    @Mock
    protected Paper     paperMock;

    P               provider;
    List<PaperSlim> pageOfSlimPapers;
    List<Paper>     pageOfPapers;

    abstract F getFilter();

    @BeforeEach
    final void setUp() {
        final WicketTester tester = new WicketTester(application);
        tester
            .getSession()
            .setLocale(new Locale("en"));

        provider = newProvider();
        provider.setService(paperSlimServiceMock);
        provider.setPaperService(paperServiceMock);

        pageOfSlimPapers = Arrays.asList(entityMock, entityMock, entityMock);
        pageOfPapers = Arrays.asList(paperMock, paperMock, paperMock, paperMock, paperMock);

        when(sessionFacadeMock.getLanguageCode()).thenReturn("en");

        localFixture();
    }

    protected abstract void localFixture();

    @AfterEach
    final void tearDown() {
        verifyNoMoreInteractions(paperSlimServiceMock, getFilter(), entityMock, paperServiceMock, paperMock);
    }

    protected abstract P newProvider();

    @Test
    void gettingModel_wrapsEntity() {
        IModel<PaperSlim> model = provider.model(entityMock);
        assertThat(model.getObject()).isEqualTo(entityMock);
    }

    @Test
    void gettingFilterState_returnsFilter() {
        assertThat(provider.getFilterState()).isEqualTo(getFilter());
    }

    @Test
    void iterating_withNoRecords_returnsNoRecords() {
        // reset the service mock
        pageOfSlimPapers = Collections.emptyList();
        localFixture();

        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertThat(it.hasNext()).isFalse();
        verifyFilterMock(new PaginationContextMatcher(0, 3, "id: DESC"));
    }

    protected abstract void verifyFilterMock(PaginationContextMatcher matcher);

    @Test
    void iterating_throughFirstFullPage() {
        Iterator<PaperSlim> it = provider.iterator(0, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(0, 3, "id: DESC"));
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
    void iterating_throughSecondFullPage() {
        Iterator<PaperSlim> it = provider.iterator(3, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(3, 3, "id: DESC"));
    }

    @Test
    void iterating_throughThirdPage() {
        provider.setSort("title", SortOrder.ASCENDING);
        Iterator<PaperSlim> it = provider.iterator(6, 3);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(6, 3, "title: ASC"));
    }

    @Test
    void instantiationWithPageSize_returnsGivenPageSize() {
        assertThat(provider.getRowsPerPage()).isEqualTo(PAGE_SIZE);
    }

    @Test
    void iterating_withDefaultPageSize_throughFirstFullPage_withPageSizeMatchingActualSize() {
        int actualSize = 3;
        assertThat(actualSize).isEqualTo(PAGE_SIZE);

        Iterator<PaperSlim> it = provider.iterator(0, actualSize);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(0, actualSize, "id: DESC"));
    }

    @Test
    void iterating_withDefaultPageSize_throughThirdNotFullPage_withPageSizeHigherThanActualSize() {
        int actualSize = 2;
        assertThat(actualSize).isLessThan(PAGE_SIZE);

        provider.setSort("title", SortOrder.DESCENDING);

        Iterator<PaperSlim> it = provider.iterator(6, actualSize);
        assertRecordsIn(it);
        verifyFilterMock(new PaginationContextMatcher(6, actualSize, "title: DESC"));
    }

    @Test
    void gettingLanguageCode() {
        assertThat(provider.getLanguageCode()).isEqualTo("en");
    }
}
