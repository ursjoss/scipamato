package ch.difty.scipamato.core.persistence;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jooq.Record;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.core.entity.CoreEntity;

@RunWith(MockitoJUnitRunner.class)
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

    @Before
    public void setUp() {
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

    @After
    public void tearDown() {
        specificTearDown();
        verifyNoMoreInteractions(stepMock, moreStepMock);
    }

    protected abstract void specificTearDown();

    @Test
    public void nullCheck() {
        assertThat(stepMock).isNotNull();
        assertThat(moreStepMock).isNotNull();
    }

    @Test
    public void settingFields_withNullSetter_throws() {
        assertDegenerateSupplierParameter(() -> getSetter().setFieldsFor(null, getEntity()), "step");
    }

    @Test
    public void settingFields_withNullEntity_throws() {
        assertDegenerateSupplierParameter(() -> getSetter().setFieldsFor(stepMock, null), "entity");
    }

    @Test
    public void settingNonKeyFields() {
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
        verify(entityMock).getLastModified();
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
