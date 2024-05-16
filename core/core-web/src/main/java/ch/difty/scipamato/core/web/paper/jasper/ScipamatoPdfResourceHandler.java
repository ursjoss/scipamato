package ch.difty.scipamato.core.web.paper.jasper;

import java.util.Objects;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

/**
 * Export configuration aware resource handler for PDF documents
 *
 * @author u.joss
 */
public class ScipamatoPdfResourceHandler extends PdfResourceHandler {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private final ClusterablePdfExporterConfiguration config;

    /**
     * Default constructor creating its own default exporter configuration.
     */
    public ScipamatoPdfResourceHandler() {
        this.config = makeDefaultExporterConfig();
    }

    /**
     * Constructor accepting a {@link ClusterablePdfExporterConfiguration}. If null is passed as config,
     * a default exporter config will apply.
     *
     * @param config
     *     the {@link ClusterablePdfExporterConfiguration}
     */
    public ScipamatoPdfResourceHandler(@Nullable final ClusterablePdfExporterConfiguration config) {
        this.config = Objects.requireNonNullElseGet(config, this::makeDefaultExporterConfig);
    }

    private ScipamatoPdfExporterConfiguration makeDefaultExporterConfig() {
        return new ScipamatoPdfExporterConfiguration.Builder(null).build();
    }

    /**
     * @see org.wicketstuff.jasperreports.handlers.IJRResourceHandler#newExporter()
     */
    @NotNull
    @Override
    public JRAbstractExporter<PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput, JRPdfExporterContext> newExporter() {
        final JRPdfExporter jrPdfExporter = new JRPdfExporter();
        jrPdfExporter.setConfiguration(config);
        return jrPdfExporter;
    }
}
