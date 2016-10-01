package ch.difty.sipamato.web.pages.paper.entry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.behavior.AttributeAppender;
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
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.logic.parsing.AuthorParser;
import ch.difty.sipamato.logic.parsing.AuthorParserFactory;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.pages.BasePage;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;

@MountPath("entry")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperEntryPage extends BasePage<Paper> {

    private static final long serialVersionUID = 1L;

    private Form<Paper> form;

    @SpringBean
    private PaperService service;

    @SpringBean
    private AuthorParserFactory authorParserFactory;

    @SpringBean
    private ApplicationProperties applicationProperties;

    public PaperEntryPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        final Paper p = new Paper();
        p.setPublicationYear(LocalDate.now().getYear());
        setDefaultModel(Model.of(p));
    }

    public PaperEntryPage(IModel<Paper> paperModel) {
        super(paperModel);
    }

    protected void onInitialize() {
        super.onInitialize();

        form = new BootstrapForm<Paper>("form", new CompoundPropertyModel<Paper>(getModel())) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                doUpdate(getModelObject());
                info("Successfully saved Paper [id " + getModelObject().getId() + "]: " + getModelObject().getAuthors() + " (" + getModelObject().getPublicationYear() + ")");
            }
        };
        if (applicationProperties.isPaperAutoSaveMode()) {
            form.add(makeAutoSaveAjaxTimerBehavior());
            String msg = new StringResourceModel("paper.autosave.info", this, null).setParameters(applicationProperties.getPaperAutoSaveInterval()).getString();
            info(msg);
        }
        queue(form);

        queueHeaderFields();
        queueTabPanel("tabs");
    }

    private void doUpdate(Paper paper) {
        try {
            modelChanged();
            Paper persisted = service.update(paper);
            if (persisted != null) {
                setModelObject(persisted); // necessary?
            } else {
                warn("Could not save Paper [id " + getModelObject().getId() + "].");
            }
        } catch (Exception ex) {
            info("Saving product failed: " + ex.toString());
        }
    }

    /**
     * scheduled behavior to save the modified text of the paper via ajax without submit. 
     */
    private AbstractAjaxTimerBehavior makeAutoSaveAjaxTimerBehavior() {
        return new AbstractAjaxTimerBehavior(Duration.seconds(15)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onTimer(AjaxRequestTarget target) {
                doUpdate(form.getModelObject());
                form.modelChanged();
            }

        };
    }

    private void queueHeaderFields() {
        queueAuthorComplex(Paper.AUTHORS, Paper.FIRST_AUTHOR, Paper.FIRST_AUTHOR_OVERRIDDEN);
        queueFieldAndLabel(new TextArea<String>(Paper.TITLE), new PropertyValidator<String>());
        queueFieldAndLabel(new TextField<String>(Paper.LOCATION));

        queueFieldAndLabel(new TextField<Integer>(Paper.ID), new PropertyValidator<Integer>());
        queueFieldAndLabel(new TextField<Integer>(Paper.PUBL_YEAR), new PropertyValidator<Integer>());
        queueFieldAndLabel(new TextField<Integer>(Paper.PMID));
        queueFieldAndLabel(new TextField<String>(Paper.DOI), new PropertyValidator<String>());
    }

    private void queueTabPanel(String tabId) {
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
        queue(new ClientSideBootstrapTabbedPanel<ITab>(tabId, tabs));
    }

    /*
     * The authors field determines the firstAuthor field, but only unless overridden. Changes in the author field (if not overridden)
     * or in the override checkbox can have an effect on the firstAuthor field (enabled, content) 
     */
    private void queueAuthorComplex(String authorsId, String firstAuthorId, String firstAuthorOverriddenId) {
        TextArea<String> authors = new TextArea<String>(authorsId);
        authors.setEscapeModelStrings(false);
        queueFieldAndLabel(authors, new PropertyValidator<String>());

        CheckBox firstAuthorOverridden = new CheckBox(firstAuthorOverriddenId);
        queueCheckBoxAndLabel(firstAuthorOverridden);

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
        queueFieldAndLabel(firstAuthor);

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

    private void queueFieldAndLabel(FormComponent<?> field) {
        queueFieldAndLabel(field, Optional.empty());
    }

    private void queueFieldAndLabel(FormComponent<?> field, PropertyValidator<?> pv) {
        queueFieldAndLabel(field, Optional.ofNullable(pv));
    }

    private void queueCheckBoxAndLabel(CheckBox field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        field.setOutputMarkupId(true);
        field.add(newOcab());
        queue(field);
    }

    private static abstract class AbstractTabPanel extends Panel {

        private static final long serialVersionUID = 1L;

        public AbstractTabPanel(String id) {
            super(id);
        }

        public AbstractTabPanel(String id, IModel<?> model) {
            super(id, model);
        }

        void queueTo(Form<Paper> form, String id) {
            queueTo(form, id, false);
        }

        void queueNewFieldTo(Form<Paper> form, String id) {
            queueTo(form, id, true);
        }

        void queueTo(Form<Paper> form, String id, boolean newField) {
            TextArea<String> field = new TextArea<String>(id);
            field.add(new PropertyValidator<String>());
            field.setOutputMarkupId(true);
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
            queue(new Label(id + LABEL_TAG, labelModel));
            field.setLabel(labelModel);
            field.add(newOcab());
            if (newField) {
                field.add(new AttributeAppender("class", " newField"));
            }
            queue(field);
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
            queue(form);

            queueTo(form, Paper.GOALS);
            queueTo(form, Paper.POPULATION);
            queueTo(form, Paper.METHODS);

            queueNewFieldTo(form, Paper.POPULATION_PLACE);
            queueNewFieldTo(form, Paper.POPULATION_PARTICIPANTS);
            queueNewFieldTo(form, Paper.POPULATION_DURATION);

            queueNewFieldTo(form, Paper.EXPOSURE_POLLUTANT);
            queueNewFieldTo(form, Paper.EXPOSURE_ASSESSMENT);

            queueNewFieldTo(form, Paper.METHOD_STUDY_DESIGN);
            queueNewFieldTo(form, Paper.METHOD_OUTCOME);
            queueNewFieldTo(form, Paper.METHOD_STATISTICS);
            queueNewFieldTo(form, Paper.METHOD_CONFOUNDERS);
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
            queue(form);

            queueTo(form, Paper.RESULT);
            queueTo(form, Paper.COMMENT);
            queueTo(form, Paper.INTERN);

            queueNewFieldTo(form, Paper.RESULT_EXPOSURE_RANGE);
            queueNewFieldTo(form, Paper.RESULT_EFFECT_ESTIMATE);
        }
    };

    private static class TabPanel3 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel3(String id, IModel<Paper> model) {
            super(id, model);
        }
    };

}
