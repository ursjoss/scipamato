package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class MetaOTCssResourceReferenceTest {

    @Test
    fun canGetInstance() {
        MetaOTCssResourceReference shouldBeInstanceOf MetaOTCssResourceReference::class
    }

    @Test
    fun assertResourceName() {
        MetaOTCssResourceReference.name shouldBeEqualTo "css/MetaOT.css"
    }
}
