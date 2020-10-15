package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class MainCssResourceReferenceTest {

    @Test
    fun canGetInstance() {
        MainCssResourceReference shouldBeInstanceOf MainCssResourceReference::class
    }

    @Test
    fun assertResourceName() {
        MainCssResourceReference.name shouldBeEqualTo "css/main.css"
    }
}
