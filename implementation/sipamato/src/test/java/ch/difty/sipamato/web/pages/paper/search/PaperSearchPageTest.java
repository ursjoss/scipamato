package ch.difty.sipamato.web.pages.paper.search;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.PageParameterNames;
import ch.difty.sipamato.web.component.data.LinkIconPanel;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderSelectorPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    private static final long SEARCH_ORDER_ID = 7;

    @MockBean
    private SearchOrderService searchOrderServiceMock;

    private final SearchOrder searchOrder = new SearchOrder(SEARCH_ORDER_ID, 1, false, null);

    @Override
    protected void setUpHook() {
        when(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.of(searchOrder));
    }

    @Override
    protected PaperSearchPage makePage() {
        final List<SearchCondition> conditions = Arrays.asList(new SearchCondition());
        final SearchOrder searchOrder = new SearchOrder(conditions);
        searchOrder.setId(5l);
        return new PaperSearchPage(Model.of(searchOrder));
    }

    @Override
    protected Class<PaperSearchPage> getPageClass() {
        return PaperSearchPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertSearchOrderSelectorPanel("searchOrderSelectorPanel");
        assertSearchOrderPanel("searchOrderPanel");
        assertResultPanel("resultPanel");
    }

    private void assertSearchOrderSelectorPanel(String b) {
        getTester().assertComponent(b, SearchOrderSelectorPanel.class);
        assertSearchOrderSelectorPanelForm(b + ":form");

        String bb = b + "Label";
        getTester().assertComponent(bb, Label.class);
        getTester().assertModelValue(bb, "Saved Searches");
    }

    private void assertSearchOrderSelectorPanelForm(String b) {
        getTester().assertComponent(b, Form.class);
    }

    private void assertSearchOrderPanel(String b) {
        getTester().assertComponent(b, SearchOrderPanel.class);
        assertSearchOrderPanelForm(b + ":form");

        String bb = b + "Label";
        getTester().assertComponent(bb, Label.class);
        getTester().assertModelValue(bb, "Search Conditions");
    }

    private void assertSearchOrderPanelForm(String b) {
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":addSearch", BootstrapAjaxButton.class);
        assertSearchTerms(b + ":searchTerms");
    }

    private void assertSearchTerms(String b) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        getTester().assertComponent(b + ":body", WebMarkupContainer.class);
        getTester().assertComponent(b + ":body:rows", DataGridView.class);
        getTester().assertComponent(b + ":body:rows:1:cells:1:cell", Label.class);
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell", LinkIconPanel.class);
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell:link", AjaxLink.class);
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell:link:image", Label.class);
    }

    private void assertResultPanel(String b) {
        getTester().assertComponent(b, ResultPanel.class);

        String bb = b + "Label";
        getTester().assertComponent(bb, Label.class);
        getTester().assertModelValue(bb, "Search Results");
    }

    @Test
    public void clickingAddSearch_forwardsToPaperSearchCriteriaPageToLoadSearchOrder() {
        PageParameters pp = new PageParameters().add(PageParameterNames.SEARCH_ORDER_ID, SEARCH_ORDER_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchOrderPanel:form");
        formTester.submit("addSearch");

        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);

        verify(searchOrderServiceMock).findById(SEARCH_ORDER_ID);
    }

    @Test
    public void clickingRemoveButtonOnSearchTerms_removesSearchTerm() {
        final String labelDisplayValue = "searchConditionDisplayValue";
        final SearchCondition sc = new SearchCondition() {
            private static final long serialVersionUID = 1L;

            public String getDisplayValue() {
                return labelDisplayValue;
            }
        };
        final List<SearchCondition> conditions = Arrays.asList(sc);
        final SearchOrder searchOrder = new SearchOrder(conditions);
        searchOrder.setId(6l);
        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrder));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();

        final String linkPath = "searchOrderPanel:form:searchTerms:body:rows:1:cells:2:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().assertContains(labelDisplayValue);
        getTester().clickLink(linkPath);
        getTester().debugComponentTrees();
        getTester().assertContainsNot(labelDisplayValue);

        // TODO test that the event is sent
        // TODO also test that receiving the event adds the filter panel to the target
        verify(searchOrderServiceMock, never()).findById(SEARCH_ORDER_ID);
    }

    @Test
    public void constructingPage_withPageParametersHavingSearchOrderId_loadsSearchOrderFromDb() {
        PageParameters pp = new PageParameters().add(PageParameterNames.SEARCH_ORDER_ID, SEARCH_ORDER_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();

        getTester().assertModelValue("searchOrderSelectorPanel:form:searchOrder", searchOrder);

        verify(searchOrderServiceMock).findById(SEARCH_ORDER_ID);
    }

    @Test
    public void constructingPage_withPageParametersLackingSearchOrderId_setsFreshSearchOrder() {
        getTester().startPage(getPageClass(), new PageParameters());
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();

        verify(searchOrderServiceMock, never()).findById(SEARCH_ORDER_ID);
    }

}
