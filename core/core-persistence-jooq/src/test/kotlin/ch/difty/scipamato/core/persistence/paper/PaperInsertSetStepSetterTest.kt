package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.persistence.InsertSetStepSetter
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong

internal class PaperInsertSetStepSetterTest : InsertSetStepSetterTest<PaperRecord, Paper>() {

    override val setter: InsertSetStepSetter<PaperRecord, Paper> = PaperInsertSetStepSetter()

    override val entity = mock<Paper>()
    private val recordMock = mock<PaperRecord>()

    override fun specificTearDown() {
        verifyNoMoreInteractions(entity, recordMock)
    }

    override fun entityFixture() {
        PaperRecordMapperTest.entityFixtureWithoutIdFields(entity)
    }

    override fun stepSetFixtureExceptAudit() {
        doReturn(moreStep).whenever(step).set(PAPER.NUMBER, PaperRecordMapperTest.NUMBER)
        doReturn(moreStep).whenever(moreStep).set(PAPER.PM_ID, PaperRecordMapperTest.PM_ID)

        doReturn(moreStep).whenever(moreStep).set(PAPER.DOI, PaperRecordMapperTest.DOI)
        doReturn(moreStep).whenever(moreStep).set(PAPER.AUTHORS, PaperRecordMapperTest.AUTHORS)
        doReturn(moreStep).whenever(moreStep).set(PAPER.FIRST_AUTHOR, PaperRecordMapperTest.FIRST_AUTHOR)
        doReturn(moreStep)
            .whenever(moreStep).set(PAPER.FIRST_AUTHOR_OVERRIDDEN, PaperRecordMapperTest.FIRST_AUTHOR_OVERRIDDEN)
        doReturn(moreStep).whenever(moreStep).set(PAPER.TITLE, PaperRecordMapperTest.TITLE)
        doReturn(moreStep).whenever(moreStep).set(PAPER.LOCATION, PaperRecordMapperTest.LOCATION)
        doReturn(moreStep).whenever(moreStep).set(PAPER.PUBLICATION_YEAR, PaperRecordMapperTest.PUBLICATION_YEAR)

        doReturn(moreStep).whenever(moreStep).set(PAPER.GOALS, PaperRecordMapperTest.GOALS)
        doReturn(moreStep).whenever(moreStep).set(PAPER.POPULATION, PaperRecordMapperTest.POPULATION)
        doReturn(moreStep).whenever(moreStep).set(PAPER.METHODS, PaperRecordMapperTest.METHODS)

        doReturn(moreStep).whenever(moreStep).set(PAPER.POPULATION_PLACE, PaperRecordMapperTest.POPULATION_PLACE)
        doReturn(moreStep)
            .whenever(moreStep).set(PAPER.POPULATION_PARTICIPANTS, PaperRecordMapperTest.POPULATION_PARTICIPANTS)
        doReturn(moreStep).whenever(moreStep).set(PAPER.POPULATION_DURATION, PaperRecordMapperTest.POPULATION_DURATION)
        doReturn(moreStep).whenever(moreStep).set(PAPER.EXPOSURE_POLLUTANT, PaperRecordMapperTest.EXPOSURE_POLLUTANT)
        doReturn(moreStep).whenever(moreStep).set(PAPER.EXPOSURE_ASSESSMENT, PaperRecordMapperTest.EXPOSURE_ASSESSMENT)
        doReturn(moreStep).whenever(moreStep).set(PAPER.METHOD_STUDY_DESIGN, PaperRecordMapperTest.METHOD_STUDY_DESIGN)
        doReturn(moreStep).whenever(moreStep).set(PAPER.METHOD_OUTCOME, PaperRecordMapperTest.METHOD_OUTCOME)
        doReturn(moreStep).whenever(moreStep).set(PAPER.METHOD_STATISTICS, PaperRecordMapperTest.METHOD_STATISTICS)
        doReturn(moreStep).whenever(moreStep).set(PAPER.METHOD_CONFOUNDERS, PaperRecordMapperTest.METHOD_CONFOUNDERS)

        doReturn(moreStep).whenever(moreStep).set(PAPER.RESULT, PaperRecordMapperTest.RESULT)
        doReturn(moreStep).whenever(moreStep).set(PAPER.COMMENT, PaperRecordMapperTest.COMMENT)
        doReturn(moreStep).whenever(moreStep).set(PAPER.INTERN, PaperRecordMapperTest.INTERN)

        doReturn(moreStep)
            .whenever(moreStep).set(PAPER.RESULT_EXPOSURE_RANGE, PaperRecordMapperTest.RESULT_EXPOSURE_RANGE)
        doReturn(moreStep)
            .whenever(moreStep).set(PAPER.RESULT_EFFECT_ESTIMATE, PaperRecordMapperTest.RESULT_EFFECT_ESTIMATE)
        doReturn(moreStep)
            .whenever(moreStep).set(PAPER.RESULT_MEASURED_OUTCOME, PaperRecordMapperTest.RESULT_MEASURED_OUTCOME)
        doReturn(moreStep).whenever(moreStep).set(PAPER.CONCLUSION, PaperRecordMapperTest.CONCLUSION)

        doReturn(moreStep).whenever(moreStep).set(PAPER.ORIGINAL_ABSTRACT, PaperRecordMapperTest.ORIGINAL_ABSTRACT)

        doReturn(moreStep)
            .whenever(moreStep).set(PAPER.MAIN_CODE_OF_CODECLASS1, PaperRecordMapperTest.MAIN_CODE_OF_CODECLASS1)
    }

    override fun setStepFixtureAudit() {
        doReturn(moreStep)
            .whenever(moreStep).set(PAPER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY)
        doReturn(moreStep)
            .whenever(moreStep)
            .set(PAPER.LAST_MODIFIED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY)
    }

    override fun verifyCallToFieldsExceptKeyAndAudit() {
        verify(entity).number
        verify(entity).pmId
        verify(entity).doi
        verify(entity).authors
        verify(entity).firstAuthor
        verify(entity).isFirstAuthorOverridden
        verify(entity).title
        verify(entity).location
        verify(entity).publicationYear

        verify(entity).goals
        verify(entity).population
        verify(entity).methods

        verify(entity).populationPlace
        verify(entity).populationParticipants
        verify(entity).populationDuration
        verify(entity).exposurePollutant
        verify(entity).exposureAssessment
        verify(entity).methodStudyDesign
        verify(entity).methodOutcome
        verify(entity).methodStatistics
        verify(entity).methodConfounders

        verify(entity).result
        verify(entity).comment
        verify(entity).intern

        verify(entity).resultExposureRange
        verify(entity).resultEffectEstimate
        verify(entity).resultMeasuredOutcome
        verify(entity).conclusion

        verify(entity).originalAbstract

        verify(entity).mainCodeOfCodeclass1
    }

    override fun verifySettingFieldsExceptKeyAndAudit() {
        verify(step).set(PAPER.NUMBER, PaperRecordMapperTest.NUMBER)
        verify(moreStep).set(PAPER.PM_ID, PaperRecordMapperTest.PM_ID)

        verify(moreStep).set(PAPER.DOI, PaperRecordMapperTest.DOI)
        verify(moreStep).set(PAPER.AUTHORS, PaperRecordMapperTest.AUTHORS)
        verify(moreStep).set(PAPER.FIRST_AUTHOR, PaperRecordMapperTest.FIRST_AUTHOR)
        verify(moreStep).set(PAPER.FIRST_AUTHOR_OVERRIDDEN, PaperRecordMapperTest.FIRST_AUTHOR_OVERRIDDEN)
        verify(moreStep).set(PAPER.TITLE, PaperRecordMapperTest.TITLE)
        verify(moreStep).set(PAPER.LOCATION, PaperRecordMapperTest.LOCATION)
        verify(moreStep).set(PAPER.PUBLICATION_YEAR, PaperRecordMapperTest.PUBLICATION_YEAR)

        verify(moreStep).set(PAPER.GOALS, PaperRecordMapperTest.GOALS)
        verify(moreStep).set(PAPER.POPULATION, PaperRecordMapperTest.POPULATION)
        verify(moreStep).set(PAPER.METHODS, PaperRecordMapperTest.METHODS)

        verify(moreStep).set(PAPER.POPULATION_PLACE, PaperRecordMapperTest.POPULATION_PLACE)
        verify(moreStep).set(PAPER.POPULATION_PARTICIPANTS, PaperRecordMapperTest.POPULATION_PARTICIPANTS)
        verify(moreStep).set(PAPER.POPULATION_DURATION, PaperRecordMapperTest.POPULATION_DURATION)
        verify(moreStep).set(PAPER.EXPOSURE_POLLUTANT, PaperRecordMapperTest.EXPOSURE_POLLUTANT)
        verify(moreStep).set(PAPER.EXPOSURE_ASSESSMENT, PaperRecordMapperTest.EXPOSURE_ASSESSMENT)
        verify(moreStep).set(PAPER.METHOD_STUDY_DESIGN, PaperRecordMapperTest.METHOD_STUDY_DESIGN)
        verify(moreStep).set(PAPER.METHOD_OUTCOME, PaperRecordMapperTest.METHOD_OUTCOME)
        verify(moreStep).set(PAPER.METHOD_STATISTICS, PaperRecordMapperTest.METHOD_STATISTICS)
        verify(moreStep).set(PAPER.METHOD_CONFOUNDERS, PaperRecordMapperTest.METHOD_CONFOUNDERS)

        verify(moreStep).set(PAPER.RESULT, PaperRecordMapperTest.RESULT)
        verify(moreStep).set(PAPER.COMMENT, PaperRecordMapperTest.COMMENT)
        verify(moreStep).set(PAPER.INTERN, PaperRecordMapperTest.INTERN)

        verify(moreStep).set(PAPER.RESULT_EXPOSURE_RANGE, PaperRecordMapperTest.RESULT_EXPOSURE_RANGE)
        verify(moreStep).set(PAPER.RESULT_EFFECT_ESTIMATE, PaperRecordMapperTest.RESULT_EFFECT_ESTIMATE)
        verify(moreStep).set(PAPER.RESULT_MEASURED_OUTCOME, PaperRecordMapperTest.RESULT_MEASURED_OUTCOME)
        verify(moreStep).set(PAPER.CONCLUSION, PaperRecordMapperTest.CONCLUSION)

        verify(moreStep).set(PAPER.ORIGINAL_ABSTRACT, PaperRecordMapperTest.ORIGINAL_ABSTRACT)

        verify(moreStep).set(PAPER.MAIN_CODE_OF_CODECLASS1, PaperRecordMapperTest.MAIN_CODE_OF_CODECLASS1)
    }

    override fun verifySettingAuditFields() {
        verify(moreStep).set(PAPER.CREATED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.CREATED_BY)
        verify(moreStep).set(PAPER.LAST_MODIFIED_BY, ch.difty.scipamato.core.persistence.RecordMapperTest.LAST_MOD_BY)
    }

    @Test
    fun consideringSettingKeyOf_withNullId_doesNotSetId() {
        whenever(entity.id).thenReturn(null)
        setter.considerSettingKeyOf(moreStep, entity)
        verify(entity).id
    }

    @Test
    fun consideringSettingKeyOf_withNonNullId_doesSetId() {
        whenever(entity.id).thenReturn(PaperRecordMapperTest.ID)

        setter.considerSettingKeyOf(moreStep, entity)

        verify(entity).id
        verify(moreStep).set(PAPER.ID, PaperRecordMapperTest.ID)
    }

    @Test
    fun resettingIdToEntity_withNullRecord_doesNothing() {
        setter.resetIdToEntity(entity, null)
        verify(entity, never()).id = anyLong()
    }

    @Test
    fun resettingIdToEntity_withNonNullRecord_setsId() {
        whenever(recordMock.id).thenReturn(3L)
        setter.resetIdToEntity(entity, recordMock)
        verify(recordMock).id
        verify(entity).id = anyLong()
    }
}
