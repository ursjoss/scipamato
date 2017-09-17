package ch.difty.scipamato.web.panel.search;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.entity.filter.SearchTerm;
import ch.difty.scipamato.entity.filter.SearchTermType;
import ch.difty.scipamato.service.CodeClassService;
import ch.difty.scipamato.service.CodeService;
import ch.difty.scipamato.web.component.data.LinkIconPanel;
import ch.difty.scipamato.web.pages.paper.search.PaperSearchCriteriaPage;
import ch.difty.scipamato.web.panel.PanelTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class SearchOrderPanelTest extends PanelTest<SearchOrderPanel> {

    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private CodeService codeServiceMock;

    @Override
    protected SearchOrderPanel makePanel() {
        SearchCondition sc = new SearchCondition();
        sc.addSearchTerm(SearchTerm.of(1, SearchTermType.STRING.getId(), 1, "authors", "foo"));
        final List<SearchCondition> conditions = Arrays.asList(sc);
        final SearchOrder searchOrder = new SearchOrder(conditions);
        searchOrder.setId(5l);
        return new SearchOrderPanel(PANEL_ID, Model.of(searchOrder));
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, Panel.class);
        assertForm(b + ":form");
    }

    private void assertForm(String b) {
        getTester().assertComponent(b, Form.class);

        String bb = b + ":addSearchCondition";
        getTester().assertComponent(bb, BootstrapAjaxButton.class);
        getTester().assertModelValue(bb, "Add Search Condition");

        assertSearchConditions(b + ":searchConditions");
    }

    private void assertSearchConditions(String b) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        getTester().assertComponent(b + ":body", WebMarkupContainer.class);
        getTester().assertComponent(b + ":body:rows", DataGridView.class);
        getTester().assertLabel(b + ":body:rows:1:cells:1:cell:link:label", "foo");
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell", LinkIconPanel.class);
        getTester().assertComponent(b + ":body:rows:1:cells:2:cell:link", AjaxLink.class);
        getTester().assertLabel(b + ":body:rows:1:cells:2:cell:link:image", "");
    }

    @Test
    public void newButtonIsEnabled_ifSearchOrderIdPresent() {
        getTester().startComponentInPage(makePanel());
        getTester().isEnabled(PANEL_ID + ":form:addSearchCondition");
    }

    @Test
    public void newButtonIsDisabled_ifSearchOrderIdNotPresent() {
        getTester().startComponentInPage(new SearchOrderPanel(PANEL_ID, Model.of(new SearchOrder())));
        getTester().isDisabled(PANEL_ID + ":form:addSearchCondition");
    }

    @Test
    public void clickingNewButton_forwardsToPaperSearchCriteriaPage() {
        getTester().startComponentInPage(makePanel());
        FormTester formTester = getTester().newFormTester(PANEL_ID + ":form", false);
        formTester.submit("addSearchCondition");
        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);

        verify(codeClassServiceMock).find(anyString());
        verify(codeServiceMock, times(8)).findCodesOfClass(isA(CodeClassId.class), anyString());
    }

    @Test
    public void clickingDeleteIconLink_() {
        getTester().startComponentInPage(makePanel());
        getTester().assertContains("foo");
        getTester().clickLink("panel:form:searchConditions:body:rows:1:cells:2:cell:link");
        getTester().assertInfoMessages("Removed foo");
        getTester().assertComponentOnAjaxResponse(PANEL_ID + ":form:searchConditions");
    }

    @Test
    public void clickingLink_opensPaperSearchCriteriaPage() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":form:searchConditions:body:rows:1:cells:1:cell:link");
        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);
    }

}
