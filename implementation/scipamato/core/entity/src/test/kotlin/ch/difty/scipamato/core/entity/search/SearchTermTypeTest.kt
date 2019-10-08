package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.search.SearchTermType.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

import org.junit.jupiter.api.Test

internal class SearchTermTypeTest {

    @Test
    fun testValues() {
        assertThat(values()).containsExactly(BOOLEAN, INTEGER, STRING, AUDIT, UNSUPPORTED)
    }

    @Test
    fun testId() {
        assertThat(BOOLEAN.id).isEqualTo(0)
        assertThat(INTEGER.id).isEqualTo(1)
        assertThat(STRING.id).isEqualTo(2)
        assertThat(AUDIT.id).isEqualTo(3)
        assertThat(UNSUPPORTED.id).isEqualTo(-1)
    }

    @Test
    fun testById_withValidIds() {
        assertThat(byId(0)).isEqualTo(BOOLEAN)
        assertThat(byId(1)).isEqualTo(INTEGER)
        assertThat(byId(2)).isEqualTo(STRING)
        assertThat(byId(3)).isEqualTo(AUDIT)
    }

    @Test
    fun testById_withInvalidIds() {
        try {
            byId(-2)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(IllegalArgumentException::class.java).hasMessage("id -2 is not supported")
        }

        try {
            byId(-1)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(IllegalArgumentException::class.java).hasMessage("id -1 is not supported")
        }

        try {
            byId(4)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(IllegalArgumentException::class.java).hasMessage("id 4 is not supported")
        }

    }

}
