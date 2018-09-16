package ch.difty.scipamato.core.web.paper.jasper.summary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.junit.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PaperSummaryDataSourceTest extends PaperDataSourceTest {

    private static final Long   NUMBER     = 100L;
    private static final String AUTHORS    = "authors";
    private static final String TITLE      = "title";
    private static final String LOCATION   = "location";
    private static final String GOALS      = "goals";
    private static final String POPULATION = "population";
    private static final String METHODS    = "methods";
    private static final String RESULT     = "result";
    private static final String COMMENT    = "comment";
    private static final String CREATED_BY = "creatingUser";

    private static final String POPULATION_LABEL = "Kollektiv";
    private static final String GOALS_LABEL      = "Ziele";
    private static final String METHODS_LABEL    = "Methoden";
    private static final String RESULT_LABEL     = "Resultat";
    private static final String COMMENT_LABEL    = "Bemerkungen";
    private static final String HEADER_PART      = "LUDOK-Zusammenfassung Nr.";
    private static final String HEADER           = HEADER_PART + " " + NUMBER;
    private static final String BRAND            = "LUDOK";

    private static final String FILE_NAME_SINGLE          = "paper_summary_no_" + NUMBER + ".pdf";
    private static final String FILE_NAME_SINGLE_FALLBACK = "paper_summary.pdf";
    private static final String FILE_NAME_MULTIPLE        = "paper_summaries.pdf";

    private       PaperSummaryDataSource ds;
    private final ReportHeaderFields     rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .populationLabel(POPULATION_LABEL)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .resultLabel(RESULT_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build();
    }

    @Override
    public void setUpHook() {
        when(paperMock.getNumber()).thenReturn(NUMBER);
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
        ds = new PaperSummaryDataSource(paperMock, rhf, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);

        verifyPaperMock(3);
    }

    private void verifyPaperMock(int i) {
        verify(paperMock, times(i)).getNumber();
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
        assertThat(ds
            .getContentDisposition()
            .toString()).isEqualTo("ATTACHMENT");
        assertThat(ds.getContentType()).isEqualTo("application/pdf");
        assertThat(ds.getExtension()).isEqualTo("pdf");
        assertThat(ds.getJasperReport()).isInstanceOf(JasperReport.class);
        assertThat(ds.getReportParameters()).isEmpty();

        assertThat(ds.getFileName()).isEqualTo(fileName);

        final JRDataSource jsds = ds.getReportDataSource();
        JRDesignField f = new JRDesignField();

        assertThat(jsds.next()).isTrue();
        assertFieldValue("number", String.valueOf(NUMBER), f, jsds);
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
        PaperSummary ps = new PaperSummary(paperMock, rhf);
        ds = new PaperSummaryDataSource(ps, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);

        verifyPaperMock(1);
    }

    @Test
    public void instantiatingWithPaperSummaryWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        reset(paperMock);
        when(paperMock.getNumber()).thenReturn(null);

        PaperSummary ps = new PaperSummary(paperMock, rhf);
        ds = new PaperSummaryDataSource(ps, pdfExporterConfigMock);

        assertThat(ds.getFileName()).isEqualTo(FILE_NAME_SINGLE_FALLBACK);

        verifyPaperMock(1);
    }

    @Test
    public void instantiatingWithPaperWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        reset(paperMock);
        when(paperMock.getNumber()).thenReturn(null);

        ds = new PaperSummaryDataSource(paperMock, rhf, pdfExporterConfigMock);

        assertThat(ds.getFileName()).isEqualTo(FILE_NAME_SINGLE_FALLBACK);

        verifyPaperMock(2);
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1L);
        when(dataProviderMock.findAllPapersByFilter()).thenReturn(Collections.singletonList(paperMock));

        ds = new PaperSummaryDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertDataSource(FILE_NAME_MULTIPLE);

        verify(dataProviderMock).size();
        verify(dataProviderMock).findAllPapersByFilter();

        verifyPaperMock(1);
    }

    @Test
    public void instantiatingWithProvider_withEmptyProvider_returnsNoRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(0L);
        ds = new PaperSummaryDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertThat(ds
            .getReportDataSource()
            .next()).isFalse();
        verify(dataProviderMock).size();
    }

    @Test
    public void instantiatingWithProvider_withNullProvider_throws() {
        AbstractPaperSlimProvider<? extends PaperSlimFilter> provider = null;
        try {
            new PaperSummaryDataSource(provider, rhf, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("dataProvider must not be null.");
        }
    }

}
