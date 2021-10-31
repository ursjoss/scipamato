package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class PymJavaScriptResourceReferenceTest {

    @Test
    fun canGetInstance() {
        PymJavaScriptResourceReference shouldBeInstanceOf PymJavaScriptResourceReference::class
    }

    @Test
    fun assertResourceName() {
        PymJavaScriptResourceReference.name shouldBeEqualTo "js/pym.v1.js"
    }
}
