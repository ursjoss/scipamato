package ch.difty.scipamato.web.pages.portal;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.web.pages.BasePageTest;

public class PublicPageTest extends BasePageTest<PublicPage> {

    @Override
    protected PublicPage makePage() {
        return new PublicPage(new PageParameters());
    }

    @Override
    protected Class<PublicPage> getPageClass() {
        return PublicPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertSearchForm("searchForm");
    }

    private void assertSearchForm(String b) {
        getTester().assertComponent(b, Form.class);

        assertLabeledTextField(b, "number");
        assertLabeledTextField(b, "authorsSearch");
        assertLabeledTextField(b, "methodsSearch");
        assertLabeledTextField(b, "pubYearFrom");
        assertLabeledTextField(b, "pubYearUntil");
    }
}
