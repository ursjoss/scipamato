package ch.difty.sipamato.web.pages.paper.search;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.web.component.data.LinkIconPanel;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderSelectorPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    @Override
    protected PaperSearchPage makePage() {
        final List<SearchCondition> conditions = Arrays.asList(new SearchCondition());
        final SearchOrder searchOrder = new SearchOrder(conditions);
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
    }

    @Test
    public void clickingAddSearch_forwardsToPaperSearchCriteriaPage() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchOrderPanel:form");
        formTester.submit("addSearch");

        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);
    }

    @Test
    public void clickingRemoveButtonOnSearchTerms_removesSearchTerm() {
        final String labelToString = "searchConditionToString";
        final SearchCondition sc = new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return labelToString;
            }
        };
        final List<SearchCondition> conditions = Arrays.asList(sc);
        final SearchOrder searchOrder = new SearchOrder(conditions);
        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrder));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();

        final String linkPath = "searchOrderPanel:form:searchTerms:body:rows:1:cells:2:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().assertContains(labelToString);
        getTester().clickLink(linkPath);
        getTester().debugComponentTrees();
        getTester().assertContainsNot(labelToString);

        // TODO test that the event is sent
        // TODO also test that receiving the event adds the filter panel to the target
    }

}
