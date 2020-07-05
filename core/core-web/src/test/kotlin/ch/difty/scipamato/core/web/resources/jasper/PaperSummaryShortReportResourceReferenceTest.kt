package ch.difty.scipamato.core.web.resources.jasper

internal class PaperSummaryShortReportResourceReferenceTest :
    JasperReportResourceReferenceTest<PaperSummaryShortReportResourceReference>() {

    override val resourceReference: PaperSummaryShortReportResourceReference
        get() = PaperSummaryShortReportResourceReference.get()

    override val reportBaseName: String
        get() = "paper_summary_short_A4"

    override val resourceReferencePath: String
        get() = "ch.difty.scipamato.core.web.resources.jasper.PaperSummaryShortReportResourceReference"
}
