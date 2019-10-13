package ch.difty.scipamato.publ.web.paper.browse;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.web.common.PanelTest;

class SimpleFilterPanelTest extends PanelTest<SimpleFilterPanel> {

    private static final String PANEL = "panel";

    private static final int COMPONENTS_WITH_EVENT_HANDLER = 16;

    private int eventHandlerCallCount;

    @Override
    protected SimpleFilterPanel makePanel() {
        return new SimpleFilterPanel(PANEL, Model.of(new PublicPaperFilter()), "en");
    }

    @Override
    protected void assertSpecificComponents() {
        getTester().assertComponent(PANEL, SimpleFilterPanel.class);

        assertLabeledTextField(PANEL, "methodsSearch");
        assertLabeledTextField(PANEL, "authorsSearch");
        assertLabeledTextField(PANEL, "pubYearFrom");
        assertLabeledTextField(PANEL, "pubYearUntil");
        assertLabeledMultiSelect(PANEL, "populationCodes");
        assertLabeledMultiSelect(PANEL, "studyDesignCodes");
        assertLabeledMultiSelect(PANEL, "keywords");
        assertLabeledTextField(PANEL, "titleSearch");
    }

    private SimpleFilterPanel makePanelSpy() {
        return new SimpleFilterPanel(PANEL, Model.of(new PublicPaperFilter()), "en") {
            @Override
            void handleChangeEvent(@NotNull final IEvent<?> event, @NotNull final FormComponent component) {
                super.handleChangeEvent(event, component);
                eventHandlerCallCount++;
            }
        };
    }

    @Test
    void notChangingAnyField() {
        getTester().startComponentInPage(makePanelSpy());
        assertThat(eventHandlerCallCount).isEqualTo(0);
    }

    @Test
    void changingTextField() {
        getTester().startComponentInPage(makePanelSpy());
        getTester().executeAjaxEvent("panel:methodsSearch", "change");
        assertThat(eventHandlerCallCount).isEqualTo(COMPONENTS_WITH_EVENT_HANDLER);
    }

    @Test
    void changingMultiselectCombo() {
        getTester().startComponentInPage(makePanelSpy());
        getTester().executeAjaxEvent("panel:populationCodes", "change");
        assertThat(eventHandlerCallCount).isEqualTo(COMPONENTS_WITH_EVENT_HANDLER);
    }

    @Test
    void changingKeywordMultiselect() {
        getTester().startComponentInPage(makePanelSpy());
        getTester().executeAjaxEvent("panel:keywords", "change");
        assertThat(eventHandlerCallCount).isEqualTo(COMPONENTS_WITH_EVENT_HANDLER);
    }

}
