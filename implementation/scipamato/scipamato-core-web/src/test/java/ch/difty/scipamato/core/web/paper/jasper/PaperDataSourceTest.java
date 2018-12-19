package ch.difty.scipamato.core.web.paper.jasper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.junit.After;
import org.mockito.Mock;

import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.web.WicketTest;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;

public abstract class PaperDataSourceTest extends WicketTest {

    @Mock
    protected AbstractPaperSlimProvider<PaperFilter> dataProviderMock;
    @Mock
    protected PaperFilter                            paperFilterMock;
    @Mock
    protected SearchOrder                            searchOrderMock;
    @Mock
    protected Paper                                  paperMock;
    @Mock
    protected ClusterablePdfExporterConfiguration    pdfExporterConfigMock;
    @Mock
    protected CoreShortFieldConcatenator             shortFieldConcatenatorMock;

    protected PaperFilter paperFilter = new PaperFilter();

    @After
    public final void tearDown() {
        verifyNoMoreInteractions(dataProviderMock, paperFilterMock, searchOrderMock, paperMock,
            shortFieldConcatenatorMock);
    }

    protected void assertFieldValue(String fieldName, String value, JRDesignField f, final JRDataSource jsds)
        throws JRException {
        f.setName(fieldName);
        assertThat(jsds.getFieldValue(f)).isEqualTo(value);
    }

}
