package ch.difty.scipamato.core.web.paper.jasper.summarytable

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler
import ch.difty.scipamato.core.web.resources.jasper.PaperSummaryTableReportResourceReference
import net.sf.jasperreports.engine.JasperReport

/**
 * DataSource for the PaperSummaryTableReport.
 *
 * The report header fields are not contained within a paper instance and make up e.g.
 * localized labels, the brand or part of the header.
 */
class PaperSummaryTableDataSource(
    dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter>,
    private val reportHeaderFields: ReportHeaderFields,
    config: ClusterablePdfExporterConfiguration?,
) : JasperPaperDataSource<PaperSummaryTable?>(
    ScipamatoPdfResourceHandler(config),
    FILE_NAME,
    dataProvider
) {
    override fun getReport(): JasperReport = PaperSummaryTableReportResourceReference.get().getReport()
    override fun makeEntity(p: Paper): PaperSummaryTable = PaperSummaryTable(p, reportHeaderFields)

    companion object {
        private const val serialVersionUID = 1L
        private const val FILE_NAME = "paper_summary_table"
    }
}
