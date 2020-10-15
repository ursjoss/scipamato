package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class SimplonCssResourceReferenceTest {

    @Test
    fun canGetInstance() {
        SimplonCssResourceReference shouldBeInstanceOf SimplonCssResourceReference::class
    }

    @Test
    fun assertResourceName() {
        SimplonCssResourceReference.name shouldBeEqualTo "css/Simplon.css"
    }
}
