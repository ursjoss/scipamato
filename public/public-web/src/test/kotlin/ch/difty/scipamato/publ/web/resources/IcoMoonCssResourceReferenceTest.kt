package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class IcoMoonCssResourceReferenceTest {

    @Test
    fun canGetInstance() {
        REF shouldBeInstanceOf IcoMoonCssResourceReference::class
    }

    @Test
    fun assertResourceName() {
        REF.name shouldBeEqualTo "css/IcoMoon.css"
    }

    companion object {
        private val REF = IcoMoonCssResourceReference.get()
    }
}
