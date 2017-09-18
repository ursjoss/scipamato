package ch.difty.scipamato.web.jasper.literaturereview;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.JasperEntityTest;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

public class PaperLiteratureReviewTest extends JasperEntityTest {

    private PaperLiteratureReview plr;
    private ReportHeaderFields rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        ReportHeaderFields.Builder b = new ReportHeaderFields.Builder(HEADER_PART, BRAND).withCaption(CAPTION).withNumber(NUMBER_LABEL);
        return b.build();
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
    public void instantiatingWithValidFieldsAndvalidLabels() {
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

}
