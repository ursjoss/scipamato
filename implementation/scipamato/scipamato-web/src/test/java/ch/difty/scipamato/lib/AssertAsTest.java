package ch.difty.scipamato.lib;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.FinalClassTest;

public class AssertAsTest extends FinalClassTest<AssertAs> {

    @Test
    public void assertingNonNullField_doesNothing() {
        assertThat(AssertAs.notNull("", "myparam")).isEmpty();
    }

    @Test
    public void assertingNullField_throwsException() {
        try {
            AssertAs.notNull(null, "myparam");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("myparam must not be null.");
        }
    }

    @Test
    public void assertingNonNullField_withoutName_doesNothing() {
        assertThat(AssertAs.notNull("")).isEmpty();
    }

    @Test
    public void assertingNullField_withNullName_throwsException() {
        try {
            AssertAs.notNull(null, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("Argument must not be null.");
        }
    }

    @Test
    public void assertingNullField_withoutName_throwsException() {
        try {
            AssertAs.notNull(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("Argument must not be null.");
        }
    }

}
