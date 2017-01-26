package ch.difty.sipamato.web.jasper.review;

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
import ch.difty.sipamato.web.jasper.PaperDataSourceTest;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;

public class PaperReviewDataSourceTest extends PaperDataSourceTest {

    private static final String FILE_NAME = "paper_review.pdf";

    private static final Long ID = 5l;

    private PaperReviewDataSource ds;

    @Override
    public void setUpHook() {
        when(paperMock.getId()).thenReturn(ID);
        when(paperMock.getFirstAuthor()).thenReturn("fa");
        when(paperMock.getPublicationYear()).thenReturn(2017);
        when(paperMock.getPopulationPlace()).thenReturn("pp");
        when(paperMock.getMethodOutcome()).thenReturn("mo");
        when(paperMock.getExposurePollutant()).thenReturn("ep");
        when(paperMock.getMethodStudyDesign()).thenReturn("msd");
        when(paperMock.getPopulationDuration()).thenReturn("pd");
        when(paperMock.getMethodStatistics()).thenReturn("ms");
        when(paperMock.getPopulationParticipants()).thenReturn("pp");
        when(paperMock.getExposureAssessment()).thenReturn("ea");
        when(paperMock.getResultExposureRange()).thenReturn("rer");
        when(paperMock.getMethodConfounders()).thenReturn("mc");
        when(paperMock.getResultEffectEstimate()).thenReturn("ree");
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
        assertFieldValue("authorYear", "fa 2017", f, jsds);
        assertFieldValue("populationPlace", "pp", f, jsds);
        assertFieldValue("methodOutcome", "mo", f, jsds);
        assertFieldValue("exposurePollutant", "ep", f, jsds);
        assertFieldValue("methodStudyDesign", "msd", f, jsds);
        assertFieldValue("populationDuration", "pd", f, jsds);
        assertFieldValue("methodStatistics", "ms", f, jsds);
        assertFieldValue("populationParticipants", "pp", f, jsds);
        assertFieldValue("exposureAssessment", "ea", f, jsds);
        assertFieldValue("resultExposureRange", "rer", f, jsds);
        assertFieldValue("methodConfounders", "mc", f, jsds);
        assertFieldValue("resultEffectEstimate", "ree", f, jsds);

        assertFieldValue("idLabel", "idl", f, jsds);
        assertFieldValue("authorYearLabel", "ayl", f, jsds);
        assertFieldValue("populationPlaceLabel", "ppl", f, jsds);
        assertFieldValue("methodOutcomeLabel", "mol", f, jsds);
        assertFieldValue("exposurePollutantLabel", "epl", f, jsds);
        assertFieldValue("methodStudyDesignLabel", "msdl", f, jsds);
        assertFieldValue("populationDurationLabel", "pdl", f, jsds);
        assertFieldValue("methodStatisticsLabel", "msl", f, jsds);
        assertFieldValue("populationParticipantsLabel", "ppl", f, jsds);
        assertFieldValue("exposureAssessmentLabel", "eal", f, jsds);
        assertFieldValue("resultExposureRangeLabel", "rerl", f, jsds);
        assertFieldValue("methodConfoundersLabel", "mcl", f, jsds);
        assertFieldValue("resultEffectEstimateLabel", "reel", f, jsds);

        assertFieldValue("brand", "b", f, jsds);
        assertFieldValue("createdBy", "cb", f, jsds);

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
        when(paperServiceMock.findByIds(Arrays.asList(ID))).thenReturn(Arrays.asList(paperMock));

        ds = new PaperReviewDataSource(dataProviderMock, paperServiceMock, "idl", "ayl", "ppl", "mol", "epl", "msdl", "pdl", "msl", "ppl", "eal", "rerl", "mcl", "reel", "b", "cb",
                pdfExporterConfigMock);
        assertDataSource(FILE_NAME);

        verify(dataProviderMock).size();
        verify(dataProviderMock).iterator(0, 1);
        verify(dataProviderMock).setRowsPerPage(Integer.MAX_VALUE);
        verify(itMock, times(2)).hasNext();
        verify(itMock).next();
        verify(paperSlimMock).getId();
        verify(paperServiceMock).findByIds(Arrays.asList(ID));

        verify(paperMock, times(2)).getId();
        verify(paperMock).getFirstAuthor();
        verify(paperMock).getPublicationYear();
        verify(paperMock).getPopulationPlace();
        verify(paperMock).getMethodOutcome();
        verify(paperMock).getExposurePollutant();
        verify(paperMock).getMethodStudyDesign();
        verify(paperMock).getPopulationDuration();
        verify(paperMock).getMethodStatistics();
        verify(paperMock).getPopulationParticipants();
        verify(paperMock).getExposureAssessment();
        verify(paperMock).getResultExposureRange();
        verify(paperMock).getMethodConfounders();
        verify(paperMock).getResultEffectEstimate();

        verifyNoMoreInteractions(itMock, paperSlimMock);
    }

}
