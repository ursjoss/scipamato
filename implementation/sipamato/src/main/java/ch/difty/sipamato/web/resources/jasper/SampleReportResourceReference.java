package ch.difty.sipamato.web.resources.jasper;

public class SampleReportResourceReference extends JasperReportResourceReference {

    private static final long serialVersionUID = 1L;

    private static final SampleReportResourceReference INSTANCE = new SampleReportResourceReference();

    private SampleReportResourceReference(final boolean cacheJasperReport) {
        super(SampleReportResourceReference.class, "sample", cacheJasperReport);
    }

    private SampleReportResourceReference() {
        this(true);
    }


    public static SampleReportResourceReference get() {
        return INSTANCE;
    }

}
