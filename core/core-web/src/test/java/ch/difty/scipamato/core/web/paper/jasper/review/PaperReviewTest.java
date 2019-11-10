package ch.difty.scipamato.core.web.paper.jasper.review;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

class PaperReviewTest extends JasperEntityTest {

    private final ReportHeaderFields rhf = newReportHeaderFields();

    private PaperReview pr;

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder("", BRAND)
            .numberLabel(NUMBER_LABEL)
            .authorYearLabel(AUTHOR_YEAR_LABEL)
            .populationPlaceLabel(POPULATION_PLACE_LABEL)
            .populationParticipantsLabel(POPULATION_PARTICIPANTS_LABEL)
            .methodOutcomeLabel(METHOD_OUTCOME_LABEL)
            .exposurePollutantLabel(EXPOSURE_POLLUTANT_LABEL)
            .methodStudyDesignLabel(METHOD_STUDY_DESIGN_LABEL)
            .populationDurationLabel(POPULATION_DURATION_LABEL)
            .exposureAssessmentLabel(EXPOSURE_ASSESSMENT_LABEL)
            .resultExposureRangeLabel(RESULT_EXPOSURE_RANGE_LABEL)
            .methodConfoundersLabel(METHOD_CONFOUNDERS_LABEL)
            .resultEffectEstimateLabel(RESULT_EFFECT_ESTIMATE_LABEL)
            .conclusionLabel(CONCLUSION_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build();
    }

    @Test
    void instantiatingWithValidFieldsAndValidLabels() {
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
        assertThat(pr.getConclusionLabel()).isEqualTo(CONCLUSION_LABEL);
        assertThat(pr.getBrand()).isEqualTo(BRAND);
        assertThat(pr.getCreatedBy()).isEqualTo(CREATED_BY);
    }

    @Test
    void instantiatingWithNullNumber_returnsBlank() {
        p.setNumber(null);
        pr = new PaperReview(p, rhf);
        assertThat(pr.getNumber()).isEqualTo("");
    }

    @Test
    void instantiatingWithNullFirstAuthorAndPubYear_returnsBlank() {
        p.setFirstAuthor(null);
        p.setPublicationYear(null);
        pr = new PaperReview(p, rhf);
        assertThat(pr.getAuthorYear()).isEqualTo("");
    }

    @Test
    void instantiatingWithNullFirstAuthorAndPubYear0_returnsBlank() {
        p.setFirstAuthor(null);
        p.setPublicationYear(0);
        pr = new PaperReview(p, rhf);
        assertThat(pr.getAuthorYear()).isEqualTo("");
    }

    @Test
    void instantiatingWithNonNullFirstAuthorButNullPubYear_returnsFirstAuthorOnly() {
        assertThat(p.getFirstAuthor()).isNotNull();
        p.setPublicationYear(null);
        pr = new PaperReview(p, rhf);
        assertThat(pr.getAuthorYear()).isEqualTo("firstAuthor");
    }

    @Test
    void instantiatingWithNonNullFirstAuthorButPubYear0_returnsFirstAuthorOnly() {
        assertThat(p.getFirstAuthor()).isNotNull();
        p.setPublicationYear(0);
        pr = new PaperReview(p, rhf);
        assertThat(pr.getAuthorYear()).isEqualTo("firstAuthor");
    }

    private void assertFieldValues() {
        assertThat(pr.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(pr.getAuthorYear()).isEqualTo(FIRST_AUTHOR + " " + PUBLICATION_YEAR);
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
        assertThat(pr.getConclusion()).isEqualTo(CONCLUSION);
    }

    @Test
    void authorYear_withNullFirstAuthorAndYear() {
        p.setFirstAuthor(null);
        p.setPublicationYear(null);

        pr = new PaperReview(p, rhf);

        assertThat(pr.getAuthorYear()).isEqualTo("");
    }

    @Test
    void authorYear_withOnlyFirstAuthor() {
        assertThat(p.getFirstAuthor()).isNotNull();
        p.setPublicationYear(null);

        pr = new PaperReview(p, rhf);

        assertThat(pr.getAuthorYear()).isEqualTo(FIRST_AUTHOR);
    }

    @Test
    void authorYear_withOnlyPubYear() {
        p.setFirstAuthor(null);
        assertThat(p.getPublicationYear()).isNotNull();

        pr = new PaperReview(p, rhf);

        assertThat(pr.getAuthorYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(PaperReview.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
