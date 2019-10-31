package ch.difty.scipamato.core.web.resources.jasper;

import org.jetbrains.annotations.NotNull;

/**
 * Unchecked JasperReport Exception that can wrap a checked exception or be
 * instantiated with a string message only.
 *
 * @author u.joss
 */
class JasperReportException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    JasperReportException(@NotNull final Exception ex) {
        super(ex);
    }

    JasperReportException(@NotNull final String msg) {
        super(msg);
    }
}
