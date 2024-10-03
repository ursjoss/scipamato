package ch.difty.scipamato.core.web.paper.jasper;

import net.sf.jasperreports.pdf.PdfExporterConfiguration;
import org.apache.wicket.util.io.IClusterable;

/**
 * Custom {@link PdfExporterConfiguration} which is clusterable, and thus ready
 * for wicket.
 *
 * @author u.joss
 */
public interface ClusterablePdfExporterConfiguration extends PdfExporterConfiguration, IClusterable {
}
