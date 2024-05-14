package ch.difty.scipamato.core.web.resources.jasper;

import org.jetbrains.annotations.NotNull;

/**
 * Resource Reference for the Literature Review Report
 *
 * @author u.joss
 */
public class PaperLiteratureReviewReportResourceReference extends JasperReportResourceReference {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private static final String NAME = "paper_literature_review_A4";

    // Use the respective constructor depending on whether your developing the
    // report or are in production.
    // Note: the concrete file that is read resides in
    // target/classes/ch/difty/scipamato/core/web/resources/jasper/
    private static final PaperLiteratureReviewReportResourceReference INSTANCE = new PaperLiteratureReviewReportResourceReference();

    /**
     * Use this constructor in production
     */
    private PaperLiteratureReviewReportResourceReference() {
        this(true);
    }

    /**
     * Use this constructor with parameter {@code false} while developing the report.
     *
     * @param cacheReport
     *     if {@code true} the compiled report is cached. If {@code false} it
     *     will be recompiled every time the report is retrieved.
     */
    @SuppressWarnings("SameParameterValue")
    private PaperLiteratureReviewReportResourceReference(final boolean cacheReport) {
        super(PaperLiteratureReviewReportResourceReference.class, NAME, cacheReport);
    }

    @NotNull
    public static PaperLiteratureReviewReportResourceReference get() {
        return INSTANCE;
    }
}
