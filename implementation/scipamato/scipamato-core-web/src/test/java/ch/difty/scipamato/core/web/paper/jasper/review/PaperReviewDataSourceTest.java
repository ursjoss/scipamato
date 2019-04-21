package ch.difty.scipamato.core.web.paper.jasper.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PaperReviewDataSourceTest extends PaperDataSourceTest {

    private static final String FILE_NAME = "paper_review.pdf";

    private static final Long NUMBER = 15L;

    private PaperReviewDataSource ds;
    private ReportHeaderFields    rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder("", "b")
            .numberLabel("nl")
            .authorYearLabel("ayl")
            .populationPlaceLabel("ppl")
            .methodOutcomeLabel("mol")
            .exposurePollutantLabel("epl")
            .methodStudyDesignLabel("msdl")
            .populationDurationLabel("pdl")
            .populationParticipantsLabel("ppal")
            .exposureAssessmentLabel("eal")
            .resultExposureRangeLabel("rerl")
            .methodConfoundersLabel("mcl")
            .resultEffectEstimateLabel("reel")
            .conclusionLabel("cc")
            .build();
    }

    @Override
    public void setUpHook() {
        when(paperMock.getNumber()).thenReturn(NUMBER);
        when(paperMock.getFirstAuthor()).thenReturn("fa");
        when(paperMock.getPublicationYear()).thenReturn(2017);
        when(paperMock.getPopulationPlace()).thenReturn("pp");
        when(paperMock.getMethodOutcome()).thenReturn("mo");
        when(paperMock.getExposurePollutant()).thenReturn("ep");
        when(paperMock.getMethodStudyDesign()).thenReturn("msd");
        when(paperMock.getPopulationDuration()).thenReturn("pd");
        when(paperMock.getPopulationParticipants()).thenReturn("pp");
        when(paperMock.getExposureAssessment()).thenReturn("ea");
        when(paperMock.getResultExposureRange()).thenReturn("rer");
        when(paperMock.getMethodConfounders()).thenReturn("mc");
        when(paperMock.getResultEffectEstimate()).thenReturn("ree");
        when(paperMock.getConclusion()).thenReturn("cc");
        when(paperMock.getCreatedByName()).thenReturn("cb");
    }

    @SuppressWarnings("SameParameterValue")
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
        assertFieldValue("authorYear", "fa 2017", f, jsds);
        assertFieldValue("populationPlace", "pp", f, jsds);
        assertFieldValue("methodOutcome", "mo", f, jsds);
        assertFieldValue("exposurePollutant", "ep", f, jsds);
        assertFieldValue("methodStudyDesign", "msd", f, jsds);
        assertFieldValue("populationDuration", "pd", f, jsds);
        assertFieldValue("populationParticipants", "pp", f, jsds);
        assertFieldValue("exposureAssessment", "ea", f, jsds);
        assertFieldValue("resultExposureRange", "rer", f, jsds);
        assertFieldValue("methodConfounders", "mc", f, jsds);
        assertFieldValue("resultEffectEstimate", "ree", f, jsds);
        assertFieldValue("conclusion", "cc", f, jsds);

        assertFieldValue("numberLabel", "nl", f, jsds);
        assertFieldValue("authorYearLabel", "ayl", f, jsds);
        assertFieldValue("populationPlaceLabel", "ppl", f, jsds);
        assertFieldValue("methodOutcomeLabel", "mol", f, jsds);
        assertFieldValue("exposurePollutantLabel", "epl", f, jsds);
        assertFieldValue("methodStudyDesignLabel", "msdl", f, jsds);
        assertFieldValue("populationDurationLabel", "pdl", f, jsds);
        assertFieldValue("populationParticipantsLabel", "ppal", f, jsds);
        assertFieldValue("exposureAssessmentLabel", "eal", f, jsds);
        assertFieldValue("resultExposureRangeLabel", "rerl", f, jsds);
        assertFieldValue("methodConfoundersLabel", "mcl", f, jsds);
        assertFieldValue("resultEffectEstimateLabel", "reel", f, jsds);
        assertFieldValue("conclusionLabel", "cc", f, jsds);

        assertFieldValue("brand", "b", f, jsds);
        assertFieldValue("createdBy", "cb", f, jsds);

        assertThat(jsds.next()).isFalse();
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1L);
        when(dataProviderMock.findAllPapersByFilter()).thenReturn(Collections.singletonList(paperMock));

        ds = new PaperReviewDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertDataSource(FILE_NAME);

        verify(dataProviderMock).size();
        verify(dataProviderMock).findAllPapersByFilter();

        verify(paperMock, times(2)).getNumber();
        verify(paperMock).getFirstAuthor();
        verify(paperMock).getPublicationYear();
        verify(paperMock).getPopulationPlace();
        verify(paperMock).getMethodOutcome();
        verify(paperMock).getExposurePollutant();
        verify(paperMock).getMethodStudyDesign();
        verify(paperMock).getPopulationDuration();
        verify(paperMock).getPopulationParticipants();
        verify(paperMock).getExposureAssessment();
        verify(paperMock).getResultExposureRange();
        verify(paperMock).getMethodConfounders();
        verify(paperMock).getResultEffectEstimate();
        verify(paperMock).getConclusion();
        verify(paperMock).getCreatedByName();
    }

}
