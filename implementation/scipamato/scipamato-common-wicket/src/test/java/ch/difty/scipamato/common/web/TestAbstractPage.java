package ch.difty.scipamato.common.web;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.scipamato.common.config.ApplicationProperties;

public class TestAbstractPage extends AbstractPage<TestRecord> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("WeakerAccess")
    public TestAbstractPage(final IModel<TestRecord> model) {
        super(CompoundPropertyModel.of(model));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queuePanelHeadingFor("panel");
        queue(new Form<TestRecord>("form"));
        queueFieldAndLabel(new TextField<String>("name"));
        queue(
            newResponsePageButton("respPageButton", () -> new TestAbstractPage(new Model(new TestRecord(10, "bar")))));
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
