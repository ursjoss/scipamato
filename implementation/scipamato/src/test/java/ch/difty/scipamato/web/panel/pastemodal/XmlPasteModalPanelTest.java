package ch.difty.scipamato.web.panel.pastemodal;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;

import ch.difty.scipamato.web.panel.PanelTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;

public class XmlPasteModalPanelTest extends PanelTest<XmlPasteModalPanel> {

    @Override
    protected XmlPasteModalPanel makePanel() {
        return new XmlPasteModalPanel(PANEL_ID);
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, Panel.class);

        b += ":form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        getTester().assertComponent(b + "content", TextArea.class);
        getTester().assertComponent(b + "submit", BootstrapAjaxButton.class);
    }

}
