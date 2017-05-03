package ch.difty.sipamato.web.jasper.literaturereview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.sipamato.web.jasper.PaperDataSourceTest;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;

public class PaperLiteratureReviewDataSourceTest extends PaperDataSourceTest {

    private static final String FILE_NAME = "paper_literature_review.pdf";

    private static final Long NUMBER = 5l;

    private PaperLiteratureReviewDataSource ds;

    @Override
    public void setUpHook() {
        when(paperMock.getNumber()).thenReturn(NUMBER);
        when(paperMock.getAuthors()).thenReturn("a");
        when(paperMock.getPublicationYear()).thenReturn(2017);
        when(paperMock.getTitle()).thenReturn("t");
        when(paperMock.getLocation()).thenReturn("l");
        when(paperMock.getPmId()).thenReturn(1234);
    }

    private void assertDataSource(String fileName) throws JRException {
        assertThat(ds.getConnectionProvider()).isNull();
        assertThat(ds.getContentDisposition().toString()).isEqualTo("ATTACHMENT");
        assertThat(ds.getContentType()).isEqualTo("application/pdf");
        assertThat(ds.getExtension()).isEqualTo("pdf");
        assertThat(ds.getJasperReport()).isInstanceOf(JasperReport.class);
        assertThat(ds.getReportParameters()).isEmpty();

        assertThat(ds.getFileName()).isEqualTo(fileName);

        final JRDataSource jsds = ds.getReportDataSource();
        JRDesignField f = new JRDesignField();

        assertThat(jsds.next()).isTrue();
        assertFieldValue("number", String.valueOf(NUMBER), f, jsds);
        assertFieldValue("authors", "a", f, jsds);
        assertFieldValue("publicationYear", "2017", f, jsds);
        assertFieldValue("title", "t", f, jsds);
        assertFieldValue("location", "l", f, jsds);
        assertFieldValue("pubmedLink", "https://www.ncbi.nlm.nih.gov/pubmed/1234", f, jsds);

        assertFieldValue("caption", "c", f, jsds);
        assertFieldValue("brand", "b", f, jsds);
        assertFieldValue("numberLabel", "nl", f, jsds);

        assertThat(jsds.next()).isFalse();
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1l);
        when(dataProviderMock.findAllPapersByFilter()).thenReturn(Arrays.asList(paperMock));

        ds = new PaperLiteratureReviewDataSource(dataProviderMock, "c", "b", "nl", pdfExporterConfigMock);
        assertDataSource(FILE_NAME);

        verify(dataProviderMock).size();
        verify(dataProviderMock).findAllPapersByFilter();

        verify(paperMock).getNumber();
        verify(paperMock).getAuthors();
        verify(paperMock).getPublicationYear();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getPmId();
    }

}
