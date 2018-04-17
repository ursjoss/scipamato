package ch.difty.scipamato.core.web.resources.jasper;

/**
 * Resource Reference for the Paper Summary Report
 *
 * @author u.joss
 */
public class PaperSummaryReportResourceReference extends JasperReportResourceReference {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "paper_summary_A4";

    // Use the respective constructor depending on whether your developing the
    // report or are in production.
    // Note: the concrete file that is read resides in
    // target/classes/ch/difty/scipamato/core/web/resources/jasper/
    private static final PaperSummaryReportResourceReference INSTANCE = new PaperSummaryReportResourceReference();

    /**
     * Use this constructor in production
     */
    private PaperSummaryReportResourceReference() {
        this(true);
    }

    /**
     * Use this constructor with parameter {@code false} while developing the
     * report.
     *
     * @param cacheReport
     *     if {@code true} the compiled report is cached. If {@code false} it
     *     will be recompiled every time the report is retrieved.
     */
    private PaperSummaryReportResourceReference(final boolean cacheReport) {
        super(PaperSummaryReportResourceReference.class, NAME, cacheReport);
    }

    public static PaperSummaryReportResourceReference get() {
        return INSTANCE;
    }

}
