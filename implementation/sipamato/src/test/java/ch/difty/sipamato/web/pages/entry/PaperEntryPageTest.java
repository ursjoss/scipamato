package ch.difty.sipamato.web.pages.entry;

import org.apache.wicket.markup.html.form.Form;
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
        assertTabPanelFields(1, 1, b, Paper.GOALS, Paper.POPULATION, Paper.EXPOSURE, Paper.METHODS, Paper.POPULATION_PLACE, Paper.POPULATION_PARTICIPANTS, Paper.POPULATION_DURATION,
                Paper.EXPOSURE_POLLUTANT, Paper.EXPOSURE_ASSESSMENT, Paper.METHOD_OUTCOME, Paper.METHOD_STATISTICS, Paper.METHOD_CONFOUNDERS);
        assertTabPanelFields(2, 3, b, Paper.RESULT, Paper.COMMENT, Paper.INTERN, Paper.RESULT_EXPOSURE_RANGE, Paper.RESULT_EFFECT_ESTIMATE);
    }

    private void assertTabPanelFields(int tabId, int panelId, String b, String... fields) {
        assertTabPanel(panelId, b);
        final String bb = b + ":" + panelId + ":tab" + tabId + "Form";
        getTester().assertComponent(bb, Form.class);
        for (String f : fields) {
            assertLabeledTextArea(bb, f);
        }
    }

    private void assertTabPanel(int i, String b) {
        final String bb = ":" + String.valueOf(i);
        getTester().assertComponent(b + bb, Panel.class);
    }

}
