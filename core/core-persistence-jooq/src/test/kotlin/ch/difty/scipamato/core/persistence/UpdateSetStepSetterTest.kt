package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.CoreEntity
import org.jooq.Record
import org.jooq.UpdateSetFirstStep
import org.jooq.UpdateSetMoreStep
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

abstract class UpdateSetStepSetterTest<R : Record, E : CoreEntity> {

    protected val step = mock<UpdateSetFirstStep<R>>()
    protected val moreStep = mock<UpdateSetMoreStep<R>>()

    protected abstract val setter: UpdateSetStepSetter<R, E>

    protected abstract val entity: E

    private fun fullFixture() {
        entityFixture()
        stepSetFixtureExceptAudit()
        stepSetFixtureAudit()
    }

    /**
     * set up the test fixture fot the entity mock, including audit fields.
     */
    protected abstract fun entityFixture()

    /**
     * fixture for stepSet, starting with `getStep()`, following up with
     * `getMoreStep()`, e.g.
     *
     * `<pre>
     * when(getStep().set(PAPER.PM_ID, PM_ID)).thenReturn(getMoreStep());
     *
     * when(getMoreStep().set(PAPER.DOI, DOI)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.AUTHORS, AUTHORS)).thenReturn(getMoreStep());
     * ...
    </pre>` *
     */
    protected abstract fun stepSetFixtureExceptAudit()

    /**
     * fixture for stepSet for audit fields, starting with getMoreStep(), e.g.
     *
     * `<pre>
     * when(getMoreStep().set(PAPER.CREATED, CREATED)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.VERSION, VERSION + 1)).thenReturn(getMoreStep());
    </pre>` *
     */
    protected abstract fun stepSetFixtureAudit()

    @AfterEach
    internal fun tearDown() {
        specificTearDown()
        verifyNoMoreInteractions(step, moreStep)
    }

    protected abstract fun specificTearDown()

    @Test
    internal fun settingNonKeyFields() {
        fullFixture()
        RecordMapperTest.auditFixtureFor(entity)
        RecordMapperTest.auditExtendedFixtureFor(entity)

        setter.setFieldsFor(step, entity)

        verifyCallToAllFieldsExceptAudit()
        verifyCallToAuditFields()
        verifyStepSettingExceptAudit()
        verifyStepSettingAudit()
    }

    /**
     * Verify the calls to all fields of the entity except the audit fields.
     */
    protected abstract fun verifyCallToAllFieldsExceptAudit()

    private fun verifyCallToAuditFields() {
        val entityMock = entity
        verify(entityMock, times(2)).created
        verify(entityMock).createdBy
        verify(entityMock, times(2)).lastModified
        verify(entityMock).lastModifiedBy
        verify(entityMock).version
    }

    /**
     * Verify the stepSetting has been called on all fields except the audit fields
     */
    protected abstract fun verifyStepSettingExceptAudit()

    /**
     * Verify the stepSetting has been called on the audit fields.
     */
    protected abstract fun verifyStepSettingAudit()
}
