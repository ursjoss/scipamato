package ch.difty.sipamato.web.pages.paper.list;

import org.apache.wicket.markup.html.form.Form;

import ch.difty.sipamato.web.pages.BasePageTest;

public class PaperListPageTest extends BasePageTest<PaperListPage> {

    @Override
    protected PaperListPage makePage() {
        return new PaperListPage(null);
    }

    @Override
    protected Class<PaperListPage> getPageClass() {
        return PaperListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "searchForm";
        getTester().assertComponent(b, Form.class);

        assertLabeledTextField(b, "authorsSearch");
        assertLabeledTextField(b, "methodsSearch");
        assertLabeledTextField(b, "fieldSearch");
        assertLabeledTextField(b, "pubYearFrom");
        assertLabeledTextField(b, "pubYearUntil");
    }

}
