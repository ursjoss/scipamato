package ch.difty.sipamato.web.resources.jasper;

/**
 * Resource Reference for the Paper Summary Report
 *
 * @author u.joss
 */
public class PaperSummaryReportResourceReference extends JasperReportResourceReference {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "paper_summary_A4";

    // Use the respective constructor depending on whether your developing the report or are in production.
    private static final PaperSummaryReportResourceReference INSTANCE = new PaperSummaryReportResourceReference();

    /**
     * Use this constructor in production
     */
    private PaperSummaryReportResourceReference() {
        this(true);
    }

    /**
     * Use this constructor with parameter <code>false</code> while developing the report.
     *
     * @param cacheReport if <code>true</code> the compiled report is cached. 
     *                    If <code>false</code> it will be recompiled every time the report is retrieved.
     */
    private PaperSummaryReportResourceReference(final boolean cacheReport) {
        super(PaperSummaryReportResourceReference.class, NAME, cacheReport);
    }

    public static PaperSummaryReportResourceReference get() {
        return INSTANCE;
    }

}
