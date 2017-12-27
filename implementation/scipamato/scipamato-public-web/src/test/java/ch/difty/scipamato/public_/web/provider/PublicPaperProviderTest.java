package ch.difty.scipamato.public_.web.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationContextMatcher;
import ch.difty.scipamato.public_.ScipamatoPublicApplication;
import ch.difty.scipamato.public_.entity.PublicPaper;
import ch.difty.scipamato.public_.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.public_.persistence.api.PublicPaperService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublicPaperProviderTest {

    private PublicPaperProvider provider;

    @Mock
    private PublicPaperService serviceMock;

    @Mock
    private PublicPaperFilter filterMock;

    @Autowired
    private ScipamatoPublicApplication application;

    private List<PublicPaper> papers = new ArrayList<>();

    @Before
    public void setUp() {
        new WicketTester(application);
        provider = new PublicPaperProvider(filterMock, 20);
        provider.setService(serviceMock);

        papers.add(new PublicPaper(1l, 1l, 1000, "authors1", "title1", "location1", 2016, "goals1", "methods1",
                "population1", "result1", "comment1"));
        papers.add(new PublicPaper(2l, 2l, 1002, "authors2", "title2", "location2", 2017, "goals2", "methods2",
                "population2", "result2", "comment2"));

        when(serviceMock.countByFilter(filterMock)).thenReturn(2);
        when(serviceMock.findPageByFilter(eq(filterMock), isA(PaginationContext.class))).thenReturn(papers);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void construct() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
        assertThat(provider.getRowsPerPage()).isEqualTo(20);
    }

    @Test
    public void construct_withNullFilter_instantiatesNewFilter() {
        PublicPaperProvider provider2 = new PublicPaperProvider(null, 10);
        assertThat(provider2.getFilterState()).isNotNull()
            .isNotEqualTo(filterMock)
            .isInstanceOf(PublicPaperFilter.class);
        assertThat(provider2.getRowsPerPage()).isEqualTo(10);
    }

    @Test
    public void canSetFilterState() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
        provider.setFilterState(new PublicPaperFilter());
        assertThat(provider.getFilterState()).isNotEqualTo(filterMock)
            .isInstanceOf(PublicPaperFilter.class);
    }

    @Test
    public void newModel() {
        PublicPaper pp = PublicPaper.builder()
            .id(5l)
            .build();
        IModel<PublicPaper> model = provider.model(pp);
        assertThat(model).isNotNull();
        assertThat(model.getObject()).isNotNull()
            .isInstanceOf(PublicPaper.class);
        assertThat(model.getObject()
            .getId()).isEqualTo(5l);
    }

    @Test
    public void gettingIterator() {
        assertThat(provider.iterator(0l, 10l)).hasSize(2);
        verify(serviceMock).findPageByFilter(eq(filterMock), isA(PaginationContext.class));
    }

    @Test
    public void gettingSize() {
        assertThat(provider.size()).isEqualTo(2);
        verify(serviceMock).countByFilter(filterMock);
    }

    @Test
    public void findingAllNumbersByFilter() {
        provider.setSort("title", SortOrder.DESCENDING);
        when(serviceMock.findPageOfNumbersByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, "title: DESC"))))
                .thenReturn(Arrays.asList(5l, 3l, 17l));
        List<Long> ids = provider.findAllPaperNumbersByFilter();
        assertThat(ids).containsExactly(5l, 3l, 17l);
        verify(serviceMock).findPageOfNumbersByFilter(eq(filterMock),
            argThat(new PaginationContextMatcher(0, Integer.MAX_VALUE, "title: DESC")));
    }

}
