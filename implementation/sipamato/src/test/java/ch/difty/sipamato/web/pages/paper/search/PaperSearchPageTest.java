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
import org.mockito.Mock;

import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.web.component.data.LinkIconPanel;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    @Mock
    private CompositeComplexPaperFilter mockCompositeComplexPaperFilter;

    @Override
    protected PaperSearchPage makePage() {
        final List<ComplexPaperFilter> filters = Arrays.asList(new ComplexPaperFilter());
        final CompositeComplexPaperFilter compositeFilter = new CompositeComplexPaperFilter(filters);
        return new PaperSearchPage(Model.of(compositeFilter));
    }

    @Override
    protected Class<PaperSearchPage> getPageClass() {
        return PaperSearchPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertForm("form");
        assertResultPanel("resultPanel");
    }

    private void assertForm(String b) {
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

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("addSearch");

        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);
    }

    @Test
    public void clickingRemoveButtonOnSearchTerms_removesFilter() {
        final String labelToString = "complexPaperFilterToString";
        final ComplexPaperFilter f = new ComplexPaperFilter() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return labelToString;
            }
        };
        final List<ComplexPaperFilter> filters = Arrays.asList(f);
        final CompositeComplexPaperFilter compositeFilter = new CompositeComplexPaperFilter(filters);
        PaperSearchPage page = new PaperSearchPage(Model.of(compositeFilter));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();

        final String linkPath = "form:searchTerms:body:rows:1:cells:2:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().assertContains(labelToString);
        getTester().clickLink(linkPath);
        getTester().debugComponentTrees();
        getTester().assertContainsNot(labelToString);
    }

}
