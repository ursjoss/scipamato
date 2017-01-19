package ch.difty.sipamato.web.jasper.summary_sp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.WicketTest;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;

public class PaperSummaryDataSourceTest extends WicketTest {

    private static final Long ID = 10l;
    private static final String AUTHORS = "authors";
    private static final String TITLE = "title";
    private static final String LOCATION = "location";
    private static final String GOALS = "goals";
    private static final String POPULATION = "population";
    private static final String METHODS = "methods";
    private static final String RESULT = "result";
    private static final String USER_NAME = "userName";

    private PaperSummaryDataSource ds;

    @Mock
    private SortablePaperSlimProvider<? extends PaperSlimFilter> dataProviderMock;
    @Mock
    private PaperService paperServiceMock;
    @Mock
    private Paper paperMock;

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
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dataProviderMock, paperServiceMock, paperMock);
    }

    @Test
    public void instantiatingWithPaper_returnsPdfDataSourceWithOneRecord() throws JRException {
        ds = new PaperSummaryDataSource(paperMock, USER_NAME);

        assertDataSource();

        verify(paperMock).getId();
        verify(paperMock).getAuthors();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getGoals();
        verify(paperMock).getPopulation();
        verify(paperMock).getMethods();
        verify(paperMock).getResult();
    }

    private void assertDataSource() throws JRException {
        assertThat(ds.getConnectionProvider()).isNull();
        assertThat(ds.getContentDisposition().toString()).isEqualTo("ATTACHMENT");
        assertThat(ds.getContentType()).isEqualTo("application/pdf");
        assertThat(ds.getExtension()).isEqualTo("pdf");
        assertThat(ds.getFileName()).isEqualTo("paper_summary_A4.pdf");
        assertThat(ds.getJasperReport()).isInstanceOf(JasperReport.class);
        assertThat(ds.getReportParameters()).isEmpty();

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

        assertFieldValue("populationLabel", "Kollektiv", f, jsds);
        assertFieldValue("methodsLabel", "Methoden", f, jsds);
        assertFieldValue("resultLabel", "Resultat", f, jsds);

        assertFieldValue("header", "LUDOK-Zusammenfassung Nr. 10", f, jsds);
        assertFieldValue("brand", "LUDOK", f, jsds);
        assertFieldValue("user", USER_NAME, f, jsds);
        assertFieldValue("year", "2017", f, jsds); // TODO use fix value with timeService

        assertThat(jsds.next()).isFalse();
    }

    private void assertFieldValue(String fieldName, String value, JRDesignField f, final JRDataSource jsds) throws JRException {
        f.setName(fieldName);
        assertThat(jsds.getFieldValue(f)).isEqualTo(value);
    }

    @Test
    public void instantiatingWithPaperSummary_returnsPdfDataSourceWithOneRecord() throws JRException {
        PaperSummary ps = new PaperSummary(ID, AUTHORS, TITLE, LOCATION, GOALS, POPULATION, METHODS, RESULT, "Kollektiv", "Methoden", "Resultat", "LUDOK-Zusammenfassung Nr.", "LUDOK", USER_NAME,
                LocalDateTime.parse("2017-01-01T15:13:12"));
        ds = new PaperSummaryDataSource(ps);

        assertDataSource();
    }

    // TODO continue
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

        ds = new PaperSummaryDataSource(dataProviderMock, paperServiceMock);
        assertDataSource();

        verify(dataProviderMock).size();
        verify(dataProviderMock).iterator(0, 1);
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

        verifyNoMoreInteractions(itMock, paperSlimMock);
    }
}
