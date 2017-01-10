package ch.difty.sipamato.web.resources.jasper;

import org.apache.wicket.resource.FileSystemResourceReference;

public class JasperReportFileSystemResourceReference extends FileSystemResourceReference {

    private static final long serialVersionUID = 1L;

    public JasperReportFileSystemResourceReference(Class<?> scope, String name) {
        super(scope, name + ".jrxml");
    }

}
