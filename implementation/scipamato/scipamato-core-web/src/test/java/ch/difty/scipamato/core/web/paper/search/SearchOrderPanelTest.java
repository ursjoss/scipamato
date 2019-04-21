package ch.difty.scipamato.core.web.paper.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.component.table.column.LinkIconPanel;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTermType;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.PanelTest;

public abstract class SearchOrderPanelTest extends PanelTest<SearchOrderPanel> {

    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private CodeService      codeServiceMock;

    @Override
    protected SearchOrderPanel makePanel() {
        SearchCondition sc = new SearchCondition();
        sc.addSearchTerm(SearchTerm.newSearchTerm(1, SearchTermType.STRING.getId(), 1, "authors", "foo"));
        final List<SearchCondition> conditions = Collections.singletonList(sc);
        final SearchOrder searchOrder = new SearchOrder(conditions);
        searchOrder.setId(5L);
        return new SearchOrderPanel(PANEL_ID, Model.of(searchOrder), getMode());
    }

    abstract Mode getMode();

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
        getTester().startComponentInPage(new SearchOrderPanel(PANEL_ID, Model.of(new SearchOrder()), getMode()));
        getTester().isDisabled(PANEL_ID + ":form:addSearchCondition");
    }

    @Test
    public void clickingNewButton_forwardsToPaperSearchCriteriaPage() {
        getTester().startComponentInPage(makePanel());
        FormTester formTester = getTester().newFormTester(PANEL_ID + ":form", false);
        formTester.submit("addSearchCondition");
        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);
    }

    @Test
    public void clickingLink_opensPaperSearchCriteriaPage() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":form:searchConditions:body:rows:1:cells:1:cell:link");
        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);
    }

    @Test
    public void searchOrderIdDefined_withRegularModel() {
        SearchOrderPanel p = makePanel();
        assertThat(p.isSearchOrderIdDefined()).isTrue();
    }

    @Test
    public void searchOrderIdDefined_withNullModel() {
        SearchOrderPanel p = new SearchOrderPanel(PANEL_ID, null, getMode());
        assertThat(p.isSearchOrderIdDefined()).isFalse();
    }

    @Test
    public void searchOrderIdDefined_withModelOFSearchOrderWIthNullId() {
        SearchOrder searchOrder = new SearchOrder();
        assertThat(searchOrder.getId()).isNull();
        SearchOrderPanel p = new SearchOrderPanel(PANEL_ID, Model.of(searchOrder), getMode());
        assertThat(p.isSearchOrderIdDefined()).isFalse();
    }

}
