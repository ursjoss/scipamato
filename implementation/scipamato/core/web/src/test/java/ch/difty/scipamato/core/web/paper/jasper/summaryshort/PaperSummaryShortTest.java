package ch.difty.scipamato.core.web.paper.jasper.summaryshort;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

class PaperSummaryShortTest extends JasperEntityTest {

    private final ReportHeaderFields rhf = newReportHeaderFields();

    private PaperSummaryShort ps;

    @Test
    void instantiating() {
        ps = new PaperSummaryShort(p, rhf);
        assertPaperSummaryShort();
    }

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
        assertThat(ps.getConclusion()).isEqualTo(CONCLUSION);
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
        assertThat(ps.getConclusionLabel()).isEqualTo(CONCLUSION_LABEL);
        assertThat(ps.getCommentLabel()).isEqualTo(COMMENT_LABEL);

        assertThat(ps.getHeader()).isEqualTo(HEADER_PART + " " + NUMBER);
        assertThat(ps.getBrand()).isEqualTo(BRAND);
        assertThat(ps.getCreatedBy()).isEqualTo(CREATED_BY);
    }

    @Test
    void goalsLabelIsBlankIfGoalsIsBlank() {
        p.setGoals("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getGoals()).isEqualTo("");
        assertThat(ps.getGoalsLabel()).isEqualTo("");
    }

    @Test
    void methodsLabelIsBlankIfMethodsIsBlank() {
        p.setMethods("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethods()).isEqualTo("");
        assertThat(ps.getMethodsLabel()).isEqualTo("");
    }

    @Test
    void methodOutcomeLabelIsBlankIfMethodOutcomeIsBlank() {
        p.setMethodOutcome("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodOutcome()).isEqualTo("");
        assertThat(ps.getMethodOutcomeLabel()).isEqualTo("");
    }

    @Test
    void resultMeasuredOutcomeLabelIsBlankIfResultMeasuredOutcomeIsBlank() {
        p.setResultMeasuredOutcome("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getResultMeasuredOutcome()).isEqualTo("");
        assertThat(ps.getResultMeasuredOutcomeLabel()).isEqualTo("");
    }

    @Test
    void methodStudyDesignLabelIsBlankIfMethodStudyDesignIsBlank() {
        p.setMethodStudyDesign("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodStudyDesign()).isEqualTo("");
        assertThat(ps.getMethodStudyDesignLabel()).isEqualTo("");
    }

    @Test
    void populationPlaceLabelIsBlankIfPopulationPlaceIsBlank() {
        p.setPopulationPlace("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getPopulationPlace()).isEqualTo("");
        assertThat(ps.getPopulationPlaceLabel()).isEqualTo("");
    }

    @Test
    void populationParticipantsLabelIsBlankIfPopulationParticipantsIsBlank() {
        p.setPopulationParticipants("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getPopulationParticipants()).isEqualTo("");
        assertThat(ps.getPopulationParticipantsLabel()).isEqualTo("");
    }

    @Test
    void populationDurationLabelIsBlankIfPopulationDurationIsBlank() {
        p.setPopulationDuration("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getPopulationDuration()).isEqualTo("");
        assertThat(ps.getPopulationDurationLabel()).isEqualTo("");
    }

    @Test
    void exposurePollutantLabelIsBlankIfExposurePollutantIsBlank() {
        p.setExposurePollutant("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getExposurePollutant()).isEqualTo("");
        assertThat(ps.getExposurePollutantLabel()).isEqualTo("");
    }

    @Test
    void exposureAssessmentLabelIsBlankIfExposureAssessmentIsBlank() {
        p.setExposureAssessment("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getExposureAssessment()).isEqualTo("");
        assertThat(ps.getExposureAssessmentLabel()).isEqualTo("");
    }

    @Test
    void resultExposureRangeLabelIsBlankIfResultExposureRangeIsBlank() {
        p.setResultExposureRange("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getResultExposureRange()).isEqualTo("");
        assertThat(ps.getResultExposureRangeLabel()).isEqualTo("");
    }

    @Test
    void methodStatisticsLabelIsBlankIfMethodStatisticsIsBlank() {
        p.setMethodStatistics("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodStatistics()).isEqualTo("");
        assertThat(ps.getMethodStatisticsLabel()).isEqualTo("");
    }

    @Test
    void methodConfoundersLabelIsBlankIfMethodConfoundersIsBlank() {
        p.setMethodConfounders("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getMethodConfounders()).isEqualTo("");
        assertThat(ps.getMethodConfoundersLabel()).isEqualTo("");
    }

    @Test
    void resultEffectEstimateLabelIsBlankIfResultIsBlank() {
        p.setResultEffectEstimate("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getResultEffectEstimate()).isEqualTo("");
        assertThat(ps.getResultEffectEstimateLabel()).isEqualTo("");
    }

    @Test
    void conclusionLabelIsBlankIfConclusionIsBlank() {
        p.setConclusion("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getConclusion()).isEqualTo("");
        assertThat(ps.getConclusionLabel()).isEqualTo("");
    }

    @Test
    void commentLabelIsBlankIfCommentIsBlank() {
        p.setComment("");
        ps = new PaperSummaryShort(p, rhf);

        assertThat(ps.getComment()).isEqualTo("");
        assertThat(ps.getCommentLabel()).isEqualTo("");
    }

    @Test
    void equalsVerify() {
        EqualsVerifier
            .forClass(PaperSummaryShort.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE)
            .verify();
    }
}
