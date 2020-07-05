package ch.difty.scipamato.common.web

import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class ModeTest {

    @Test
    fun testValues() {
        Mode.values() shouldContainAll listOf(Mode.EDIT, Mode.VIEW, Mode.SEARCH)
    }
}
