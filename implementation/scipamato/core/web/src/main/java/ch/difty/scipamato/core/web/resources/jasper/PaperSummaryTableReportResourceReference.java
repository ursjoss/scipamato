package ch.difty.scipamato.core.web.resources.jasper;

import org.jetbrains.annotations.NotNull;

/**
 * Resource Reference for the Paper Summary Table Report
 *
 * @author u.joss
 */
public class PaperSummaryTableReportResourceReference extends JasperReportResourceReference {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "paper_summary_table_A4";

    // Use the respective constructor depending on whether your developing the
    // report or are in production.
    // Note: the concrete file that is read resides in
    // target/classes/ch/difty/scipamato/core/web/resources/jasper/
    private static final PaperSummaryTableReportResourceReference INSTANCE = new PaperSummaryTableReportResourceReference();

    /**
     * Use this constructor in production
     */
    private PaperSummaryTableReportResourceReference() {
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
    @SuppressWarnings("SameParameterValue")
    private PaperSummaryTableReportResourceReference(final boolean cacheReport) {
        super(PaperSummaryTableReportResourceReference.class, NAME, cacheReport);
    }

    @NotNull
    public static PaperSummaryTableReportResourceReference get() {
        return INSTANCE;
    }
}
