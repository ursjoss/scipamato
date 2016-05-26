package ch.difty.sipamato.web.pages.entry;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.LoopItem;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.web.pages.AbstractPageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.AjaxBootstrapTabbedPanel;

public class PaperEntryPageTest extends AbstractPageTest<PaperEntryPage> {

    @Override
    protected Class<PaperEntryPage> getPageClass() {
        return PaperEntryPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        assertLabeledTextArea(b, Paper.AUTHORS);
        assertLabeledTextField(b, Paper.FIRST_AUTHOR);
        assertLabeledCheckBox(b, Paper.FIRST_AUTHOR_OVERRIDDEN);
        assertLabeledTextArea(b, Paper.TITLE);
        assertLabeledTextField(b, Paper.LOCATION);

        assertLabeledTextField(b, Paper.ID);
        assertLabeledTextField(b, Paper.PUBL_YEAR);
        assertLabeledTextField(b, Paper.PMID);
        assertLabeledTextField(b, Paper.DOI);

        b += ":tabs";
        getTester().assertComponent(b, AjaxBootstrapTabbedPanel.class);
        b += ":tabs-container:tabs";
        assertTabPanel(0, b);
        assertTabPanel(1, b);
        assertTabPanel(2, b);
    }

    private void assertTabPanel(int i, String b) {
        final String bb = ":" + String.valueOf(i);
        getTester().assertComponent(b + bb, LoopItem.class);
        getTester().assertComponent(b + bb + ":link:title", Label.class);
    }
}
