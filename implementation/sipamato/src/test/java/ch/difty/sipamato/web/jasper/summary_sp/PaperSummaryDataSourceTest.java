package ch.difty.sipamato.web.jasper.summary_sp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.web.jasper.PaperDataSourceTest;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;

public class PaperSummaryDataSourceTest extends PaperDataSourceTest {

    private static final Long ID = 10l;
    private static final String AUTHORS = "authors";
    private static final String TITLE = "title";
    private static final String LOCATION = "location";
    private static final String GOALS = "goals";
    private static final String POPULATION = "population";
    private static final String METHODS = "methods";
    private static final String RESULT = "result";
    private static final String COMMENT = "comment";
    private static final String CREATED_BY = "creatingUser";

    private static final String POPULATION_LABEL = "Kollektiv";
    private static final String METHODS_LABEL = "Methoden";
    private static final String RESULT_LABEL = "Resultat";
    private static final String COMMENT_LABEL = "Bemerkungen";
    private static final String HEADER_PART = "LUDOK-Zusammenfassung Nr.";
    private static final String HEADER = HEADER_PART + " " + ID;
    private static final String BRAND = "LUDOK";

    private static final String FILE_NAME_SINGLE = "paper_summary_id_" + ID + ".pdf";
    private static final String FILE_NAME_MULTIPLE = "paper_summaries.pdf";

    private PaperSummaryDataSource ds;

    @Override
    public void setUpHook() {
        when(paperMock.getId()).thenReturn(ID);
        when(paperMock.getAuthors()).thenReturn(AUTHORS);
        when(paperMock.getTitle()).thenReturn(TITLE);
        when(paperMock.getLocation()).thenReturn(LOCATION);
        when(paperMock.getGoals()).thenReturn(GOALS);
        when(paperMock.getPopulation()).thenReturn(POPULATION);
        when(paperMock.getMethods()).thenReturn(METHODS);
        when(paperMock.getResult()).thenReturn(RESULT);
        when(paperMock.getComment()).thenReturn(COMMENT);
        when(paperMock.getCreatedByName()).thenReturn(CREATED_BY);
    }

    @Test
    public void instantiatingWithPaper_returnsPdfDataSourceWithOneRecord() throws JRException {
        ds = new PaperSummaryDataSource(paperMock, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);

        verify(paperMock, times(3)).getId();
        verify(paperMock).getAuthors();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getGoals();
        verify(paperMock).getPopulation();
        verify(paperMock).getMethods();
        verify(paperMock).getResult();
        verify(paperMock).getComment();
        verify(paperMock).getCreatedByName();
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
        assertFieldValue("authors", AUTHORS, f, jsds);
        assertFieldValue("title", TITLE, f, jsds);
        assertFieldValue("location", LOCATION, f, jsds);
        assertFieldValue("goals", GOALS, f, jsds);
        assertFieldValue("population", POPULATION, f, jsds);
        assertFieldValue("methods", METHODS, f, jsds);
        assertFieldValue("result", RESULT, f, jsds);
        assertFieldValue("comment", COMMENT, f, jsds);

        assertFieldValue("populationLabel", POPULATION_LABEL, f, jsds);
        assertFieldValue("methodsLabel", METHODS_LABEL, f, jsds);
        assertFieldValue("resultLabel", RESULT_LABEL, f, jsds);
        assertFieldValue("commentLabel", COMMENT_LABEL, f, jsds);

        assertFieldValue("header", HEADER, f, jsds);
        assertFieldValue("brand", BRAND, f, jsds);
        assertFieldValue("createdBy", CREATED_BY, f, jsds);

        assertThat(jsds.next()).isFalse();
    }

    @Test
    public void instantiatingWithPaperSummary_returnsPdfDataSourceWithOneRecord() throws JRException {
        PaperSummary ps = new PaperSummary(ID, AUTHORS, TITLE, LOCATION, GOALS, POPULATION, METHODS, RESULT, COMMENT, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND,
                CREATED_BY);
        ds = new PaperSummaryDataSource(ps, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        @SuppressWarnings("unchecked")
        Iterator<PaperSlim> itMock = mock(Iterator.class);
        PaperSlim paperSlimMock = mock(PaperSlim.class);

        when(dataProviderMock.size()).thenReturn(1l);
        when(dataProviderMock.iterator(0, 1)).thenReturn(itMock);
        when(itMock.hasNext()).thenReturn(true, false);
        when(itMock.next()).thenReturn(paperSlimMock);
        when(paperSlimMock.getId()).thenReturn(ID);
        when(paperServiceMock.findByIds(Arrays.asList(ID))).thenReturn(Arrays.asList(paperMock));

        ds = new PaperSummaryDataSource(dataProviderMock, paperServiceMock, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);
        assertDataSource(FILE_NAME_MULTIPLE);

        verify(dataProviderMock).size();
        verify(dataProviderMock).iterator(0, 1);
        verify(dataProviderMock).setRowsPerPage(Integer.MAX_VALUE);
        verify(itMock, times(2)).hasNext();
        verify(itMock).next();
        verify(paperSlimMock).getId();
        verify(paperServiceMock).findByIds(Arrays.asList(ID));

        verify(paperMock).getId();
        verify(paperMock).getAuthors();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getGoals();
        verify(paperMock).getPopulation();
        verify(paperMock).getMethods();
        verify(paperMock).getResult();
        verify(paperMock).getComment();
        verify(paperMock).getCreatedByName();

        verifyNoMoreInteractions(itMock, paperSlimMock);
    }

    @Test
    public void instantiatingWithProvider_withEmptyProvider_returnsNoRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(0l);
        ds = new PaperSummaryDataSource(dataProviderMock, paperServiceMock, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);
        assertThat(ds.getReportDataSource().next()).isFalse();
        verify(dataProviderMock).size();
        verify(dataProviderMock).setRowsPerPage(Integer.MAX_VALUE);
    }

    @Test
    public void instantiatingWithProvider_withNullProivder_throws() throws JRException {
        try {
            new PaperSummaryDataSource(null, paperServiceMock, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dataProvider must not be null.");
        }
    }

    @Test
    public void instantiatingWithProvider_withNullService_throws() throws JRException {
        try {
            new PaperSummaryDataSource(dataProviderMock, null, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("paperService must not be null.");
        }
    }

}
