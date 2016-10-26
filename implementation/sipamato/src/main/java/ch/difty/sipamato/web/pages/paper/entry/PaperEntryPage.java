package ch.difty.sipamato.web.pages.paper.entry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClass;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.logic.parsing.AuthorParser;
import ch.difty.sipamato.logic.parsing.AuthorParserFactory;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.model.CodeClassModel;
import ch.difty.sipamato.web.model.CodeModel;
import ch.difty.sipamato.web.pages.AutoSaveAwarePage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;

@MountPath("entry")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperEntryPage extends AutoSaveAwarePage<Paper> {

    private static final long serialVersionUID = 1L;

    // TODO replace with global setting in some bean
    private static final String LANG = "de";

    private Form<Paper> form;

    @SpringBean
    private PaperService service;

    @SpringBean
    private AuthorParserFactory authorParserFactory;

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

    @Override
    protected void implSpecificOnInitialize() {
        form = new Form<Paper>("form", new CompoundPropertyModel<Paper>(getModel())) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                doUpdate(getModelObject());
                info(new StringResourceModel("save.successful.hint", this, null).setParameters(getModelObject().getId(), getModelObject().getAuthors(), getModelObject().getPublicationYear())
                        .getString());
            }
        };
        queue(form);

        queueHeaderFields();
        queueTabPanel("tabs");
    }

    @Override
    protected Form<Paper> getForm() {
        return form;
    }

    @Override
    protected String getEntityName() {
        return "Paper";
    };

    @Override
    protected void doUpdate(Paper paper) {
        try {
            modelChanged();
            Paper persisted = service.update(paper);
            if (persisted != null) {
                setModelObject(persisted); // necessary?
                setClean();
            } else {
                warn(new StringResourceModel("save.unsuccessful.hint", this, null).setParameters(getModelObject().getId()));
            }
        } catch (Exception ex) {
            warn(new StringResourceModel("save.error.hint", this, null).setParameters(getModelObject().getId(), ex.getMessage()));
        }
    }

    private void queueHeaderFields() {
        queueAuthorComplex(Paper.AUTHORS, Paper.FIRST_AUTHOR, Paper.FIRST_AUTHOR_OVERRIDDEN);
        queueFieldAndLabel(new TextArea<String>(Paper.TITLE), new PropertyValidator<String>());
        queueFieldAndLabel(new TextField<String>(Paper.LOCATION));

        TextField<Integer> id = new TextField<Integer>(Paper.ID);
        id.setEnabled(false);
        queueFieldAndLabel(id);
        queueFieldAndLabel(new TextField<Integer>(Paper.PUBL_YEAR), new PropertyValidator<Integer>());
        queueFieldAndLabel(new TextField<Integer>(Paper.PMID));
        queueFieldAndLabel(new TextField<String>(Paper.DOI), new PropertyValidator<String>());
        makeAndQueueSubmitButton("submit");
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

    private void makeAndQueueSubmitButton(String id) {
        SubmitLink submitLink = new SubmitLink(id, form);
        submitLink.add(new ButtonBehavior());
        submitLink.setBody(new StringResourceModel("button.save.label"));
        submitLink.setDefaultFormProcessing(true);
        queue(submitLink);
    }

    private void queueCheckBoxAndLabel(CheckBox field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
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

    private class TabPanel3 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel3(String id, IModel<Paper> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<Paper> form = new Form<Paper>("tab3Form");
            queue(form);

            CodeClassModel codeClassModel = new CodeClassModel(LANG);
            List<CodeClass> codeClasses = codeClassModel.getObject();

            // TODO restrict modelClasses to only one type (some adaptermodel or so?)
            // TODO visualization (pills ??)
            // TODO storing the stuff
            // TODO visual style of the boxes
            makeCodeClassComplex(CodeClassId.CC1, codeClasses);
            makeCodeClassComplex(CodeClassId.CC2, codeClasses);
            makeCodeClassComplex(CodeClassId.CC3, codeClasses);
            makeCodeClassComplex(CodeClassId.CC4, codeClasses);
            makeCodeClassComplex(CodeClassId.CC5, codeClasses);
            makeCodeClassComplex(CodeClassId.CC6, codeClasses);
            makeCodeClassComplex(CodeClassId.CC7, codeClasses);
            makeCodeClassComplex(CodeClassId.CC8, codeClasses);

        }

        private void makeCodeClassComplex(CodeClassId ccId, final List<CodeClass> codeClasses) {
            final int id = ccId.getId();
            final String className = codeClasses.stream().filter(cc -> cc.getId() == id).map(CodeClass::getName).findFirst().orElse(ccId.name());
            queue(new Label("codesClass" + id + "Label", Model.of(className)));

            final PropertyModel<List<Code>> model = new PropertyModel<List<Code>>(getModel(), Paper.CODES);
            final CodeModel choices = new CodeModel(ccId, LANG);
            final IChoiceRenderer<Code> choiceRenderer = new ChoiceRenderer<Code>(Code.NAME, Code.DISPLAY_VALUE);
            queue(new BootstrapMultiSelect<Code>("codesClass" + id, model, choices, choiceRenderer));
        }
    }

}
