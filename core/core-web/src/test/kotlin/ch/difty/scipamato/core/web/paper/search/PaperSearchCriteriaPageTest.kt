package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.web.common.BasePageTest
import ch.difty.scipamato.core.web.paper.common.SearchablePaperPanel
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test
import java.util.Optional

internal class PaperSearchCriteriaPageTest : BasePageTest<PaperSearchCriteriaPage>() {

    private val searchCondition = SearchCondition()

    private val searchOrder = SearchOrder(SEARCH_ORDER_ID, "soName", 1, false, null, null)

    override fun setUpHook() {
        every { searchOrderServiceMock.findById(SEARCH_ORDER_ID) } returns Optional.of(searchOrder)
        every { searchOrderServiceMock.findPageByFilter(any(), any()) } returns listOf(searchOrder)
    }

    override fun makePage(): PaperSearchCriteriaPage =
        PaperSearchCriteriaPage(Model.of(searchCondition), SEARCH_ORDER_ID)

    override val pageClass: Class<PaperSearchCriteriaPage>
        get() = PaperSearchCriteriaPage::class.java

    override fun assertSpecificComponents() {
        tester.assertComponent("contentPanel", SearchablePaperPanel::class.java)
        assertForm("contentPanel:form")
    }

    @Suppress("SameParameterValue")
    private fun assertForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        tester.assertComponent("$b:firstAuthorOverridden", CheckBox::class.java)
    }

    @Test
    fun submittingForm_savesSearchCondition_andRemainsOnPagePage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("contentPanel:form")
        formTester.submit()
        tester.assertRenderedPage(pageClass)
        tester.assertNoErrorMessage()
        verify { searchOrderServiceMock.saveOrUpdateSearchCondition(searchCondition, SEARCH_ORDER_ID, "en_us") }
        verify(exactly = 0) { searchOrderServiceMock.findPageByFilter(any(), any()) }
    }

    @Test
    fun submittingForm_andClickingSubmitButton_savesSearchConditionAndForwardsToPaperSearchPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("contentPanel:form")
        formTester.submit("submit")
        tester.assertRenderedPage(PaperSearchPage::class.java)
        tester.assertNoErrorMessage()
        verify { searchOrderServiceMock.saveOrUpdateSearchCondition(searchCondition, SEARCH_ORDER_ID, "en_us") }
        verify { searchOrderServiceMock.findPageByFilter(any(), any()) }
    }

    @Test
    fun submittingForm_withErrorInService_addsErrorMessage() {
        every {
            searchOrderServiceMock.saveOrUpdateSearchCondition(searchCondition, SEARCH_ORDER_ID, "en_us")
        } throws RuntimeException("foo")
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("contentPanel:form")
        formTester.submit()
        tester.assertErrorMessages(
            "An unexpected error occurred when trying to save Search Order [id ]: foo",
            "An unexpected error occurred when trying to save Search Order [id ]: foo"
        )
        tester.assertRenderedPage(pageClass)
        verify(exactly = 2) { searchOrderServiceMock.saveOrUpdateSearchCondition(searchCondition, SEARCH_ORDER_ID, "en_us") }
        verify(exactly = 0) { searchOrderServiceMock.findPageByFilter(any(), any()) }
    }

    companion object {
        private const val SEARCH_ORDER_ID: Long = 5
    }
}
