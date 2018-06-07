package ch.difty.scipamato.core.web.paper.common;

import static ch.difty.scipamato.core.entity.Paper.PaperFields.*;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Size;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.component.SerializableSupplier;
import ch.difty.scipamato.core.NewsletterAware;
import ch.difty.scipamato.core.entity.*;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.web.common.BasePanel;
import ch.difty.scipamato.core.web.common.SelfUpdateEvent;
import ch.difty.scipamato.core.web.model.CodeClassModel;
import ch.difty.scipamato.core.web.model.CodeModel;
import ch.difty.scipamato.core.web.model.NewsletterTopicModel;
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource;

public abstract class PaperPanel<T extends CodeBoxAware & NewsletterAware> extends BasePanel<T> {

    private static final long serialVersionUID = 1L;

    protected static final String TITLE_ATTR = "title";
    private static final   String CHANGE     = "change";

    private ResourceLink<Void> summaryLink;
    private ResourceLink<Void> summaryShortLink;
    private String             pubmedXml;

    protected TextArea<String>                   authors;
    protected TextField<String>                  firstAuthor;
    protected TextArea<Object>                   title;
    protected TextField<Object>                  location;
    protected TextField<Object>                  publicationYear;
    protected TextField<Object>                  doi;
    protected TextArea<String>                   originalAbstract;
    private   BootstrapAjaxLink<Void>            pubmedRetrieval;
    private   DataTable<PaperAttachment, String> attachments;

    private final NewsletterTopicModel newsletterTopicChoice = new NewsletterTopicModel(getLocalization());

    private final PageReference callingPage;

    private Form<T> form;

    public PaperPanel(String id) {
        this(id, null);
    }

    public PaperPanel(String id, IModel<T> model) {
        this(id, model, null);
    }

    public PaperPanel(String id, IModel<T> model, Mode mode) {
        this(id, model, mode, null);
    }

    public PaperPanel(String id, IModel<T> model, Mode mode, PageReference previousPage) {
        super(id, model, mode);
        this.callingPage = previousPage;
    }

    protected PageReference getCallingPage() {
        return callingPage;
    }

    protected DataTable<PaperAttachment, String> getAttachments() {
        return attachments;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        form = new Form<T>("form", new CompoundPropertyModel<>(getModel())) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                onFormSubmit();
            }
        };
        form.setMultiPart(true);
        queue(form);

        queueHeaderFields();
        queueTabPanel("tabs");

        queuePubmedRetrievalLink("pubmedRetrieval");
    }

    protected abstract void onFormSubmit();

    protected String getPubmedXml() {
        return pubmedXml;
    }

    public Form<T> getForm() {
        return form;
    }

    private void queueHeaderFields() {
        queueAuthorComplex(AUTHORS.getName(), FIRST_AUTHOR.getName(), FIRST_AUTHOR_OVERRIDDEN.getName());
        title = new TextArea<>(TITLE.getName());

        final ItemNavigator<Long> pm = getPaperIdManager();
        queue(newNavigationButton("previous", GlyphIconType.stepbackward, pm::hasPrevious, () -> {
            pm.previous();
            return pm.getItemWithFocus();
        }));
        queue(newNavigationButton("next", GlyphIconType.stepforward, pm::hasNext, () -> {
            pm.next();
            return pm.getItemWithFocus();
        }));

        queueFieldAndLabel(title, new PropertyValidator<String>());
        location = new TextField<>(LOCATION.getName());
        queueFieldAndLabel(location, new PropertyValidator<String>());

        publicationYear = new TextField<>(PUBL_YEAR.getName());
        queueFieldAndLabel(publicationYear, new PropertyValidator<Integer>());
        TextField<Object> pmId = new TextField<>(PMID.getName());
        pmId.add(newPmIdChangeBehavior());
        queueFieldAndLabel(pmId);
        doi = new TextField<>(DOI.getName());
        queueFieldAndLabel(doi, new PropertyValidator<String>());

        TextField<Integer> number = new TextField<>(NUMBER.getName());
        queueFieldAndLabel(number, new PropertyValidator<Integer>());
        TextField<Integer> id = new TextField<Integer>(IdScipamatoEntity.IdScipamatoEntityFields.ID.getName()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                if (event
                        .getPayload()
                        .getClass() == SelfUpdateEvent.class) {
                    ((SelfUpdateEvent) event.getPayload())
                        .getTarget()
                        .add(this);
                    event.dontBroadcastDeeper();
                }
            }
        };
        id.setEnabled(isSearchMode());
        queueFieldAndLabel(id);

        TextField<String> created = new TextField<String>(CoreEntity.CoreEntityFields.CREATED_DV.getName()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                if (event
                        .getPayload()
                        .getClass() == SelfUpdateEvent.class) {
                    ((SelfUpdateEvent) event.getPayload())
                        .getTarget()
                        .add(this);
                    event.dontBroadcastDeeper();
                }
            }
        };
        created.setEnabled(isSearchMode());
        queueFieldAndLabel(created);

        TextField<String> modified = new TextField<String>(CoreEntity.CoreEntityFields.MODIFIED_DV.getName()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                if (event
                        .getPayload()
                        .getClass() == SelfUpdateEvent.class) {
                    ((SelfUpdateEvent) event.getPayload())
                        .getTarget()
                        .add(this);
                    event.dontBroadcastDeeper();
                }
            }
        };
        modified.setEnabled(isSearchMode());
        queueFieldAndLabel(modified);

        makeAndQueueBackButton("back");
        queue(newExcludeButton("exclude"));
        makeAndQueueSubmitButton("submit");

        summaryLink = makeSummaryLink("summary");
        form.addOrReplace(summaryLink);
        summaryShortLink = makeSummaryShortLink("summaryShort");
        form.addOrReplace(summaryShortLink);

        BootstrapAjaxLink<Void> addRemoveNewsletter = new BootstrapAjaxLink<Void>("modAssociation",
            Buttons.Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                add(new ButtonBehavior()
                    .setType(Buttons.Type.Info)
                    .setSize(Size.Large));
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
                modifyNewsletterAssociation(target);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(isEditMode() && (!isAssociatedWithNewsletter() || isAssociatedWithWipNewsletter()));
                add(new AttributeModifier(TITLE_ATTR, getTitleModel()));
                setIconType(getIconType());
            }

            private StringResourceModel getTitleModel() {
                String key;
                if (!isAssociatedWithNewsletter())
                    key = "modNewsletterAssociation-add.label";
                else if (isAssociatedWithWipNewsletter())
                    key = "modNewsletterAssociation-del.label";
                else
                    key = "";
                return new StringResourceModel(key, this, null);
            }

            private IconType getIconType() {
                if (!isAssociatedWithNewsletter())
                    return FontAwesomeIconType.plus_square;
                else if (isAssociatedWithNewsletter())
                    return FontAwesomeIconType.envelope_open_o;
                else
                    return FontAwesomeIconType.envelope_o;
            }
        };
        addRemoveNewsletter.setOutputMarkupPlaceholderTag(true);
        queue(addRemoveNewsletter);
    }

    private OnChangeAjaxBehavior newPmIdChangeBehavior() {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                target.add(pubmedRetrieval);
            }
        };
    }

    private void queueTabPanel(String tabId) {
        List<ITab> tabs = new ArrayList<>();
        tabs.add(new AbstractTab(new StringResourceModel("tab1" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel1(panelId, form.getModel());
            }

        });
        tabs.add(new AbstractTab(new StringResourceModel("tab2" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel2(panelId, form.getModel());
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("tab3" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel3(panelId, form.getModel());
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("tab4" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel4(panelId, form.getModel());
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("tab5" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel5(panelId, form.getModel());
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("tab6" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel6(panelId, form.getModel());
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("tab7" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel7(panelId, form.getModel());
            }
        });
        queue(new ClientSideBootstrapTabbedPanel<>(tabId, tabs));
    }

    /*
     * The authors field determines the firstAuthor field, but only unless
     * overridden. Changes in the author field (if not overridden) or in the
     * override checkbox can have an effect on the firstAuthor field (enabled,
     * content)
     */
    private void queueAuthorComplex(String authorsId, String firstAuthorId, String firstAuthorOverriddenId) {
        authors = new TextArea<>(authorsId);
        authors.setEscapeModelStrings(false);
        queueFieldAndLabel(authors, new PropertyValidator<String>());

        PropertyModel<Boolean> firstAuthorOverriddenModel = new PropertyModel<>(getModel(), firstAuthorOverriddenId);
        CheckBoxX firstAuthorOverridden = new CheckBoxX(firstAuthorOverriddenId, firstAuthorOverriddenModel);
        firstAuthorOverridden
            .getConfig()
            .withThreeState(isSearchMode())
            .withUseNative(true);
        queueCheckBoxAndLabel(firstAuthorOverridden);

        firstAuthor = makeFirstAuthor(firstAuthorId, firstAuthorOverridden);
        firstAuthor.setOutputMarkupId(true);
        queueFieldAndLabel(firstAuthor);

        addAuthorBehavior(authors, firstAuthorOverridden, firstAuthor);
    }

    /** override if special behavior is required */
    protected TextField<String> makeFirstAuthor(final String firstAuthorId, final CheckBox firstAuthorOverridden) {
        return new TextField<>(firstAuthorId);
    }

    /** override if special behavior is required */
    protected void addAuthorBehavior(TextArea<String> authors, CheckBox firstAuthorOverridden,
        TextField<String> firstAuthor) {
    }

    protected abstract BootstrapButton newNavigationButton(String id, GlyphIconType icon,
        SerializableSupplier<Boolean> isEnabled, SerializableSupplier<Long> idSupplier);

    private void makeAndQueueBackButton(String id) {
        BootstrapButton back = new BootstrapButton(id, new StringResourceModel("button.back.label"),
            Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                if (callingPage != null)
                    setResponsePage(callingPage.getPage());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(callingPage != null);
            }
        };
        back.setDefaultFormProcessing(false);
        back.add(
            new AttributeModifier(TITLE_ATTR, new StringResourceModel("button.back.title", this, null).getString()));
        queue(back);
    }

    protected abstract BootstrapButton newExcludeButton(String id);

    private void makeAndQueueSubmitButton(String id) {
        BootstrapButton submit = new BootstrapButton(id, new StringResourceModel(getSubmitLinkResourceLabel()),
            Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            /**
             * Refresh the summary link to use the updated model
             */
            @Override
            public void onAfterSubmit() {
                super.onAfterSubmit();
                summaryLink = makeSummaryLink("summary");
                form.addOrReplace(summaryLink);
                summaryShortLink = makeSummaryShortLink("summaryShort");
                form.addOrReplace(summaryShortLink);
            }
        };
        submit.setDefaultFormProcessing(true);
        submit.setEnabled(!isViewMode());
        queue(submit);
    }

    private ResourceLink<Void> makeSummaryLink(String id) {
        return makePdfResourceLink(id, getSummaryDataSource());
    }

    /** implement to return PaperSummaryDataSource */
    protected abstract PaperSummaryDataSource getSummaryDataSource();

    private ResourceLink<Void> makeSummaryShortLink(String id) {
        return makePdfResourceLink(id, getSummaryShortDataSource());
    }

    /** implement to return PaperSummaryShortDataSource */
    protected abstract PaperSummaryShortDataSource getSummaryShortDataSource();

    private ResourceLink<Void> makePdfResourceLink(String id, JasperPaperDataSource<?> dataSource) {
        final String button = "button.";
        ResourceLink<Void> link = new ResourceLink<Void>(id, dataSource) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                if (isVisible())
                    add(new ButtonBehavior()
                        .setType(Buttons.Type.Info)
                        .setBlock(true)
                        .setSize(Size.Large));
            }

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                if (event
                        .getPayload()
                        .getClass() == SelfUpdateEvent.class) {
                    if (isVisible()) {
                        setEnabled(false);
                        add(new AttributeModifier(TITLE_ATTR,
                            new StringResourceModel(button + id + ".title.disabled", this, null).getString()));
                        ((SelfUpdateEvent) event.getPayload())
                            .getTarget()
                            .add(this);
                    }
                    event.dontBroadcastDeeper();
                }
            }
        };
        link.setOutputMarkupId(true);
        link.setOutputMarkupPlaceholderTag(true);
        link.setBody(new StringResourceModel(button + id + ".label"));
        link.setVisible(isEditMode());
        link.add(
            new AttributeModifier(TITLE_ATTR, new StringResourceModel(button + id + ".title", this, null).getString()));
        return link;
    }

    private abstract class AbstractTabPanel extends Panel {

        private static final long serialVersionUID = 1L;

        public AbstractTabPanel(String id) {
            super(id);
        }

        AbstractTabPanel(String id, IModel<?> model) {
            super(id, model);
        }

        TextArea<String> queueTo(FieldEnumType fieldType) {
            return queueTo(fieldType, false, null);
        }

        void queueTo(FieldEnumType fieldType, PropertyValidator<?> pv) {
            queueTo(fieldType, false, pv);
        }

        void queueNewFieldTo(FieldEnumType fieldType) {
            queueTo(fieldType, true, null);
        }

        TextArea<String> queueTo(FieldEnumType fieldType, boolean newField, PropertyValidator<?> pv) {
            String id = fieldType.getName();
            TextArea<String> field = makeField(id, newField);
            field.setOutputMarkupId(true);
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
            queue(new Label(id + LABEL_TAG, labelModel));
            field.setLabel(labelModel);
            if (newField) {
                addNewFieldSpecificAttributes(field);
            }
            if (pv != null && isEditMode()) {
                field.add(pv);
            }
            queue(field);
            return field;
        }

        /**
         * The new fields are present on the page more than once, they need to be able
         * to handle the {@link NewFieldChangeEvent}.
         */
        private TextArea<String> makeField(String id, boolean newField) {
            if (!newField) {
                return new TextArea<>(id);
            } else {
                return new TextArea<String>(id) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onEvent(IEvent<?> event) {
                        if (event
                                .getPayload()
                                .getClass() == NewFieldChangeEvent.class) {
                            ((NewFieldChangeEvent) event.getPayload()).considerAddingToTarget(this);
                            event.dontBroadcastDeeper();
                        }
                    }
                };
            }
        }

        /**
         * New fields need to broadcast the {@link NewFieldChangeEvent} and have a
         * special visual indication that they are a new field.
         */
        private void addNewFieldSpecificAttributes(final TextArea<String> field) {
            field.add(new AttributeAppender("class", " newField"));
            field.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    final String id = field.getId();
                    final String markupId = field.getMarkupId();
                    send(getPage(), Broadcast.BREADTH, new NewFieldChangeEvent(target)
                        .withId(id)
                        .withMarkupId(markupId));
                }
            });
        }

    }

    private class TabPanel1 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel1(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            queue(new Form<T>("tab1Form"));

            queueTo(GOALS, new PropertyValidator<String>());
            queueTo(POPULATION);
            queueTo(METHODS);

            queueNewFieldTo(POPULATION_PLACE);
            queueNewFieldTo(POPULATION_PARTICIPANTS);
            queueNewFieldTo(POPULATION_DURATION);

            queueNewFieldTo(EXPOSURE_POLLUTANT);
            queueNewFieldTo(EXPOSURE_ASSESSMENT);

            queueNewFieldTo(METHOD_STUDY_DESIGN);
            queueNewFieldTo(METHOD_OUTCOME);
            queueNewFieldTo(METHOD_STATISTICS);
            queueNewFieldTo(METHOD_CONFOUNDERS);
        }
    }

    private class TabPanel2 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel2(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            queue(new Form<T>("tab2Form"));

            queueTo(RESULT);
            queueTo(COMMENT);
            queueTo(INTERN);

            queueNewFieldTo(RESULT_MEASURED_OUTCOME);
            queueNewFieldTo(RESULT_EXPOSURE_RANGE);
            queueNewFieldTo(RESULT_EFFECT_ESTIMATE);
        }
    }

    private class TabPanel3 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        private static final String CODES_CLASS_BASE_NAME = "codesClass";

        TabPanel3(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> tab3Form = new Form<>("tab3Form");
            queue(tab3Form);

            CodeClassModel codeClassModel = new CodeClassModel(getLocalization());
            List<CodeClass> codeClasses = codeClassModel.getObject();

            makeCodeClass1Complex(codeClasses, tab3Form);
            makeCodeClassComplex(CodeClassId.CC2, codeClasses);
            makeCodeClassComplex(CodeClassId.CC3, codeClasses);
            makeCodeClassComplex(CodeClassId.CC4, codeClasses);
            makeCodeClassComplex(CodeClassId.CC5, codeClasses);
            makeCodeClassComplex(CodeClassId.CC6, codeClasses);
            makeCodeClassComplex(CodeClassId.CC7, codeClasses);
            makeCodeClassComplex(CodeClassId.CC8, codeClasses);
        }

        private void makeCodeClass1Complex(final List<CodeClass> codeClasses, Form<T> form) {
            final TextField<String> mainCodeOfCodeClass1 = new TextField<>(MAIN_CODE_OF_CODECLASS1.getName());
            final BootstrapMultiSelect<Code> codeClass1 = makeCodeClassComplex(CodeClassId.CC1, codeClasses);
            addCodeClass1ChangeBehavior(mainCodeOfCodeClass1, codeClass1);
            addMainCodeOfClass1(mainCodeOfCodeClass1);
            if (isEditMode())
                form.add(new CodeClass1ConsistencyValidator(codeClass1, mainCodeOfCodeClass1));
        }

        private void addMainCodeOfClass1(TextField<String> field) {
            String id = field.getId();
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
            queue(new Label(id + LABEL_TAG, labelModel));
            field.add(new PropertyValidator<>());
            field.setOutputMarkupId(true);
            field.setLabel(labelModel);
            field.setEnabled(isSearchMode());
            queue(field);
        }

        private BootstrapMultiSelect<Code> makeCodeClassComplex(final CodeClassId codeClassId,
            final List<CodeClass> codeClasses) {
            final int id = codeClassId.getId();
            final String className = codeClasses
                .stream()
                .filter(cc -> cc.getId() == id)
                .map(CodeClass::getName)
                .findFirst()
                .orElse(codeClassId.name());
            queue(new Label(CODES_CLASS_BASE_NAME + id + "Label", Model.of(className)));

            final ChainingModel<List<Code>> model = new ChainingModel<List<Code>>(getModel()) {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public List<Code> getObject() {
                    return ((IModel<CodeBoxAware>) getTarget())
                        .getObject()
                        .getCodesOf(codeClassId);
                }

                @SuppressWarnings("unchecked")
                @Override
                public void setObject(final List<Code> codes) {
                    ((IModel<CodeBoxAware>) getTarget())
                        .getObject()
                        .clearCodesOf(codeClassId);
                    if (CollectionUtils.isNotEmpty(codes)) {
                        ((IModel<CodeBoxAware>) getTarget())
                            .getObject()
                            .addCodes(codes);
                    }
                }
            };
            final CodeModel choices = new CodeModel(codeClassId, getLocalization());
            final IChoiceRenderer<Code> choiceRenderer = new ChoiceRenderer<>(
                CoreEntity.CoreEntityFields.DISPLAY_VALUE.getName(), Code.CodeFields.CODE.getName());
            final StringResourceModel noneSelectedModel = new StringResourceModel("codes.noneSelected", this, null);
            final BootstrapSelectConfig config = new BootstrapSelectConfig()
                .withMultiple(true)
                .withNoneSelectedText(noneSelectedModel.getObject())
                .withLiveSearch(true);
            final BootstrapMultiSelect<Code> multiSelect = new BootstrapMultiSelect<>(CODES_CLASS_BASE_NAME + id, model,
                choices, choiceRenderer).with(config);
            multiSelect.add(new AttributeModifier("data-width", "fit"));
            queue(multiSelect);
            return multiSelect;
        }
    }

    private class TabPanel4 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel4(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            queue(new Form<T>("tab4Form"));

            queueNewFieldTo(METHOD_STUDY_DESIGN);
            queueNewFieldTo(METHOD_OUTCOME);

            queueNewFieldTo(POPULATION_PLACE);
            queueNewFieldTo(POPULATION_PARTICIPANTS);
            queueNewFieldTo(POPULATION_DURATION);

            queueNewFieldTo(EXPOSURE_POLLUTANT);
            queueNewFieldTo(EXPOSURE_ASSESSMENT);
            queueNewFieldTo(METHOD_STATISTICS);
            queueNewFieldTo(METHOD_CONFOUNDERS);

            queueNewFieldTo(RESULT_MEASURED_OUTCOME);
            queueNewFieldTo(RESULT_EXPOSURE_RANGE);
            queueNewFieldTo(RESULT_EFFECT_ESTIMATE);
        }
    }

    private class TabPanel5 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel5(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            queue(new Form<T>("tab5Form"));

            originalAbstract = queueTo(ORIGINAL_ABSTRACT);
        }
    }

    private class TabPanel6 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel6(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            queue(newTab6Frm("tab6Form"));
            queue(newDropZoneFileUpload());
            attachments = newAttachmentTable("attachments");
            queue(attachments);
        }

        private Form<Void> newTab6Frm(String id) {
            Form<Void> tab6Form = new Form<>(id);
            tab6Form.setOutputMarkupId(true);
            tab6Form.setMultiPart(true);
            return tab6Form;
        }
    }

    private class TabPanel7 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel7(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            queue(new Form<>("tab7Form"));
            queueTo(NEWSLETTER_HEADLINE);
            makeAndQueuePublicationStatusSelectBox("newsletterTopic");
        }

        private void makeAndQueuePublicationStatusSelectBox(final String id) {
            final ChainingModel<NewsletterTopic> model = new ChainingModel<NewsletterTopic>(getModel()) {
                private static final long serialVersionUID = 1L;

                private final List<NewsletterTopic> topics = newsletterTopicChoice.load();

                @Override
                public NewsletterTopic getObject() {
                    final Integer topicId = ((IModel<NewsletterAware>) getTarget())
                        .getObject()
                        .getNewsletterTopicId();
                    if (topicId == null)
                        return null;
                    return topics
                        .stream()
                        .filter(nt -> topicId.equals(nt.getId()))
                        .findFirst()
                        .orElse(null);
                }

                @Override
                public void setObject(final NewsletterTopic topic) {
                    ((IModel<NewsletterAware>) getTarget())
                        .getObject()
                        .setNewsletterTopic(topic);
                }
            };
            final IChoiceRenderer<NewsletterTopic> choiceRenderer = new ChoiceRenderer<NewsletterTopic>(
                NewsletterTopic.NewsletterTopicFields.TITLE.getName(),
                NewsletterTopic.NewsletterTopicFields.ID.getName());

            final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
            final BootstrapSelectConfig config = new BootstrapSelectConfig()
                .withNoneSelectedText(noneSelectedModel.getObject())
                .withLiveSearch(true);
            final BootstrapSelect<NewsletterTopic> topic = new BootstrapSelect<NewsletterTopic>(id, model,
                newsletterTopicChoice, choiceRenderer).with(config);
            topic.setNullValid(true);
            queue(topic);
            queue(new Label(id + LABEL_TAG, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)));
        }
    }

    protected abstract boolean isAssociatedWithNewsletter();

    protected abstract boolean isAssociatedWithWipNewsletter();

    protected abstract void modifyNewsletterAssociation(final AjaxRequestTarget target);

    protected abstract DataTable<PaperAttachment, String> newAttachmentTable(String id);

    protected abstract DropZoneFileUpload newDropZoneFileUpload();

    /** override if needed */
    protected void addCodeClass1ChangeBehavior(final TextField<String> mainCodeOfCodeClass1,
        final BootstrapMultiSelect<Code> codeClass1) {
    }

    static class CodeClass1ConsistencyValidator extends AbstractFormValidator {

        private static final long serialVersionUID = 1L;

        private final FormComponent<?>[] components;

        public CodeClass1ConsistencyValidator(BootstrapMultiSelect<Code> codeClass1,
            TextField<String> mainCodeOfCodeClass1) {
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
            @SuppressWarnings("unchecked") final BootstrapMultiSelect<Code> codeClass1 = (BootstrapMultiSelect<Code>) components[0];
            final FormComponent<?> mainCode = components[1];

            if (!codeClass1
                .getModelObject()
                .isEmpty() && mainCode.getModelObject() == null) {
                String key = resourceKey();
                error(mainCode, key + ".mainCodeOfCodeclass1Required");
            }
        }
    }

    private void queuePubmedRetrievalLink(String linkId) {
        pubmedRetrieval = new BootstrapAjaxLink<Void>(linkId, Buttons.Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                add(new ButtonBehavior()
                    .setType(Buttons.Type.Info)
                    .setSize(Size.Medium));
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
                getPubmedArticleAndCompare(target);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(isEditMode());
                if (hasPubMedId()) {
                    setEnabled(true);
                    add(new AttributeModifier(TITLE_ATTR,
                        new StringResourceModel("pubmedRetrieval.title", this, null).getString()));
                } else {
                    setEnabled(false);
                    add(new AttributeModifier(TITLE_ATTR,
                        new StringResourceModel("pubmedRetrieval.title.disabled", this, null).getString()));
                }
            }
        };
        pubmedRetrieval.setOutputMarkupPlaceholderTag(true);
        pubmedRetrieval.setLabel(new StringResourceModel("pubmedRetrieval.label", this, null));
        queue(pubmedRetrieval);
    }

    protected abstract boolean hasPubMedId();

    /** override to do something with the pasted content */
    protected void getPubmedArticleAndCompare(AjaxRequestTarget target) {
    }

}
