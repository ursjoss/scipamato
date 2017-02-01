package ch.difty.sipamato.web.jasper.literature_review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.sipamato.persistance.jooq.SipamatoPageRequest;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.web.jasper.PaperDataSourceTest;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;

public class PaperLiteratureReviewDataSourceTest extends PaperDataSourceTest {

    private static final String FILE_NAME = "paper_literature_review.pdf";

    private static final Long ID = 5l;

    private PaperLiteratureReviewDataSource ds;

    @Override
    public void setUpHook() {
        when(paperMock.getId()).thenReturn(ID);
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
        assertFieldValue("id", String.valueOf(ID), f, jsds);
        assertFieldValue("authors", "a", f, jsds);
        assertFieldValue("publicationYear", "2017", f, jsds);
        assertFieldValue("title", "t", f, jsds);
        assertFieldValue("location", "l", f, jsds);
        assertFieldValue("pubmedLink", "https://www.ncbi.nlm.nih.gov/pubmed/1234", f, jsds);

        assertFieldValue("caption", "c", f, jsds);
        assertFieldValue("brand", "b", f, jsds);

        assertThat(jsds.next()).isFalse();
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1l);
        when(dataProviderMock.getFilterState()).thenReturn(paperFilterMock);
        when(dataProviderMock.getSort()).thenReturn(sortParamMock);
        when(sortParamMock.isAscending()).thenReturn(true);
        when(sortParamMock.getProperty()).thenReturn("foo");
        // TODO be more specific and also refactor part of this test into the base class
        when(paperServiceMock.findByFilter(isA(PaperFilter.class), isA(SipamatoPageRequest.class))).thenReturn(pageMock);
        when(pageMock.getContent()).thenReturn(Arrays.asList(paperMock));

        ds = new PaperLiteratureReviewDataSource(dataProviderMock, paperServiceMock, "c", "b", pdfExporterConfigMock);
        assertDataSource(FILE_NAME);

        verify(dataProviderMock).size();
        verify(dataProviderMock).getFilterState();
        verify(dataProviderMock).getSort();
        verify(sortParamMock).isAscending();
        verify(sortParamMock).getProperty();
        verify(paperServiceMock).findByFilter(isA(PaperFilter.class), isA(SipamatoPageRequest.class));
        verify(pageMock).getContent();

        verify(paperMock).getId();
        verify(paperMock).getAuthors();
        verify(paperMock).getPublicationYear();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getPmId();
    }

}
