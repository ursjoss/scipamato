package ch.difty.scipamato.web.jasper.summaryshort;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.web.jasper.JasperEntityTest;

public class PaperSummaryShortTest extends JasperEntityTest {

    private PaperSummaryShort ps;

    @Test
    public void instantiatingUsingIndividualFields() {
        ps = new PaperSummaryShort(NUMBER, AUTHORS, TITLE, LOCATION, GOALS, POPULATION_PLACE, METHODS, RESULT_EFFECT_ESTIMATE, COMMENT, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_EFFECT_ESTIMATE_LABEL, COMMENT_LABEL, HEADER_PART, BRAND,
                CREATED_BY);

        assertPaperSummaryShort();
    }

    @Test
    public void instantiatingUsingPaper() {
        ps = new PaperSummaryShort(p, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_EFFECT_ESTIMATE_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertPaperSummaryShort();
    }

    private void assertPaperSummaryShort() {
        assertThat(ps.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(ps.getAuthors()).isEqualTo(AUTHORS);
        assertThat(ps.getTitle()).isEqualTo(TITLE);
        assertThat(ps.getLocation()).isEqualTo(LOCATION);
        assertThat(ps.getGoals()).isEqualTo(GOALS);
        assertThat(ps.getPopulationPlace()).isEqualTo(POPULATION_PLACE);
        assertThat(ps.getMethods()).isEqualTo(METHODS);
        assertThat(ps.getResultEffectEstimate()).isEqualTo(RESULT_EFFECT_ESTIMATE);
        assertThat(ps.getComment()).isEqualTo(COMMENT);

        assertThat(ps.getPopulationPlaceLabel()).isEqualTo(POPULATION_PLACE_LABEL);
        assertThat(ps.getMethodsLabel()).isEqualTo(METHODS_LABEL);
        assertThat(ps.getResultEffectEstimateLabel()).isEqualTo(RESULT_EFFECT_ESTIMATE_LABEL);
        assertThat(ps.getCommentLabel()).isEqualTo(COMMENT_LABEL);

        assertThat(ps.getHeader()).isEqualTo(HEADER_PART + " " + NUMBER);
        assertThat(ps.getBrand()).isEqualTo(BRAND);
        assertThat(ps.getCreatedBy()).isEqualTo(CREATED_BY);
    }

    @Test
    public void withNullFieldsWherePossible_providesEmptyStrings() {
        ps = new PaperSummaryShort(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(ps.getNumber()).isEmpty();
        assertThat(ps.getAuthors()).isEmpty();
        assertThat(ps.getTitle()).isEmpty();
        assertThat(ps.getLocation()).isEmpty();
        assertThat(ps.getGoals()).isEmpty();
        assertThat(ps.getPopulationPlace()).isEmpty();
        assertThat(ps.getMethods()).isEmpty();
        assertThat(ps.getComment()).isEmpty();
        assertThat(ps.getResultEffectEstimate()).isEmpty();

        assertThat(ps.getPopulationPlaceLabel()).isEmpty();
        assertThat(ps.getMethodsLabel()).isEmpty();
        assertThat(ps.getResultEffectEstimateLabel()).isEmpty();
        assertThat(ps.getCommentLabel()).isEmpty();

        assertThat(ps.getHeader()).isEmpty();
        assertThat(ps.getBrand()).isEmpty();
        assertThat(ps.getCreatedBy()).isEmpty();
    }

    @Test
    public void withNullFieldsExceptId_providesHeaderWithIdOnly() {
        ps = new PaperSummaryShort(NUMBER, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(ps.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(ps.getHeader()).isEqualTo(String.valueOf(NUMBER));
    }

    @Test
    public void withNullFieldsExceptHeaderPart_providesHeaderPartOnly() {
        ps = new PaperSummaryShort(null, null, null, null, null, null, null, null, null, null, null, null, null, HEADER_PART, null, null);

        assertThat(ps.getNumber()).isEmpty();
        assertThat(ps.getHeader()).isEqualTo(HEADER_PART);
    }

    @Test
    public void populationPlaceLabelIsBlankIfPopulationPlaceIsBlank() {
        p.setPopulationPlace("");
        ps = new PaperSummaryShort(p, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_EFFECT_ESTIMATE_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getPopulationPlace()).isEqualTo("");
        assertThat(ps.getPopulationPlaceLabel()).isEqualTo("");
    }

    @Test
    public void methodsLabelIsBlankIfMethodsIsBlank() {
        p.setMethods("");
        ps = new PaperSummaryShort(p, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_EFFECT_ESTIMATE_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getMethods()).isEqualTo("");
        assertThat(ps.getMethodsLabel()).isEqualTo("");
    }

    @Test
    public void resultEffectEstimateLabelIsBlankIfResultIsBlank() {
        p.setResultEffectEstimate("");
        ps = new PaperSummaryShort(p, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_EFFECT_ESTIMATE_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getResultEffectEstimate()).isEqualTo("");
        assertThat(ps.getResultEffectEstimateLabel()).isEqualTo("");
    }

    @Test
    public void commentLabelIsBlankIfCommentIsBlank() {
        p.setComment("");
        ps = new PaperSummaryShort(p, POPULATION_PLACE_LABEL, METHODS_LABEL, RESULT_EFFECT_ESTIMATE_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getComment()).isEqualTo("");
        assertThat(ps.getCommentLabel()).isEqualTo("");
    }
}
