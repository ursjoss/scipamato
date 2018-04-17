package ch.difty.scipamato.core.web.resources.jasper;

/**
 * Resource Reference for the Paper Summary Short Report
 *
 * @author u.joss
 */
public class PaperSummaryShortReportResourceReference extends JasperReportResourceReference {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "paper_summary_short_A4";

    // Use the respective constructor depending on whether your developing the
    // report or are in production.
    // Note: the concrete file that is read resides in
    // target/classes/ch/difty/scipamato/core/web/resources/jasper/
    private static final PaperSummaryShortReportResourceReference INSTANCE = new PaperSummaryShortReportResourceReference();

    /**
     * Use this constructor in production
     */
    private PaperSummaryShortReportResourceReference() {
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
    private PaperSummaryShortReportResourceReference(final boolean cacheReport) {
        super(PaperSummaryShortReportResourceReference.class, NAME, cacheReport);
    }

    public static PaperSummaryShortReportResourceReference get() {
        return INSTANCE;
    }

}
