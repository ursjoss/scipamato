package ch.difty.scipamato.core.web.resources.jasper

internal class PaperReviewReportResourceReferenceTest : JasperReportResourceReferenceTest<PaperReviewReportResourceReference>() {

    override val resourceReference: PaperReviewReportResourceReference
        get() = PaperReviewReportResourceReference.get()

    override val reportBaseName: String
        get() = "paper_review_A4"

    override val resourceReferencePath: String
        get() = "ch.difty.scipamato.core.web.resources.jasper.PaperReviewReportResourceReference"
}
