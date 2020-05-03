package ch.difty.scipamato.core.web.paper.entry

import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.Paper.NewsletterLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.markup.html.form.Form
import org.junit.jupiter.api.Test
import java.util.Optional

internal class EditablePaperPanelInViewModeTest : EditablePaperPanelTest() {

    override val mode: Mode
        get() = Mode.VIEW

    override fun setUpLocalHook() {
        // when referring to PaperSearchPage
        every { searchOrderServiceMock.findById(SEARCH_ORDER_ID) } returns Optional.empty()
    }

    override fun assertSpecificComponents() {
        var b = PANEL_ID
        tester.assertComponent(b, EditablePaperPanel::class.java)
        assertCommonComponents(b)
        b += ":form"
        assertTextFieldWithLabel("$b:id", 1L, "ID")
        assertTextFieldWithLabel("$b:number", 100L, "SciPaMaTo-Core-No.")
        assertTextFieldWithLabel("$b:publicationYear", 2017, "Pub. Year")
        assertTextFieldWithLabel("$b:pmId", 1234, "PMID")
        tester.assertInvisible("$b:submit")
        assertTextFieldWithLabel("$b:createdDisplayValue", "u1 (2017-02-01 13:34:45)", "Created")
        assertTextFieldWithLabel("$b:modifiedDisplayValue", "u2 (2017-03-01 13:34:45)", "Last Modified")
        tester.assertComponent("$b:back", BootstrapButton::class.java)
        tester.assertComponent("$b:previous", BootstrapButton::class.java)
        tester.assertComponent("$b:next", BootstrapButton::class.java)
        // disabled as paperManagerMock is not mocked here
        tester.isDisabled("$b:previous")
        tester.isDisabled("$b:next")
        tester.assertInvisible("$b:exclude")
        tester.assertInvisible("$b:pubmedRetrieval")
        tester.assertInvisible("$b:modAssociation")
        tester.clickLink("panel:form:tabs:tabs-container:tabs:5:link")
        val bb = "$b:tabs:panel:tab6Form"
        tester.assertInvisible("$bb:dropzone")
        tester.assertComponent("$bb:attachments", BootstrapDefaultDataTable::class.java)
        tester.assertComponent(bb, Form::class.java)
        verifyCodeAndCodeClassCalls(1, 1)
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun isAssociatedWithNewsletter_withNewsletterLink() {
        val p = makePanel()
        p.modelObject.newsletterLink = NewsletterLink(1, "i1", 1, 1, "t1", "hl")
        p.isAssociatedWithNewsletter.shouldBeTrue()
    }

    @Test
    override fun specificFields_areDisabled() {
        tester.startComponentInPage(makePanel())
        tester.isDisabled("panel:form:id")
        tester.isDisabled("panel:form:firstAuthorOverridden")
        tester.isDisabled("panel:form:createdDisplayValue")
        tester.isDisabled("panel:form:modifiedDisplayValue")
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }
}
