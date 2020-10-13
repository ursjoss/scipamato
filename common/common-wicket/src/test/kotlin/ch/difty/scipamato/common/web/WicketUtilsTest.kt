package ch.difty.scipamato.common.web

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class WicketUtilsTest {

    @Test
    fun labelTag() {
        LABEL_TAG shouldBeEqualTo "Label"
    }

    @Test
    fun labelResourceTag() {
        LABEL_RESOURCE_TAG shouldBeEqualTo ".label"
    }

    @Test
    fun shortLabelResourceTag() {
        SHORT_LABEL_RESOURCE_TAG shouldBeEqualTo ".short.label"
    }

    @Test
    fun panelHeaderResourceTag() {
        PANEL_HEADER_RESOURCE_TAG shouldBeEqualTo ".header"
    }
}
