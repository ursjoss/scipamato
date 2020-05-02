package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class MetaOTCssResourceReferenceTest {

    @Test
    fun canGetInstance() {
        REF shouldBeInstanceOf MetaOTCssResourceReference::class
    }

    @Test
    fun assertResourceName() {
        REF.name shouldBeEqualTo "css/MetaOT.css"
    }

    companion object {
        private val REF = MetaOTCssResourceReference.get()
    }
}