package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class MainCssResourceReferenceTest {

    @Test
    fun canGetInstance() {
        REF shouldBeInstanceOf MainCssResourceReference::class
    }

    @Test
    fun assertResourceName() {
        REF.name shouldBeEqualTo "css/main.css"
    }

    companion object {
        private val REF = MainCssResourceReference.get()
    }
}
