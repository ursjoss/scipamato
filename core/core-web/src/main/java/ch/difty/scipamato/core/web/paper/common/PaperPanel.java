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
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.component.SerializableSupplier;
import ch.difty.scipamato.core.NewsletterAware;
import ch.difty.scipamato.core.entity.*;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.web.common.BasePanel;
import ch.difty.scipamato.core.web.common.SelfUpdateBroadcastingBehavior;
import ch.difty.scipamato.core.web.common.SelfUpdateEvent;
import ch.difty.scipamato.core.web.model.CodeClassModel;
import ch.difty.scipamato.core.web.model.CodeModel;
import ch.difty.scipamato.core.web.model.NewsletterTopicModel;
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource;

@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection", "SpellCheckingInspection" })
public abstract class PaperPanel<T extends CodeBoxAware & NewsletterAware> extends BasePanel<T> {

    private static final long serialVersionUID = 1L;

    protected static final String TITLE_ATTR = "title";
    private static final   String CHANGE     = "change";

    private ResourceLink<Void> summaryLink;
    private ResourceLink<Void> summaryShortLink;

    protected TextArea<String>                   authors;
    protected TextField<String>                  firstAuthor;
    protected TextArea<Object>                   title;
    protected TextField<Object>                  location;
    protected TextField<Object>                  publicationYear;
    protected TextField<Object>                  pmId;
    protected TextField<Object>                  doi;
    protected TextArea<String>                   originalAbstract;
    private   BootstrapAjaxLink<Void>            pubmedRetrieval;
    private   DataTable<PaperAttachment, String> attachments;
    private   BootstrapButton                    submit;

    private final NewsletterTopicModel newsletterTopicChoice = new NewsletterTopicModel(getLocalization());

    private final PageReference callingPage;

    private final IModel<Integer> tabIndexModel;

    private Form<T> form;

    PaperPanel(@NotNull String id, @Nullable IModel<T> model, @NotNull Mode mode) {
        this(id, model, mode, null, Model.of(0));
    }

    public PaperPanel(@NotNull String id, @Nullable IModel<T> model, @NotNull Mode mode,
        @Nullable PageReference previousPage, @NotNull IModel<Integer> tabIndexModel) {
        super(id, model, mode);
        this.callingPage = previousPage;
        this.tabIndexModel = tabIndexModel;
    }

    @Nullable
    protected PageReference getCallingPage() {
        return callingPage;
    }

    @NotNull
    protected DataTable<PaperAttachment, String> getAttachments() {
        return attachments;
    }

    @NotNull
    protected IModel<Integer> getTabIndexModel() {
        return tabIndexModel;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        form = new Form<>("form", new CompoundPropertyModel<>(getModel())) {
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

    @NotNull
    public Form<T> getForm() {
        return form;
    }

    private void queueHeaderFields() {
        queueAuthorComplex(AUTHORS.getFieldName(), FIRST_AUTHOR.getFieldName(), FIRST_AUTHOR_OVERRIDDEN.getFieldName());
        title = new TextArea<>(TITLE.getFieldName());

        final ItemNavigator<Long> pm = getPaperIdManager();
        queue(newNavigationButton("previous", GlyphIconType.stepbackward, pm::hasPrevious, () -> {
            pm.previous();
            return pm.getItemWithFocus();
        }));
        queue(newNavigationButton("next", GlyphIconType.stepforward, pm::hasNext, () -> {
            pm.next();
            return pm.getItemWithFocus();
        }));

        queueFieldAndLabel(title, new PropertyValidator<>());
        location = new TextField<>(LOCATION.getFieldName());
        queueFieldAndLabel(location, new PropertyValidator<>());

        publicationYear = new TextField<>(PUBL_YEAR.getFieldName());
        queueFieldAndLabel(publicationYear, new PropertyValidator<>());
        pmId = new TextField<>(PMID.getFieldName());
        pmId.add(newPmIdChangeBehavior());
        queueFieldAndLabel(pmId);
        doi = new TextField<>(DOI.getFieldName());
        queueFieldAndLabel(doi, new PropertyValidator<>());

        addDisableBehavior(title, location, publicationYear, pmId, doi);

        queueNumberField(NUMBER.getFieldName());

        TextField<Integer> id = newSelfUpdatingTextField(IdScipamatoEntity.IdScipamatoEntityFields.ID.getFieldName());
        id.setEnabled(isSearchMode());
        queueFieldAndLabel(id);

        TextField<String> created = newSelfUpdatingTextField(CoreEntity.CoreEntityFields.CREATED_DV.getFieldName());
        created.setEnabled(isSearchMode());
        queueFieldAndLabel(created);

        TextField<String> modified = newSelfUpdatingTextField(CoreEntity.CoreEntityFields.MODIFIED_DV.getFieldName());
        modified.setEnabled(isSearchMode());
        queueFieldAndLabel(modified);

        makeAndQueueBackButton("back", () -> getPaperIdManager().isModified());
        queue(newExcludeButton("exclude"));
        makeAndQueueSubmitButton("submit");

        summaryLink = makeSummaryLink("summary");
        form.addOrReplace(summaryLink);
        summaryShortLink = makeSummaryShortLink("summaryShort");
        form.addOrReplace(summaryShortLink);

        considerAddingMoreValidation();

        BootstrapAjaxLink<Void> addRemoveNewsletter = new BootstrapAjaxLink<>("modAssociation", Buttons.Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                add(new ButtonBehavior()
                    .setType(Buttons.Type.Info)
                    .setSize(Size.Medium));
            }

            @Override
            public void onClick(@NotNull final AjaxRequestTarget target) {
                modifyNewsletterAssociation(target);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(shallBeVisible());
                setEnabled(shallBeEnabled());
                add(new AttributeModifier(TITLE_ATTR, getTitleResourceModel()));
                setIconType(getIconType());
            }

            // Hide if in EditMode
            // Otherwise show if we have an open newsletter or if it is already assigned to a (closed) newsletter
            private boolean shallBeVisible() {
                return isEditMode() && (isaNewsletterInStatusWip() || isAssociatedWithNewsletter());
            }

            private boolean shallBeEnabled() {
                return !isAssociatedWithNewsletter() || isAssociatedWithWipNewsletter();
            }

            private StringResourceModel getTitleResourceModel() {
                return new StringResourceModel("modNewsletterAssociation-" + getTitleKey() + ".title", this,
                    PaperPanel.this.getModel());
            }

            private String getTitleKey() {
                if (!isAssociatedWithNewsletter())
                    return "add";
                return isAssociatedWithWipNewsletter() ? "del" : "closed";
            }

            // Show the + icon if not assigned to a newsletter yet
            // Otherwise: Show the open envelope if assigned to current, closed envelope if assigned to closed nl.
            private IconType getIconType() {
                if (!isAssociatedWithNewsletter())
                    return FontAwesome5IconType.plus_square_s;
                else if (isAssociatedWithWipNewsletter())
                    return FontAwesome5IconType.envelope_open_r;
                else
                    return FontAwesome5IconType.envelope_r;
            }
        };
        addRemoveNewsletter.setOutputMarkupPlaceholderTag(true);
        queue(addRemoveNewsletter);
    }

    private <U> TextField<U> newSelfUpdatingTextField(final String id) {
        return new TextField<>(id) {
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
    }

    private void queueNumberField(String id) {
        final IModel<String> labelModel = Model.of(
            firstWordOfBrand() + "-" + new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
        TextField<Integer> number = new TextField<>(id);
        queue(new Label(id + LABEL_TAG, labelModel));
        number.setLabel(labelModel);
        number.setOutputMarkupId(true);
        queue(number);
        if (isEditMode())
            number.add(new PropertyValidator<>());
        addDisableBehavior(number);
    }

    private String firstWordOfBrand() {
        final String brand = getProperties().getBrand();
        final String divider = " ";
        if (brand.contains(divider))
            return brand.substring(0, brand.indexOf(divider));
        else
            return brand;
    }

    /**
     * If more validators are required, override this
     */
    protected void considerAddingMoreValidation() {
    }

    private OnChangeAjaxBehavior newPmIdChangeBehavior() {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(@NotNull final AjaxRequestTarget target) {
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
        queue(new BootstrapTabbedPanel<>(tabId, tabs, tabIndexModel));
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
        queueFieldAndLabel(authors, new PropertyValidator<>());

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
        addDisableBehavior(authors, firstAuthor);
    }

    private void addDisableBehavior(final FormComponent<?>... components) {
        if (isEditMode() || isSearchMode())
            for (final FormComponent<?> fc : components) {
                fc.add(new AjaxFormComponentUpdatingBehavior("input") {
                    @Override
                    protected void onUpdate(@NotNull final AjaxRequestTarget target) {
                        disableButton(target, submit);
                    }
                });
            }
    }

    private void disableButton(final AjaxRequestTarget target, final BootstrapButton... buttons) {
        for (final BootstrapButton b : buttons) {
            if (b.isEnabled()) {
                b.setEnabled(false);
                target.add(b);
            }
        }
    }

    /**
     * override if special behavior is required
     *
     * @param firstAuthorId
     *     the firstAuthorId as string
     * @param firstAuthorOverridden
     *     the checkbox for firstAuthorOverridden
     * @return the first author TextField
     */
    @NotNull
    protected TextField<String> makeFirstAuthor(@NotNull final String firstAuthorId,
        @NotNull final CheckBox firstAuthorOverridden) {
        return new TextField<>(firstAuthorId);
    }

    /**
     * override if special behavior is required
     *
     * @param authors
     *     text area for the authors field
     * @param firstAuthorOverridden
     *     checkbox for firstAuthorOverridden
     * @param firstAuthor
     *     text field for firstAuthor
     */
    protected void addAuthorBehavior(@NotNull TextArea<String> authors, @NotNull CheckBox firstAuthorOverridden,
        @NotNull TextField<String> firstAuthor) {
    }

    @NotNull
    protected abstract BootstrapButton newNavigationButton(@NotNull String id, @NotNull GlyphIconType icon,
        @NotNull SerializableSupplier<Boolean> isEnabled, SerializableSupplier<Long> idSupplier);

    private void makeAndQueueBackButton(String id, SerializableSupplier<Boolean> forceRequerySupplier) {
        BootstrapButton back = new BootstrapButton(id, new StringResourceModel("button.back.label"),
            Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                if (Boolean.TRUE.equals(forceRequerySupplier.get()))
                    restartSearchInPaperSearchPage();
                else if (callingPage != null)
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

    protected abstract void restartSearchInPaperSearchPage();

    @NotNull
    protected abstract BootstrapButton newExcludeButton(@NotNull String id);

    private void makeAndQueueSubmitButton(String id) {
        submit = new BootstrapButton(id, new StringResourceModel(getSubmitLinkResourceLabel()), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                super.onSubmit();
                onFormSubmit();
                doOnSubmit();
            }

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

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                enableButton(this, !isViewMode(), event);
            }
        };
        submit.setDefaultFormProcessing(true);
        submit.setEnabled(!isViewMode());
        submit.setVisible(!isViewMode());
        queue(submit);
    }

    protected abstract void doOnSubmit();

    private void enableButton(final BootstrapButton button, final boolean enabled, final IEvent<?> event) {
        if (event
                .getPayload()
                .getClass() == SelfUpdateEvent.class && button.isVisible()) {
            button.setEnabled(enabled);
            ((SelfUpdateEvent) event.getPayload())
                .getTarget()
                .add(button);
            event.dontBroadcastDeeper();
        }
    }

    private ResourceLink<Void> makeSummaryLink(String id) {
        return makePdfResourceLink(id, getSummaryDataSource());
    }

    /**
     * @return {@link PaperSummaryDataSource}
     */
    @Nullable
    protected abstract PaperSummaryDataSource getSummaryDataSource();

    private ResourceLink<Void> makeSummaryShortLink(String id) {
        return makePdfResourceLink(id, getSummaryShortDataSource());
    }

    /**
     * @return {@link PaperSummaryShortDataSource}
     */
    @Nullable
    protected abstract PaperSummaryShortDataSource getSummaryShortDataSource();

    private ResourceLink<Void> makePdfResourceLink(String id, JasperPaperDataSource<?> dataSource) {
        final String button = "button.";
        ResourceLink<Void> link = new ResourceLink<>(id, dataSource) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                if (isVisible())
                    add(new ButtonBehavior()
                        .setType(Buttons.Type.Info)
                        .setBlock(true)
                        .setSize(Size.Medium));
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
        link.setVisible(!isSearchMode());
        link.add(
            new AttributeModifier(TITLE_ATTR, new StringResourceModel(button + id + ".title", this, null).getString()));
        return link;
    }

    private abstract class AbstractTabPanel extends Panel {

        private static final long serialVersionUID = 1L;

        abstract int tabIndex();

        AbstractTabPanel(@NotNull String id, @NotNull IModel<?> model) {
            super(id, model);
        }

        @NotNull
        TextArea<String> queueTo(FieldEnumType fieldType) {
            return queueTo(fieldType, false, null);
        }

        void queueTo(@NotNull FieldEnumType fieldType, @Nullable PropertyValidator<?> pv) {
            queueTo(fieldType, false, pv);
        }

        void queueNewFieldTo(@NotNull FieldEnumType fieldType) {
            queueTo(fieldType, true, null);
        }

        @NotNull
        TextArea<String> queueTo(@NotNull FieldEnumType fieldType, boolean newField,
            @Nullable PropertyValidator<?> pv) {
            String id = fieldType.getFieldName();
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
            addDisableBehavior(field);
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
                return new TextArea<>(id) {
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
                protected void onUpdate(@NotNull final AjaxRequestTarget target) {
                    final String id = field.getId();
                    final String markupId = field.getMarkupId();
                    send(getPage(), Broadcast.BREADTH, new NewFieldChangeEvent(target)
                        .withId(id)
                        .withMarkupId(markupId));
                }
            });
        }

        @Override
        protected void onConfigure() {
            super.onConfigure();
            tabIndexModel.setObject(tabIndex());
        }
    }

    private class TabPanel1 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel1(@NotNull String id, @NotNull IModel<T> model) {
            super(id, model);
        }

        @Override
        int tabIndex() {
            return 0;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final Form<T> tab1Form = new Form<>("tab1Form", new CompoundPropertyModel<>(getModel())) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    onFormSubmit();
                }
            };
            tab1Form.add(new SelfUpdateBroadcastingBehavior(getPage()));
            queue(tab1Form);

            queueTo(GOALS, new PropertyValidator<>());
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

        TabPanel2(@NotNull String id, @NotNull IModel<T> model) {
            super(id, model);
        }

        @Override
        int tabIndex() {
            return 1;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final Form<T> tab2Form = new Form<>("tab2Form", new CompoundPropertyModel<>(getModel())) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    onFormSubmit();
                }
            };
            tab2Form.add(new SelfUpdateBroadcastingBehavior(getPage()));
            queue(tab2Form);

            queueTo(RESULT);
            queueTo(COMMENT);
            queueTo(INTERN);

            queueNewFieldTo(RESULT_MEASURED_OUTCOME);
            queueNewFieldTo(RESULT_EXPOSURE_RANGE);
            queueNewFieldTo(RESULT_EFFECT_ESTIMATE);
            queueNewFieldTo(CONCLUSION);
        }

    }

    private class TabPanel3 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        private static final String CODES_CLASS_BASE_NAME = "codesClass";

        TabPanel3(@NotNull String id, @NotNull IModel<T> model) {
            super(id, model);
        }

        @Override
        int tabIndex() {
            return 2;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> tab3Form = new Form<>("tab3Form", new CompoundPropertyModel<>(getModel())) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    onFormSubmit();
                }
            };
            tab3Form.setMultiPart(true);
            tab3Form.add(new SelfUpdateBroadcastingBehavior(getPage()));
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
            final TextField<String> mainCodeOfCodeClass1 = new TextField<>(MAIN_CODE_OF_CODECLASS1.getFieldName());
            final BootstrapMultiSelect<Code> codeClass1 = makeCodeClassComplex(CodeClassId.CC1, codeClasses);
            addCodeClass1ChangeBehavior(mainCodeOfCodeClass1, codeClass1);
            addMainCodeOfClass1(mainCodeOfCodeClass1);
            if (isEditMode())
                form.add(new CodeClass1ConsistencyValidator(codeClass1, mainCodeOfCodeClass1));
            addDisableBehavior(codeClass1);
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

            final ChainingModel<List<Code>> model = new ChainingModel<>(getModel()) {
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
                    if (codes != null && !codes.isEmpty()) {
                        ((IModel<CodeBoxAware>) getTarget())
                            .getObject()
                            .addCodes(codes);
                    }
                }
            };
            final CodeModel choices = new CodeModel(codeClassId, getLocalization());
            final IChoiceRenderer<Code> choiceRenderer = new ChoiceRenderer<>(
                CoreEntity.CoreEntityFields.DISPLAY_VALUE.getFieldName(), Code.CodeFields.CODE.getFieldName());
            final StringResourceModel noneSelectedModel = new StringResourceModel("codes.noneSelected", this, null);
            final StringResourceModel selectAllModel = new StringResourceModel(SELECT_ALL_RESOURCE_TAG, this, null);
            final StringResourceModel deselectAllModel = new StringResourceModel(DESELECT_ALL_RESOURCE_TAG, this, null);
            final BootstrapSelectConfig config = new BootstrapSelectConfig()
                .withMultiple(true)
                .withActionsBox(choices
                                    .getObject()
                                    .size() > getProperties().getMultiSelectBoxActionBoxWithMoreEntriesThan())
                .withSelectAllText(selectAllModel.getString())
                .withDeselectAllText(deselectAllModel.getString())
                .withNoneSelectedText(noneSelectedModel.getObject())
                .withLiveSearch(true)
                .withLiveSearchStyle("contains");
            final BootstrapMultiSelect<Code> multiSelect = new BootstrapMultiSelect<>(CODES_CLASS_BASE_NAME + id, model,
                choices, choiceRenderer).with(config);
            multiSelect.add(new AttributeModifier("data-width", "100%"));
            queue(multiSelect);
            addDisableBehavior(multiSelect);
            return multiSelect;
        }

    }

    private class TabPanel4 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel4(@NotNull String id, @NotNull IModel<T> model) {
            super(id, model);
        }

        @Override
        int tabIndex() {
            return 3;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> tab4Form = new Form<>("tab4Form", new CompoundPropertyModel<>(getModel())) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    onFormSubmit();
                }
            };
            tab4Form.add(new SelfUpdateBroadcastingBehavior(getPage()));
            queue(tab4Form);

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
            queueNewFieldTo(CONCLUSION);
        }
    }

    private class TabPanel5 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel5(@NotNull String id, @NotNull IModel<T> model) {
            super(id, model);
        }

        @Override
        int tabIndex() {
            return 4;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> tab5Form = new Form<>("tab5Form", new CompoundPropertyModel<>(getModel())) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    onFormSubmit();
                }
            };
            tab5Form.add(new SelfUpdateBroadcastingBehavior(getPage()));
            queue(tab5Form);

            originalAbstract = queueTo(ORIGINAL_ABSTRACT);
            addDisableBehavior(originalAbstract);
        }
    }

    private class TabPanel6 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel6(@NotNull String id, @NotNull IModel<T> model) {
            super(id, model);
        }

        @Override
        int tabIndex() {
            return 5;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            Form<T> tab6Form = new Form<>("tab6Form", new CompoundPropertyModel<>(getModel())) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    onFormSubmit();
                }
            };
            tab6Form.setOutputMarkupId(true);
            tab6Form.setMultiPart(true);
            tab6Form.add(new SelfUpdateBroadcastingBehavior(getPage()));
            queue(tab6Form);
            queue(newDropZoneFileUpload());
            attachments = newAttachmentTable("attachments");
            queue(attachments);
        }
    }

    @SuppressWarnings("unchecked")
    private class TabPanel7 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        TabPanel7(@NotNull String id, @NotNull IModel<T> model) {
            super(id, model);
        }

        @Override
        int tabIndex() {
            return 6;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            Form<T> tab7Form = new Form<>("tab7Form", new CompoundPropertyModel<>(getModel())) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    onFormSubmit();
                }
            };
            tab7Form.add(new SelfUpdateBroadcastingBehavior(getPage()));
            queue(tab7Form);
            queueHeadline(NEWSLETTER_HEADLINE);
            makeAndQueueNewsletterTopicSelectBox("newsletterTopic");
            queueIssue("newsletterIssue");
        }

        private void queueHeadline(FieldEnumType fieldType) {
            String id = fieldType.getFieldName();
            TextArea<String> field = new TextArea<>(id) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setEnabled(isSearchMode() || isAssociatedWithNewsletter());
                }
            };
            field.setOutputMarkupId(true);
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
            queue(new Label(id + LABEL_TAG, labelModel));
            field.setLabel(labelModel);
            addDisableBehavior(field);
            queue(field);
        }

        private void queueIssue(String id) {
            TextField<String> field = new TextField<>(id) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setVisible(isSearchMode());
                }
            };
            field.setOutputMarkupId(true);
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
            queue(new Label(id + LABEL_TAG, labelModel) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setVisible(isSearchMode());
                }
            });
            field.setLabel(labelModel);
            queue(field);
        }

        private void makeAndQueueNewsletterTopicSelectBox(final String id) {
            final ChainingModel<NewsletterTopic> model = new ChainingModel<>(getModel()) {
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
            final IChoiceRenderer<NewsletterTopic> choiceRenderer = new ChoiceRenderer<>(
                NewsletterTopic.NewsletterTopicFields.TITLE.getFieldName(),
                NewsletterTopic.NewsletterTopicFields.ID.getFieldName());

            final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
            final BootstrapSelectConfig config = new BootstrapSelectConfig()
                .withNoneSelectedText(noneSelectedModel.getObject())
                .withLiveSearch(true);
            final BootstrapSelect<NewsletterTopic> topic = new BootstrapSelect<>(id, model, newsletterTopicChoice,
                choiceRenderer) {

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setEnabled(isSearchMode() || isAssociatedWithNewsletter());
                }
            }.with(config);
            topic.setNullValid(true);
            queue(topic);
            queue(new Label(id + LABEL_TAG, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)));
            addDisableBehavior(topic);
        }
    }

    protected abstract boolean isAssociatedWithNewsletter();

    protected abstract boolean isAssociatedWithWipNewsletter();

    protected abstract boolean isaNewsletterInStatusWip();

    protected abstract void modifyNewsletterAssociation(@NotNull final AjaxRequestTarget target);

    @NotNull
    protected abstract DataTable<PaperAttachment, String> newAttachmentTable(@NotNull String id);

    @NotNull
    protected abstract DropZoneFileUpload newDropZoneFileUpload();

    /**
     * override if needed
     *
     * @param mainCodeOfCodeClass1
     *     text field with mainCode of code class1
     * @param codeClass1
     *     bootstrap multi-select for the codes of code class 1
     */
    protected void addCodeClass1ChangeBehavior(@NotNull final TextField<String> mainCodeOfCodeClass1,
        @NotNull final BootstrapMultiSelect<Code> codeClass1) {
    }

    @SuppressWarnings("unchecked")
    static class CodeClass1ConsistencyValidator extends AbstractFormValidator {

        private static final long serialVersionUID = 1L;

        private final FormComponent<?>[] components;

        CodeClass1ConsistencyValidator(@NotNull BootstrapMultiSelect<Code> codeClass1,
            @NotNull TextField<String> mainCodeOfCodeClass1) {
            components = new FormComponent<?>[] { codeClass1, mainCodeOfCodeClass1 };
        }

        @NotNull
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return components;
        }

        @Override
        public void validate(@NotNull Form<?> form) {
            final BootstrapMultiSelect<Code> codeClass1 = (BootstrapMultiSelect<Code>) components[0];
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
        pubmedRetrieval = new BootstrapAjaxLink<>(linkId, Buttons.Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                add(new ButtonBehavior()
                    .setType(Buttons.Type.Info)
                    .setSize(Size.Medium));
            }

            @Override
            public void onClick(@NotNull final AjaxRequestTarget target) {
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

    /**
     * override to do something with the pasted content
     *
     * @param target
     *     the ajax request target
     **/
    protected void getPubmedArticleAndCompare(@NotNull final AjaxRequestTarget target) {
    }
}
