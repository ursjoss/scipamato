package ch.difty.sipamato.web.panel.paper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ChainingModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.time.Duration;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeBoxAware;
import ch.difty.sipamato.entity.CodeClass;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.web.jasper.summary_sp.PaperSummaryDataSource;
import ch.difty.sipamato.web.model.CodeClassModel;
import ch.difty.sipamato.web.model.CodeModel;
import ch.difty.sipamato.web.pages.Mode;
import ch.difty.sipamato.web.panel.AbstractPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;

public abstract class PaperPanel<T extends CodeBoxAware> extends AbstractPanel<T> {

    private static final long serialVersionUID = 1L;

    ResourceLink<Void> summaryLink;

    public PaperPanel(String id) {
        super(id);
    }

    public PaperPanel(String id, IModel<T> model) {
        super(id, model);
    }

    public PaperPanel(String id, IModel<T> model, Mode mode) {
        super(id, model, mode);
    }

    private Form<T> form;

    @Override
    public void onInitialize() {
        super.onInitialize();
        form = new Form<T>("form", new CompoundPropertyModel<T>(getModel())) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                onFormSubmit();
            }
        };
        queue(form);

        queueHeaderFields();
        queueTabPanel("tabs");
    }

    protected abstract void onFormSubmit();

    public Form<T> getForm() {
        return form;
    }

    private void queueHeaderFields() {
        queueAuthorComplex(Paper.AUTHORS, Paper.FIRST_AUTHOR, Paper.FIRST_AUTHOR_OVERRIDDEN);
        queueFieldAndLabel(new TextArea<String>(Paper.TITLE), new PropertyValidator<String>());
        queueFieldAndLabel(new TextField<String>(Paper.LOCATION), new PropertyValidator<String>());

        queueFieldAndLabel(new TextField<Integer>(Paper.PUBL_YEAR), new PropertyValidator<Integer>());
        queueFieldAndLabel(new TextField<Integer>(Paper.PMID));
        queueFieldAndLabel(new TextField<String>(Paper.DOI), new PropertyValidator<String>());

        TextField<Integer> id = new TextField<Integer>(Paper.ID);
        id.setEnabled(isSearchMode());
        queueFieldAndLabel(id);

        TextField<String> created = new TextField<String>(Paper.CREATED_DV);
        created.setEnabled(isSearchMode());
        queueFieldAndLabel(created);

        TextField<String> modified = new TextField<String>(Paper.MODIFIED_DV);
        modified.setEnabled(isSearchMode());
        queueFieldAndLabel(modified);

        makeAndQueueSubmitButton("submit");

        queue(makeSummaryLink("summaryLink"));

        // make sure attributes updated during persisting are reflected
        reflectPersistedChangesViaTimer(id, created, modified);
    }

    /**
     * Override this if you don't want to add the timer behavior or implement it differently.
     * @param id
     * @param created
     * @param modified
     */
    protected  void reflectPersistedChangesViaTimer(TextField<Integer> id, TextField<String> created, TextField<String> modified) {
        add(new AbstractAjaxTimerBehavior(Duration.seconds(1d)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onTimer(AjaxRequestTarget target) {
                target.add(id);
                target.add(created);
                target.add(modified);
            }
        }); 
    };

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
        tabs.add(new AbstractTab(new StringResourceModel("tab4" + LABEL_RECOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel4(panelId, form.getModel());
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

        PropertyModel<Boolean> firstAuthorOverriddenModel = new PropertyModel<Boolean>(getModel(), firstAuthorOverriddenId);
        CheckBoxX firstAuthorOverridden = new CheckBoxX(firstAuthorOverriddenId, firstAuthorOverriddenModel);
        firstAuthorOverridden.getConfig().withThreeState(isSearchMode()).withUseNative(true);
        queueCheckBoxAndLabel(firstAuthorOverridden);

        TextField<String> firstAuthor = makeFirstAuthor(firstAuthorId, firstAuthorOverridden);
        firstAuthor.setOutputMarkupId(true);
        queueFieldAndLabel(firstAuthor);

        addAuthorBehavior(authors, firstAuthorOverridden, firstAuthor);
    }

    /** override if special behavior is required */
    protected TextField<String> makeFirstAuthor(String firstAuthorId, CheckBox firstAuthorOverridden) {
        return new TextField<String>(firstAuthorId);
    }

    /** override if special behavior is required */
    protected void addAuthorBehavior(TextArea<String> authors, CheckBox firstAuthorOverridden, TextField<String> firstAuthor) {
    }

    private void queueFieldAndLabel(FormComponent<?> field) {
        queueFieldAndLabel(field, Optional.empty());
    }

    private void makeAndQueueSubmitButton(String id) {
        BootstrapButton submit = new BootstrapButton(id, new StringResourceModel(getSubmitLinkResourceLabel()), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onAfterSubmit() {
                super.onAfterSubmit();
                summaryLink.replaceWith(makeSummaryLink("summaryLink"));
            }
        };
        submit.setDefaultFormProcessing(true);
        submit.setEnabled(!isViewMode());
        queue(submit);
    }

    private ResourceLink<Void> makeSummaryLink(String id) {
        summaryLink = new ResourceLink<Void>(id, getSummaryDataSource());
        summaryLink.setVisible(!isSearchMode());
        summaryLink.setOutputMarkupId(true);
        summaryLink.setBody(new StringResourceModel("link.summary.label"));
        return summaryLink;
    }

    /** implement to return PaperSummaryDataSource */
    protected abstract PaperSummaryDataSource getSummaryDataSource();

    private abstract class AbstractTabPanel extends Panel {

        private static final long serialVersionUID = 1L;

        public AbstractTabPanel(String id) {
            super(id);
        }

        public AbstractTabPanel(String id, IModel<?> model) {
            super(id, model);
        }

        void queueTo(Form<T> form, String id) {
            queueTo(form, id, false, Optional.empty());
        }

        void queueTo(Form<T> form, String id, PropertyValidator<?> pv) {
            queueTo(form, id, false, Optional.ofNullable(pv));
        }

        void queueNewFieldTo(Form<T> form, String id) {
            queueTo(form, id, true, Optional.empty());
        }

        void queueTo(Form<T> form, String id, boolean newField, Optional<PropertyValidator<?>> pv) {
            TextArea<String> field = new TextArea<String>(id);
            field.setOutputMarkupId(true);
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
            queue(new Label(id + LABEL_TAG, labelModel));
            field.setLabel(labelModel);
            if (newField) {
                field.add(new AttributeAppender("class", " newField"));
            }
            if (pv.isPresent() && isEditMode()) {
                field.add(pv.get());
            }
            queue(field);
        }
    }

    private class TabPanel1 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel1(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> form = new Form<>("tab1Form");
            queue(form);

            queueTo(form, Paper.GOALS, new PropertyValidator<String>());
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
    }

    private class TabPanel2 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel2(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> form = new Form<>("tab2Form");
            queue(form);

            queueTo(form, Paper.RESULT);
            queueTo(form, Paper.COMMENT);
            queueTo(form, Paper.INTERN);

            queueNewFieldTo(form, Paper.RESULT_MEASURED_OUTCOME);
            queueNewFieldTo(form, Paper.RESULT_EXPOSURE_RANGE);
            queueNewFieldTo(form, Paper.RESULT_EFFECT_ESTIMATE);
        }
    }

    private class TabPanel3 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        private static final String CODES_CLASS_BASE_NAME = "codesClass";

        public TabPanel3(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> form = new Form<>("tab3Form");
            queue(form);

            CodeClassModel codeClassModel = new CodeClassModel(getLocalization().getLocalization());
            List<CodeClass> codeClasses = codeClassModel.getObject();

            makeCodeClass1Complex(codeClasses, form);
            makeCodeClassComplex(CodeClassId.CC2, codeClasses);
            makeCodeClassComplex(CodeClassId.CC3, codeClasses);
            makeCodeClassComplex(CodeClassId.CC4, codeClasses);
            makeCodeClassComplex(CodeClassId.CC5, codeClasses);
            makeCodeClassComplex(CodeClassId.CC6, codeClasses);
            makeCodeClassComplex(CodeClassId.CC7, codeClasses);
            makeCodeClassComplex(CodeClassId.CC8, codeClasses);
        }

        private void makeCodeClass1Complex(final List<CodeClass> codeClasses, Form<T> form) {
            final TextField<String> mainCodeOfCodeClass1 = new TextField<String>(Paper.MAIN_CODE_OF_CODECLASS1);
            final BootstrapMultiSelect<Code> codeClass1 = makeCodeClassComplex(CodeClassId.CC1, codeClasses);
            addCodeClass1ChangeBehavior(mainCodeOfCodeClass1, codeClass1);
            addMainCodeOfClass1(mainCodeOfCodeClass1);
            form.add(new CodeClass1ConsistencyValidator(codeClass1, mainCodeOfCodeClass1));
        }

        private void addMainCodeOfClass1(TextField<String> field) {
            String id = field.getId();
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
            queue(new Label(id + LABEL_TAG, labelModel));
            field.add(new PropertyValidator<String>());
            field.setOutputMarkupId(true);
            field.setLabel(labelModel);
            field.setEnabled(isSearchMode());
            queue(field);
        }

        private BootstrapMultiSelect<Code> makeCodeClassComplex(final CodeClassId codeClassId, final List<CodeClass> codeClasses) {
            final CodeClassId ccId = CodeClassId.fromId(codeClassId.getId()).get();
            final int id = ccId.getId();
            final String className = codeClasses.stream().filter(cc -> cc.getId() == id).map(CodeClass::getName).findFirst().orElse(ccId.name());
            queue(new Label(CODES_CLASS_BASE_NAME + id + "Label", Model.of(className)));

            final ChainingModel<List<Code>> model = new ChainingModel<List<Code>>(getModel()) {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                public List<Code> getObject() {
                    return ((IModel<CodeBoxAware>) getTarget()).getObject().getCodesOf(ccId);
                };

                @SuppressWarnings("unchecked")
                public void setObject(final List<Code> codes) {
                    ((IModel<CodeBoxAware>) getTarget()).getObject().clearCodesOf(ccId);
                    if (CollectionUtils.isNotEmpty(codes)) {
                        ((IModel<CodeBoxAware>) getTarget()).getObject().addCodes(codes);
                    }
                }
            };
            final CodeModel choices = new CodeModel(ccId, getLocalization().getLocalization());
            final IChoiceRenderer<Code> choiceRenderer = new ChoiceRenderer<Code>(Code.DISPLAY_VALUE, Code.CODE);
            final StringResourceModel noneSelectedModel = new StringResourceModel("codes.noneSelected", this, null);
            final BootstrapSelectConfig config = new BootstrapSelectConfig().withMultiple(true).withNoneSelectedText(noneSelectedModel.getObject()).withLiveSearch(true);
            final BootstrapMultiSelect<Code> multiSelect = new BootstrapMultiSelect<Code>(CODES_CLASS_BASE_NAME + id, model, choices, choiceRenderer).with(config);
            multiSelect.add(new AttributeModifier("data-width", "fit"));
            queue(multiSelect);
            return multiSelect;
        }
    }

    private class TabPanel4 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel4(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> form = new Form<>("tab4Form");
            queue(form);

            queueTo(form, Paper.ORIGINAL_ABSTRACT);
        }
    }

    /** override if needed */
    protected void addCodeClass1ChangeBehavior(final TextField<String> mainCodeOfCodeClass1, final BootstrapMultiSelect<Code> codeClass1) {
    }

    static class CodeClass1ConsistencyValidator extends AbstractFormValidator {

        private static final long serialVersionUID = 1L;

        private final FormComponent<?>[] components;

        public CodeClass1ConsistencyValidator(BootstrapMultiSelect<Code> codeClass1, TextField<String> mainCodeOfCodeClass1) {
            AssertAs.notNull(codeClass1);
            AssertAs.notNull(mainCodeOfCodeClass1);
            components = new FormComponent<?>[] { codeClass1, mainCodeOfCodeClass1 };
        }

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return components;
        }

        @Override
        public void validate(Form<?> form) {
            @SuppressWarnings("unchecked")
            final BootstrapMultiSelect<Code> codeClass1 = (BootstrapMultiSelect<Code>) components[0];
            final FormComponent<?> mainCode = components[1];

            if (!codeClass1.getModelObject().isEmpty() && mainCode.getModelObject() == null) {
                String key = resourceKey();
                error(mainCode, key + ".mainCodeOfCodeclass1Required");
            }
        }
    }

}
