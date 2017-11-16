package ch.difty.scipamato.web.provider;

import static org.assertj.core.api.Assertions.*;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.ScipamatoPublicApplication;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublicPaperProviderTest {

    private PublicPaperProvider provider;

    @Mock
    private PublicPaperFilter filterMock;

    @Autowired
    private ScipamatoPublicApplication application;

    @Before
    public void setUp() {
        new WicketTester(application);
        provider = new PublicPaperProvider(filterMock, 20);
    }

    @Test
    public void construct() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
        assertThat(provider.getRowsPerPage()).isEqualTo(20);
    }

    @Test
    public void construct_withNullFilter_instantiatesNewFilter() {
        PublicPaperProvider provider2 = new PublicPaperProvider(null, 10);
        assertThat(provider2.getFilterState()).isNotNull().isNotEqualTo(filterMock).isInstanceOf(PublicPaperFilter.class);
        assertThat(provider2.getRowsPerPage()).isEqualTo(10);
    }

    @Test
    public void canSetFilterState() {
        assertThat(provider.getFilterState()).isEqualTo(filterMock);
        provider.setFilterState(new PublicPaperFilter());
        assertThat(provider.getFilterState()).isNotEqualTo(filterMock).isInstanceOf(PublicPaperFilter.class);
    }

    @Test
    public void newModel() {
        PublicPaper pp = PublicPaper.builder().id(5l).build();
        IModel<PublicPaper> model = provider.model(pp);
        assertThat(model).isNotNull();
        assertThat(model.getObject()).isNotNull().isInstanceOf(PublicPaper.class);
        assertThat(model.getObject().getId()).isEqualTo(5l);
    }

    @Test
    public void gettingIterator() {
        // TODO implement with service call
        assertThat(provider.iterator(0l, 10l)).hasSize(2);
    }

    @Test
    public void gettingSize() {
        // TODO implement with service call
        assertThat(provider.size()).isEqualTo(2);
    }
}
