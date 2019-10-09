package ch.difty.scipamato.core.web.paper.jasper.summary;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator;
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldWithEmptyMainFieldConcatenator;
import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

class PaperSummaryTest extends JasperEntityTest {

    private final CoreShortFieldConcatenator shortFieldConcatenator = new CoreShortFieldWithEmptyMainFieldConcatenator();

    private PaperSummary       ps;
    private ReportHeaderFields rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .populationLabel(POPULATION_LABEL)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .resultLabel(RESULT_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build();
    }

    @Test
    void instantiating() {
        ps = new PaperSummary(p, shortFieldConcatenator, rhf);
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
    void populationLabelIsBlankIfPopulationIsBlank() {
        p.setPopulation("");
        ps = newPaperSummary();

        assertThat(ps.getPopulation()).isEqualTo("");
        assertThat(ps.getPopulationLabel()).isEqualTo("");
    }

    private PaperSummary newPaperSummary() {
        return new PaperSummary(p, shortFieldConcatenator, rhf);
    }

    @Test
    void methodsLabelIsBlankIfMethodsIsBlank() {
        p.setMethods("");
        ps = newPaperSummary();

        assertThat(ps.getMethods()).isEqualTo("");
        assertThat(ps.getMethodsLabel()).isEqualTo("");
    }

    @Test
    void resultLabelIsBlankIfResultIsBlank() {
        p.setResult("");
        ps = newPaperSummary();

        assertThat(ps.getResult()).isEqualTo("");
        assertThat(ps.getResultLabel()).isEqualTo("");
    }

    @Test
    void commentLabelIsBlankIfCommentIsBlank() {
        p.setComment("");
        ps = newPaperSummary();

        assertThat(ps.getComment()).isEqualTo("");
        assertThat(ps.getCommentLabel()).isEqualTo("");
    }

    @Test
    void headerHasNoNumberIfNumberIsNull() {
        p.setNumber(null);
        ps = newPaperSummary();

        assertThat(ps.getHeader()).isEqualTo("headerPart");
    }

    @Test
    void headerOnlyShowsIdIfHeaderPartIsBlank() {
        rhf = ReportHeaderFields
            .builder("", BRAND)
            .populationLabel(POPULATION_LABEL)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .resultLabel(RESULT_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build();
        p.setNumber(5L);
        ps = newPaperSummary();

        assertThat(ps.getHeader()).isEqualTo("5");
    }

    @Test
    void equalsVerify() {
        EqualsVerifier
            .forClass(PaperSummary.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE)
            .verify();
    }
}
