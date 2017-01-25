package ch.difty.sipamato.web.resources.jasper;

/**
 * Resource Reference for the Paper Review Report
 *
 * @author u.joss
 */
public class PaperReviewReportResourceReference extends JasperReportResourceReference {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "paper_review_A4";

    // Use the respective constructor depending on whether your developing the report or are in production.
    private static final PaperReviewReportResourceReference INSTANCE = new PaperReviewReportResourceReference(false);

    /**
     * Use this constructor in production
     */
    private PaperReviewReportResourceReference() {
        this(true);
    }

    /**
     * Use this constructor with parameter <code>false</code> while developing the report.
     *
     * @param cacheReport if <code>true</code> the compiled report is cached. 
     *                    If <code>false</code> it will be recompiled every time the report is retrieved.
     */
    private PaperReviewReportResourceReference(final boolean cacheReport) {
        super(PaperReviewReportResourceReference.class, NAME, cacheReport);
    }

    public static PaperReviewReportResourceReference get() {
        return INSTANCE;
    }

}
