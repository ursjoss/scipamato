package ch.difty.sipamato.web.pages.jasper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.jasperreports.EmbeddedJRReport;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import ch.difty.sipamato.auth.Roles;
import ch.difty.sipamato.web.jasper.WebappDataSource;
import ch.difty.sipamato.web.pages.BasePage;

/**
 * Simple Jasper reports example with PDF output and a jasper reports panel..
 * 
 * @author Eelco Hillenius
 */
@MountPath("/pdfe")
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class SimplePdfPage extends BasePage<Void> {
    private static final long serialVersionUID = 1L;

    public SimplePdfPage(final PageParameters parameters) {
        super(parameters);
    }

    /**
     * Constructor.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        final File reportFile = new File(context.getRealPath("/reports/WebappReport.jasper"));
        System.out.println(reportFile);
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("BaseDir", new File(context.getRealPath("/reports")));

        JRResource pdfResource = new JRConcreteResource<PdfResourceHandler>(reportFile, new PdfResourceHandler());
        pdfResource.setReportParameters(parameters);
        pdfResource.setReportDataSource(new WebappDataSource());

        add(new EmbeddedJRReport("report", pdfResource));
        // TODO embedded geht derzeit nicht.
    }

    /**
     * @see org.apache.wicket.Component#isVersioned()
     */
    @Override
    public boolean isVersioned() {
        return false;
    }
}
