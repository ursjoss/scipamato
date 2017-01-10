package ch.difty.sipamato.web.resources.jasper;

import java.io.IOException;
import java.io.InputStream;

public class SampleReportFileSystemResourceReference extends JasperReportFileSystemResourceReference {

    private static final long serialVersionUID = 1L;

    private static final SampleReportFileSystemResourceReference INSTANCE = new SampleReportFileSystemResourceReference();

    public SampleReportFileSystemResourceReference() {
        super(SampleReportFileSystemResourceReference.class, "sample");
    }

    public static SampleReportFileSystemResourceReference get() {
        return INSTANCE;
    }

    public InputStream getInputStream() {
        JasperFileSystemResource fsr;
        try {
            fsr = (JasperFileSystemResource) getResource();
            return fsr.getInputStream();
        } catch (IOException ex) {
            throw new JasperReportException(ex);
        }
    }

}
