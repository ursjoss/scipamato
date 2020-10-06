package ch.difty.scipamato.core.web.paper.jasper.review

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler
import ch.difty.scipamato.core.web.resources.jasper.PaperReviewReportResourceReference
import net.sf.jasperreports.engine.JasperReport

/**
 * DataSource for the PaperReviewReport.
 *
 * The meta fields are not contained within a paper instance and make up e.g.
 * localized labels, the brand or part of the header.
 */
class PaperReviewDataSource(
    dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter>,
    private val reportHeaderFields: ReportHeaderFields,
    config: ClusterablePdfExporterConfiguration?,
) : JasperPaperDataSource<PaperReview>(
    ScipamatoPdfResourceHandler(config),
    FILE_BASE_NAME,
    dataProvider
) {
    override fun getReport(): JasperReport = PaperReviewReportResourceReference.get().getReport()
    override fun makeEntity(p: Paper): PaperReview = PaperReview(p, reportHeaderFields)

    companion object {
        private const val FILE_BASE_NAME = "paper_review"
        private const val serialVersionUID = 1L
    }
}
