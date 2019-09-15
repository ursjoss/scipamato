package ch.difty.scipamato.common.web

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class ModeTest {

    @Test
    fun testValues() {
        assertThat(Mode.values()).containsExactly(Mode.EDIT, Mode.VIEW, Mode.SEARCH)
    }
}
