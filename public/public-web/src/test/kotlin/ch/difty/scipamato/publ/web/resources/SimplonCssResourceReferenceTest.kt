package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class SimplonCssResourceReferenceTest {

    @Test
    fun canGetInstance() {
        REF shouldBeInstanceOf SimplonCssResourceReference::class
    }

    @Test
    fun assertResourceName() {
        REF.name shouldBeEqualTo "css/Simplon.css"
    }

    companion object {
        private val REF = SimplonCssResourceReference.get()
    }
}
