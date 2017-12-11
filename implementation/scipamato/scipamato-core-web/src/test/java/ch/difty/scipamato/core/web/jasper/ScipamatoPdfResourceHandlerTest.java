package ch.difty.scipamato.core.web.jasper;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.core.web.jasper.ScipamatoPdfExporterConfiguration.Builder;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;

public class ScipamatoPdfResourceHandlerTest {

    @Test
    public void testWithDefaultConfig() {
        ScipamatoPdfResourceHandler h = new ScipamatoPdfResourceHandler();
        assertThat(h.getContentType()).isEqualTo("application/pdf");
        assertThat(h.getExtension()).isEqualTo("pdf");
        validateDefaultConfig(h);
    }

    private void validateDefaultConfig(ScipamatoPdfResourceHandler h) {
        JRAbstractExporter<PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput, JRPdfExporterContext> exporter = h.newExporter();
        assertThat(exporter).isNotNull();
        assertThat(exporter.getExporterKey()).isEqualTo("net.sf.jasperreports.pdf");
        assertThat(exporter.getExporterPropertiesPrefix()).isEqualTo("net.sf.jasperreports.export.pdf.");
    }

    @Test
    public void testWithExplicitConfigNull() {
        ScipamatoPdfResourceHandler h = new ScipamatoPdfResourceHandler(null);
        validateDefaultConfig(h);
    }

    @Test
    public void testWithExplicitConfig() {
        PdfExporterConfiguration c = new Builder("hp", 1l).withAuthor("a").build();
        ScipamatoPdfResourceHandler h = new ScipamatoPdfResourceHandler(c);
        validateDefaultConfig(h);
    }
}
