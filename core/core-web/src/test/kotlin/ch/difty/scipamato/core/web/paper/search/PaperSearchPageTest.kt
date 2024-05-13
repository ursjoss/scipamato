package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.CorePageParameters
import ch.difty.scipamato.core.web.common.BasePageTest
import ch.difty.scipamato.core.web.paper.result.ResultPanel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.Optional

@Suppress("SameParameterValue")
internal class PaperSearchPageTest : BasePageTest<PaperSearchPage>() {

    private val paperSlim = PaperSlim(41L, 41L, "fa", 2020, "title")
    private val searchOrder = SearchOrder(SO_ID, "soName", 1, false, null, null)

    override fun setUpHook() {
        every { searchOrderServiceMock.findById(SO_ID) } returns Optional.of(searchOrder)
        every { searchOrderServiceMock.findPageByFilter(any(), any()) } returns listOf(searchOrder)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(searchOrderServiceMock)
    }

    override fun makePage(): PaperSearchPage {
        val conditions = listOf(SearchCondition())
        val so = SearchOrder(conditions).apply {
            id = 5L
        }
        return PaperSearchPage(Model.of(so), Mode.EDIT)
    }

    override val pageClass: Class<PaperSearchPage>
        get() = PaperSearchPage::class.java

    override fun assertSpecificComponents() {
        assertSearchOrderSelectorPanel("searchOrderSelectorPanel")
        assertSearchOrderPanel("searchOrderPanel")
        assertResultPanel("resultPanel")
        verify { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify { paperSlimServiceMock.countBySearchOrder(any()) }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    private fun assertSearchOrderSelectorPanel(b: String) {
        tester.assertLabel(b + LABEL, "Saved Searches")
        tester.assertComponent(b, SearchOrderSelectorPanel::class.java)
    }

    private fun assertSearchOrderPanel(b: String) {
        tester.assertLabel(b + LABEL, "Search Conditions")
        tester.assertComponent(b, SearchOrderPanel::class.java)
    }

    private fun assertResultPanel(b: String) {
        tester.assertLabel(b + LABEL, "Search Results")
        tester.assertComponent(b, ResultPanel::class.java)
    }

    @Test
    fun clickingAddSearchCondition_forwardsToPaperSearchCriteriaPageToLoadSearchOrder() {
        val pp = PageParameters().add(CorePageParameters.SEARCH_ORDER_ID.getName(), SO_ID)

        tester.startPage(pageClass, pp)

        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("searchOrderPanel:form")
        formTester.submit("addSearchCondition")
        tester.assertRenderedPage(PaperSearchCriteriaPage::class.java)

        verify { searchOrderServiceMock.findById(SO_ID) }
        verify { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify { paperSlimServiceMock.countBySearchOrder(any()) }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun clickingRemoveButtonOnSearchCondition_removesSearchCondition() {
        val labelDisplayValue = "searchConditionDisplayValue"
        val sc: SearchCondition = object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = labelDisplayValue
        }
        val conditions = listOf(sc)
        val so = SearchOrder(conditions)
        so.id = 6L
        val page = PaperSearchPage(Model.of(so), Mode.EDIT)

        tester.startPage(page)

        tester.assertRenderedPage(pageClass)
        val linkPath = "searchOrderPanel:form:searchConditions:body:rows:1:cells:2:cell:link"
        tester.assertComponent(linkPath, AjaxLink::class.java)
        tester.assertContains(labelDisplayValue)
        tester.clickLink(linkPath)
        tester.assertContainsNot(labelDisplayValue)
        tester.assertComponentOnAjaxResponse("searchOrderSelectorPanel")
        tester.assertComponentOnAjaxResponse("searchOrderPanel")
        tester.assertComponentOnAjaxResponse("resultPanelLabel")
        tester.assertComponentOnAjaxResponse("resultPanel")

        // TODO test that the event is sent, and also that receiving the event adds the
        // filter panel to the target. See also next test
        verify(exactly = 0) { searchOrderServiceMock.findById(SO_ID) }
        verify(exactly = 2) { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 2) { paperSlimServiceMock.countBySearchOrder(any()) }
        verify(exactly = 5) { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun clickingNewSearchCondition_reloadsPage() {
        every { searchOrderServiceMock.saveOrUpdate(any()) } returns SearchOrder().apply {
            id = 27L
        }
        every { searchOrderServiceMock.findById(27L) } returns Optional.of(SearchOrder())
        val labelDisplayValue = "searchConditionDisplayValue"
        val sc: SearchCondition = object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = labelDisplayValue
        }
        val conditions = listOf(sc)
        val so = SearchOrder(conditions)
        so.id = 6L
        val page = PaperSearchPage(Model.of(so), Mode.EDIT)

        tester.startPage(page)

        tester.assertRenderedPage(pageClass)
        val linkPath = "searchOrderSelectorPanel:form:new"
        tester.assertComponent(linkPath, AjaxSubmitLink::class.java)
        tester.clickLink(linkPath)
        tester.assertRenderedPage(PaperSearchPage::class.java)

        verify { searchOrderServiceMock.saveOrUpdate(any()) }
        verify(exactly = 2) { searchOrderServiceMock.findPageByFilter(any(), any()) }
    }

    @Test
    fun clickingNewSearchCondition_withOptimisticLockingException_failsSaveAndWarns() {
        every { searchOrderServiceMock.saveOrUpdate(any()) } throws
            OptimisticLockingException("searchOrder", "record", OptimisticLockingException.Type.UPDATE)
        val labelDisplayValue = "searchConditionDisplayValue"
        val sc: SearchCondition = object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = labelDisplayValue
        }
        val conditions = listOf(sc)
        val so = SearchOrder(conditions)
        so.id = 6L
        val page = PaperSearchPage(Model.of(so), Mode.EDIT)

        tester.startPage(page)

        tester.assertRenderedPage(pageClass)
        val linkPath = "searchOrderSelectorPanel:form:new"
        tester.assertComponent(linkPath, AjaxSubmitLink::class.java)
        tester.clickLink(linkPath)
        tester.assertErrorMessages(
            "The searchOrder with id 6 has been modified concurrently by another user. " +
                "Please reload it and apply your changes once more."
        )
        tester.assertRenderedPage(PaperSearchPage::class.java)

        verify(exactly = 2) { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify { searchOrderServiceMock.saveOrUpdate(any()) }
    }

    @Test
    fun clickingRemoveButtonOnResults_removesResultAndSavesSearchOrder() {
        val searchOrder1 = SearchOrder().apply {
            id = SO_ID
        }
        val searchOrder2 = SearchOrder().apply {
            id = SO_ID
        }
        every { paperSlimServiceMock.countBySearchOrder(searchOrder1) } returnsMany listOf(1, 0)
        every { paperSlimServiceMock.findPageBySearchOrder(searchOrder1, any()) } returns listOf(paperSlim)
        every { searchOrderServiceMock.saveOrUpdate(any()) } returns searchOrder2

        val page = PaperSearchPage(Model.of(searchOrder1), Mode.EDIT)
        tester.startPage(page)

        tester.assertRenderedPage(pageClass)
        val someTextInRow = "fa-solid fa-ban fa-fw"
        tester.assertContains(someTextInRow)
        val linkPath = "resultPanel:table:body:rows:1:cells:5:cell:link"
        tester.assertComponent(linkPath, AjaxLink::class.java)
        tester.clickLink(linkPath)
        tester.assertContainsNot(someTextInRow)

        verify(exactly = 2) { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder1) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrder1, any()) }
        verify(exactly = 1) { searchOrderServiceMock.saveOrUpdate(searchOrder1) }
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder2) }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsBySearchOrder(searchOrder1, any()) }
        verify(exactly = 3) { paperServiceMock.findPageOfIdsBySearchOrder(searchOrder2, any()) }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsByFilter(any(), any()) }
    }

    @Test
    fun constructingPage_withPageParametersHavingSearchOrderId_loadsSearchOrderFromDb() {
        val pp = PageParameters().add(CorePageParameters.SEARCH_ORDER_ID.getName(), SO_ID)

        tester.startPage(pageClass, pp)

        tester.assertRenderedPage(pageClass)
        tester.assertModelValue("searchOrderSelectorPanel:form:searchOrder", searchOrder)

        verify { searchOrderServiceMock.findById(SO_ID) }
        verify { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify { paperSlimServiceMock.countBySearchOrder(any()) }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun constructingPage_withPageParametersLackingSearchOrderId_setsFreshSearchOrder() {
        tester.startPage(pageClass, PageParameters())

        tester.assertRenderedPage(pageClass)

        verify(exactly = 0) { searchOrderServiceMock.findById(SO_ID) }
        verify { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify { paperSlimServiceMock.countBySearchOrder(any()) }
        verify(exactly = 2) { paperServiceMock.findPageOfIdsBySearchOrder(any(), any()) }
    }

    @Test
    fun searchOrderMock_withNoExclusions_hidesShowExcludedButton() {
        val searchOrder1 = SearchOrder().apply {
            id = SO_ID
        }
        every { paperSlimServiceMock.countBySearchOrder(searchOrder1) } returnsMany listOf(1, 0)
        every { paperSlimServiceMock.findPageBySearchOrder(searchOrder1, any()) } returns listOf(paperSlim)
        every { searchOrderServiceMock.saveOrUpdate(any()) } returns SearchOrder()

        val page = PaperSearchPage(Model.of(searchOrder1), Mode.EDIT)
        tester.startPage(page)

        tester.assertRenderedPage(pageClass)
        val linkPath = "searchOrderSelectorPanel:form:showExcluded"
        tester.assertInvisible(linkPath)

        verify(exactly = 1) { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify(exactly = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder1) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrder1, any()) }
    }

    @Test
    fun searchOrderMock_withExclusions_whenClicking_sendsEvent() {
        val searchOrder1 = SearchOrder().apply {
            id = SO_ID
            setExcludedPaperIds(listOf(5L, 3L))
        }
        every { paperSlimServiceMock.countBySearchOrder(searchOrder1) } returnsMany listOf(1, 0)
        every { paperSlimServiceMock.findPageBySearchOrder(searchOrder1, any()) } returns listOf(paperSlim)
        every { searchOrderServiceMock.saveOrUpdate(any()) } returns SearchOrder()

        val page = PaperSearchPage(Model.of(searchOrder1), Mode.EDIT)
        tester.startPage(page)

        tester.assertRenderedPage(pageClass)
        val linkPath = "searchOrderSelectorPanel:form:showExcluded"
        tester.assertComponent(linkPath, AjaxCheckBox::class.java)
        tester.executeAjaxEvent(linkPath, "click")
        tester.assertComponentOnAjaxResponse("resultPanelLabel")
        tester.assertComponentOnAjaxResponse("resultPanel")

        verify(exactly = 1) { searchOrderServiceMock.findPageByFilter(any(), any()) }
        verify(atLeast = 1) { paperSlimServiceMock.countBySearchOrder(searchOrder1) }
        verify(exactly = 1) { paperSlimServiceMock.findPageBySearchOrder(searchOrder1, any()) }
    }

    companion object {
        private const val SO_ID: Long = 7
        private const val LABEL = "Label"
    }
}
