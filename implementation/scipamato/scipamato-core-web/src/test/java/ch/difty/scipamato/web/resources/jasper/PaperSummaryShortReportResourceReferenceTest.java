package ch.difty.scipamato.web.resources.jasper;

public class PaperSummaryShortReportResourceReferenceTest extends JasperReportResourceReferenceTest<PaperSummaryShortReportResourceReference> {

    @Override
    protected PaperSummaryShortReportResourceReference getResourceReference() {
        return PaperSummaryShortReportResourceReference.get();
    }

    @Override
    protected String getReportBaseName() {
        return "paper_summary_short_A4";
    }

    @Override
    protected String getResourceReferencePath() {
        return "ch.difty.scipamato.web.resources.jasper.PaperSummaryShortReportResourceReference";
    }
}
