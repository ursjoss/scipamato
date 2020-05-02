package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.CoreEntity
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.jooq.Record
import org.jooq.UpdateSetFirstStep
import org.jooq.UpdateSetMoreStep
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

abstract class UpdateSetStepSetterTest<R : Record, E : CoreEntity> {

    protected val step = mockk<UpdateSetFirstStep<R>>()
    protected val moreStep = mockk<UpdateSetMoreStep<R>>()

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
        confirmVerified(step, moreStep)
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
        verify(exactly = 2) { entityMock.created }
        verify { entityMock.createdBy }
        verify(exactly = 2) { entityMock.lastModified }
        verify { entityMock.lastModifiedBy }
        verify { entityMock.version }
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
