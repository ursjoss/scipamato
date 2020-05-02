package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class PymJavaScriptResourceReferenceTest {

    @Test
    fun canGetInstance() {
        REF shouldBeInstanceOf PymJavaScriptResourceReference::class
    }

    @Test
    fun assertResourceName() {
        REF.name shouldBeEqualTo "js/pym.v1.js"
    }

    companion object {
        private val REF = PymJavaScriptResourceReference.get()
    }
}