package ch.difty.sipamato.web.pages.entry;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.web.pages.AbstractPageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;

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
        getTester().assertComponent(b, ClientSideBootstrapTabbedPanel.class);
        b += ":panelsContainer:panels";
        assertTabPanelFields(1, b, Paper.GOALS, Paper.POPULATION, Paper.METHODS);

    }

    private void assertTabPanel(int i, String b) {
        final String bb = ":" + String.valueOf(i);
        getTester().assertComponent(b + bb, Panel.class);
    }

    private void assertTabPanelFields(int i, String b, String... fields) {
        assertTabPanel(i, b);
        String id = String.valueOf(i);
        final String bb = b + ":" + id + ":tab" + id + "Form";
        getTester().assertComponent(bb, Form.class);
        for (String f : fields) {
            assertLabeledTextArea(bb, f);
        }
    }
}
