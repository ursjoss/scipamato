package ch.difty.scipamato.core.web.paper.jasper.summaryshort

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler
import ch.difty.scipamato.core.web.resources.jasper.PaperSummaryShortReportResourceReference
import net.sf.jasperreports.engine.JasperReport

/**
 * DataSource for the PaperSummaryShortReport.
 *
 * Can be instantiated in different ways, either by passing in
 *
 *  * a single [Paper] + report header fields + export config
 *  * a single [PaperSummaryShort] + export config
 *  * a collection of [PaperSummaryShort] entities + export config or
 *  * an instance of a [AbstractPaperSlimProvider] + report header fields export config
 *
 * The report header fields are not contained within a paper instance and make up e.g.
 * localized labels, the brand or part of the header.
 */
class PaperSummaryShortDataSource : JasperPaperDataSource<PaperSummaryShort?> {

    private lateinit var reportHeaderFields: ReportHeaderFields

    /**
     * Build up the paper summary short from a [paper] and any additional
     * information not contained within the paper
     *
     * @param paper an instance of [Paper]
     * @param reportHeaderFields collection of localized labels for report fields
     * @param config [ClusterablePdfExporterConfiguration]
     */
    constructor(
        paper: Paper,
        reportHeaderFields: ReportHeaderFields,
        config: ClusterablePdfExporterConfiguration?,
    ) : this(
        paperSummaryShorts = listOf(PaperSummaryShort(paper, reportHeaderFields)),
        config = config,
        baseName = makeSinglePaperBaseName(if (paper.number != null) paper.number.toString() else null)
    ) {
        this.reportHeaderFields = reportHeaderFields
    }

    /**
     * Populate the report from a single [PaperSummaryShort], using a specific file name including the id of the paper.
     *
     * @param paperSummaryShort single [PaperSummaryShort]
     * @param config the [ClusterablePdfExporterConfiguration]
     */
    internal constructor(
        paperSummaryShort: PaperSummaryShort,
        config: ClusterablePdfExporterConfiguration?,
    ) : this(
        paperSummaryShorts = listOf(paperSummaryShort),
        config = config,
        baseName = makeSinglePaperBaseName(paperSummaryShort.number)
    )

    /**
     * Populate the report from a collection of [PaperSummaryShort],
     * using a specific file name including the id of the paper.
     *
     * @param paperSummaryShorts collection of [PaperSummaryShort] instances
     * @param config the [ClusterablePdfExporterConfiguration]
     * @param baseName the file name without the extension (.pdf)
     */
    private constructor(
        paperSummaryShorts: Collection<PaperSummaryShort>,
        config: ClusterablePdfExporterConfiguration?,
        baseName: String,
    ) : super(ScipamatoPdfResourceHandler(config), baseName, paperSummaryShorts)

    /**
     * Using the dataProvider for the Result Panel as record source.
     *
     * @param dataProvider the [AbstractPaperSlimProvider]
     * @param reportHeaderFields collection of localized labels for the report fields
     * @param config [ClusterablePdfExporterConfiguration]
     */
    constructor(
        dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter?>,
        reportHeaderFields: ReportHeaderFields,
        config: ClusterablePdfExporterConfiguration?,
    ) : super(ScipamatoPdfResourceHandler(config), BASE_NAME_MULTIPLE, dataProvider) {
        this.reportHeaderFields = reportHeaderFields
    }

    override fun getReport(): JasperReport = PaperSummaryShortReportResourceReference.get().getReport()
    override fun makeEntity(p: Paper): PaperSummaryShort = PaperSummaryShort(p, reportHeaderFields)

    companion object {
        private const val serialVersionUID = 1L
        private const val BASE_NAME_SINGLE = "paper_summary_short_no_"
        private const val BASE_NAME_SINGLE_FALLBACK = "paper_summary_short"
        private const val BASE_NAME_MULTIPLE = "paper_summaries_short"

        private fun makeSinglePaperBaseName(number: String?): String {
            return if (!number.isNullOrEmpty())
                BASE_NAME_SINGLE + number
            else
                BASE_NAME_SINGLE_FALLBACK
        }
    }
}
