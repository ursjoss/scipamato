package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.sipamato.web.pages.BasePageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

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
        String b = "form";
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":addSearch", BootstrapAjaxButton.class);
    }
}
