package ch.difty.scipamato.common.web

import org.amshove.kluent.shouldBeEqualTo

import org.junit.jupiter.api.Test

internal class WicketUtilsTest {

    @Test
    fun labelTag() {
        WicketUtils.LABEL_TAG shouldBeEqualTo "Label"
    }

    @Test
    fun labelResourceTag() {
        WicketUtils.LABEL_RESOURCE_TAG shouldBeEqualTo ".label"
    }

    @Test
    fun shortLabelResourceTag() {
        WicketUtils.SHORT_LABEL_RESOURCE_TAG shouldBeEqualTo ".short.label"
    }

    @Test
    fun panelHeaderResourceTag() {
        WicketUtils.PANEL_HEADER_RESOURCE_TAG shouldBeEqualTo ".header"
    }

    @Test
    fun dummyTest() {
        WicketUtils.dummyMethod() shouldBeEqualTo "Label.label.loading.title.short.label.header"
    }
}
