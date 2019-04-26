package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class AssertAsTest extends FinalClassTest<AssertAs> {

    @Test
    void assertingNonNullField_doesNothing() {
        assertThat(AssertAs.notNull("", "myparam")).isEmpty();
    }

    @Test
    void assertingNullField_throwsException() {
        try {
            AssertAs.notNull(null, "myparam");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("myparam must not be null.");
        }
    }

    @Test
    void assertingNonNullField_withoutName_doesNothing() {
        assertThat(AssertAs.notNull("")).isEmpty();
    }

    @Test
    void assertingNullField_withNullName_throwsException() {
        try {
            AssertAs.notNull(null, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("Argument must not be null.");
        }
    }

    @Test
    void assertingNullField_withoutName_throwsException() {
        try {
            AssertAs.notNull(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("Argument must not be null.");
        }
    }

}
