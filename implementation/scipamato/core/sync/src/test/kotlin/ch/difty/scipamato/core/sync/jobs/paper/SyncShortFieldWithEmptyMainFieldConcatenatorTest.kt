package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.TableField
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import java.sql.ResultSet
import java.sql.SQLException

internal class SyncShortFieldWithEmptyMainFieldConcatenatorTest {

    private val sfc = SyncShortFieldWithEmptyMainFieldConcatenator()

    private val resultSet = mock<ResultSet>()

    private val throwingConcatenator = object : SyncShortFieldWithEmptyMainFieldConcatenator() {
        override fun methodsFrom(
            rs: ResultSet,
            methodField: TableField<PaperRecord, String>,
            methodStudyDesignField: TableField<PaperRecord, String>,
            methodOutcomeField: TableField<PaperRecord, String>,
            populationPlaceField: TableField<PaperRecord, String>,
            exposurePollutantField: TableField<PaperRecord, String>,
            exposureAssessmentField: TableField<PaperRecord, String>,
            methodStatisticsField: TableField<PaperRecord, String>,
            methodConfoundersField: TableField<PaperRecord, String>
        ): String = throw SQLException("boom")

        override fun populationFrom(
            rs: ResultSet,
            populationField: TableField<PaperRecord, String>,
            populationPlaceField: TableField<PaperRecord, String>,
            populationParticipantsField: TableField<PaperRecord, String>,
            populationDurationField: TableField<PaperRecord, String>
        ): String = throw SQLException("boom")

        override fun resultFrom(
            rs: ResultSet,
            resultField: TableField<PaperRecord, String>,
            resultMeasuredOutcomeField: TableField<PaperRecord, String>,
            resultExposureRangeField: TableField<PaperRecord, String>,
            resultEffectEstimateField: TableField<PaperRecord, String>,
            conclusionField: TableField<PaperRecord, String>
        ): String = throw SQLException("boom")
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(resultSet)
    }

    @Test
    fun methods_withNonNullMethod_returnsMethod() {
        stubMethodFieldsWithMainFieldReturning("method")
        assertThat(sfc.methodsFrom(resultSet)).isEqualTo("method")
        verifyCallingMethodsFields()
    }

    @Test
    fun methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() {
        stubMethodFieldsWithMainFieldReturning(null)
        assertThat(sfc.methodsFrom(resultSet)).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / " +
                "Exposure Assessment: ea / Statistical Method: ms / Confounders: mc"
        )
        verifyCallingMethodsFields()
    }

    @Suppress("ComplexMethod")
    private fun stubMethodFieldsWithMainFieldReturning(mainFixture: String?) {
        whenever(resultSet.getString(anyString())).thenAnswer { invocationsOnMock ->
            when (invocationsOnMock.arguments[0] as String) {
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
        verify(resultSet).getString(PAPER.METHODS.name)
        verify(resultSet).getString(PAPER.METHOD_STUDY_DESIGN.name)
        verify(resultSet).getString(PAPER.METHOD_OUTCOME.name)
        verify(resultSet).getString(PAPER.POPULATION_PLACE.name)
        verify(resultSet).getString(PAPER.EXPOSURE_POLLUTANT.name)
        verify(resultSet).getString(PAPER.EXPOSURE_ASSESSMENT.name)
        verify(resultSet).getString(PAPER.METHOD_STATISTICS.name)
        verify(resultSet).getString(PAPER.METHOD_CONFOUNDERS.name)
    }

    @Test
    fun population_withNonNullPopulation_returnsPopulation() {
        stubPopulationFieldsWithMainFieldReturning("population")
        assertThat(sfc.populationFrom(resultSet)).isEqualTo("population")
        verifyCallingPopulationFields()
    }

    @Test
    fun population_withNullPopulation_returnsPopulationShortFieldsConcatenated() {
        stubPopulationFieldsWithMainFieldReturning(null)
        assertThat(sfc.populationFrom(resultSet)).isEqualTo("Place: ppl / Participants: ppa / Study Duration: pd")
        verifyCallingPopulationFields()
    }

    private fun stubPopulationFieldsWithMainFieldReturning(mainFixture: String?) {
        whenever(resultSet.getString(anyString())).thenAnswer { invocationsOnMock ->
            when (invocationsOnMock.arguments[0] as String) {
                PAPER.POPULATION.name -> mainFixture
                PAPER.POPULATION_PLACE.name -> "ppl"
                PAPER.POPULATION_PARTICIPANTS.name -> "ppa"
                PAPER.POPULATION_DURATION.name -> "pd"
                else -> throw IllegalArgumentException("unhandled stub")
            }
        }
    }

    private fun verifyCallingPopulationFields() {
        verify(resultSet).getString(PAPER.POPULATION.name)
        verify(resultSet).getString(PAPER.POPULATION_PLACE.name)
        verify(resultSet).getString(PAPER.POPULATION_PARTICIPANTS.name)
        verify(resultSet).getString(PAPER.POPULATION_DURATION.name)
    }

    @Test
    fun result_withNonNullResult_returnsResult() {
        stubResultFieldsWithMainFieldReturning("result")
        assertThat(sfc.resultFrom(resultSet)).isEqualTo("result")
        verifyCallingResultFields()
    }

    @Test
    fun result_withNullResult_returnsResultShortFieldsConcatenated() {
        stubResultFieldsWithMainFieldReturning(null)
        assertThat(sfc.resultFrom(resultSet)).isEqualTo(
            "Measured Outcome: rmo / Exposure (Range): rer / Effect Estimate: ree / Conclusion: cc"
        )
        verifyCallingResultFields()
    }

    private fun stubResultFieldsWithMainFieldReturning(mainFixture: String?) {
        whenever(resultSet.getString(anyString())).thenAnswer { invocationsOnMock ->
            when (invocationsOnMock.arguments[0] as String) {
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
        verify(resultSet).getString(PAPER.RESULT.name)
        verify(resultSet).getString(PAPER.RESULT_MEASURED_OUTCOME.name)
        verify(resultSet).getString(PAPER.RESULT_EXPOSURE_RANGE.name)
        verify(resultSet).getString(PAPER.RESULT_EFFECT_ESTIMATE.name)
        verify(resultSet).getString(PAPER.CONCLUSION.name)
    }

    @Test
    fun methodsFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.methodsFrom(mock(ResultSet::class.java))).isNull()
    }

    @Test
    fun populationFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.populationFrom(mock(ResultSet::class.java))).isNull()
    }

    @Test
    fun resultFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.resultFrom(mock(ResultSet::class.java))).isNull()
    }
}
