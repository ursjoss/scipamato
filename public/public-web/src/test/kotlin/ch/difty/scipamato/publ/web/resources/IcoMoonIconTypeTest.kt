package ch.difty.scipamato.publ.web.resources

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class IcoMoonIconTypeTest {

    @Test
    fun arrow_right() {
        IcoMoonIconType.arrow_right.cssClassName() shouldBeEqualTo "icon-arrow-right"
    }

    @Test
    fun link() {
        IcoMoonIconType.link.cssClassName() shouldBeEqualTo "icon-link"
    }
}
