package ch.difty.sipamato.web.pages.paper.list;

import org.apache.wicket.markup.html.form.Form;

import ch.difty.sipamato.web.pages.AbstractPageTest;

public class PaperListPageTest extends AbstractPageTest<PaperListPage> {

    @Override
    protected Class<PaperListPage> getPageClass() {
        return PaperListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "searchForm";
        getTester().assertComponent(b, Form.class);

        assertLabeledTextField(b, "searchField");
    }

}
