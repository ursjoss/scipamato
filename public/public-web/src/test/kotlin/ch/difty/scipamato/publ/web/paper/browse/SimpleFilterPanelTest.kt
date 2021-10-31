package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import ch.difty.scipamato.publ.web.common.PanelTest
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

class SimpleFilterPanelTest : PanelTest<SimpleFilterPanel>() {

    private var eventHandlerCallCount = 0

    override fun makePanel(): SimpleFilterPanel =
        SimpleFilterPanel(PANEL, Model.of(PublicPaperFilter()), "en")

    override fun assertSpecificComponents() {
        tester.assertComponent(PANEL, SimpleFilterPanel::class.java)
        assertLabeledTextField(PANEL, "methodsSearch")
        assertLabeledTextField(PANEL, "authorsSearch")
        assertLabeledTextField(PANEL, "pubYearFrom")
        assertLabeledTextField(PANEL, "pubYearUntil")
        assertLabeledMultiSelect(PANEL, "populationCodes")
        assertLabeledMultiSelect(PANEL, "studyDesignCodes")
        assertLabeledMultiSelect(PANEL, "keywords")
        assertLabeledTextField(PANEL, "titleSearch")
    }

    private fun makePanelSpy(): SimpleFilterPanel = object :
        SimpleFilterPanel(PANEL, Model.of(PublicPaperFilter()), "en") {
        override fun handleChangeEvent(event: IEvent<*>, component: FormComponent<*>) {
            super.handleChangeEvent(event, component)
            eventHandlerCallCount++
        }
    }

    @Test
    fun notChangingAnyField() {
        tester.startComponentInPage(makePanelSpy())
        eventHandlerCallCount shouldBeEqualTo 0
    }

    @Test
    fun changingTextField() {
        tester.startComponentInPage(makePanelSpy())
        tester.executeAjaxEvent("panel:methodsSearch", "change")
        eventHandlerCallCount shouldBeEqualTo COMPONENTS_WITH_EVENT_HANDLER
    }

    @Test
    fun changingMultiselectCombo() {
        tester.startComponentInPage(makePanelSpy())
        tester.executeAjaxEvent("panel:populationCodes", "change")
        eventHandlerCallCount shouldBeEqualTo COMPONENTS_WITH_EVENT_HANDLER
    }

    @Test
    fun changingKeywordMultiselect() {
        tester.startComponentInPage(makePanelSpy())
        tester.executeAjaxEvent("panel:keywords", "change")
        eventHandlerCallCount shouldBeEqualTo COMPONENTS_WITH_EVENT_HANDLER
    }

    companion object {
        private const val PANEL = "panel"
        private const val COMPONENTS_WITH_EVENT_HANDLER = 16
    }
}
