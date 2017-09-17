package ch.difty.scipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.ScipamatoEntity;

@RunWith(MockitoJUnitRunner.class)
public abstract class InsertSetStepSetterTest<R extends Record, E extends ScipamatoEntity> {

    @Mock
    private InsertSetStep<R> stepMock;
    @Mock
    private InsertSetMoreStep<R> moreStepMock;

    protected InsertSetStep<R> getStep() {
        return stepMock;
    }

    protected InsertSetMoreStep<R> getMoreStep() {
        return moreStepMock;
    }

    protected abstract InsertSetStepSetter<R, E> getSetter();

    protected abstract E getEntity();

    @Before
    public void setUp() {
        entityFixture();
        stepSetFixtureExceptAudit();
        setStepFixtureAudit();
    }

    protected abstract void entityFixture();

    /**
     * Create the test fixture for the set steps, starting with getStep(), continuing with getMoreStep(), e.g.
     *
     * <code><pre>
     *  when(getStep().set(PAPER.PM_ID, PM_ID)).thenReturn(getMoreStep());
     *
     *  when(getMoreStep().set(PAPER.DOI, DOI)).thenReturn(getMoreStep());
     *  when(getMoreStep().set(PAPER.AUTHORS, AUTHORS)).thenReturn(getMoreStep());
     *  ...
     * </pre></code>
     *
     * <b>Note:</b> No need to set the audit fields - those are managed later
     */
    protected abstract void stepSetFixtureExceptAudit();

    /**
     * Create the test fixture for set steps, continuing with getMoreStep(), e.g.
     *
     * <code><pre>
     * when(getMoreStep().set(PAPER.CREATED, CREATED)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.VERSION, VERSION)).thenReturn(getMoreStep());
     * </pre></code
     */
    protected abstract void setStepFixtureAudit();

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
    public void settingNonKeyFields_withNullSetter_throws() {
        try {
            getSetter().setNonKeyFieldsFor(null, getEntity());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("step must not be null.");
        }
    }

    @Test
    public void settingNonKeyFields_withNullEntity_throws() {
        try {
            getSetter().setNonKeyFieldsFor(getStep(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("entity must not be null.");
        }
    }

    @Test
    public void settingNonKeyFields() {
        getSetter().setNonKeyFieldsFor(getStep(), getEntity());

        verifyCallToFieldsExceptKeyAndAudit();
        verifyCallToAuditFields();
        verifySettingFieldsExceptKeyAndAudit();
        verifySettingAuditFields();
    }

    protected abstract void verifyCallToFieldsExceptKeyAndAudit();

    private void verifyCallToAuditFields() {
        E entityMock = getEntity();
        verify(entityMock, never()).getCreated();
        verify(entityMock).getCreatedBy();
        verify(entityMock, never()).getLastModified();
        verify(entityMock).getLastModifiedBy();
        verify(entityMock, never()).getVersion();
    }

    protected abstract void verifySettingFieldsExceptKeyAndAudit();

    protected abstract void verifySettingAuditFields();

    @Test
    public void consideringSettingKeyOf_withNullSetter_throws() {
        try {
            getSetter().considerSettingKeyOf(null, getEntity());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("step must not be null.");
        }
    }

    @Test
    public void consideringSettingKeyOf_withNullEntity_throws() {
        try {
            getSetter().considerSettingKeyOf(getMoreStep(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("entity must not be null.");
        }
    }

}
