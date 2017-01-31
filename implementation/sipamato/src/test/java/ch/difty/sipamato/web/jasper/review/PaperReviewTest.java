package ch.difty.sipamato.web.jasper.review;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.web.jasper.JasperEntityTest;

public class PaperReviewTest extends JasperEntityTest {

    private PaperReview pr;

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        try {
            new PaperReview(null, "idl", "ayl", "ppl", "mol", "epl", "msdl", "pdl", "ppl", "eal", "rerl", "mcl", "reel", "b", "cb");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("paper must not be null.");
        }
    }

    @Test
    public void instantiatingWithAllNullFields_returnsBlankValues() {
        pr = new PaperReview(new Paper(), null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getId()).isEmpty();
        assertThat(pr.getAuthorYear()).isEmpty();
        assertThat(pr.getPopulationPlace()).isEmpty();
        assertThat(pr.getMethodOutcome()).isEmpty();
        assertThat(pr.getExposurePollutant()).isEmpty();
        assertThat(pr.getMethodStudyDesign()).isEmpty();
        assertThat(pr.getPopulationDuration()).isEmpty();
        assertThat(pr.getPopulationParticipants()).isEmpty();
        assertThat(pr.getExposureAssessment()).isEmpty();
        assertThat(pr.getResultExposureRange()).isEmpty();
        assertThat(pr.getMethodConfounders()).isEmpty();
        assertThat(pr.getResultEffectEstimate()).isEmpty();

        assertBlankLabels();
    }

    private void assertBlankLabels() {
        assertThat(pr.getIdLabel()).isEmpty();
        assertThat(pr.getAuthorYearLabel()).isEmpty();
        assertThat(pr.getPopulationPlaceLabel()).isEmpty();
        assertThat(pr.getMethodOutcomeLabel()).isEmpty();
        assertThat(pr.getExposurePollutantLabel()).isEmpty();
        assertThat(pr.getMethodStudyDesignLabel()).isEmpty();
        assertThat(pr.getPopulationDurationLabel()).isEmpty();
        assertThat(pr.getPopulationParticipantsLabel()).isEmpty();
        assertThat(pr.getExposureAssessmentLabel()).isEmpty();
        assertThat(pr.getResultExposureRangeLabel()).isEmpty();
        assertThat(pr.getMethodConfoundersLabel()).isEmpty();
        assertThat(pr.getResultEffectEstimateLabel()).isEmpty();
        assertThat(pr.getBrand()).isEmpty();
        assertThat(pr.getCreatedBy()).isEmpty();
    }

    @Test
    public void instantiatingWithValidFieldsButNullLabels() {
        pr = new PaperReview(p, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertFieldValues();
        assertBlankLabels();
    }

    private void assertFieldValues() {
        assertThat(pr.getId()).isEqualTo(String.valueOf(ID));
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
    public void instantiatingWithValidFieldsAndvalidLabels() {
        pr = new PaperReview(p, "idl", "ayl", "ppl", "mol", "epl", "msdl", "pdl", "ppl", "eal", "rerl", "mcl", "reel", "b", "cb");

        assertFieldValues();

        assertThat(pr.getIdLabel()).isEqualTo("idl");
        assertThat(pr.getAuthorYearLabel()).isEqualTo("ayl");
        assertThat(pr.getPopulationPlaceLabel()).isEqualTo("ppl");
        assertThat(pr.getMethodOutcomeLabel()).isEqualTo("mol");
        assertThat(pr.getExposurePollutantLabel()).isEqualTo("epl");
        assertThat(pr.getMethodStudyDesignLabel()).isEqualTo("msdl");
        assertThat(pr.getPopulationDurationLabel()).isEqualTo("pdl");
        assertThat(pr.getPopulationParticipantsLabel()).isEqualTo("ppl");
        assertThat(pr.getExposureAssessmentLabel()).isEqualTo("eal");
        assertThat(pr.getResultExposureRangeLabel()).isEqualTo("rerl");
        assertThat(pr.getMethodConfoundersLabel()).isEqualTo("mcl");
        assertThat(pr.getResultEffectEstimateLabel()).isEqualTo("reel");
        assertThat(pr.getBrand()).isEqualTo("b");
        assertThat(pr.getCreatedBy()).isEqualTo("cb");
    }

    @Test
    public void authorYear_withNullFirstAuthorAndYear() {
        p.setFirstAuthor(null);
        p.setPublicationYear(null);

        pr = new PaperReview(p, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getAuthorYear()).isEqualTo("");
    }

    @Test
    public void authorYear_withOnlyFirstAuthor() {
        assertThat(p.getFirstAuthor()).isNotNull();
        p.setPublicationYear(null);

        pr = new PaperReview(p, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getAuthorYear()).isEqualTo(FIRST_AUTHOR);
    }

    @Test
    public void authorYear_withOnlyPubYear() {
        p.setFirstAuthor(null);
        assertThat(p.getPublicationYear()).isNotNull();

        pr = new PaperReview(p, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getAuthorYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
    }

}
