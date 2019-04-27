package ch.difty.scipamato.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.FinalClassTest;

class WicketUtilsTest extends FinalClassTest<WicketUtils> {

    @Test
    void labelTag() {
        assertThat(WicketUtils.LABEL_TAG).isEqualTo("Label");
    }

    @Test
    void labelResourceTag() {
        assertThat(WicketUtils.LABEL_RESOURCE_TAG).isEqualTo(".label");
    }

    @Test
    void shortLabelResourceTag() {
        assertThat(WicketUtils.SHORT_LABEL_RESOURCE_TAG).isEqualTo(".short.label");
    }

    @Test
    void panelHeaderResourceTag() {
        assertThat(WicketUtils.PANEL_HEADER_RESOURCE_TAG).isEqualTo(".header");
    }

    @Test
    void dummyTest() {
        assertThat(WicketUtils.dummyMethod()).isEqualTo("Label.label.loading.title.short.label.header");
    }
}
