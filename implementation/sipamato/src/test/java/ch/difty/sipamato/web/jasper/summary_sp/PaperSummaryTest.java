package ch.difty.sipamato.web.jasper.summary_sp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.entity.Paper;

public class PaperSummaryTest {

    private static final long ID = 1l;
    private static final String AUTHORS = "authors";
    private static final String TITLE = "title";
    private static final String LOCATION = "location";
    private static final String GOALS = "goals";
    private static final String POPULATION = "population";
    private static final String METHODS = "methods";
    private static final String RESULT = "results";
    private static final String POPULATION_LABEL = "populationLabel";
    private static final String METHODS_LABEL = "methodsLabel";
    private static final String RESULT_LABEL = "resultLabel";
    private static final String HEADER_PART = "headerPart";
    private static final String BRAND = "brand";
    private static final String CREATED_BY = "creatingUser";

    private PaperSummary ps;

    @Test
    public void instantiatingUsingIndividualFields() {
        ps = new PaperSummary(ID, AUTHORS, TITLE, LOCATION, GOALS, POPULATION, METHODS, RESULT, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, HEADER_PART, BRAND, CREATED_BY);

        assertPaperSummary();
    }

    @Test
    public void instantiatingUsingPaper() {
        Paper p = new Paper();
        p.setId(ID);
        p.setAuthors(AUTHORS);
        p.setTitle(TITLE);
        p.setLocation(LOCATION);
        p.setGoals(GOALS);
        p.setPopulation(POPULATION);
        p.setMethods(METHODS);
        p.setResult(RESULT);
        p.setCreatedByName(CREATED_BY);
        ps = new PaperSummary(p, POPULATION_LABEL, METHODS_LABEL, RESULT_LABEL, HEADER_PART, BRAND);

        assertPaperSummary();
    }

    private void assertPaperSummary() {
        assertThat(ps.getId()).isEqualTo(String.valueOf(ID));
        assertThat(ps.getAuthors()).isEqualTo(AUTHORS);
        assertThat(ps.getTitle()).isEqualTo(TITLE);
        assertThat(ps.getLocation()).isEqualTo(LOCATION);
        assertThat(ps.getGoals()).isEqualTo(GOALS);
        assertThat(ps.getPopulation()).isEqualTo(POPULATION);
        assertThat(ps.getMethods()).isEqualTo(METHODS);
        assertThat(ps.getResult()).isEqualTo(RESULT);

        assertThat(ps.getPopulationLabel()).isEqualTo(POPULATION_LABEL);
        assertThat(ps.getMethodsLabel()).isEqualTo(METHODS_LABEL);
        assertThat(ps.getResultLabel()).isEqualTo(RESULT_LABEL);

        assertThat(ps.getHeader()).isEqualTo(HEADER_PART + " " + ID);
        assertThat(ps.getBrand()).isEqualTo(BRAND);
        assertThat(ps.getCreatedBy()).isEqualTo(CREATED_BY);
    }

    @Test
    public void withNullFieldsWherePossible_providesEmptyStrings() {
        ps = new PaperSummary(null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(ps.getId()).isEmpty();
        assertThat(ps.getAuthors()).isEmpty();
        assertThat(ps.getTitle()).isEmpty();
        assertThat(ps.getLocation()).isEmpty();
        assertThat(ps.getGoals()).isEmpty();
        assertThat(ps.getPopulation()).isEmpty();
        assertThat(ps.getMethods()).isEmpty();
        assertThat(ps.getResult()).isEmpty();

        assertThat(ps.getPopulationLabel()).isEmpty();
        assertThat(ps.getMethodsLabel()).isEmpty();
        assertThat(ps.getResultLabel()).isEmpty();

        assertThat(ps.getHeader()).isEmpty();
        assertThat(ps.getBrand()).isEmpty();
        assertThat(ps.getCreatedBy()).isEmpty();
    }

    @Test
    public void withNullFieldsExceptId_providesHeaderWithIdOnly() {
        ps = new PaperSummary(ID, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(ps.getId()).isEqualTo(String.valueOf(ID));
        assertThat(ps.getHeader()).isEqualTo(String.valueOf(ID));
    }

    @Test
    public void withNullFieldsExceptHeaderPart_providesHeaderPartOnly() {
        ps = new PaperSummary(null, null, null, null, null, null, null, null, null, null, null, HEADER_PART, null, null);

        assertThat(ps.getId()).isEmpty();
        assertThat(ps.getHeader()).isEqualTo(HEADER_PART);
    }

}
