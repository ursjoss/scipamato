package ch.difty.sipamato.web.jasper;

import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;

public class SipamatoPdfResourceHandler extends PdfResourceHandler {

    private static final long serialVersionUID = 1L;

    private final PdfExporterConfiguration config;

    public SipamatoPdfResourceHandler() {
        this.config = makeDefaultExporterConfig();
    }

    private SipamatoPdfExporterConfiguration makeDefaultExporterConfig() {
        return new SipamatoPdfExporterConfiguration.Builder(null).build();
    }

    public SipamatoPdfResourceHandler(final PdfExporterConfiguration config) {
        if (config != null) {
            this.config = config;
        } else {
            this.config = makeDefaultExporterConfig();
        }
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
