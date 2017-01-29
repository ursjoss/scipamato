package ch.difty.sipamato.web.jasper.summary_table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.web.jasper.PaperDataSourceTest;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;

public class PaperSummaryTableDataSourceTest extends PaperDataSourceTest {

    private static final Long ID = 10l;
    private static final String FIRST_AUTHOR = "firstAuthor";
    private static final int PUBLICATION_YEAR = 2017;
    private static final String GOALS = "goals";
    private static final String TITLE = "title";
    private static final String RESULT = "result";
    private static final String CODES_OF_CC1 = "1F";
    private static final String CODES_OF_CC4 = "4A,4C";
    private static final String CODES_OF_CC7 = "7B";
    private static final String CAPTION = "caption";
    private static final String BRAND = "brand";

    private static final String FILE_NAME = "paper_summary_table.pdf";

    private final List<Code> codesOfCodeClass1 = new ArrayList<>();
    private final List<Code> codesOfCodeClass4 = new ArrayList<>();
    private final List<Code> codesOfCodeClass7 = new ArrayList<>();

    private PaperSummaryTableDataSource ds;

    @Override
    public void setUpHook() {
        codesOfCodeClass1.add(new Code("1F", "Code1F", "", false, CodeClassId.CC1.getId(), "CC1", "CC1D", 1));
        codesOfCodeClass4.add(new Code("4A", "Code4A", "", false, CodeClassId.CC4.getId(), "CC4", "CC4D", 1));
        codesOfCodeClass4.add(new Code("4C", "Code4C", "", false, CodeClassId.CC4.getId(), "CC4", "CC4D", 3));
        codesOfCodeClass7.add(new Code("7B", "Code7B", "", false, CodeClassId.CC7.getId(), "CC7", "CC7D", 2));

        when(paperMock.getId()).thenReturn(ID);
        when(paperMock.getFirstAuthor()).thenReturn(FIRST_AUTHOR);
        when(paperMock.getPublicationYear()).thenReturn(PUBLICATION_YEAR);
        when(paperMock.getGoals()).thenReturn(GOALS);
        when(paperMock.getTitle()).thenReturn(TITLE);
        when(paperMock.getResult()).thenReturn(RESULT);

        when(paperMock.getCodesOf(CodeClassId.CC1)).thenReturn(codesOfCodeClass1);
        when(paperMock.getCodesOf(CodeClassId.CC4)).thenReturn(codesOfCodeClass4);
        when(paperMock.getCodesOf(CodeClassId.CC7)).thenReturn(codesOfCodeClass7);
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
        assertFieldValue("firstAuthor", FIRST_AUTHOR, f, jsds);
        assertFieldValue("publicationYear", String.valueOf(PUBLICATION_YEAR), f, jsds);
        assertFieldValue("goals", GOALS, f, jsds);
        assertFieldValue("title", TITLE, f, jsds);
        assertFieldValue("result", RESULT, f, jsds);
        assertFieldValue("codesOfClass1", CODES_OF_CC1, f, jsds);
        assertFieldValue("codesOfClass4", CODES_OF_CC4, f, jsds);
        assertFieldValue("codesOfClass7", CODES_OF_CC7, f, jsds);

        assertFieldValue("caption", CAPTION, f, jsds);
        assertFieldValue("brand", BRAND, f, jsds);

        assertThat(jsds.next()).isFalse();
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
        when(paperServiceMock.findWithCodesByIds(Arrays.asList(ID))).thenReturn(Arrays.asList(paperMock));

        ds = new PaperSummaryTableDataSource(dataProviderMock, paperServiceMock, true, CAPTION, BRAND, pdfExporterConfigMock);
        assertDataSource(FILE_NAME);

        verify(dataProviderMock).size();
        verify(dataProviderMock).iterator(0, 1);
        verify(dataProviderMock).setRowsPerPage(Integer.MAX_VALUE);
        verify(itMock, times(2)).hasNext();
        verify(itMock).next();
        verify(paperSlimMock).getId();
        verify(paperServiceMock).findWithCodesByIds(Arrays.asList(ID));

        verify(paperMock).getId();
        verify(paperMock).getFirstAuthor();
        verify(paperMock).getPublicationYear();
        verify(paperMock).getGoals();
        verify(paperMock).getTitle();
        verify(paperMock).getResult();
        verify(paperMock).getCodesOf(CodeClassId.CC1);
        verify(paperMock).getCodesOf(CodeClassId.CC4);
        verify(paperMock).getCodesOf(CodeClassId.CC7);

        verifyNoMoreInteractions(itMock, paperSlimMock);
    }

    @Test
    public void instantiatingWithProvider_withEmptyProvider_returnsNoRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(0l);
        ds = new PaperSummaryTableDataSource(dataProviderMock, paperServiceMock, true, CAPTION, BRAND, pdfExporterConfigMock);
        assertThat(ds.getReportDataSource().next()).isFalse();
        verify(dataProviderMock).size();
        verify(dataProviderMock).setRowsPerPage(Integer.MAX_VALUE);
    }

    @Test
    public void instantiatingWithProvider_withNullProivder_throws() throws JRException {
        try {
            new PaperSummaryTableDataSource(null, paperServiceMock, true, CAPTION, BRAND, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dataProvider must not be null.");
        }
    }

    @Test
    public void instantiatingWithProvider_withNullService_throws() throws JRException {
        try {
            new PaperSummaryTableDataSource(dataProviderMock, null, true, CAPTION, BRAND, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("paperService must not be null.");
        }
    }

}
