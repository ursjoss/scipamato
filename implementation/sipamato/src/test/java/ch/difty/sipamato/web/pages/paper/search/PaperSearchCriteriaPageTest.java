package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

public class PaperSearchCriteriaPageTest extends BasePageTest<PaperSearchCriteriaPage> {

    @Override
    protected PaperSearchCriteriaPage makePage() {
        return new PaperSearchCriteriaPage(new PageParameters());
    }

    @Override
    protected Class<PaperSearchCriteriaPage> getPageClass() {
        return PaperSearchCriteriaPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        getTester().assertComponent("contentPanel", SearchablePaperPanel.class);
    }

    @Test
    public void clickingAddSearch_forwardsToPaperSearchCriteriaPage() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("contentPanel:form");
        formTester.submit();

        getTester().assertRenderedPage(PaperSearchPage.class);
    }

}
