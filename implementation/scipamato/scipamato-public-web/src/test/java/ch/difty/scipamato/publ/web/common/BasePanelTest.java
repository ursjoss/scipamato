package ch.difty.scipamato.publ.web.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.publ.web.WicketTest;

public class BasePanelTest extends WicketTest {

    private BasePanel<String> panel;

    @Test
    public void instantiatingWithIdOnly() {
        panel = new BasePanel<String>("panel") {
            private static final long serialVersionUID = 1L;
        };
        assertThat(panel.getLocalization()).isEqualTo("en_us");
    }

    @Test
    public void instantiatingWithIdAndModel() {
        panel = new BasePanel<String>("panel", Model.of("foo")) {
            private static final long serialVersionUID = 1L;
        };
        assertThat(panel.getLocalization()).isEqualTo("en_us");
    }

    @Test
    public void instantiatingWithIdAndModelAndMode() {
        panel = new BasePanel<String>("panel", Model.of("foo"), Mode.EDIT) {
            private static final long serialVersionUID = 1L;
        };
        assertThat(panel.getLocalization()).isEqualTo("en_us");
    }

}
