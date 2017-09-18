package ch.difty.scipamato.web.resources.jasper;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Static resource reference for a jasper report, wrapping the xml report definition (jrxml), but also providing
 * access to the compiled {@link JasperReport}.
 *
 * The compiled report is typically cached, in order to avoid multiple compilation, which is time intensive.
 * However, e.g. when developing the report, you might want to override the caching, so the compilation picks
 * up the latest changes in the report definition file.
 *
 * @author u.joss
 */
public abstract class JasperReportResourceReference extends PackageResourceReference implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportResourceReference.class);

    private static final long serialVersionUID = 1L;

    private final boolean cacheReport;
    private JasperReport report;

    /**
     * Resource reference for a particular jasper report, wrapping the XML report definition.
     *
     * @param scope
     * @param name
     *           the name of the report (without the extension .jrxml
     * @param cacheReport
     *           if {@code true} the report is only compiled from xml when the report is requested for the first time. 
     */
    public JasperReportResourceReference(final Class<?> scope, final String name, final boolean cacheReport) {
        super(scope, name + ".jrxml");
        this.cacheReport = cacheReport;
    }

    /**
     * @return the compiled {@link JasperReport} object.
     * @throws {@link JasperReportException}, an unchecked runtime exception wrapping
     *    two checked exceptions ({@link ResourceStreamNotFoundException} and {@link JRException})
     */
    public JasperReport getReport() {
        if (!cacheReport || report == null) {
            try {
                report = JasperCompileManager.compileReport(getInputStream());
                LOGGER.info("Successfully compiled JasperReport {}...", getName());
            } catch (final JRException e) {
                throw new JasperReportException(e);
            }
        }
        return report;
    }

    private InputStream getInputStream() {
        try {
            return getResourceStream().getInputStream();
        } catch (final ResourceStreamNotFoundException e) {
            throw new JasperReportException(e);
        }
    }

    private IResourceStream getResourceStream() {
        final IResourceStream rs = getResource().getResourceStream();
        if (rs != null) {
            return rs;
        } else {
            throw new JasperReportException("Unable to locate resource stream for jasper file '" + getName() + "'");
        }
    }

    public boolean isCacheReport() {
        return cacheReport;
    }
}
