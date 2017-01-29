package ch.difty.sipamato.web.jasper.summary_table;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.web.jasper.JasperEntityTest;

public class PaperSummaryTableTest extends JasperEntityTest {

    private static final String CAPTION_PART = "caption";
    private static final String CAPTION = CAPTION_PART + " " + ID;

    private PaperSummaryTable pst;

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        try {
            new PaperSummaryTable(null, "foo");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullArgumentException.class).hasMessage("paper must not be null.");
        }
    }

    @Test
    public void constructionWithPaper() {
        pst = new PaperSummaryTable(p, CAPTION_PART);
        assertThat(pst.getCaption()).isEqualTo(CAPTION);
        assertThat(pst.getId()).isEqualTo(String.valueOf(ID));
        assertThat(pst.getFirstAuthor()).isEqualTo(FIRST_AUTHOR);
        assertThat(pst.getPublicationYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
        assertThat(pst.getGoals()).isEqualTo(GOALS);
        assertThat(pst.getTitle()).isEqualTo(TITLE);
        assertThat(pst.getResult()).isEqualTo(RESULT);

        assertThat(pst.getCodesOfClass1()).isEqualTo("1F");
        assertThat(pst.getCodesOfClass4()).isEqualTo("4A,4C");
        assertThat(pst.getCodesOfClass7()).isEqualTo("7B");
    }

    @Test
    public void constructionWithPaper_withNullCaption_returnsIdOnly() {
        pst = new PaperSummaryTable(p, null);
        assertThat(pst.getCaption()).isEqualTo("1");
    }

    @Test
    public void constructionWithPaperWithNoId_withNullCaption_returnsBlankCaption() {
        p.setId(null);
        pst = new PaperSummaryTable(p, null);
        assertThat(pst.getCaption()).isEqualTo("");
    }

    @Test
    public void constructionWithPaperWithNoCodeOfClass7_returnsBlank() {
        p.clearCodes();
        pst = new PaperSummaryTable(p, CAPTION_PART);
        assertThat(pst.getCodesOfClass4()).isEqualTo("");
    }
}
