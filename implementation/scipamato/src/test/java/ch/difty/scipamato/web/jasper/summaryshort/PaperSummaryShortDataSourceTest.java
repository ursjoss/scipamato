package ch.difty.scipamato.web.jasper.summaryshort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.scipamato.entity.filter.PaperSlimFilter;
import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.web.jasper.PaperDataSourceTest;
import ch.difty.scipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;

public class PaperSummaryShortDataSourceTest extends PaperDataSourceTest {

    private static final Long NUMBER = 100l;
    private static final String AUTHORS = "authors";
    private static final String TITLE = "title";
    private static final String LOCATION = "location";
    private static final String GOALS = "goals";
    private static final String POPULATION_PLACE = "populationPlace";
    private static final String METHODS = "methods";
    private static final String RESULT = "result";
    private static final String COMMENT = "comment";
    private static final String CREATED_BY = "creatingUser";

    private static final String POPULATION_PLACE_LABEL = "Ort/Land";
    private static final String METHODS_LABEL = "Methoden";
    private static final String RESULT_LABEL = "Resultat";
    private static final String COMMENT_LABEL = "Bemerkungen";
    private static final String HEADER_PART = "LUDOK-Zusammenfassung Nr.";
    private static final String HEADER = HEADER_PART + " " + NUMBER;
    private static final String BRAND = "LUDOK";

    private static final String FILE_NAME_SINGLE = "paper_summary_short_no_" + NUMBER + ".pdf";
    private static final String FILE_NAME_MULTIPLE = "paper_summaries_short.pdf";

    private PaperSummaryShortDataSource ds;

    @Override
    public void setUpHook() {
        when(paperMock.getNumber()).thenReturn(NUMBER);
        when(paperMock.getAuthors()).thenReturn(AUTHORS);
        when(paperMock.getTitle()).thenReturn(TITLE);
        when(paperMock.getLocation()).thenReturn(LOCATION);
        when(paperMock.getGoals()).thenReturn(GOALS);
        when(paperMock.getPopulationPlace()).thenReturn(POPULATION_PLACE);
        when(paperMock.getMethods()).thenReturn(METHODS);
        when(paperMock.getResult()).thenReturn(RESULT);
        when(paperMock.getComment()).thenReturn(COMMENT);
        when(paperMock.getCreatedByName()).thenReturn(CREATED_BY);
    }

    @Test
    public void instantiatingWithPaper_returnsPdfDataSourceWithOneRecord() throws JRException {
        ds = new PaperSummaryShortDataSource(paperMock, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);

        verify(paperMock, times(3)).getNumber();
        verify(paperMock).getAuthors();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getGoals();
        verify(paperMock).getPopulationPlace();
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
        assertFieldValue("number", String.valueOf(NUMBER), f, jsds);
        assertFieldValue("authors", AUTHORS, f, jsds);
        assertFieldValue("title", TITLE, f, jsds);
        assertFieldValue("location", LOCATION, f, jsds);
        assertFieldValue("goals", GOALS, f, jsds);
        assertFieldValue("populationPlace", POPULATION_PLACE, f, jsds);
        assertFieldValue("methods", METHODS, f, jsds);
        assertFieldValue("result", RESULT, f, jsds);
        assertFieldValue("comment", COMMENT, f, jsds);

        assertFieldValue("populationPlaceLabel", POPULATION_PLACE_LABEL, f, jsds);
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
        PaperSummaryShort ps = new PaperSummaryShort(NUMBER, AUTHORS, TITLE, LOCATION, GOALS, POPULATION_PLACE, METHODS, RESULT, COMMENT, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL,
                HEADER_PART, BRAND, CREATED_BY);
        ds = new PaperSummaryShortDataSource(ps, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1l);
        when(dataProviderMock.findAllPapersByFilter()).thenReturn(Arrays.asList(paperMock));

        ds = new PaperSummaryShortDataSource(dataProviderMock, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);
        assertDataSource(FILE_NAME_MULTIPLE);

        verify(dataProviderMock).size();
        verify(dataProviderMock).findAllPapersByFilter();

        verify(paperMock).getNumber();
        verify(paperMock).getAuthors();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getGoals();
        verify(paperMock).getPopulationPlace();
        verify(paperMock).getMethods();
        verify(paperMock).getResult();
        verify(paperMock).getComment();
        verify(paperMock).getCreatedByName();
    }

    @Test
    public void instantiatingWithProvider_withEmptyProvider_returnsNoRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(0l);
        ds = new PaperSummaryShortDataSource(dataProviderMock, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);
        assertThat(ds.getReportDataSource().next()).isFalse();
        verify(dataProviderMock).size();
    }

    @Test
    public void instantiatingWithProvider_withNullProvider_throws() throws JRException {
        AbstractPaperSlimProvider<? extends PaperSlimFilter> provider = null;
        try {
            new PaperSummaryShortDataSource(provider, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dataProvider must not be null.");
        }
    }

}
