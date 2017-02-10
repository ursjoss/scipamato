package ch.difty.sipamato.web.jasper.summarytable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.web.jasper.JasperEntityTest;

public class PaperSummaryTableTest extends JasperEntityTest {

    private static final String BRAND = "brand";
    private static final String CAPTION = "caption";

    private PaperSummaryTable pst;

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        try {
            new PaperSummaryTable(null, true, "foo", "bar");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullArgumentException.class).hasMessage("paper must not be null.");
        }
    }

    @Test
    public void constructionWithPaper_includingResults() {
        pst = new PaperSummaryTable(p, true, CAPTION, BRAND);
        assertPst();
        assertThat(pst.getResult()).isEqualTo(RESULT);
    }

    private void assertPst() {
        assertThat(pst.getCaption()).isEqualTo(CAPTION);
        assertThat(pst.getBrand()).isEqualTo(BRAND);

        assertThat(pst.getId()).isEqualTo(String.valueOf(ID));
        assertThat(pst.getFirstAuthor()).isEqualTo(FIRST_AUTHOR);
        assertThat(pst.getPublicationYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
        assertThat(pst.getGoals()).isEqualTo(GOALS);
        assertThat(pst.getTitle()).isEqualTo(TITLE);

        assertThat(pst.getCodesOfClass1()).isEqualTo("1F");
        assertThat(pst.getCodesOfClass4()).isEqualTo("4A,4C");
        assertThat(pst.getCodesOfClass7()).isEqualTo("7B");
    }

    @Test
    public void constructionWithPaper_notIncludingResults() {
        pst = new PaperSummaryTable(p, false, CAPTION, BRAND);
        assertPst();
        assertThat(pst.getResult()).isEmpty();
    }

    @Test
    public void constructionWithPaperWithNoCodeOfClass7_returnsBlank() {
        p.clearCodes();
        pst = new PaperSummaryTable(p, true, CAPTION, BRAND);
        assertThat(pst.getCodesOfClass4()).isEqualTo("");
    }
}
