package ch.difty.scipamato.web.resources.jasper;

/**
 * Unchecked JasperReport Exception that can wrap a checked exception
 * or be instantiated with a string message only.
 *
 * @author u.joss
 */
public class JasperReportException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JasperReportException(final Exception ex) {
        super(ex);
    }

    public JasperReportException(final String msg) {
        super(msg);
    }

}
