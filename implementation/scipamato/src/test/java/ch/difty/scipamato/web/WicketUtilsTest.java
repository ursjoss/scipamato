package ch.difty.scipamato.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.FinalClassTest;

public class WicketUtilsTest extends FinalClassTest<WicketUtils> {

    @Test
    public void labelTag() {
        assertThat(WicketUtils.LABEL_TAG).isEqualTo("Label");
    }

    @Test
    public void labelResourceTag() {
        assertThat(WicketUtils.LABEL_RECOURCE_TAG).isEqualTo(".label");
    }

    @Test
    public void shortLabelResourceTag() {
        assertThat(WicketUtils.SHORT_LABEL_RECOURCE_TAG).isEqualTo(".short.label");
    }

    @Test
    public void panelHeaderResourceTag() {
        assertThat(WicketUtils.PANEL_HEADER_RESOURCE_TAG).isEqualTo(".header");
    }

}
