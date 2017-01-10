package ch.difty.sipamato.web.resources.jasper;

public class StudySummaryReportResourceReference extends JasperReportResourceReference {

    private static final long serialVersionUID = 1L;

    // Instantiate the Reference with <code>cacheJasperReport = false</code> to compile the jrxml every time to the jasper file (for development purposes) 
    private static final StudySummaryReportResourceReference INSTANCE = new StudySummaryReportResourceReference(false);

    private StudySummaryReportResourceReference(final boolean cacheJasperReport) {
        super(StudySummaryReportResourceReference.class, "study_summary_A4", cacheJasperReport);
    }
    
    private StudySummaryReportResourceReference() {
        this(true);
    }

    public static StudySummaryReportResourceReference get() {
        return INSTANCE;
    }

}
