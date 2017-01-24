package ch.difty.sipamato.web.resources.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.web.WicketTest;

public class PaperSummaryReportResourceReferenceTest extends WicketTest {

    private static final String JASPER_JRXML = "paper_summary_A4.jrxml";

    private final PaperSummaryReportResourceReference ref = PaperSummaryReportResourceReference.get();

    @Test
    public void testDefaultAttributes() {
        assertThat(ref.getScope()).isEqualTo(PaperSummaryReportResourceReference.class);
        assertThat(ref.getName()).isEqualTo(JASPER_JRXML);
        assertThat(ref.getExtension()).isEqualTo("jrxml");
        assertThat(ref.getDependencies()).isEmpty();
        assertThat(ref.getLocale()).isNull();
    }

    @Test
    public void testKey() {
        assertThat(ref.getKey().getScope()).isEqualTo("ch.difty.sipamato.web.resources.jasper.PaperSummaryReportResourceReference");
        assertThat(ref.getKey().getName()).isEqualTo(JASPER_JRXML);
        assertThat(ref.getKey().getLocale()).isNull();
        assertThat(ref.getKey().getStyle()).isNull();
        assertThat(ref.getKey().getVariation()).isNull();
    }

    @Test
    public void gettingReport() {
        assertThat(ref.getReport().getName()).isEqualTo("paper_summary_A4");
    }

}
