package ch.difty.sipamato.web.pages.entry;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
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

        makeAndAddAuthorComplex(Paper.AUTHORS, Paper.FIRST_AUTHOR, Paper.FIRST_AUTHOR_OVERRIDDEN);
        addFieldAndLabel(new TextArea<String>(Paper.TITLE), new PropertyValidator<String>());
        addFieldAndLabel(new TextField<String>(Paper.LOCATION));
    }

    private void makeAndAddAuthorComplex(String authorsId, String firstAuthorId, String firstAuthorOverriddenId) {
        TextArea<String> authorsField = new TextArea<String>(authorsId);
        addFieldAndLabel(authorsField, new PropertyValidator<String>());

        CheckBox firstAuthorOverridden = new CheckBox(firstAuthorOverriddenId);
        addCheckBoxAndLabel(firstAuthorOverridden);

        TextField<String> firstAuthorField = new TextField<String>(firstAuthorId) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(firstAuthorOverridden.getModelObject());
            }
        };
        firstAuthorField.add(new PropertyValidator<String>());
        firstAuthorField.setOutputMarkupId(true);
        addFieldAndLabel(firstAuthorField);

        firstAuthorOverridden.add(makeFirstAuthorChangeBehavior(authorsField, firstAuthorOverridden, firstAuthorField));
        authorsField.add(makeFirstAuthorChangeBehavior(authorsField, firstAuthorOverridden, firstAuthorField));
    }

    private OnChangeAjaxBehavior makeFirstAuthorChangeBehavior(TextArea<String> authorsField, CheckBox firstAuthorOverridden, TextField<String> firstAuthorField) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (!firstAuthorOverridden.getModelObject()) {
                    AuthorParser p = new AuthorParser(authorsField.getValue());
                    firstAuthorField.setModelObject(p.getFirstAuthor().orElse(null));
                }
                target.add(firstAuthorField);
            }
        };
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
        form.add(label);
        field.setLabel(labelModel);
        form.add(field);
        if (pv.isPresent()) {
            field.add(pv.get());
        }
    }

    private void addCheckBoxAndLabel(CheckBox field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + ".label", this, null);
        Label label = new Label(id + "Label", labelModel);
        form.add(label);
        field.setLabel(labelModel);
        form.add(field);
    }

}
