package ch.difty.scipamato.core.web.common.pastemodal;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Test;

import ch.difty.scipamato.core.web.common.PanelTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;

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
        getTester().assertComponent(b + "dropzone", DropZoneFileUpload.class);
        getTester().assertComponent(b + "submit", BootstrapAjaxButton.class);
        getTester().assertComponent(b + "cancel", BootstrapAjaxButton.class);
    }

    @Test
    public void clickingCancel_clearsPastedContentAndClosesWindow() {
        XmlPasteModalPanel panel = makePanel();
        getTester().startComponentInPage(panel);
        panel.get("form:content")
            .setDefaultModelObject("abc");
        assertThat(panel.getPastedContent()).isEqualTo("abc");

        getTester().executeAjaxEvent("panel:form:cancel", "click");

        assertThat(panel.getPastedContent()).isNull();
        getTester().assertNoFeedbackMessage(0);

        assertThat(getTester().getLastResponse()
            .getDocument()).contains("win.current.close();");
    }

    @Test
    public void clickingSubmit_keepsPastedContentAndClosesWindow() {
        XmlPasteModalPanel panel = makePanel();
        getTester().startComponentInPage(panel);
        panel.get("form:content")
            .setDefaultModelObject("def");
        assertThat(panel.getPastedContent()).isEqualTo("def");

        getTester().executeAjaxEvent("panel:form:submit", "click");

        assertThat(panel.getPastedContent()).isEqualTo("def");
        getTester().assertNoFeedbackMessage(0);

        assertThat(getTester().getLastResponse()
            .getDocument()).contains("win.current.close();");
    }

}
