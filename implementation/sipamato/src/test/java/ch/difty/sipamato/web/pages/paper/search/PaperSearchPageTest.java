package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    @Mock
    private CompositeComplexPaperFilter mockCompositeComplexPaperFilter;

    @Override
    protected PaperSearchPage makePage() {
        return new PaperSearchPage(new PageParameters());
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

}
