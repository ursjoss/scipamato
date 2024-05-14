package ch.difty.scipamato.core.web.paper.jasper.summary

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler
import ch.difty.scipamato.core.web.resources.jasper.PaperSummaryReportResourceReference
import net.sf.jasperreports.engine.JasperReport

/**
 * DataSource for the PaperSummaryReport.
 *
 * Can be instantiated in different ways, either by passing in
 *
 *  * a single [Paper] + report header fields + export config
 *  * a single [PaperSummary] + export config
 *  * a collection of [PaperSummary] entities + export config or
 *  * an instance of a [AbstractPaperSlimProvider] + report header fields export config
 *
 * The report header fields are not contained within a paper instance and make
 * up e.g. localized labels, the brand or part of the header.
 */
class PaperSummaryDataSource : JasperPaperDataSource<PaperSummary> {

    private lateinit var reportHeaderFields: ReportHeaderFields
    private lateinit var shortFieldConcatenator: CoreShortFieldConcatenator

    /**
     * Build up the paper summary from a [Paper] and any additional
     * information not contained within the paper
     *
     * @param paper instance of [Paper]
     * @param reportHeaderFields collection of localized labels for report fields
     * @param shortFieldConcatenator utility bean to manage the content of the fields method/population/result
     * @param config [ClusterablePdfExporterConfiguration]
     */
    constructor(
        paper: Paper,
        reportHeaderFields: ReportHeaderFields,
        shortFieldConcatenator: CoreShortFieldConcatenator,
        config: ClusterablePdfExporterConfiguration?,
    ) : this(listOf(PaperSummary(paper, shortFieldConcatenator, reportHeaderFields)), config,
        makeSinglePaperBaseName(if (paper.number != null) paper.number.toString() else null)) {
        this.reportHeaderFields = reportHeaderFields
        this.shortFieldConcatenator = shortFieldConcatenator
    }

    /**
     * Populate the report from a single [PaperSummary], using a specific file
     * name including the id of the paper.
     *
     * @param paperSummary single [PaperSummary]
     * @param config the [ClusterablePdfExporterConfiguration]
     */
    internal constructor(
        paperSummary: PaperSummary,
        config: ClusterablePdfExporterConfiguration?,
    ) : this(listOf(paperSummary), config, makeSinglePaperBaseName(paperSummary.number))

    /**
     * Populate the report from a collection of [PaperSummaries][PaperSummary],
     * using a specific file name including the id of the paper.
     *
     * @param paperSummaries collection of [PaperSummary] instances
     * @param config the [ClusterablePdfExporterConfiguration]
     * @param baseName the file name without the extension (.pdf)
     */
    private constructor(
        paperSummaries: Collection<PaperSummary>,
        config: ClusterablePdfExporterConfiguration?,
        baseName: String,
    ) : super(ScipamatoPdfResourceHandler(config), baseName, paperSummaries)

    /**
     * Using the dataProvider for the Result Panel as record source.
     *
     * @param dataProvider the [AbstractPaperSlimProvider]
     * @param reportHeaderFields collection of localized labels for the report fields
     * @param shortFieldConcatenator utility bean to manage the content of the fields method/population/result
     * @param config [ClusterablePdfExporterConfiguration]
     */
    constructor(
        dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter?>,
        reportHeaderFields: ReportHeaderFields,
        shortFieldConcatenator: CoreShortFieldConcatenator,
        config: ClusterablePdfExporterConfiguration?,
    ) : super(ScipamatoPdfResourceHandler(config), BASE_NAME_MULTIPLE, dataProvider) {
        this.reportHeaderFields = reportHeaderFields
        this.shortFieldConcatenator = shortFieldConcatenator
    }

    override fun getReport(): JasperReport = PaperSummaryReportResourceReference.get().getReport()
    override fun makeEntity(p: Paper): PaperSummary = PaperSummary(p, shortFieldConcatenator, reportHeaderFields)

    companion object {
        private const val serialVersionUID = 1L
        private const val BASE_NAME_SINGLE = "paper_summary_no_"
        private const val BASE_NAME_SINGLE_FALLBACK = "paper_summary"
        private const val BASE_NAME_MULTIPLE = "paper_summaries"

        private fun makeSinglePaperBaseName(number: String?): String {
            return if (!number.isNullOrEmpty())
                BASE_NAME_SINGLE + number
            else
                BASE_NAME_SINGLE_FALLBACK
        }
    }
}
