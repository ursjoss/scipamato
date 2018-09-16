package ch.difty.scipamato.core.web.resources.jasper;

/**
 * Unchecked JasperReport Exception that can wrap a checked exception or be
 * instantiated with a string message only.
 *
 * @author u.joss
 */
class JasperReportException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    JasperReportException(final Exception ex) {
        super(ex);
    }

    JasperReportException(final String msg) {
        super(msg);
    }

}
