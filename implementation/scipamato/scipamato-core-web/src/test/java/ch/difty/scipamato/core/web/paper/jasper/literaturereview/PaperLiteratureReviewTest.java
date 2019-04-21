package ch.difty.scipamato.core.web.paper.jasper.literaturereview;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

public class PaperLiteratureReviewTest extends JasperEntityTest {

    private static final String PUBMED_BASE_URL = "baseUrl/";

    private PaperLiteratureReview plr;
    private ReportHeaderFields    rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .numberLabel(NUMBER_LABEL)
            .captionLabel(CAPTION)
            .pubmedBaseUrl(PUBMED_BASE_URL)
            .build();
    }

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        assertDegenerateSupplierParameter(() -> new PaperLiteratureReview(null, rhf), "p");
    }

    @Test
    public void degenerateConstruction_withNullReportHeaderFields_throws() {
        assertDegenerateSupplierParameter(() -> new PaperLiteratureReview(new Paper(), null), "rhf");
    }

    @Test
    public void degenerateConstruction_withNullPubmedBaseUrl_throws() {
        ReportHeaderFields rhf2 = ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .numberLabel(NUMBER_LABEL)
            .captionLabel(CAPTION)
            .build();
        assertThat(rhf2.getPubmedBaseUrl()).isNull();
        assertDegenerateSupplierParameter(() -> new PaperLiteratureReview(new Paper(), rhf2), "pubmedBaseUrl");
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
        assertThat(plr.getGoals()).isEqualTo(GOALS);
        assertThat(plr.getLocation()).isEqualTo(LOCATION);
        assertThat(plr.getDoi()).isEqualTo(DOI);
        assertThat(plr.getPubmedLink()).isEqualTo("baseUrl/" + PM_ID);
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
        EqualsVerifier
            .forClass(PaperLiteratureReview.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void testingToString() {
        plr = new PaperLiteratureReview(p, rhf);
        assertThat(plr.toString()).isEqualTo(
            "PaperLiteratureReview(number=100, authors=authors, publicationYear=2017, title=title, goals=goals, "
            + "location=location, doi=doi, pubmedLink=baseUrl/1234, caption=caption, brand=brand, numberLabel=numberLabel)");
    }

}
