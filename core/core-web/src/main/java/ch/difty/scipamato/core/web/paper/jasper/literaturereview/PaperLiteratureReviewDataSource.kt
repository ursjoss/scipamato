package ch.difty.scipamato.core.web.paper.jasper.literaturereview

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler
import ch.difty.scipamato.core.web.resources.jasper.PaperLiteratureReviewReportResourceReference
import net.sf.jasperreports.engine.JasperReport

/**
 * DataSource for the PaperLiteratureReviewReport.
 *
 * The meta fields are not contained within a paper instance and make up the caption
 */
class PaperLiteratureReviewDataSource(
    dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter?>,
    private val reportHeaderFields: ReportHeaderFields,
    config: ClusterablePdfExporterConfiguration,
) : JasperPaperDataSource<PaperLiteratureReview>(
    ScipamatoPdfResourceHandler(config),
    FILE_NAME,
    dataProvider,
) {
    override fun getParameterMap(): HashMap<String, Any> = hashMapOf("show_goal" to false)
    override fun getReport(): JasperReport = PaperLiteratureReviewReportResourceReference.get().getReport()
    override fun makeEntity(p: Paper): PaperLiteratureReview = PaperLiteratureReview(p, reportHeaderFields)

    companion object {
        private const val serialVersionUID = 1L
        private const val FILE_NAME = "paper_literature_review"
    }
}
