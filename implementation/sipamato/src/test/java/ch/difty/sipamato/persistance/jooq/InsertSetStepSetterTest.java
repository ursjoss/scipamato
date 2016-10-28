package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public abstract class InsertSetStepSetterTest<R extends Record, E extends SipamatoEntity> {

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
        stepSetFixture();
    }

    protected abstract void entityFixture();

    protected abstract void stepSetFixture();

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

        verifyCallToAllNonKeyFields();
        verifySetting();
    }

    protected abstract void verifyCallToAllNonKeyFields();

    protected abstract void verifySetting();

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
