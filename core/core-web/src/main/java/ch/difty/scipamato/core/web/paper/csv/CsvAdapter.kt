package ch.difty.scipamato.core.web.paper.csv

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.review.PaperReview
import com.univocity.parsers.csv.CsvFormat
import com.univocity.parsers.csv.CsvWriter
import com.univocity.parsers.csv.CsvWriterSettings
import java.io.Serializable
import java.io.StringWriter

/**
 * CSV Adapter accepting a list of objects of type [T]
 * and builds a full CSV file as a single String.
 *
 * Works well for small number of records but doesn't scale well.
 */
interface CsvAdapter<T> : Serializable {
    fun build(types: List<T>): String
}

@Suppress("FunctionName", "SpreadOperator")
abstract class AbstractCsvAdapter<T>(
    private val rowMapper: (Collection<T>) -> List<Array<String>>,
    private val headers: Array<String?>
) : CsvAdapter<T> {

    override fun build(types: List<T>): String {
        val rows: List<Array<String>> = rowMapper(types)
        StringWriterWithBom().apply {
            CsvWriter(this, SemicolonDelimitedSettings()).apply {
                writeHeaders(*headers)
                writeRowsAndClose(rows)
            }
            return toString()
        }
    }

    private fun StringWriterWithBom() = StringWriter().apply { write("\ufeff") }

    private fun SemicolonDelimitedSettings() = CsvWriterSettings().apply {
        format = CsvFormat().apply { delimiter = ';' }
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

/**
 * Adapter to build the Review CSV file, accepting a list of [Paper]s
 */
class ReviewCsvAdapter(private val rhf: ReportHeaderFields) : AbstractCsvAdapter<Paper>(
    rowMapper = @JvmSerializableLambda { types -> types.map { PaperReview(it, rhf).toRow() } },
    headers = arrayOf(
        rhf.numberLabel,
        rhf.authorYearLabel,
        rhf.populationPlaceLabel,
        rhf.methodOutcomeLabel,
        rhf.exposurePollutantLabel,
        rhf.methodStudyDesignLabel,
        rhf.populationDurationLabel,
        rhf.populationParticipantsLabel,
        rhf.exposureAssessmentLabel,
        rhf.resultExposureRangeLabel,
        rhf.methodStatisticsLabel,
        rhf.methodConfoundersLabel,
        rhf.resultEffectEstimateLabel,
        rhf.conclusionLabel,
        rhf.commentLabel,
        rhf.internLabel,
        rhf.goalsLabel,
        rhf.populationLabel,
        rhf.methodsLabel,
        rhf.resultLabel,
    ),
) {
    companion object {
        private const val serialVersionUID: Long = 1L
        const val FILE_NAME = "review.csv"
    }
}

private fun PaperReview.toRow() = arrayOf(
    number,
    authorYear,
    populationPlace,
    methodOutcome,
    exposurePollutant,
    methodStudyDesign,
    populationDuration,
    populationParticipants,
    exposureAssessment,
    resultExposureRange,
    methodStatistics,
    methodConfounders,
    resultEffectEstimate,
    conclusion,
    comment,
    intern,
    goals,
    population,
    methods,
    result,
)
