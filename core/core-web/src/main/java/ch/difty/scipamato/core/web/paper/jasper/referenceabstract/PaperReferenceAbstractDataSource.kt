package ch.difty.scipamato.core.web.paper.jasper.referenceabstract

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler
import ch.difty.scipamato.core.web.resources.jasper.PaperReferenceAbstractResourceReference
import net.sf.jasperreports.engine.JasperReport
import java.util.HashMap

/**
 * DataSource for the PaperReferenceAbstractReport.
 *
 * The meta fields are not contained within a paper instance and make up the caption
 */
class PaperReferenceAbstractDataSource(
    dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter?>,
    private val reportHeaderFields: ReportHeaderFields,
    config: ClusterablePdfExporterConfiguration,
) : JasperPaperDataSource<PaperReferenceAbstract>(
    ScipamatoPdfResourceHandler(config),
    FILE_NAME,
    dataProvider,
) {
    override fun getParameterMap(): HashMap<String, Any> = hashMapOf("show_goal" to false)
    override fun getReport(): JasperReport = PaperReferenceAbstractResourceReference.get().getReport()
    override fun makeEntity(p: Paper): PaperReferenceAbstract = PaperReferenceAbstract(p, reportHeaderFields)

    companion object {
        private const val serialVersionUID = 1L
        private const val FILE_NAME = "paper_reference_abstract"
    }
}
