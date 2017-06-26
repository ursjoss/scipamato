package ch.difty.scipamato.web.jasper.summary;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.web.jasper.JasperEntityTest;

public class PaperSummaryTest extends JasperEntityTest {

    private PaperSummary ps;

    @Test
    public void instantiatingUsingIndividualFields() {
        ps = new PaperSummary(NUMBER, AUTHORS, TITLE, LOCATION, GOALS, POPULATION, METHODS, RESULT, COMMENT, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND,
                CREATED_BY);

        assertPaperSummary();
    }

    @Test
    public void instantiatingUsingPaper() {
        ps = new PaperSummary(p, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertPaperSummary();
    }

    private void assertPaperSummary() {
        assertThat(ps.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(ps.getAuthors()).isEqualTo(AUTHORS);
        assertThat(ps.getTitle()).isEqualTo(TITLE);
        assertThat(ps.getLocation()).isEqualTo(LOCATION);
        assertThat(ps.getGoals()).isEqualTo(GOALS);
        assertThat(ps.getPopulation()).isEqualTo(POPULATION);
        assertThat(ps.getMethods()).isEqualTo(METHODS);
        assertThat(ps.getResult()).isEqualTo(RESULT);
        assertThat(ps.getComment()).isEqualTo(COMMENT);

        assertThat(ps.getPopulationLabel()).isEqualTo(POPULATION_LABEL);
        assertThat(ps.getMethodsLabel()).isEqualTo(METHODS_LABEL);
        assertThat(ps.getResultLabel()).isEqualTo(RESULT_LABEL);
        assertThat(ps.getCommentLabel()).isEqualTo(COMMENT_LABEL);

        assertThat(ps.getHeader()).isEqualTo(HEADER_PART + " " + NUMBER);
        assertThat(ps.getBrand()).isEqualTo(BRAND);
        assertThat(ps.getCreatedBy()).isEqualTo(CREATED_BY);
    }

    @Test
    public void withNullFieldsWherePossible_providesEmptyStrings() {
        ps = new PaperSummary(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(ps.getNumber()).isEmpty();
        assertThat(ps.getAuthors()).isEmpty();
        assertThat(ps.getTitle()).isEmpty();
        assertThat(ps.getLocation()).isEmpty();
        assertThat(ps.getGoals()).isEmpty();
        assertThat(ps.getPopulation()).isEmpty();
        assertThat(ps.getMethods()).isEmpty();
        assertThat(ps.getComment()).isEmpty();
        assertThat(ps.getResult()).isEmpty();

        assertThat(ps.getPopulationLabel()).isEmpty();
        assertThat(ps.getMethodsLabel()).isEmpty();
        assertThat(ps.getResultLabel()).isEmpty();
        assertThat(ps.getCommentLabel()).isEmpty();

        assertThat(ps.getHeader()).isEmpty();
        assertThat(ps.getBrand()).isEmpty();
        assertThat(ps.getCreatedBy()).isEmpty();
    }

    @Test
    public void withNullFieldsExceptId_providesHeaderWithIdOnly() {
        ps = new PaperSummary(NUMBER, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(ps.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(ps.getHeader()).isEqualTo(String.valueOf(NUMBER));
    }

    @Test
    public void withNullFieldsExceptHeaderPart_providesHeaderPartOnly() {
        ps = new PaperSummary(null, null, null, null, null, null, null, null, null, null, null, null, null, HEADER_PART, null, null);

        assertThat(ps.getNumber()).isEmpty();
        assertThat(ps.getHeader()).isEqualTo(HEADER_PART);
    }

    @Test
    public void populationLabelIsBlankIfPopulationIsBlank() {
        p.setPopulation("");
        ps = new PaperSummary(p, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getPopulation()).isEqualTo("");
        assertThat(ps.getPopulationLabel()).isEqualTo("");
    }

    @Test
    public void methodsLabelIsBlankIfMethodsIsBlank() {
        p.setMethods("");
        ps = new PaperSummary(p, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getMethods()).isEqualTo("");
        assertThat(ps.getMethodsLabel()).isEqualTo("");
    }

    @Test
    public void resultLabelIsBlankIfResultIsBlank() {
        p.setResult("");
        ps = new PaperSummary(p, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getResult()).isEqualTo("");
        assertThat(ps.getResultLabel()).isEqualTo("");
    }

    @Test
    public void commentLabelIsBlankIfCommentIsBlank() {
        p.setComment("");
        ps = new PaperSummary(p, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, COMMENT_LABEL, HEADER_PART, BRAND);

        assertThat(ps.getComment()).isEqualTo("");
        assertThat(ps.getCommentLabel()).isEqualTo("");
    }

}
