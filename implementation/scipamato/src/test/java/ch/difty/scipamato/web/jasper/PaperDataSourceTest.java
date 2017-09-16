package ch.difty.scipamato.web.jasper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.mockito.Mock;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.scipamato.web.WicketTest;
import ch.difty.scipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.export.PdfExporterConfiguration;

public abstract class PaperDataSourceTest extends WicketTest {

    @Mock
    protected AbstractPaperSlimProvider<PaperFilter> dataProviderMock;
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
