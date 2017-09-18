package ch.difty.scipamato.web.jasper.summaryshort;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.web.jasper.JasperEntityTest;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

public class PaperSummaryShortTest extends JasperEntityTest {

    private PaperSummaryShort ps;
    private ReportHeaderFields rhf = newReportHeaderFields();

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction_withNullPaper() {
        new PaperSummaryShort(null, rhf);
    }

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction_withNullReportHeaderFields() {
        new PaperSummaryShort(p, null);
    }

    @Test
    public void instantiating() {
        ps = new PaperSummaryShort(p, rhf);
        assertPaperSummaryShort();
    }

    private ReportHeaderFields newReportHeaderFields() {
        ReportHeaderFields.Builder b = new ReportHeaderFields.Builder(HEADER_PART, BRAND).withGoals(GOALS_LABEL).withMethods(METHODS_LABEL).withMethodOutcome(
                METHOD_OUTCOME_LABEL).withResultMeasuredOutcome(RESULT_MEASURED_OUTCOME_LABEL).withMethodStudyDesign(METHOD_STUDY_DESIGN_LABEL).withPopulationPlace(
                        POPULATION_PLACE_LABEL).withPopulationPariticpants(POPULATION_PARTICIPANTS_LABEL).withPopulationDuration(POPULATION_DURATION_LABEL).withExposurePollutant(
                                EXPOSURE_POLLUTANT_LABEL).withExposureAssessment(EXPOSURE_ASSESSMENT_LABEL).withResultExposureRange(RESULT_EXPOSURE_RANGE_LABEL).withMethodStatistics(
                                        METHOD_STATISTICS_LABEL).withMethodConfounders(METHOD_CONFOUNDERS_LABEL).withResultEffectEstimate(RESULT_EFFECT_ESTIMATE_LABEL).withComment(COMMENT_LABEL);
        return b.build();
    }

    private void assertPaperSummaryShort() {
        assertThat(ps.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(ps.getAuthors()).isEqualTo(AUTHORS);
        assertThat(ps.getTitle()).isEqualTo(TITLE);
        assertThat(ps.getLocation()).isEqualTo(LOCATION);
        assertThat(ps.getGoals()).isEqualTo(GOALS);
        assertThat(ps.getMethods()).isEqualTo(METHODS);
        assertThat(ps.getMethodOutcome()).isEqualTo(METHOD_OUTCOME);
        assertThat(ps.getResultMeasuredOutcome()).isEqualTo(RESULT_MEASURED_OUTCOME);
        assertThat(ps.getMethodStudyDesign()).isEqualTo(METHOD_STUDY_DESIGN);
        assertThat(ps.getPopulationPlace()).isEqualTo(POPULATION_PLACE);
        assertThat(ps.getPopulationParticipants()).isEqualTo(POPULATION_PARTICIPANTS);
        assertThat(ps.getPopulationDuration()).isEqualTo(POPULATION_DURATION);
        assertThat(ps.getExposurePollutant()).isEqualTo(EXPOSURE_POLLUTANT);
        assertThat(ps.getExposureAssessment()).isEqualTo(EXPOSURE_ASSESSMENT);
        assertThat(ps.getResultExposureRange()).isEqualTo(RESULT_EXPOSURE_RANGE);
        assertThat(ps.getMethodStatistics()).isEqualTo(METHOD_STATISTICS);
        assertThat(ps.getMethodConfounders()).isEqualTo(METHOD_CONFOUNDERS);
        assertThat(ps.getResultEffectEstimate()).isEqualTo(RESULT_EFFECT_ESTIMATE);
        assertThat(ps.getComment()).isEqualTo(COMMENT);

        assertThat(ps.getGoalsLabel()).isEqualTo(GOALS_LABEL);
        assertThat(ps.getMethodsLabel()).isEqualTo(METHODS_LABEL);
        assertThat(ps.getMethodOutcomeLabel()).isEqualTo(METHOD_OUTCOME_LABEL);
        assertThat(ps.getResultMeasuredOutcomeLabel()).isEqualTo(RESULT_MEASURED_OUTCOME_LABEL);
        assertThat(ps.getMethodStudyDesignLabel()).isEqualTo(METHOD_STUDY_DESIGN_LABEL);
        assertThat(ps.getPopulationPlaceLabel()).isEqualTo(POPULATION_PLACE_LABEL);
        assertThat(ps.getPopulationParticipantsLabel()).isEqualTo(POPULATION_PARTICIPANTS_LABEL);
        assertThat(ps.getPopulationDurationLabel()).isEqualTo(POPULATION_DURATION_LABEL);
        assertThat(ps.getExposurePollutantLabel()).isEqualTo(EXPOSURE_POLLUTANT_LABEL);
        assertThat(ps.getExposureAssessmentLabel()).isEqualTo(EXPOSURE_ASSESSMENT_LABEL);
        assertThat(ps.getResultExposureRangeLabel()).isEqualTo(RESULT_EXPOSURE_RANGE_LABEL);
        assertThat(ps.getMethodStatisticsLabel()).isEqualTo(METHOD_STATISTICS_LABEL);
        assertThat(ps.getMethodConfoundersLabel()).isEqualTo(METHOD_CONFOUNDERS_LABEL);
        assertThat(ps.getResultEffectEstimateLabel()).isEqualTo(RESULT_EFFECT_ESTIMATE_LABEL);
        assertThat(ps.getCommentLabel()).isEqualTo(COMMENT_LABEL);

        assertThat(ps.getHeader()).isEqualTo(HEADER_PART + " " + NUMBER);
        assertThat(ps.getBrand()).isEqualTo(BRAND);
        assertThat(ps.getCreatedBy()).isEqualTo(CREATED_BY);
    }

    @Test
    public void goalsLabelIsBlankIfGoalsIsBlank() {
        p.setGoals("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getGoals()).isEqualTo("");
        assertThat(ps.getGoalsLabel()).isEqualTo("");
    }

    @Test
    public void methodsLabelIsBlankIfMethodsIsBlank() {
        p.setMethods("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethods()).isEqualTo("");
        assertThat(ps.getMethodsLabel()).isEqualTo("");
    }

    @Test
    public void methodOutcomeLabelIsBlankIfMethodOutcomeIsBlank() {
        p.setMethodOutcome("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodOutcome()).isEqualTo("");
        assertThat(ps.getMethodOutcomeLabel()).isEqualTo("");
    }

    @Test
    public void resultMeasuredOutcomeLabelIsBlankIfResultMeasuredOutcomeIsBlank() {
        p.setResultMeasuredOutcome("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getResultMeasuredOutcome()).isEqualTo("");
        assertThat(ps.getResultMeasuredOutcomeLabel()).isEqualTo("");
    }

    @Test
    public void methodStudyDesignLabelIsBlankIfMethodStudyDesignIsBlank() {
        p.setMethodStudyDesign("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodStudyDesign()).isEqualTo("");
        assertThat(ps.getMethodStudyDesignLabel()).isEqualTo("");
    }

    @Test
    public void populationPlaceLabelIsBlankIfPopulationPlaceIsBlank() {
        p.setPopulationPlace("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getPopulationPlace()).isEqualTo("");
        assertThat(ps.getPopulationPlaceLabel()).isEqualTo("");
    }

    @Test
    public void populationParticipantsLabelIsBlankIfPopulationParticipantsIsBlank() {
        p.setPopulationParticipants("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getPopulationParticipants()).isEqualTo("");
        assertThat(ps.getPopulationParticipantsLabel()).isEqualTo("");
    }

    @Test
    public void populationDurationLabelIsBlankIfPopulationDurationIsBlank() {
        p.setPopulationDuration("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getPopulationDuration()).isEqualTo("");
        assertThat(ps.getPopulationDurationLabel()).isEqualTo("");
    }

    @Test
    public void exposurePollutantLabelIsBlankIfExposurePollutantIsBlank() {
        p.setExposurePollutant("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getExposurePollutant()).isEqualTo("");
        assertThat(ps.getExposurePollutantLabel()).isEqualTo("");
    }

    @Test
    public void exposureAssessmentLabelIsBlankIfExposureAssessmentIsBlank() {
        p.setExposureAssessment("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getExposureAssessment()).isEqualTo("");
        assertThat(ps.getExposureAssessmentLabel()).isEqualTo("");
    }

    @Test
    public void resultExposureRangeLabelIsBlankIfResultExposureRangeIsBlank() {
        p.setResultExposureRange("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getResultExposureRange()).isEqualTo("");
        assertThat(ps.getResultExposureRangeLabel()).isEqualTo("");
    }

    @Test
    public void methodStatisticsLabelIsBlankIfMethodStatisticsIsBlank() {
        p.setMethodStatistics("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodStatistics()).isEqualTo("");
        assertThat(ps.getMethodStatisticsLabel()).isEqualTo("");
    }

    @Test
    public void methodConfoundersLabelIsBlankIfMethodConfoundersIsBlank() {
        p.setMethodConfounders("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodConfounders()).isEqualTo("");
        assertThat(ps.getMethodConfoundersLabel()).isEqualTo("");
    }

    @Test
    public void resultEffectEstimateLabelIsBlankIfResultIsBlank() {
        p.setResultEffectEstimate("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getResultEffectEstimate()).isEqualTo("");
        assertThat(ps.getResultEffectEstimateLabel()).isEqualTo("");
    }

    @Test
    public void commentLabelIsBlankIfCommentIsBlank() {
        p.setComment("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getComment()).isEqualTo("");
        assertThat(ps.getCommentLabel()).isEqualTo("");
    }
}
