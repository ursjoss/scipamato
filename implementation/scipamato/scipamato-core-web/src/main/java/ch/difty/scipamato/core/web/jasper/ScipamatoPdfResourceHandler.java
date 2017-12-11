package ch.difty.scipamato.core.web.jasper;

import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;

/**
 * Export configuration aware resource handler for PDF documents
 *
 * @author u.joss
 */
public class ScipamatoPdfResourceHandler extends PdfResourceHandler {

    private static final long serialVersionUID = 1L;

    private final PdfExporterConfiguration config;

    /**
     * Default constructor creating its own default exporter configuration.
     */
    public ScipamatoPdfResourceHandler() {
        this.config = makeDefaultExporterConfig();
    }

    /**
     * Constructor accepting a {@link PdfExporterConfiguration}
     */
    public ScipamatoPdfResourceHandler(final PdfExporterConfiguration config) {
        if (config != null) {
            this.config = config;
        } else {
            this.config = makeDefaultExporterConfig();
        }
    }

    private ScipamatoPdfExporterConfiguration makeDefaultExporterConfig() {
        return new ScipamatoPdfExporterConfiguration.Builder(null).build();
    }

    /**
     * @see org.wicketstuff.jasperreports.handlers.IJRResourceHandler#newExporter()
     */
    @Override
    public JRAbstractExporter<PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput, JRPdfExporterContext> newExporter() {
        final JRPdfExporter jrPdfExporter = new JRPdfExporter();
        jrPdfExporter.setConfiguration(config);
        return jrPdfExporter;
    }

}
