@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.common

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

import org.junit.jupiter.api.Test

internal class NullCheckTest {

    @Test
    fun assertingNonNullField_doesNothing() {
        assertThat(AssertAs.notNull("", "myparam")).isEmpty()
    }

    @Test
    fun assertingNullField_throwsException() {
        try {
            AssertAs.notNull<Any>(null, "myparam")
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(NullArgumentException::class.java)
                    .hasMessage("myparam must not be null.")
        }

    }

    @Test
    fun assertingNonNullField_withoutName_doesNothing() {
        assertThat(AssertAs.notNull("")).isEmpty()
    }

    @Test
    fun assertingNullField_withoutName_throwsException() {
        try {
            AssertAs.notNull<Any>(null)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(NullArgumentException::class.java)
                    .hasMessage("Argument must not be null.")
        }
    }

}
