package ch.difty.sipamato.web.pages.paper.entry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.logic.parsing.AuthorParser;
import ch.difty.sipamato.logic.parsing.AuthorParserFactory;
import ch.difty.sipamato.persistance.repository.PaperRepository;
import ch.difty.sipamato.web.pages.BasePage;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;

@MountPath("entry")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperEntryPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private Form<Paper> form;

    @SpringBean
    private PaperRepository repo;

    @SpringBean
    private AuthorParserFactory authorParserFactory;

    public PaperEntryPage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();

        Paper p = repo.findById(2l);
        form = new Form<Paper>("form", new CompoundPropertyModel<Paper>(Model.of(p))) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                repo.update(getModelObject());
                info("Successfully saved Paper with id " + getModelObject().getId() + ": " + getModelObject().getAuthors() + " (" + getModelObject().getPublicationYear() + ")");
            }
        };
        add(form);

        makeAndAddHeaderFields();
        makeAndAddTabPanel("tabs");
    }

    private void makeAndAddHeaderFields() {
        makeAndAddAuthorComplex(Paper.AUTHORS, Paper.FIRST_AUTHOR, Paper.FIRST_AUTHOR_OVERRIDDEN);
        addFieldAndLabel(new TextArea<String>(Paper.TITLE), new PropertyValidator<String>());
        addFieldAndLabel(new TextField<String>(Paper.LOCATION));

        addFieldAndLabel(new TextField<Integer>(Paper.ID), new PropertyValidator<Integer>());
        addFieldAndLabel(new TextField<Integer>(Paper.PUBL_YEAR), new PropertyValidator<Integer>());
        addFieldAndLabel(new TextField<Integer>(Paper.PMID));
        addFieldAndLabel(new TextField<String>(Paper.DOI), new PropertyValidator<String>());
    }

    private void makeAndAddTabPanel(String tabId) {
        List<ITab> tabs = new ArrayList<>();
        tabs.add(new AbstractTab(new StringResourceModel("tab1" + LABEL_RECOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel1(panelId, form.getModel());
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("tab2" + LABEL_RECOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel2(panelId, form.getModel());
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("tab3" + LABEL_RECOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel3(panelId, form.getModel());
            }
        });
        form.add(new ClientSideBootstrapTabbedPanel<ITab>(tabId, tabs));
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
                    AuthorParser p = authorParserFactory.createParser(authors.getValue());
                    firstAuthor.setModelObject(p.getFirstAuthor().orElse(null));
                }
                target.add(firstAuthor);
            }
        };
    }

    private void addFieldAndLabel(FormComponent<?> field) {
        addFieldAndLabel(form, field, Optional.empty());
    }

    private void addFieldAndLabel(FormComponent<?> field, PropertyValidator<?> pv) {
        addFieldAndLabel(form, field, Optional.ofNullable(pv));
    }

    private void addCheckBoxAndLabel(CheckBox field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        form.add(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        form.add(field);
    }

    private static abstract class AbstractTabPanel extends Panel {

        private static final long serialVersionUID = 1L;

        public AbstractTabPanel(String id) {
            super(id);
        }

        public AbstractTabPanel(String id, IModel<?> model) {
            super(id, model);
        }

        void makeAndAddTo(Form<Paper> form, String id) {
            TextArea<String> field = new TextArea<String>(id);
            field.add(new PropertyValidator<String>());
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
            form.add(new Label(id + LABEL_TAG, labelModel));
            field.setLabel(labelModel);
            form.add(field);
        }
    }

    private static class TabPanel1 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel1(String id, IModel<Paper> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<Paper> form = new Form<Paper>("tab1Form");
            add(form);

            makeAndAddTo(form, Paper.GOALS);
            makeAndAddTo(form, Paper.POPULATION);
            makeAndAddTo(form, Paper.EXPOSURE);
            makeAndAddTo(form, Paper.METHODS);

            makeAndAddTo(form, Paper.POPULATION_PLACE);
            makeAndAddTo(form, Paper.POPULATION_PARTICIPANTS);
            makeAndAddTo(form, Paper.POPULATION_DURATION);

            makeAndAddTo(form, Paper.EXPOSURE_POLLUTANT);
            makeAndAddTo(form, Paper.EXPOSURE_ASSESSMENT);

            makeAndAddTo(form, Paper.METHOD_OUTCOME);
            makeAndAddTo(form, Paper.METHOD_STATISTICS);
            makeAndAddTo(form, Paper.METHOD_CONFOUNDERS);
        }
    };

    private static class TabPanel2 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel2(String id, IModel<Paper> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<Paper> form = new Form<Paper>("tab2Form");
            add(form);

            makeAndAddTo(form, Paper.RESULT);
            makeAndAddTo(form, Paper.COMMENT);
            makeAndAddTo(form, Paper.INTERN);

            makeAndAddTo(form, Paper.RESULT_EXPOSURE_RANGE);
            makeAndAddTo(form, Paper.RESULT_EFFECT_ESTIMATE);
        }
    };

    private static class TabPanel3 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel3(String id, IModel<Paper> model) {
            super(id, model);
        }
    };

}
