package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.fail;
import static org.fest.assertions.api.Assertions.assertThat;
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

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public abstract class UpdateSetStepSetterTest<R extends Record, E extends SipamatoEntity> {

    @Mock
    private UpdateSetFirstStep<R> stepMock;
    @Mock
    private UpdateSetMoreStep<R> moreStepMock;

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
    public void settingFields_withNullSetter_throws() {
        try {
            getSetter().setFieldsFor(null, getEntity());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("step must not be null.");
        }
    }

    @Test
    public void settingFields_withNullEntity_throws() {
        try {
            getSetter().setFieldsFor(stepMock, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("entity must not be null.");
        }
    }

    @Test
    public void settingNonKeyFields() {
        getSetter().setFieldsFor(stepMock, getEntity());

        verifyCallToAllAllFields();
        verifySetting();
    }

    protected abstract void verifyCallToAllAllFields();

    protected abstract void verifySetting();

}
