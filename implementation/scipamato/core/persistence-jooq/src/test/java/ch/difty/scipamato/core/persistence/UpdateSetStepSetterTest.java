package ch.difty.scipamato.core.persistence;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.jooq.Record;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.entity.CoreEntity;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class UpdateSetStepSetterTest<R extends Record, E extends CoreEntity> {

    @Mock
    private UpdateSetFirstStep<R> stepMock;
    @Mock
    private UpdateSetMoreStep<R>  moreStepMock;

    protected UpdateSetFirstStep<R> getStep() {
        return stepMock;
    }

    protected UpdateSetMoreStep<R> getMoreStep() {
        return moreStepMock;
    }

    protected abstract UpdateSetStepSetter<R, E> getSetter();

    protected abstract E getEntity();

    private void fullFixture() {
        entityFixture();
        stepSetFixtureExceptAudit();
        stepSetFixtureAudit();
    }

    /**
     * set up the test fixture fot the entity mock, including audit fields.
     */
    protected abstract void entityFixture();

    /**
     * fixture for stepSet, starting with {@code getStep()}, following up with
     * {@code getMoreStep()}, e.g.
     *
     * <code><pre>
     * when(getStep().set(PAPER.PM_ID, PM_ID)).thenReturn(getMoreStep());
     *
     * when(getMoreStep().set(PAPER.DOI, DOI)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.AUTHORS, AUTHORS)).thenReturn(getMoreStep());
     * ...
     * </pre></code>
     */
    protected abstract void stepSetFixtureExceptAudit();

    /**
     * fixture for stepSet for audit fields, starting with getMoreStep(), e.g.
     *
     * <code><pre>
     * when(getMoreStep().set(PAPER.CREATED, CREATED)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.VERSION, VERSION + 1)).thenReturn(getMoreStep());
     * </pre></code>
     */
    protected abstract void stepSetFixtureAudit();

    @AfterEach
    void tearDown() {
        specificTearDown();
        verifyNoMoreInteractions(stepMock, moreStepMock);
    }

    protected abstract void specificTearDown();

    @Test
    void nullCheck() {
        assertThat(stepMock).isNotNull();
        assertThat(moreStepMock).isNotNull();
    }

    @Test
    void settingFields_withNullSetter_throws() {
        assertDegenerateSupplierParameter(() -> getSetter().setFieldsFor(null, getEntity()), "step");
    }

    @Test
    void settingFields_withNullEntity_throws() {
        assertDegenerateSupplierParameter(() -> getSetter().setFieldsFor(stepMock, null), "entity");
    }

    @Test
    void settingNonKeyFields() {
        fullFixture();
        RecordMapperTest.auditFixtureFor(getEntity());
        RecordMapperTest.auditExtendedFixtureFor(getEntity());

        getSetter().setFieldsFor(stepMock, getEntity());

        verifyCallToAllFieldsExceptAudit();
        verifyCallToAuditFields();
        verifyStepSettingExceptAudit();
        verifyStepSettingAudit();
    }

    /**
     * Verify the calls to all fields of the entity except the audit fields.
     */
    protected abstract void verifyCallToAllFieldsExceptAudit();

    private void verifyCallToAuditFields() {
        E entityMock = getEntity();
        verify(entityMock).getCreated();
        verify(entityMock).getCreatedBy();
        verify(entityMock, times(3)).getLastModified();
        verify(entityMock).getLastModifiedBy();
        verify(entityMock).getVersion();
    }

    /**
     * Verify the stepSetting has been called on all fields except the audit fields
     */
    protected abstract void verifyStepSettingExceptAudit();

    /**
     * Verify the stepSetting has been called on the audit fields.
     */
    protected abstract void verifyStepSettingAudit();

}
