package ch.difty.scipamato.common.web;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.config.ApplicationProperties;

public class TestAbstractPage extends AbstractPage<TestRecord> {
    private static final long serialVersionUID = 1L;

    public TestAbstractPage(final IModel<TestRecord> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<Void>("form"));
        queueFieldAndLabel(new TextField<String>("foo"));
        queue(newResponsePageButton("respPageButton", () -> new TestHomePage(new PageParameters())));
    }

    @Override
    protected ApplicationProperties getProperties() {
        return new TestApplicationProperties();
    }

    @Override
    protected boolean isNavbarVisible() {
        return true;
    }

}
