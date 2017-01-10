package ch.difty.sipamato.web.resources.jasper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.resource.FileSystemResource;

public class JasperFileSystemResource extends FileSystemResource {

    private static final long serialVersionUID = 1L;

    public InputStream getInputStream() throws IOException {
        return super.getInputStream();
    }
}
