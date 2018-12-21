package ch.difty.scipamato.core.web.resources.jasper;

import java.io.InputStream;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * Static resource reference for a jasper report, wrapping the xml report
 * definition (jrxml), but also providing access to the compiled
 * {@link JasperReport}.
 * <p>
 * The compiled report is typically cached, in order to avoid multiple
 * compilation, which is time intensive. However, e.g. when developing the
 * report, you might want to override the caching, so the compilation picks up
 * the latest changes in the report definition file.
 *
 * @author u.joss
 */
@Slf4j
@EqualsAndHashCode
public abstract class JasperReportResourceReference extends PackageResourceReference implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean      cacheReport;
    private       JasperReport report;

    /**
     * Resource reference for a particular jasper report, wrapping the XML report
     * definition.
     *
     * @param scope
     *     part of the generated URL
     * @param name
     *     part of the generated URL - the name of the report (without the extension .jrxml
     * @param cacheReport
     *     if {@code true} the report is only compiled from xml when the
     *     report is requested for the first time.
     */
    JasperReportResourceReference(final Class<?> scope, final String name, final boolean cacheReport) {
        super(scope, name + ".jrxml");
        this.cacheReport = cacheReport;
    }

    /**
     * @return the compiled {@link JasperReport} object.
     * @throws JasperReportException
     *     an unchecked runtime exception wrapping two checked exceptions
     *     ({@link ResourceStreamNotFoundException} and {@link JRException})
     */
    public JasperReport getReport() {
        if (!cacheReport || report == null) {
            try {
                report = JasperCompileManager.compileReport(getInputStream());
                log.info("Successfully compiled JasperReport {}...", getName());
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

    boolean isCacheReport() {
        return cacheReport;
    }
}
