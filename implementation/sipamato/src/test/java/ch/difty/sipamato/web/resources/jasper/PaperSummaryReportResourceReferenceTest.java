package ch.difty.sipamato.web.resources.jasper;

public class PaperSummaryReportResourceReferenceTest extends ReportResourceReferenceTest<PaperSummaryReportResourceReference> {

    @Override
    protected PaperSummaryReportResourceReference getResourceReference() {
        return PaperSummaryReportResourceReference.get();
    }

    @Override
    protected String getReportBaseName() {
        return "paper_summary_A4";
    }

    @Override
    protected String getResourceReferencePath() {
        return "ch.difty.sipamato.web.resources.jasper.PaperSummaryReportResourceReference";
    }
}
