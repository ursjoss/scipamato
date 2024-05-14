package ch.difty.scipamato.core.web.resources.jasper

internal class PaperSummaryTableReportResourceReferenceTest :
    JasperReportResourceReferenceTest<PaperSummaryTableReportResourceReference>() {

    override val resourceReference: PaperSummaryTableReportResourceReference
        get() = PaperSummaryTableReportResourceReference.get()

    override val reportBaseName: String
        get() = "paper_summary_table_A4"

    override val resourceReferencePath: String
        get() = "ch.difty.scipamato.core.web.resources.jasper.PaperSummaryTableReportResourceReference"
}
