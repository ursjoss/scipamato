package ch.difty.scipamato.core.web.resources.jasper

internal class PaperSummaryReportResourceReferenceTest :
    JasperReportResourceReferenceTest<PaperSummaryReportResourceReference>() {

    override val resourceReference: PaperSummaryReportResourceReference
        get() = PaperSummaryReportResourceReference.get()

    override val reportBaseName: String
        get() = "paper_summary_A4"

    override val resourceReferencePath: String
        get() = "ch.difty.scipamato.core.web.resources.jasper.PaperSummaryReportResourceReference"
}
