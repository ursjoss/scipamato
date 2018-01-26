package ch.difty.scipamato.publ.web.common;

import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Test;

import ch.difty.scipamato.publ.web.WicketTest;

public abstract class PanelTest<T extends Panel> extends WicketTest {

    protected static final String PANEL_ID = "panel";

    @Test
    public void assertPanel() {
        getTester().startComponentInPage(makePanel());

        assertSpecificComponents();

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    protected abstract T makePanel();

    protected abstract void assertSpecificComponents();

}
