package ch.difty.scipamato.core.web.resources.jasper;

class PaperSummaryTableReportResourceReferenceTest
    extends JasperReportResourceReferenceTest<PaperSummaryTableReportResourceReference> {

    @Override
    protected PaperSummaryTableReportResourceReference getResourceReference() {
        return PaperSummaryTableReportResourceReference.get();
    }

    @Override
    protected String getReportBaseName() {
        return "paper_summary_table_A4";
    }

    @Override
    protected String getResourceReferencePath() {
        return "ch.difty.scipamato.core.web.resources.jasper.PaperSummaryTableReportResourceReference";
    }
}
