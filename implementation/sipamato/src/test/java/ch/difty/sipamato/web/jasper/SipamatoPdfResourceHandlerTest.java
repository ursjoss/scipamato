package ch.difty.sipamato.web.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.web.jasper.SipamatoPdfExporterConfiguration.Builder;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;

public class SipamatoPdfResourceHandlerTest {

    @Test
    public void testWithDefaultConfig() {
        SipamatoPdfResourceHandler h = new SipamatoPdfResourceHandler();
        assertThat(h.getContentType()).isEqualTo("application/pdf");
        assertThat(h.getExtension()).isEqualTo("pdf");
        validateDefaultConfig(h);
    }

    private void validateDefaultConfig(SipamatoPdfResourceHandler h) {
        JRAbstractExporter<PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput, JRPdfExporterContext> exporter = h.newExporter();
        assertThat(exporter).isNotNull();
        assertThat(exporter.getExporterKey()).isEqualTo("net.sf.jasperreports.pdf");
        assertThat(exporter.getExporterPropertiesPrefix()).isEqualTo("net.sf.jasperreports.export.pdf.");
    }

    @Test
    public void testWithExplicitConfigNull() {
        SipamatoPdfResourceHandler h = new SipamatoPdfResourceHandler(null);
        validateDefaultConfig(h);
    }

    @Test
    public void testWithExplicitConfig() {
        PdfExporterConfiguration c = new Builder("hp", 1l).withAuthor("a").build();
        SipamatoPdfResourceHandler h = new SipamatoPdfResourceHandler(c);
        validateDefaultConfig(h);
    }
}
