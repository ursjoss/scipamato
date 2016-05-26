package ch.difty.sipamato.web.pages.entry;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.logic.parsing.AuthorParser;
import ch.difty.sipamato.web.pages.BasePage;

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

        TextArea<String> authorsField = new TextArea<String>(Paper.AUTHORS);
        addFieldAndLabel(authorsField, new PropertyValidator<String>());

        TextField<String> firstAuthorField = new TextField<String>(Paper.FIRST_AUTHOR);
        firstAuthorField.setEnabled(false);
        firstAuthorField.setOutputMarkupId(true);
        addFieldAndLabel(firstAuthorField);

        authorsField.add(new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                AuthorParser p = new AuthorParser(authorsField.getValue());
                firstAuthorField.setModelObject(p.getFirstAuthor().orElse(null));
                target.add(firstAuthorField);
            }
        });

        addFieldAndLabel(new TextArea<String>(Paper.TITLE), new PropertyValidator<String>());
        addFieldAndLabel(new TextField<String>(Paper.LOCATION));
    }

    private void addFieldAndLabel(FormComponent<String> field) {
        addFieldAndLabel(field, Optional.empty());
    }

    private void addFieldAndLabel(FormComponent<String> field, PropertyValidator<?> pv) {
        addFieldAndLabel(field, Optional.ofNullable(pv));
    }

    private void addFieldAndLabel(FormComponent<String> field, Optional<PropertyValidator<?>> pv) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + ".label", this, null);
        Label label = new Label(id + "Label", labelModel);
        field.setLabel(labelModel);
        form.add(label);
        form.add(field);
        if (pv.isPresent()) {
            field.add(pv.get());
        }
    }
}
