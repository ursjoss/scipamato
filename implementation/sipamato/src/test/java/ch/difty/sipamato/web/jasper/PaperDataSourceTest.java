package ch.difty.sipamato.web.jasper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.After;
import org.mockito.Mock;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.web.WicketTest;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.export.PdfExporterConfiguration;

public abstract class PaperDataSourceTest extends WicketTest {

    @Mock
    protected SortablePaperSlimProvider<PaperFilter> dataProviderMock;
    @Mock
    protected PaperFilter paperFilterMock;
    @Mock
    protected SearchOrder searchOrderMock;
    @Mock
    protected Paper paperMock;
    @Mock
    protected PdfExporterConfiguration pdfExporterConfigMock;

    protected PaperFilter paperFilter = new PaperFilter();

    @After
    public final void tearDown() {
        verifyNoMoreInteractions(dataProviderMock, paperFilterMock, searchOrderMock, paperMock);
    }

    protected void assertFieldValue(String fieldName, String value, JRDesignField f, final JRDataSource jsds) throws JRException {
        f.setName(fieldName);
        assertThat(jsds.getFieldValue(f)).isEqualTo(value);
    }

}
