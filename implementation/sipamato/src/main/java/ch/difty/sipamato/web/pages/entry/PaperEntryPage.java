package ch.difty.sipamato.web.pages.entry;

import java.time.LocalDate;
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

    private Form<Paper> form;

    public PaperEntryPage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();

        form = new Form<Paper>("form", new CompoundPropertyModel<Paper>(getNewDefaultModel())) {
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

        addFieldAndLabel(new TextField<Integer>(Paper.ID), new PropertyValidator<Integer>());
        addFieldAndLabel(new TextField<Integer>(Paper.PUBL_YEAR), new PropertyValidator<Integer>());
        addFieldAndLabel(new TextField<Integer>(Paper.PMID));
        addFieldAndLabel(new TextField<String>(Paper.DOI), new PropertyValidator<Integer>());

    }

    private Model<Paper> getNewDefaultModel() {
        final Paper p = new Paper();
        p.setPublicationYear(LocalDate.now().getYear());
        return Model.of(p);
    }

    /*
     * The authors field determines the firstAuthor field, but only unless overridden. Changes in the author field (if not overridden)
     * or in the override checkbox can have an effect on the firstAuthor field (enabled, content) 
     */
    private void makeAndAddAuthorComplex(String authorsId, String firstAuthorId, String firstAuthorOverriddenId) {
        TextArea<String> authors = new TextArea<String>(authorsId);
        authors.setEscapeModelStrings(false);
        addFieldAndLabel(authors, new PropertyValidator<String>());

        CheckBox firstAuthorOverridden = new CheckBox(firstAuthorOverriddenId);
        addCheckBoxAndLabel(firstAuthorOverridden);

        TextField<String> firstAuthor = new TextField<String>(firstAuthorId) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(firstAuthorOverridden.getModelObject());
            }
        };
        firstAuthor.add(new PropertyValidator<String>());
        firstAuthor.setOutputMarkupId(true);
        addFieldAndLabel(firstAuthor);

        firstAuthorOverridden.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
        authors.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
    }

    /*
     * Behavior to parse the authors string and populate the firstAuthor field - but only if not overridden. 
     */
    private OnChangeAjaxBehavior makeFirstAuthorChangeBehavior(TextArea<String> authors, CheckBox overridden, TextField<String> firstAuthor) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (!overridden.getModelObject()) {
                    AuthorParser p = new AuthorParser(authors.getValue());
                    firstAuthor.setModelObject(p.getFirstAuthor().orElse(null));
                }
                target.add(firstAuthor);
            }
        };
    }

    private void addFieldAndLabel(FormComponent<?> field) {
        addFieldAndLabel(field, Optional.empty());
    }

    private void addFieldAndLabel(FormComponent<?> field, PropertyValidator<?> pv) {
        addFieldAndLabel(field, Optional.ofNullable(pv));
    }

    private void addFieldAndLabel(FormComponent<?> field, Optional<PropertyValidator<?>> pv) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        form.add(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        form.add(field);
        if (pv.isPresent()) {
            field.add(pv.get());
        }
    }

    private void addCheckBoxAndLabel(CheckBox field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        form.add(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        form.add(field);
    }

}
