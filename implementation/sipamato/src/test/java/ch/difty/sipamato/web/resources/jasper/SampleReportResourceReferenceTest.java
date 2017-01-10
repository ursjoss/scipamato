package ch.difty.sipamato.web.resources.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.web.WicketTest;

public class SampleReportResourceReferenceTest extends WicketTest {

    private static final String JASPER_JRXML = "sample.jrxml";

    private final SampleReportResourceReference ref = SampleReportResourceReference.get();

    @Test
    public void testDefaultAttributes() {
        assertThat(ref.getScope()).isEqualTo(SampleReportResourceReference.class);
        assertThat(ref.getName()).isEqualTo(JASPER_JRXML);
        assertThat(ref.getExtension()).isEqualTo("jrxml");
        assertThat(ref.getDependencies()).isEmpty();
        assertThat(ref.getLocale()).isNull();
    }

    @Test
    public void testKey() {
        assertThat(ref.getKey().getScope()).isEqualTo("ch.difty.sipamato.web.resources.jasper.SampleReportResourceReference");
        assertThat(ref.getKey().getName()).isEqualTo(JASPER_JRXML);
        assertThat(ref.getKey().getLocale()).isNull();
        assertThat(ref.getKey().getStyle()).isNull();
        assertThat(ref.getKey().getVariation()).isNull();
    }

    @Test
    public void gettingReport() {
        assertThat(ref.getReport().getName()).isEqualTo("WebappReport");
    }

    @Test
    public void gettingInputStream() {
        assertThat(ref.getInputStream()).isNotNull();
    }

    //    TODO have the resource compile the report and provide both the report xml as stream as also the compiled jasper report

}
