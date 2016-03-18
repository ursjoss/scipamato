package ch.difty.sipamato;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation("USER")
public class PaperEntryPage extends BasePage {

    private static final long serialVersionUID = 1L;

    public PaperEntryPage(PageParameters parameters) {
        super(parameters);
    }

    private Form<Paper> form;

    protected void onInitialize() {
        super.onInitialize();

        form = new Form<Paper>("form", new CompoundPropertyModel<Paper>(Model.of(new Paper()))) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                info("form submitted. Paper:" + getModelObject());
            }
        };
        add(form);

        TextField<String> authorField = new RequiredTextField<String>(Paper.AUTHOR);
        addFieldAndLabel(authorField);

        TextField<String> firstAuthorField = new TextField<String>(Paper.FIRST_AUTHOR);
        firstAuthorField.setEnabled(false);
        firstAuthorField.setOutputMarkupId(true);
        addFieldAndLabel(firstAuthorField);

        authorField.add(new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(firstAuthorField);
            }
        });

        addFieldAndLabel(new RequiredTextField<String>(Paper.TITLE));
        addFieldAndLabel(new TextField<String>(Paper.LOCATION));
    }

    private void addFieldAndLabel(FormComponent<String> field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + ".label", this, null);
        Label label = new Label(id + "Label", labelModel);
        field.setLabel(labelModel);
        form.add(label);
        form.add(field);
    }

}
