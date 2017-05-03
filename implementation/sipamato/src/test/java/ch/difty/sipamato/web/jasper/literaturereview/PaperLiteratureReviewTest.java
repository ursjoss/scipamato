package ch.difty.sipamato.web.jasper.literaturereview;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.web.jasper.JasperEntityTest;

public class PaperLiteratureReviewTest extends JasperEntityTest {

    private PaperLiteratureReview plr;

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        try {
            new PaperLiteratureReview(null, "c", "br", "nl");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("paper must not be null.");
        }
    }

    @Test
    public void instantiatingWithAllNullFields_returnsBlankValues() {
        plr = new PaperLiteratureReview(new Paper(), null, null, null);

        assertThat(plr.getNumber()).isEmpty();
        assertThat(plr.getAuthors()).isEmpty();
        assertThat(plr.getPublicationYear()).isEmpty();
        assertThat(plr.getTitle()).isEmpty();
        assertThat(plr.getLocation()).isEmpty();
        assertThat(plr.getPubmedLink()).isEmpty();

        assertThat(plr.getCaption()).isEmpty();
        assertThat(plr.getBrand()).isEmpty();
        assertThat(plr.getNumberLabel()).isEmpty();
    }

    @Test
    public void instantiatingWithValidFieldsButNullLabels() {
        plr = new PaperLiteratureReview(p, null, null, null);

        validateFields();

        assertThat(plr.getCaption()).isEmpty();
        assertThat(plr.getBrand()).isEmpty();
        assertThat(plr.getNumberLabel()).isEmpty();

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
    public void instantiatingWithValidFieldsAndvalidLabels() {
        plr = new PaperLiteratureReview(p, "c", "br", "nl");

        validateFields();

        assertThat(plr.getCaption()).isEqualTo("c");
        assertThat(plr.getBrand()).isEqualTo("br");
        assertThat(plr.getNumberLabel()).isEqualTo("nl");
    }

}
