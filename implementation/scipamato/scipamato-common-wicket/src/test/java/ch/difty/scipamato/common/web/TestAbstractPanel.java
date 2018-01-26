package ch.difty.scipamato.common.web;

import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;

public class TestAbstractPanel extends AbstractPanel<TestRecord> {

    private static final long serialVersionUID = 1L;

    public TestAbstractPanel(String id, Mode mode) {
        super(id, null, mode);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queueFieldAndLabel(new TextField<String>("foo"));

        queueFieldAndLabel(new TextField<String>("bar"), new PropertyValidator<String>());

        queueCheckBoxAndLabel(new CheckBoxX("baz", Model.of(true)));
    }

}
