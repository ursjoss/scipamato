package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.CoreEntity
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.jooq.InsertSetMoreStep
import org.jooq.InsertSetStep
import org.jooq.Record
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

abstract class InsertSetStepSetterTest<R : Record, E : CoreEntity> {

    protected val step = mockk<InsertSetStep<R>>()
    protected val moreStep = mockk<InsertSetMoreStep<R>>()

    protected abstract val setter: InsertSetStepSetter<R, E>

    protected abstract val entity: E

    protected abstract fun entityFixture()

    /**
     * Create the test fixture for the set steps, starting with getStep(),
     * continuing with getMoreStep(), e.g.
     *
     * `<pre>
     * when(getStep().set(PAPER.PM_ID, PM_ID)).thenReturn(getMoreStep());
     *
     * when(getMoreStep().set(PAPER.DOI, DOI)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.AUTHORS, AUTHORS)).thenReturn(getMoreStep());
     * ...
    </pre>` *
     *
     * **Note:** No need to set the audit fields - those are managed later
     */
    protected abstract fun stepSetFixtureExceptAudit()

    /**
     * Create the test fixture for set steps, continuing with getMoreStep(), e.g.
     *
     * ```
     * when(getMoreStep().set(PAPER.CREATED, CREATED)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.VERSION, VERSION)).thenReturn(getMoreStep());
     * ```
     */
    protected abstract fun setStepFixtureAudit()

    @AfterEach
    internal fun tearDown() {
        specificTearDown()
        confirmVerified(step, moreStep)
    }

    protected abstract fun specificTearDown()

    @Test
    internal fun settingNonKeyFields() {
        entityFixture()
        stepSetFixtureExceptAudit()
        setStepFixtureAudit()

        RecordMapperTest.auditFixtureFor(entity)

        setter.setNonKeyFieldsFor(step, entity)

        verifyCallToFieldsExceptKeyAndAudit()
        verifyCallToAuditFields()
        verifySettingFieldsExceptKeyAndAudit()
        verifySettingAuditFields()
    }

    protected abstract fun verifyCallToFieldsExceptKeyAndAudit()

    private fun verifyCallToAuditFields() {
        val entityMock = entity
        verify(exactly = 0) { entityMock.created }
        verify { entityMock.createdBy }
        verify(exactly = 0) { entityMock.lastModified }
        verify { entityMock.lastModifiedBy }
        verify(exactly = 0) { entityMock.version }
    }

    protected abstract fun verifySettingFieldsExceptKeyAndAudit()

    protected abstract fun verifySettingAuditFields()
}
