package ch.difty.scipamato.core.web.common;

import org.apache.wicket.markup.html.panel.Panel;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.WicketTest;

public abstract class PanelTest<T extends Panel> extends WicketTest {

    protected static final String PANEL_ID = "panel";

    @Test
    void assertPanel() {
        getTester().startComponentInPage(makePanel());

        assertSpecificComponents();

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    protected abstract T makePanel();

    protected abstract void assertSpecificComponents();
}
