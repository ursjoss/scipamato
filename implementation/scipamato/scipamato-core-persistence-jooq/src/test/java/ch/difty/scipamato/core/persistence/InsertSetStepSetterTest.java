package ch.difty.scipamato.core.persistence;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.entity.CoreEntity;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class InsertSetStepSetterTest<R extends Record, E extends CoreEntity> {

    @Mock
    private InsertSetStep<R>     stepMock;
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

    protected abstract void entityFixture();

    /**
     * Create the test fixture for the set steps, starting with getStep(),
     * continuing with getMoreStep(), e.g.
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
     * <code>
     *
     * <pre>
     * when(getMoreStep().set(PAPER.CREATED, CREATED)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
     * when(getMoreStep().set(PAPER.VERSION, VERSION)).thenReturn(getMoreStep());
     * </pre>
     * <p>
     * </code
     */
    protected abstract void setStepFixtureAudit();

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
    void settingNonKeyFields_withNullSetter_throws() {
        assertDegenerateSupplierParameter(() -> getSetter().setNonKeyFieldsFor(null, getEntity()), "step");
    }

    @Test
    void settingNonKeyFields_withNullEntity_throws() {
        assertDegenerateSupplierParameter(() -> getSetter().setNonKeyFieldsFor(getStep(), null), "entity");
    }

    @Test
    void settingNonKeyFields() {
        entityFixture();
        stepSetFixtureExceptAudit();
        setStepFixtureAudit();

        RecordMapperTest.auditFixtureFor(getEntity());

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
    void consideringSettingKeyOf_withNullSetter_throws() {
        try {
            getSetter().considerSettingKeyOf(null, getEntity());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("step must not be null.");
        }
    }

    @Test
    void consideringSettingKeyOf_withNullEntity_throws() {
        try {
            getSetter().considerSettingKeyOf(getMoreStep(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("entity must not be null.");
        }
    }

}
