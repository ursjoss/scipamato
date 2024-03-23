@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.jooq.TableField
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.sql.ResultSet
import java.sql.SQLException

internal class SyncShortFieldWithEmptyMainFieldConcatenatorTest {

    private val sfc = SyncShortFieldWithEmptyMainFieldConcatenator()

    private val resultSet = mockk<ResultSet>()

    private val throwingConcatenator = object : SyncShortFieldWithEmptyMainFieldConcatenator() {
        override fun methodsFrom(
            rs: ResultSet,
            methodField: TableField<PaperRecord, String?>,
            methodStudyDesignField: TableField<PaperRecord, String?>,
            methodOutcomeField: TableField<PaperRecord, String?>,
            populationPlaceField: TableField<PaperRecord, String?>,
            exposurePollutantField: TableField<PaperRecord, String?>,
            exposureAssessmentField: TableField<PaperRecord, String?>,
            methodStatisticsField: TableField<PaperRecord, String?>,
            methodConfoundersField: TableField<PaperRecord, String?>
        ): String = throw SQLException("boom")

        override fun populationFrom(
            rs: ResultSet,
            populationField: TableField<PaperRecord, String?>,
            populationPlaceField: TableField<PaperRecord, String?>,
            populationParticipantsField: TableField<PaperRecord, String?>,
            populationDurationField: TableField<PaperRecord, String?>
        ): String = throw SQLException("boom")

        override fun resultFrom(
            rs: ResultSet,
            resultField: TableField<PaperRecord, String?>,
            resultMeasuredOutcomeField: TableField<PaperRecord, String?>,
            resultExposureRangeField: TableField<PaperRecord, String?>,
            resultEffectEstimateField: TableField<PaperRecord, String?>,
            conclusionField: TableField<PaperRecord, String?>
        ): String = throw SQLException("boom")
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(resultSet)
    }

    @Test
    fun methods_withNonNullMethod_returnsMethod() {
        stubMethodFieldsWithMainFieldReturning("method")
        sfc.methodsFrom(resultSet) shouldBeEqualTo "method"
        verifyCallingMethodsFields()
    }

    @Test
    fun methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() {
        stubMethodFieldsWithMainFieldReturning(null)
        sfc.methodsFrom(resultSet) shouldBeEqualTo
            "Studiendesign: msd / Zielgrössen: mo / Ort/Land: pp / Schadstoff: ep / " +
            "Belastungsabschätzung: ea / Statistische Methode: ms / Störfaktoren: mc"
        verifyCallingMethodsFields()
    }

    @Suppress("ComplexMethod")
    private fun stubMethodFieldsWithMainFieldReturning(mainFixture: String?) {
        val slot = slot<String>()
        every { resultSet.getString(capture(slot)) } answers {
            when (slot.captured) {
                PAPER.METHODS.name -> mainFixture
                PAPER.METHOD_STUDY_DESIGN.name -> "msd"
                PAPER.METHOD_OUTCOME.name -> "mo"
                PAPER.POPULATION_PLACE.name -> "pp"
                PAPER.EXPOSURE_POLLUTANT.name -> "ep"
                PAPER.EXPOSURE_ASSESSMENT.name -> "ea"
                PAPER.METHOD_STATISTICS.name -> "ms"
                PAPER.METHOD_CONFOUNDERS.name -> "mc"
                else -> throw IllegalArgumentException("unhandled stub")
            }
        }
    }

    private fun verifyCallingMethodsFields() {
        verify { resultSet.getString(PAPER.METHODS.name) }
        verify { resultSet.getString(PAPER.METHOD_STUDY_DESIGN.name) }
        verify { resultSet.getString(PAPER.METHOD_OUTCOME.name) }
        verify { resultSet.getString(PAPER.POPULATION_PLACE.name) }
        verify { resultSet.getString(PAPER.EXPOSURE_POLLUTANT.name) }
        verify { resultSet.getString(PAPER.EXPOSURE_ASSESSMENT.name) }
        verify { resultSet.getString(PAPER.METHOD_STATISTICS.name) }
        verify { resultSet.getString(PAPER.METHOD_CONFOUNDERS.name) }
    }

    @Test
    fun population_withNonNullPopulation_returnsPopulation() {
        stubPopulationFieldsWithMainFieldReturning("population")
        sfc.populationFrom(resultSet) shouldBeEqualTo "population"
        verifyCallingPopulationFields()
    }

    @Test
    fun population_withNullPopulation_returnsPopulationShortFieldsConcatenated() {
        stubPopulationFieldsWithMainFieldReturning(null)
        sfc.populationFrom(resultSet) shouldBeEqualTo "Ort/Land: ppl / Studienteilnehmer: ppa / Studiendauer: pd"
        verifyCallingPopulationFields()
    }

    private fun stubPopulationFieldsWithMainFieldReturning(mainFixture: String?) {
        val slot = slot<String>()
        every { resultSet.getString(capture(slot)) } answers {
            when (slot.captured) {
                PAPER.POPULATION.name -> mainFixture
                PAPER.POPULATION_PLACE.name -> "ppl"
                PAPER.POPULATION_PARTICIPANTS.name -> "ppa"
                PAPER.POPULATION_DURATION.name -> "pd"
                else -> throw IllegalArgumentException("unhandled stub")
            }
        }
    }

    private fun verifyCallingPopulationFields() {
        verify { resultSet.getString(PAPER.POPULATION.name) }
        verify { resultSet.getString(PAPER.POPULATION_PLACE.name) }
        verify { resultSet.getString(PAPER.POPULATION_PARTICIPANTS.name) }
        verify { resultSet.getString(PAPER.POPULATION_DURATION.name) }
    }

    @Test
    fun result_withNonNullResult_returnsResult() {
        stubResultFieldsWithMainFieldReturning("result")
        sfc.resultFrom(resultSet) shouldBeEqualTo "result"
        verifyCallingResultFields()
    }

    @Test
    fun result_withNullResult_returnsResultShortFieldsConcatenated() {
        stubResultFieldsWithMainFieldReturning(null)
        sfc.resultFrom(resultSet) shouldBeEqualTo
            "Gemessene Zielgrösse: rmo / Gemessene Belastung (Spanne): rer / Resultate: ree / Schlussfolgerung: cc"
        verifyCallingResultFields()
    }

    private fun stubResultFieldsWithMainFieldReturning(mainFixture: String?) {
        val slot = slot<String>()
        every { resultSet.getString(capture(slot)) } answers {
            when (slot.captured) {
                PAPER.RESULT.name -> mainFixture
                PAPER.RESULT_MEASURED_OUTCOME.name -> "rmo"
                PAPER.RESULT_EXPOSURE_RANGE.name -> "rer"
                PAPER.RESULT_EFFECT_ESTIMATE.name -> "ree"
                PAPER.CONCLUSION.name -> "cc"
                else -> throw IllegalArgumentException("unhandled stub")
            }
        }
    }

    private fun verifyCallingResultFields() {
        verify { resultSet.getString(PAPER.RESULT.name) }
        verify { resultSet.getString(PAPER.RESULT_MEASURED_OUTCOME.name) }
        verify { resultSet.getString(PAPER.RESULT_EXPOSURE_RANGE.name) }
        verify { resultSet.getString(PAPER.RESULT_EFFECT_ESTIMATE.name) }
        verify { resultSet.getString(PAPER.CONCLUSION.name) }
    }

    @Test
    fun methodsFrom_withThrowingMethod_returnsNull() {
        throwingConcatenator.methodsFrom(mockk()).shouldBeNull()
    }

    @Test
    fun populationFrom_withThrowingMethod_returnsNull() {
        throwingConcatenator.populationFrom(mockk()).shouldBeNull()
    }

    @Test
    fun resultFrom_withThrowingMethod_returnsNull() {
        throwingConcatenator.resultFrom(mockk()).shouldBeNull()
    }
}
