package ch.difty.scipamato.web.jasper.review;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.JasperEntityTest;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

public class PaperReviewTest extends JasperEntityTest {

    private PaperReview pr;
    private ReportHeaderFields rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        ReportHeaderFields.Builder b = new ReportHeaderFields.Builder("", BRAND).withNumber(NUMBER_LABEL)
                .withAuthorYear(AUTHOR_YEAR_LABEL)
                .withPopulationPlace(POPULATION_PLACE_LABEL)
                .withPopulationPariticpants(POPULATION_PARTICIPANTS_LABEL)
                .withMethodOutcome(METHOD_OUTCOME_LABEL)
                .withExposurePollutant(EXPOSURE_POLLUTANT_LABEL)
                .withMethodStudyDesign(METHOD_STUDY_DESIGN_LABEL)
                .withPopulationDuration(POPULATION_DURATION_LABEL)
                .withExposureAssessment(EXPOSURE_ASSESSMENT_LABEL)
                .withResultExposureRange(RESULT_EXPOSURE_RANGE_LABEL)
                .withMethodConfounders(METHOD_CONFOUNDERS_LABEL)
                .withResultEffectEstimate(RESULT_EFFECT_ESTIMATE_LABEL)
                .withComment(COMMENT_LABEL);
        return b.build();
    }

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        try {
            new PaperReview(null, rhf);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("p must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullReportHeaderFields_throws() {
        try {
            new PaperReview(new Paper(), null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("rhf must not be null.");
        }
    }

    @Test
    public void instantiatingWithValidFieldsAndvalidLabels() {
        pr = new PaperReview(p, rhf);

        assertFieldValues();

        assertThat(pr.getNumberLabel()).isEqualTo(NUMBER_LABEL);
        assertThat(pr.getAuthorYearLabel()).isEqualTo(AUTHOR_YEAR_LABEL);
        assertThat(pr.getPopulationPlaceLabel()).isEqualTo(POPULATION_PLACE_LABEL);
        assertThat(pr.getMethodOutcomeLabel()).isEqualTo(METHOD_OUTCOME_LABEL);
        assertThat(pr.getExposurePollutantLabel()).isEqualTo(EXPOSURE_POLLUTANT_LABEL);
        assertThat(pr.getMethodStudyDesignLabel()).isEqualTo(METHOD_STUDY_DESIGN_LABEL);
        assertThat(pr.getPopulationDurationLabel()).isEqualTo(POPULATION_DURATION_LABEL);
        assertThat(pr.getPopulationParticipantsLabel()).isEqualTo(POPULATION_PARTICIPANTS_LABEL);
        assertThat(pr.getExposureAssessmentLabel()).isEqualTo(EXPOSURE_ASSESSMENT_LABEL);
        assertThat(pr.getResultExposureRangeLabel()).isEqualTo(RESULT_EXPOSURE_RANGE_LABEL);
        assertThat(pr.getMethodConfoundersLabel()).isEqualTo(METHOD_CONFOUNDERS_LABEL);
        assertThat(pr.getResultEffectEstimateLabel()).isEqualTo(RESULT_EFFECT_ESTIMATE_LABEL);
        assertThat(pr.getBrand()).isEqualTo(BRAND);
        assertThat(pr.getCreatedBy()).isEqualTo(CREATED_BY);
    }

    private void assertFieldValues() {
        assertThat(pr.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(pr.getAuthorYear()).isEqualTo(FIRST_AUTHOR + " " + String.valueOf(PUBLICATION_YEAR));
        assertThat(pr.getPopulationPlace()).isEqualTo(POPULATION_PLACE);
        assertThat(pr.getMethodOutcome()).isEqualTo(METHOD_OUTCOME);
        assertThat(pr.getExposurePollutant()).isEqualTo(EXPOSURE_POLLUTANT);
        assertThat(pr.getMethodStudyDesign()).isEqualTo(METHOD_STUDY_DESIGN);
        assertThat(pr.getPopulationDuration()).isEqualTo(POPULATION_DURATION);
        assertThat(pr.getPopulationParticipants()).isEqualTo(POPULATION_PARTICIPANTS);
        assertThat(pr.getExposureAssessment()).isEqualTo(EXPOSURE_ASSESSMENT);
        assertThat(pr.getResultExposureRange()).isEqualTo(RESULT_EXPOSURE_RANGE);
        assertThat(pr.getMethodConfounders()).isEqualTo(METHOD_CONFOUNDERS);
        assertThat(pr.getResultEffectEstimate()).isEqualTo(RESULT_EFFECT_ESTIMATE);
    }

    @Test
    public void authorYear_withNullFirstAuthorAndYear() {
        p.setFirstAuthor(null);
        p.setPublicationYear(null);

        pr = new PaperReview(p, rhf);

        assertThat(pr.getAuthorYear()).isEqualTo("");
    }

    @Test
    public void authorYear_withOnlyFirstAuthor() {
        assertThat(p.getFirstAuthor()).isNotNull();
        p.setPublicationYear(null);

        pr = new PaperReview(p, rhf);

        assertThat(pr.getAuthorYear()).isEqualTo(FIRST_AUTHOR);
    }

    @Test
    public void authorYear_withOnlyPubYear() {
        p.setFirstAuthor(null);
        assertThat(p.getPublicationYear()).isNotNull();

        pr = new PaperReview(p, rhf);

        assertThat(pr.getAuthorYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
    }

}
