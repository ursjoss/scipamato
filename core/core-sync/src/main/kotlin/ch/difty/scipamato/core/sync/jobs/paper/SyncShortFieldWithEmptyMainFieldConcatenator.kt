package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.paper.AbstractShortFieldConcatenator
import ch.difty.scipamato.core.db.Tables
import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import org.jooq.TableField
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.sql.SQLException

private val log = logger()

/**
 * Gathers the content for the the fields methods, population and result.
 *
 * There are some main fields (result, population and method) that could alternatively
 * be represented with a number of Short field (Kurzerfassung). If the main field is populated, it will always
 * have precedence, regardless of whether there's content in the respective short fields. If the main field is null,
 * all respective short fields with content are concatenated into the respective field in SciPaMaTo-Public. Note
 * that there's a known deficiency: The labels that are included with the short fields are always in english, they
 * will not adapt to the browser locale of a viewer.
 */
@Component
internal open class SyncShortFieldWithEmptyMainFieldConcatenator : AbstractShortFieldConcatenator(false), SyncShortFieldConcatenator {

    override fun methodsFrom(rs: ResultSet): String? = try {
        methodsFrom(rs, Tables.PAPER.METHODS, Tables.PAPER.METHOD_STUDY_DESIGN, Tables.PAPER.METHOD_OUTCOME,
            Tables.PAPER.POPULATION_PLACE, Tables.PAPER.EXPOSURE_POLLUTANT, Tables.PAPER.EXPOSURE_ASSESSMENT, Tables.PAPER.METHOD_STATISTICS,
            Tables.PAPER.METHOD_CONFOUNDERS)
    } catch (se: SQLException) {
        log.error(UNABLE_MSG, se)
        null
    }

    @Suppress("LongParameterList")
    @Throws(SQLException::class)
    open fun methodsFrom(
        rs: ResultSet,
        methodField: TableField<PaperRecord, String?>,
        methodStudyDesignField: TableField<PaperRecord, String?>,
        methodOutcomeField: TableField<PaperRecord, String?>,
        populationPlaceField: TableField<PaperRecord, String?>,
        exposurePollutantField: TableField<PaperRecord, String?>,
        exposureAssessmentField: TableField<PaperRecord, String?>,
        methodStatisticsField: TableField<PaperRecord, String?>,
        methodConfoundersField: TableField<PaperRecord, String?>
    ): String = methodsFrom(
        rs.getString(methodField.name),
        rs.getString(methodStudyDesignField.name),
        rs.getString(methodOutcomeField.name),
        rs.getString(populationPlaceField.name),
        rs.getString(exposurePollutantField.name),
        rs.getString(exposureAssessmentField.name),
        rs.getString(methodStatisticsField.name),
        rs.getString(methodConfoundersField.name)
    )

    override fun populationFrom(rs: ResultSet): String? = try {
        populationFrom(
            rs,
            Tables.PAPER.POPULATION,
            Tables.PAPER.POPULATION_PLACE,
            Tables.PAPER.POPULATION_PARTICIPANTS,
            Tables.PAPER.POPULATION_DURATION
        )
    } catch (se: SQLException) {
        log.error(UNABLE_MSG, se)
        null
    }

    // package-private for stubbing purposes
    @Throws(SQLException::class)
    open fun populationFrom(
        rs: ResultSet,
        populationField: TableField<PaperRecord, String?>,
        populationPlaceField: TableField<PaperRecord, String?>,
        populationParticipantsField: TableField<PaperRecord, String?>,
        populationDurationField: TableField<PaperRecord, String?>
    ): String = populationFrom(
        rs.getString(populationField.name),
        rs.getString(populationPlaceField.name),
        rs.getString(populationParticipantsField.name),
        rs.getString(populationDurationField.name)
    )

    override fun resultFrom(rs: ResultSet): String? = try {
        resultFrom(
            rs,
            Paper.PAPER.RESULT,
            Paper.PAPER.RESULT_MEASURED_OUTCOME,
            Paper.PAPER.RESULT_EXPOSURE_RANGE,
            Paper.PAPER.RESULT_EFFECT_ESTIMATE,
            Paper.PAPER.CONCLUSION
        )
    } catch (se: SQLException) {
        log.error(UNABLE_MSG, se)
        null
    }

    // package-private for stubbing purposes
    @Throws(SQLException::class)
    open fun resultFrom(
        rs: ResultSet,
        resultField: TableField<PaperRecord, String?>,
        resultMeasuredOutcomeField: TableField<PaperRecord, String?>,
        resultExposureRangeField: TableField<PaperRecord, String?>,
        resultEffectEstimateField: TableField<PaperRecord, String?>,
        conclusionField: TableField<PaperRecord, String?>
    ): String = resultFrom(
        rs.getString(resultField.name),
        rs.getString(resultMeasuredOutcomeField.name),
        rs.getString(resultExposureRangeField.name),
        rs.getString(resultEffectEstimateField.name),
        rs.getString(conclusionField.name)
    )

    companion object {
        private const val UNABLE_MSG = "Unable to evaluate recordset"
    }
}
