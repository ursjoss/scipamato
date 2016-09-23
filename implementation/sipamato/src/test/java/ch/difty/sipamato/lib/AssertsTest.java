package ch.difty.sipamato.lib;

import static org.assertj.core.api.Assertions.fail;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class AssertsTest {

    @Test
    public void assertingNonNullField_doesNothing() {
        Asserts.notNull(new String(), "myparam");
    }

    @Test
    public void assertingNullField_throwsException() {
        try {
            Asserts.notNull(null, "myparam");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("myparam must not be null.");
        }
    }

    @Test
    public void assertingNonNullField_withoutName_doesNothing() {
        Asserts.notNull(new String());
    }

    @Test
    public void assertingNullField_withNullName_throwsException() {
        try {
            Asserts.notNull(null, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("Argument must not be null.");
        }
    }

    @Test
    public void assertingNullField_withoutName_throwsException() {
        try {
            Asserts.notNull(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("Argument must not be null.");
        }
    }
}
