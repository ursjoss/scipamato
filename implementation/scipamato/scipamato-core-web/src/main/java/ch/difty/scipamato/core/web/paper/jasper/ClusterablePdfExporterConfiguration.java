package ch.difty.scipamato.core.web.paper.jasper;

import org.apache.wicket.util.io.IClusterable;

import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * Custom {@link PdfExporterConfiguration} which is clusterable, and thus ready
 * for wicket.
 *
 * @author u.joss
 */
public interface ClusterablePdfExporterConfiguration extends PdfExporterConfiguration, IClusterable {

}
