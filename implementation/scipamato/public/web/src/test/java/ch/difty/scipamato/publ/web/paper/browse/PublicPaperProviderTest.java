package ch.difty.scipamato.publ.web.paper.browse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationContextMatcher;
import ch.difty.scipamato.publ.ScipamatoPublicApplication;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.api.PublicPaperService;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootTest
class PublicPaperProviderTest {

    private PublicPaperProvider provider;

    @Mock
    private PublicPaperService serviceMock;

    @Mock
    private PublicPaperFilter filterMock;

    @Autowired
    private ScipamatoPublicApplication application;

    private final List<PublicPaper> papers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        new WicketTester(application);
        provider = new PublicPaperProvider(filterMock, 20);
        provider.setService(serviceMock);

        papers.add(new PublicPaper(1L, 1L, 1000, "authors1", "auths", "title1", "location1", "journal1", 2016, "goals1",
            "methods1", "population1", "result1", "comment1"));
        papers.add(new PublicPaper(2L, 2L, 1002, "authors2", "auths", "title2", "location2", "journal2", 2017, "goals2",
            "methods2", "population2", "result2", "comment2"));

        when(serviceMock.countByFilter(filterMock)).thenReturn(2);
        when(serviceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    void construct() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
        assertThat(provider.getRowsPerPage()).isEqualTo(20);
    }

    @Test
    void construct_withNullFilter_instantiatesNewFilter() {
        PublicPaperProvider provider2 = new PublicPaperProvider(null, 10);
        assertThat(provider2.getFilterState())
            .isNotNull()
            .isNotEqualTo(filterMock)
            .isInstanceOf(PublicPaperFilter.class);
        assertThat(provider2.getRowsPerPage()).isEqualTo(10);
    }

    @Test
    void canSetFilterState() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
        provider.setFilterState(new PublicPaperFilter());
        assertThat(provider.getFilterState())
            .isNotEqualTo(filterMock)
            .isInstanceOf(PublicPaperFilter.class);
    }

    @Test
    void newModel() {
        PublicPaper pp = PublicPaper
            .builder()
            .id(5L)
            .build();
        IModel<PublicPaper> model = provider.model(pp);
        assertThat(model).isNotNull();
        assertThat(model.getObject())
            .isNotNull()
            .isInstanceOf(PublicPaper.class);
        assertThat(model
            .getObject()
            .getId()).isEqualTo(5L);
    }

    @Test
    void gettingIterator_withAscendingSort() {
        provider.setSort("title", SortOrder.ASCENDING);
        assertThat(provider.iterator(0L, 10L))
            .extracting("id")
            .containsExactlyInAnyOrder(1L, 2L);
        verify(serviceMock).findPageByFilter(eq(filterMock), isA(PaginationContext.class));
    }

    @Test
    void gettingIterator_withDescendingSort() {
        provider.setSort("title", SortOrder.DESCENDING);
        assertThat(provider.iterator(0L, 10L))
            .extracting("id")
            .containsExactlyInAnyOrder(2L, 1L);
        verify(serviceMock).findPageByFilter(eq(filterMock), isA(PaginationContext.class));
    }

    @Test
    void gettingSize() {
        assertThat(provider.size()).isEqualTo(2);
        verify(serviceMock).countByFilter(filterMock);
    }

    @Test
    void findingAllNumbersByFilter_descendingSort() {
        findingAllNumbersByFilter(SortOrder.DESCENDING);
    }

    @Test
    void findingAllNumbersByFilter_ascendingSort() {
        findingAllNumbersByFilter(SortOrder.ASCENDING);
    }

    private void findingAllNumbersByFilter(final SortOrder srt) {
        final String sortDescription = "title: " + (SortOrder.DESCENDING.equals(srt) ? "DESC" : "ASC");
        provider.setSort("title", srt);

        when(serviceMock.findPageOfNumbersByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, sortDescription)))).thenReturn(
            Arrays.asList(5L, 3L, 17L));

        assertThat(provider.findAllPaperNumbersByFilter()).containsExactly(5L, 3L, 17L);

        verify(serviceMock).findPageOfNumbersByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, sortDescription)));
    }

}
