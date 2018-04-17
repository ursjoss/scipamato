package ch.difty.scipamato.core.web.resources.jasper;

public class PaperSummaryReportResourceReferenceTest
    extends JasperReportResourceReferenceTest<PaperSummaryReportResourceReference> {

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
        return "ch.difty.scipamato.core.web.resources.jasper.PaperSummaryReportResourceReference";
    }
}
