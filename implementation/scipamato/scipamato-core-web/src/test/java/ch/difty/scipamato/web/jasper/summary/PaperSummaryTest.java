package ch.difty.scipamato.web.jasper.summary;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.web.jasper.JasperEntityTest;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PaperSummaryTest extends JasperEntityTest {

    private PaperSummary ps;
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
    public void instantiating() {
        ps = new PaperSummary(p, rhf);
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
    public void populationLabelIsBlankIfPopulationIsBlank() {
        p.setPopulation("");
        ps = newPaperSummary();

        assertThat(ps.getPopulation()).isEqualTo("");
        assertThat(ps.getPopulationLabel()).isEqualTo("");
    }

    private PaperSummary newPaperSummary() {
        return new PaperSummary(p, rhf);
    }

    @Test
    public void methodsLabelIsBlankIfMethodsIsBlank() {
        p.setMethods("");
        ps = newPaperSummary();

        assertThat(ps.getMethods()).isEqualTo("");
        assertThat(ps.getMethodsLabel()).isEqualTo("");
    }

    @Test
    public void resultLabelIsBlankIfResultIsBlank() {
        p.setResult("");
        ps = newPaperSummary();

        assertThat(ps.getResult()).isEqualTo("");
        assertThat(ps.getResultLabel()).isEqualTo("");
    }

    @Test
    public void commentLabelIsBlankIfCommentIsBlank() {
        p.setComment("");
        ps = newPaperSummary();

        assertThat(ps.getComment()).isEqualTo("");
        assertThat(ps.getCommentLabel()).isEqualTo("");
    }

    @Test
    public void equalsverify() {
        EqualsVerifier.forClass(PaperSummary.class).withRedefinedSuperclass().suppress(Warning.STRICT_INHERITANCE).verify();
    }
}
