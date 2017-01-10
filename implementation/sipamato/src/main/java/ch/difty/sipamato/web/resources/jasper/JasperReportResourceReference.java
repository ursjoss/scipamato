package ch.difty.sipamato.web.resources.jasper;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

public abstract class JasperReportResourceReference extends PackageResourceReference implements Serializable {

    private static final long serialVersionUID = 1L;

    private JasperReport compileReport;
    private final boolean cacheJasperReport;

    public JasperReportResourceReference(final Class<?> scope, final String name, final boolean cacheJasperReport) {
        super(scope, name + ".jrxml");
        this.cacheJasperReport = cacheJasperReport;
    }

    private IResourceStream getResourceStream() {
        final IResourceStream rs = getResource().getResourceStream();
        if (rs != null) {
            return rs;
        } else {
            throw new JasperReportException("Unable to locate resource stream for jasper file '" + getName() + "'");
        }
    }

    public InputStream getInputStream() {
        try {
            return getResourceStream().getInputStream();
        } catch (ResourceStreamNotFoundException e) {
            throw new JasperReportException(e);
        }
    }

    public JasperReport getReport() {
        if (!cacheJasperReport || compileReport == null) {
            try {
                compileReport = JasperCompileManager.compileReport(getInputStream());
            } catch (JRException e) {
                throw new JasperReportException(e);
            }
        }
        return compileReport;
    }

}
