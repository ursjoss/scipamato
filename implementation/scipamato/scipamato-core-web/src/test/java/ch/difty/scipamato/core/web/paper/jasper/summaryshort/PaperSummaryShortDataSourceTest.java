package ch.difty.scipamato.core.web.paper.jasper.summaryshort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PaperSummaryShortDataSourceTest extends PaperDataSourceTest {

    private static final Long   NUMBER                  = 100L;
    private static final String AUTHORS                 = "authors";
    private static final String TITLE                   = "title";
    private static final String LOCATION                = "location";
    private static final String GOALS                   = "goals";
    private static final String METHODS                 = "methods";
    private static final String METHOD_OUTCOME          = "methodOutcome";
    private static final String RESULT_MEASURED_OUTCOME = "resultMeasuredOutcome";
    private static final String METHOD_STUDY_DESIGN     = "methodStudyDesign";
    private static final String POPULATION_PLACE        = "populationPlace";
    private static final String POPULATION_PARTICIPANTS = "populationParticipants";
    private static final String POPULATION_DURATION     = "populationDuration";
    private static final String EXPOSURE_POLLUTANT      = "exposurePollutant";
    private static final String EXPOSURE_ASSESSMENT     = "exposureAssessment";
    private static final String RESULT_EXPOSURE_RANGE   = "resultExposureRange";
    private static final String METHOD_STATISTICS       = "methodStatistics";
    private static final String METHOD_CONFOUNDERS      = "methodConfounders";
    private static final String RESULT_EFFECT_ESTIMATE  = "resultEffectEstimate";
    private static final String CONCLUSION              = "conclusion";
    private static final String COMMENT                 = "comment";
    private static final String CREATED_BY              = "creatingUser";

    private static final String GOALS_LABEL                   = "Ziele";
    private static final String METHODS_LABEL                 = "Methoden";
    private static final String METHOD_OUTCOME_LABEL          = "Gesundheitliche Zielgrössen";
    private static final String RESULT_MEASURED_OUTCOME_LABEL = "Gemessene Zielgrösse";
    private static final String METHOD_STUDY_DESIGN_LABEL     = "Studiendesign";
    private static final String POPULATION_PLACE_LABEL        = "Ort/Land (Studie)";
    private static final String POPULATION_PARTICIPANTS_LABEL = "Studienteilnehmer";
    private static final String POPULATION_DURATION_LABEL     = "Studiendauer";
    private static final String EXPOSURE_POLLUTANT_LABEL      = "Schadstoff";
    private static final String EXPOSURE_ASSESSMENT_LABEL     = "Belastungsabschätzung";
    private static final String RESULT_EXPOSURE_RANGE_LABEL   = "Gemessene Belastung (Spanne)";
    private static final String METHOD_STATISTICS_LABEL       = "Statistische Methode";
    private static final String METHOD_CONFOUNDERS_LABEL      = "Störfaktoren Methode";
    private static final String RESULT_EFFECT_ESTIMATE_LABEL  = "Ergebnisse";
    private static final String CONCLUSION_LABEL              = "Schlussfolgerung";
    private static final String COMMENT_LABEL                 = "Bemerkungen";
    private static final String HEADER_PART                   = "LUDOK-Zusammenfassung Nr.";
    private static final String HEADER                        = HEADER_PART + " " + NUMBER;
    private static final String BRAND                         = "LUDOK";

    private static final String FILE_NAME_SINGLE          = "paper_summary_short_no_" + NUMBER + ".pdf";
    private static final String FILE_NAME_SINGLE_FALLBACK = "paper_summary_short.pdf";
    private static final String FILE_NAME_MULTIPLE        = "paper_summaries_short.pdf";

    private       PaperSummaryShortDataSource ds;
    private final ReportHeaderFields          rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .methodOutcomeLabel(METHOD_OUTCOME_LABEL)
            .resultMeasuredOutcomeLabel(RESULT_MEASURED_OUTCOME_LABEL)
            .methodStudyDesignLabel(METHOD_STUDY_DESIGN_LABEL)
            .populationPlaceLabel(POPULATION_PLACE_LABEL)
            .populationParticipantsLabel(POPULATION_PARTICIPANTS_LABEL)
            .populationDurationLabel(POPULATION_DURATION_LABEL)
            .exposurePollutantLabel(EXPOSURE_POLLUTANT_LABEL)
            .exposureAssessmentLabel(EXPOSURE_ASSESSMENT_LABEL)
            .resultExposureRangeLabel(RESULT_EXPOSURE_RANGE_LABEL)
            .methodStatisticsLabel(METHOD_STATISTICS_LABEL)
            .methodConfoundersLabel(METHOD_CONFOUNDERS_LABEL)
            .resultEffectEstimateLabel(RESULT_EFFECT_ESTIMATE_LABEL)
            .conclusionLabel(CONCLUSION_LABEL)
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
        when(paperMock.getMethods()).thenReturn(METHODS);
        when(paperMock.getMethodOutcome()).thenReturn(METHOD_OUTCOME);
        when(paperMock.getResultMeasuredOutcome()).thenReturn(RESULT_MEASURED_OUTCOME);
        when(paperMock.getMethodStudyDesign()).thenReturn(METHOD_STUDY_DESIGN);
        when(paperMock.getPopulationPlace()).thenReturn(POPULATION_PLACE);
        when(paperMock.getPopulationParticipants()).thenReturn(POPULATION_PARTICIPANTS);
        when(paperMock.getPopulationDuration()).thenReturn(POPULATION_DURATION);
        when(paperMock.getExposurePollutant()).thenReturn(EXPOSURE_POLLUTANT);
        when(paperMock.getExposureAssessment()).thenReturn(EXPOSURE_ASSESSMENT);
        when(paperMock.getResultExposureRange()).thenReturn(RESULT_EXPOSURE_RANGE);
        when(paperMock.getMethodStatistics()).thenReturn(METHOD_STATISTICS);
        when(paperMock.getMethodConfounders()).thenReturn(METHOD_CONFOUNDERS);
        when(paperMock.getResultEffectEstimate()).thenReturn(RESULT_EFFECT_ESTIMATE);
        when(paperMock.getConclusion()).thenReturn(CONCLUSION);
        when(paperMock.getComment()).thenReturn(COMMENT);
        when(paperMock.getCreatedByName()).thenReturn(CREATED_BY);
    }

    @Test
    public void instantiatingWithPaper_returnsPdfDataSourceWithOneRecord() throws JRException {
        ds = new PaperSummaryShortDataSource(paperMock, rhf, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);

        verifyPaperMock(3);
    }

    private void verifyPaperMock(int i) {
        verify(paperMock, times(i)).getNumber();
        verify(paperMock).getAuthors();
        verify(paperMock).getTitle();
        verify(paperMock).getLocation();
        verify(paperMock).getGoals();
        verify(paperMock).getMethods();
        verify(paperMock).getMethodOutcome();
        verify(paperMock).getResultMeasuredOutcome();
        verify(paperMock).getMethodStudyDesign();
        verify(paperMock).getPopulationPlace();
        verify(paperMock).getPopulationParticipants();
        verify(paperMock).getPopulationDuration();
        verify(paperMock).getExposurePollutant();
        verify(paperMock).getExposureAssessment();
        verify(paperMock).getResultExposureRange();
        verify(paperMock).getMethodStatistics();
        verify(paperMock).getMethodConfounders();
        verify(paperMock).getResultEffectEstimate();
        verify(paperMock).getConclusion();
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
        assertFieldValue("methods", METHODS, f, jsds);
        assertFieldValue("methodOutcome", METHOD_OUTCOME, f, jsds);
        assertFieldValue("methodStudyDesign", METHOD_STUDY_DESIGN, f, jsds);
        assertFieldValue("populationPlace", POPULATION_PLACE, f, jsds);
        assertFieldValue("populationParticipants", POPULATION_PARTICIPANTS, f, jsds);
        assertFieldValue("populationDuration", POPULATION_DURATION, f, jsds);
        assertFieldValue("exposurePollutant", EXPOSURE_POLLUTANT, f, jsds);
        assertFieldValue("exposureAssessment", EXPOSURE_ASSESSMENT, f, jsds);
        assertFieldValue("resultExposureRange", RESULT_EXPOSURE_RANGE, f, jsds);
        assertFieldValue("methodStatistics", METHOD_STATISTICS, f, jsds);
        assertFieldValue("methodConfounders", METHOD_CONFOUNDERS, f, jsds);
        assertFieldValue("resultEffectEstimate", RESULT_EFFECT_ESTIMATE, f, jsds);
        assertFieldValue("conclusion", CONCLUSION, f, jsds);
        assertFieldValue("comment", COMMENT, f, jsds);

        assertFieldValue("goalsLabel", GOALS_LABEL, f, jsds);
        assertFieldValue("methodsLabel", METHODS_LABEL, f, jsds);
        assertFieldValue("methodOutcomeLabel", METHOD_OUTCOME_LABEL, f, jsds);
        assertFieldValue("methodStudyDesignLabel", METHOD_STUDY_DESIGN_LABEL, f, jsds);

        assertFieldValue("populationPlaceLabel", POPULATION_PLACE_LABEL, f, jsds);
        assertFieldValue("populationParticipantsLabel", POPULATION_PARTICIPANTS_LABEL, f, jsds);
        assertFieldValue("populationDurationLabel", POPULATION_DURATION_LABEL, f, jsds);
        assertFieldValue("exposurePollutantLabel", EXPOSURE_POLLUTANT_LABEL, f, jsds);
        assertFieldValue("exposureAssessmentLabel", EXPOSURE_ASSESSMENT_LABEL, f, jsds);
        assertFieldValue("resultExposureRangeLabel", RESULT_EXPOSURE_RANGE_LABEL, f, jsds);
        assertFieldValue("methodStatisticsLabel", METHOD_STATISTICS_LABEL, f, jsds);
        assertFieldValue("methodConfoundersLabel", METHOD_CONFOUNDERS_LABEL, f, jsds);
        assertFieldValue("resultEffectEstimateLabel", RESULT_EFFECT_ESTIMATE_LABEL, f, jsds);
        assertFieldValue("conclusionLabel", CONCLUSION_LABEL, f, jsds);
        assertFieldValue("commentLabel", COMMENT_LABEL, f, jsds);

        assertFieldValue("header", HEADER, f, jsds);
        assertFieldValue("brand", BRAND, f, jsds);
        assertFieldValue("createdBy", CREATED_BY, f, jsds);

        assertThat(jsds.next()).isFalse();
    }

    @Test
    public void instantiatingWithPaperSummaryShort_returnsPdfDataSourceWithOneRecord() throws JRException {
        PaperSummaryShort ps = new PaperSummaryShort(paperMock, rhf);
        ds = new PaperSummaryShortDataSource(ps, pdfExporterConfigMock);

        assertDataSource(FILE_NAME_SINGLE);

        verifyPaperMock(1);
    }

    @Test
    public void instantiatingWithPaperSummaryWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        reset(paperMock);
        when(paperMock.getNumber()).thenReturn(null);

        PaperSummaryShort ps = new PaperSummaryShort(paperMock, rhf);
        ds = new PaperSummaryShortDataSource(ps, pdfExporterConfigMock);

        assertThat(ds.getFileName()).isEqualTo(FILE_NAME_SINGLE_FALLBACK);

        verifyPaperMock(1);
    }

    @Test
    public void instantiatingWithPaperWithoutNumber_returnsPdfDataSourceWithOneRecordAndFallBackName() {
        reset(paperMock);
        when(paperMock.getNumber()).thenReturn(null);

        ds = new PaperSummaryShortDataSource(paperMock, rhf, pdfExporterConfigMock);

        assertThat(ds.getFileName()).isEqualTo(FILE_NAME_SINGLE_FALLBACK);

        verifyPaperMock(2);
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1L);
        when(dataProviderMock.findAllPapersByFilter()).thenReturn(Collections.singletonList(paperMock));

        ds = new PaperSummaryShortDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertDataSource(FILE_NAME_MULTIPLE);

        verify(dataProviderMock).size();
        verify(dataProviderMock).findAllPapersByFilter();

        verifyPaperMock(1);
    }

    @Test
    public void instantiatingWithProvider_withEmptyProvider_returnsNoRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(0L);
        ds = new PaperSummaryShortDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertThat(ds
            .getReportDataSource()
            .next()).isFalse();
        verify(dataProviderMock).size();
    }

    @Test
    public void instantiatingWithProvider_withNullProvider_throws() {
        AbstractPaperSlimProvider<? extends PaperSlimFilter> provider = null;
        try {
            new PaperSummaryShortDataSource(provider, rhf, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("dataProvider must not be null.");
        }
    }

}
