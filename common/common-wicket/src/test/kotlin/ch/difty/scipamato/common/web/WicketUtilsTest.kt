package ch.difty.scipamato.common.web

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class WicketUtilsTest {

    @Test
    fun labelTag() {
        assertThat(WicketUtils.LABEL_TAG).isEqualTo("Label")
    }

    @Test
    fun labelResourceTag() {
        assertThat(WicketUtils.LABEL_RESOURCE_TAG).isEqualTo(".label")
    }

    @Test
    fun shortLabelResourceTag() {
        assertThat(WicketUtils.SHORT_LABEL_RESOURCE_TAG).isEqualTo(".short.label")
    }

    @Test
    fun panelHeaderResourceTag() {
        assertThat(WicketUtils.PANEL_HEADER_RESOURCE_TAG).isEqualTo(".header")
    }

    @Test
    fun dummyTest() {
        assertThat(WicketUtils.dummyMethod()).isEqualTo("Label.label.loading.title.short.label.header")
    }
}
