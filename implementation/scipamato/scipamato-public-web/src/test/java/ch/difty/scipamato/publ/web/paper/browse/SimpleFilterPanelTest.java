package ch.difty.scipamato.publ.web.paper.browse;

import org.apache.wicket.model.Model;

import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.web.common.PanelTest;

public class SimpleFilterPanelTest extends PanelTest<SimpleFilterPanel> {

    private static final String PANEL = "panel";

    @Override
    protected SimpleFilterPanel makePanel() {
        return new SimpleFilterPanel(PANEL, Model.of(new PublicPaperFilter()));
    }

    @Override
    protected void assertSpecificComponents() {
        getTester().debugComponentTrees();
        getTester().assertComponent(PANEL, SimpleFilterPanel.class);

        assertLabeledTextField(PANEL, "methodsSearch");
        assertLabeledTextField(PANEL, "authorsSearch");
        assertLabeledTextField(PANEL, "pubYearFrom");
        assertLabeledTextField(PANEL, "pubYearUntil");
        assertLabeledMultiSelect(PANEL, "populationCodes");
        assertLabeledMultiSelect(PANEL, "studyDesignCodes");
    }

}
