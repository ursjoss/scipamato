package ch.difty.sipamato.web.resources.jasper;

public class JasperReportException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public JasperReportException(final Exception ex) {
        super(ex);
    }
    
    public JasperReportException(final String msg) {
        super(msg);
    }

    
}
