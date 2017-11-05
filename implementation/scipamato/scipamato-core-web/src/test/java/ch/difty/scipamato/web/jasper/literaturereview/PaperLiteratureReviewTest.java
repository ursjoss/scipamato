package ch.difty.scipamato.web.jasper.literaturereview;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.JasperEntityTest;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PaperLiteratureReviewTest extends JasperEntityTest {

    private PaperLiteratureReview plr;
    private ReportHeaderFields rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields.builder(HEADER_PART, BRAND).numberLabel(NUMBER_LABEL).captionLabel(CAPTION).build();
    }

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        try {
            new PaperLiteratureReview(null, rhf);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("p must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullReportHeaderFields_throws() {
        try {
            new PaperLiteratureReview(new Paper(), null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("rhf must not be null.");
        }
    }

    @Test
    public void instantiatingWithValidFieldsAndValidLabels() {
        plr = new PaperLiteratureReview(p, rhf);

        validateFields();

        assertThat(plr.getCaption()).isEqualTo(CAPTION);
        assertThat(plr.getBrand()).isEqualTo(BRAND);
        assertThat(plr.getNumberLabel()).isEqualTo(NUMBER_LABEL);
    }

    private void validateFields() {
        assertThat(plr.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(plr.getAuthors()).isEqualTo(AUTHORS);
        assertThat(plr.getPublicationYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
        assertThat(plr.getTitle()).isEqualTo(TITLE);
        assertThat(plr.getLocation()).isEqualTo(LOCATION);
        assertThat(plr.getPubmedLink()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/" + PM_ID);
    }

    @Test
    public void pubmedLink_withPaperWithPmIdNull_isBlank() {
        p.setPmId(null);
        plr = new PaperLiteratureReview(p, rhf);
        assertThat(plr.getPubmedLink()).isEqualTo("");
    }

    @Test
    public void number_withPaperWithNumberNull_isBlank() {
        p.setNumber(null);
        plr = new PaperLiteratureReview(p, rhf);
        assertThat(plr.getNumber()).isEqualTo("");
    }

    @Test
    public void publicationYear_withPaperWithYearNull_isBlank() {
        p.setPublicationYear(null);
        plr = new PaperLiteratureReview(p, rhf);
        assertThat(plr.getPublicationYear()).isEqualTo("");
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(PaperLiteratureReview.class).withRedefinedSuperclass().suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testingToString() {
        plr = new PaperLiteratureReview(p, rhf);
        assertThat(plr.toString()).isEqualTo(
                "PaperLiteratureReview(number=100, authors=authors, publicationYear=2017, title=title, location=location, pubmedLink=https://www.ncbi.nlm.nih.gov/pubmed/1234, caption=caption, brand=brand, numberLabel=numberLabel)");
    }

}
