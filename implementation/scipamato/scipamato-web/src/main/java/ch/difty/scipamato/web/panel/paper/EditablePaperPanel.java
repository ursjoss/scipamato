package ch.difty.scipamato.web.panel.paper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.PaperAttachment;
import ch.difty.scipamato.logic.parsing.AuthorParser;
import ch.difty.scipamato.logic.parsing.AuthorParserFactory;
import ch.difty.scipamato.persistence.PaperService;
import ch.difty.scipamato.persistence.PubmedArticleService;
import ch.difty.scipamato.pubmed.PubmedArticleFacade;
import ch.difty.scipamato.web.PageParameterNames;
import ch.difty.scipamato.web.component.SerializableConsumer;
import ch.difty.scipamato.web.component.SerializableSupplier;
import ch.difty.scipamato.web.component.data.LinkIconColumn;
import ch.difty.scipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;
import ch.difty.scipamato.web.jasper.ScipamatoPdfExporterConfiguration;
import ch.difty.scipamato.web.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.web.jasper.summaryshort.PaperSummaryShortDataSource;
import ch.difty.scipamato.web.pages.BasePage;
import ch.difty.scipamato.web.pages.Mode;
import ch.difty.scipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.scipamato.web.pages.paper.provider.PaperAttachmentProvider;
import ch.difty.scipamato.web.pages.paper.search.PaperSearchPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

/**
 * The {@link EditablePaperPanel} is backed by the {@link Paper} entity. The purpose is to create new papers
 * or edit existing papers.
 *
 * @author u.joss
 */
public abstract class EditablePaperPanel extends PaperPanel<Paper> {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER = "column.header.";

    private final Long searchOrderId;
    private final boolean showingExclusions;

    @SpringBean
    private PaperService paperService;

    @SpringBean
    private AuthorParserFactory authorParserFactory;

    @SpringBean
    private PubmedArticleService pubmedArticleService;

    public EditablePaperPanel(String id, IModel<Paper> model, PageReference previousPage, Long searchOrderId, boolean showingExclusions) {
        super(id, model, Mode.EDIT, previousPage);
        this.searchOrderId = searchOrderId;
        this.showingExclusions = showingExclusions;
    }

    /**
     * If the user does not choose to override the first author, the field can be disabled. 
     */
    @Override
    protected TextField<String> makeFirstAuthor(String firstAuthorId, CheckBox firstAuthorOverridden) {
        TextField<String> firstAuthor = new TextField<String>(firstAuthorId) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(firstAuthorOverridden.getModelObject());
            }
        };
        firstAuthor.add(new PropertyValidator<String>());
        return firstAuthor;
    }

    /**
     * Add the behavior to derive the first author from the authors field (unless overridden).
     */
    @Override
    protected void addAuthorBehavior(TextArea<String> authors, CheckBox firstAuthorOverridden, TextField<String> firstAuthor) {
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

    /**
     * Prepares the {@link PaperSummaryDataSource} for exporting the current entity into the pdf.
     */
    @Override
    protected PaperSummaryDataSource getSummaryDataSource() {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summary", this, null).getString();
        ReportHeaderFields rhf = new ReportHeaderFields.Builder(headerPart, brand)
            .withPopulation(new StringResourceModel(Paper.POPULATION + LABEL_RECOURCE_TAG, this, null).getString())
            .withGoals(new StringResourceModel(Paper.GOALS + LABEL_RECOURCE_TAG, this, null).getString())
            .withMethods(new StringResourceModel(Paper.METHODS + LABEL_RECOURCE_TAG, this, null).getString())
            .withResult(new StringResourceModel(Paper.RESULT + LABEL_RECOURCE_TAG, this, null).getString())
            .withComment(new StringResourceModel(Paper.COMMENT + LABEL_RECOURCE_TAG, this, null).getString())
            .build();
        ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(headerPart, getModelObject().getNumber())
            .withCreator(brand)
            .withPaperTitle(getModelObject().getTitle())
            .withPaperAuthor(getModelObject().getFirstAuthor())
            .withSubject(getModelObject().getMethods())
            .withAuthor(getModelObject().getCreatedByFullName())
            .withCodes(getModelObject().getCodes())
            .withCompression()
            .build();
        return new PaperSummaryDataSource(getModelObject(), rhf, config);
    }

    /**
     * Prepares the {@link PaperSummaryDataSource} for exporting the current entity into the pdf.
     */
    @Override
    protected PaperSummaryShortDataSource getSummaryShortDataSource() {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summaryShort", this, null).getString();
        ReportHeaderFields rhf = new ReportHeaderFields.Builder(headerPart, brand)
            .withGoals(new StringResourceModel(Paper.GOALS + LABEL_RECOURCE_TAG, this, null).getString())
            .withMethods(new StringResourceModel(Paper.METHODS + LABEL_RECOURCE_TAG, this, null).getString())
            .withMethodOutcome(new StringResourceModel(Paper.METHOD_OUTCOME + LABEL_RECOURCE_TAG, this, null).getString())
            .withResultMeasuredOutcome(new StringResourceModel(Paper.RESULT_MEASURED_OUTCOME + LABEL_RECOURCE_TAG, this, null).getString())
            .withMethodStudyDesign(new StringResourceModel(Paper.METHOD_STUDY_DESIGN + LABEL_RECOURCE_TAG, this, null).getString())
            .withPopulationPlace(new StringResourceModel(Paper.POPULATION_PLACE + LABEL_RECOURCE_TAG, this, null).getString())
            .withPopulationPariticpants(new StringResourceModel(Paper.POPULATION_PARTICIPANTS + LABEL_RECOURCE_TAG, this, null).getString())
            .withPopulationDuration(new StringResourceModel(Paper.POPULATION_DURATION + LABEL_RECOURCE_TAG, this, null).getString())
            .withExposurePollutant(new StringResourceModel(Paper.EXPOSURE_POLLUTANT + LABEL_RECOURCE_TAG, this, null).getString())
            .withExposureAssessment(new StringResourceModel(Paper.EXPOSURE_ASSESSMENT + LABEL_RECOURCE_TAG, this, null).getString())
            .withResultExposureRange(new StringResourceModel(Paper.RESULT_EXPOSURE_RANGE + LABEL_RECOURCE_TAG, this, null).getString())
            .withMethodStatistics(new StringResourceModel(Paper.METHOD_STATISTICS + LABEL_RECOURCE_TAG, this, null).getString())
            .withMethodConfounders(new StringResourceModel(Paper.METHOD_CONFOUNDERS + LABEL_RECOURCE_TAG, this, null).getString())
            .withResultEffectEstimate(new StringResourceModel(Paper.RESULT_EFFECT_ESTIMATE + LABEL_RECOURCE_TAG, this, null).getString())
            .withComment(new StringResourceModel(Paper.COMMENT + LABEL_RECOURCE_TAG, this, null).getString())
            .build();
        ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(headerPart, getModelObject().getNumber())
            .withCreator(brand)
            .withPaperTitle(getModelObject().getTitle())
            .withPaperAuthor(getModelObject().getFirstAuthor())
            .withSubject(getModelObject().getMethods())
            .withAuthor(getModelObject().getCreatedByFullName())
            .withCodes(getModelObject().getCodes())
            .withCompression()
            .build();
        return new PaperSummaryShortDataSource(getModelObject(), rhf, config);
    }

    /**
     * Enable the codeClass1 field to update the mainCodeOfCodeClass1 field automatically.
     */
    @Override
    protected void addCodeClass1ChangeBehavior(final TextField<String> mainCodeOfCodeClass1, final BootstrapMultiSelect<Code> codeClass1) {
        codeClass1.add(makeCodeClass1ChangeBehavior(codeClass1, mainCodeOfCodeClass1));
    }

    /*
     * Behavior to set the field mainCodeOfCodeClass1 to the first selection of the codeClass1 field.
     * Needs to be capable of handling the situation where the initial first choice is removed from the codeClass1 field.
     */
    private OnChangeAjaxBehavior makeCodeClass1ChangeBehavior(final BootstrapMultiSelect<Code> codeClass1, final TextField<String> mainCodeOfCodeClass1) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                if (codeClass1 != null && codeClass1.getModelObject() != null) {
                    final Collection<Code> codesOfClass1 = codeClass1.getModelObject();
                    switch (codesOfClass1.size()) {
                    case 0:
                        setMainCodeOfClass1(null, mainCodeOfCodeClass1, target);
                        break;
                    case 1:
                        setMainCodeOfClass1(codesOfClass1.iterator().next().getCode(), mainCodeOfCodeClass1, target);
                        break;
                    default:
                        ensureMainCodeIsPartOfCodes(codesOfClass1, mainCodeOfCodeClass1, target);
                        break;
                    }
                }
            }

            private void setMainCodeOfClass1(final String code, final TextField<String> mainCodeOfCodeClass1, final AjaxRequestTarget target) {
                mainCodeOfCodeClass1.setModelObject(code);
                target.add(mainCodeOfCodeClass1);
            }

            private void ensureMainCodeIsPartOfCodes(Collection<Code> codesOfClass1, TextField<String> mainCodeOfCodeClass1, AjaxRequestTarget target) {
                final Optional<String> main = Optional.ofNullable(mainCodeOfCodeClass1.getModelObject());
                final Optional<String> match = codesOfClass1.stream().map(Code::getCode).filter(c -> main.isPresent() && main.get().equals(c)).findFirst();
                if (main.isPresent() && !match.isPresent()) {
                    mainCodeOfCodeClass1.setModelObject(null);
                    target.add(mainCodeOfCodeClass1);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getPubmedArticleAndCompare(AjaxRequestTarget target) {
        Paper paper = getModelObject();
        Integer pmId = paper.getPmId();
        if (pmId != null) {
            Optional<PubmedArticleFacade> pao = pubmedArticleService.getPubmedArticleWithPmid(pmId);
            if (pao.isPresent()) {
                PubmedArticleFacade pa = pao.get();
                if (String.valueOf(pmId).equals(pa.getPmId())) {
                    setFieldsIfNotSetCompareOtherwise(paper, pa, target);
                }
            } else {
                error(new StringResourceModel("pubmedRetrieval.pmid.error", this, getModel()).getString());
            }
        } else {
            error(new StringResourceModel("pubmedRetrieval.pmid.missing", this, null).getString());
        }
        if (getPage() instanceof BasePage) {
            target.add(((BasePage<Paper>) getPage()).getFeedbackPanel());
        }
    }

    private class ProcessingRecord {
        private List<String> modifiedFields = new ArrayList<>();
        private List<String> differingFields = new ArrayList<>();

        void addChangedField(String name) {
            modifiedFields.add(name);
        }

        void addDifferingField(String name) {
            differingFields.add(name);
        }

        boolean isDirty() {
            return !modifiedFields.isEmpty();
        }

        boolean allMatching() {
            return differingFields.isEmpty();
        }

        String getModifiedFieldString() {
            return modifiedFields.stream().collect(Collectors.joining(", "));
        }

    }

    /**
     * If a field in the scipamato paper is null or filled with a default value indicating 'not set',
     * the value from the pubmed paper is inserted. Otherwise the two values are compared and differences
     * are alerted (except for the abstract, which may have subtle differences difficult to control).
     */
    private void setFieldsIfNotSetCompareOtherwise(Paper p, PubmedArticleFacade a, AjaxRequestTarget target) {
        final ProcessingRecord pr = new ProcessingRecord();

        processStringField(Paper.AUTHORS, a.getAuthors(), Paper::getAuthors, Paper::setAuthors, p, pr, target, authors, firstAuthor);
        processStringField(Paper.FIRST_AUTHOR, a.getFirstAuthor(), Paper::getFirstAuthor, Paper::setFirstAuthor, p, pr, target, firstAuthor);
        processStringField(Paper.TITLE, a.getTitle(), Paper::getTitle, Paper::setTitle, p, pr, target, title);
        processIntegerField(Paper.PUBL_YEAR, a.getPublicationYear(), Paper::getPublicationYear, Paper::setPublicationYear, "year.parse.error", p, pr, target, publicationYear);
        processStringField(Paper.LOCATION, a.getLocation(), Paper::getLocation, Paper::setLocation, p, pr, target, location);
        processStringField(Paper.DOI, a.getDoi(), Paper::getDoi, Paper::setDoi, p, pr, target, doi);
        processStringField(Paper.ORIGINAL_ABSTRACT, a.getOriginalAbstract(), Paper::getOriginalAbstract, Paper::setOriginalAbstract, p, pr, target, originalAbstract);

        provideUserInfo(pr);
    }

    /**
     * Sets the paper's {@code fieldName} value if it has been null - and informs about the change.
     * If the value is not null, it compares the paper's value with the article's value and informs about mismatches - unless it's the original abstract,
     * which is not compared, as the differences can be subtle but not really that important.
     * @param fieldName the name of the field (as defined in the entity)
     * @param articleValue the string value of the article field
     * @param getter the lambda of the getter for the paper field
     * @param setter the lambda of the setter for the paper field
     * @param p the Paper
     * @param pr the processing record to capture the resulting flags
     * @param target AjaxRequestTarget
     * @param fcs the form components that need to be added to the AjaxTargetRequest in case of changed values
     */
    private void processStringField(final String fieldName, final String articleValue, final Function<Paper, String> getter, final BiConsumer<Paper, String> setter, final Paper p,
            final ProcessingRecord pr, final AjaxRequestTarget target, final FormComponent<?>... fcs) {
        final String localizedFieldName = getLocalizedFieldName(fieldName);
        final String paperValue = getter.apply(p);
        if (paperValue == null || Paper.NA_AUTHORS.equals(paperValue) || Paper.NA_STRING.equals(paperValue)) {
            setPaperFieldFromArticleAndInform(localizedFieldName, articleValue, setter, p, pr, target, fcs);
        } else {
            if (!Paper.ORIGINAL_ABSTRACT.equals(fieldName))
                warnNonmatchingFields(localizedFieldName, articleValue, paperValue, pr);
        }
    }

    private void setPaperFieldFromArticleAndInform(final String fieldName, final String articleValue, final BiConsumer<Paper, String> setter, final Paper p, final ProcessingRecord pr,
            final AjaxRequestTarget target, final FormComponent<?>... fcs) {
        setter.accept(p, articleValue);
        pr.addChangedField(fieldName);
        addTargets(target, fcs);
    }

    /**
     * Sets the paper's integer {@code fieldName} value if it has been null - and informs about the change.
     * If the value is not null, it compares the paper's value with the article's value and informs about mismatches.
     * If the paper's value cannot be converted to integer, an error message is issued.
     * @param fieldName the name of the field (as defined in the entity)
     * @param rawArticleValue the string value of the article field
     * @param getter the lambda of the getter for the paper field
     * @param setter the lambda of the setter for the paper field
     * @param conversionResourceString the resource string for the error message to be issued if the article value cannot be converted to integer.
     * @param p the Paper
     * @param pr the processing record to capture the resulting flags
     * @param target the AjaxRequestTarget
     * @param fcs the form components that need to be added to the AjaxTargetRequest in case of changed values
     */
    private void processIntegerField(final String fieldName, final String rawArticleValue, final Function<Paper, Integer> getter, final BiConsumer<Paper, Integer> setter,
            final String conversionResourceString, final Paper p, final ProcessingRecord pr, final AjaxRequestTarget target, final FormComponent<?>... fcs) {
        final String localizedFieldName = getLocalizedFieldName(fieldName);
        final Integer paperValue = getter.apply(p);
        if (paperValue == null || Paper.NA_PUBL_YEAR == paperValue.intValue()) {
            try {
                final int articleValue = Integer.parseInt(rawArticleValue);
                setPaperFieldFromArticleAndInform(localizedFieldName, articleValue, setter, p, pr, target, fcs);
            } catch (final Exception ex) {
                error(new StringResourceModel(conversionResourceString, this, null).setParameters(rawArticleValue).getString());
            }
        } else {
            warnNonmatchingFields(localizedFieldName, rawArticleValue, String.valueOf(paperValue), pr);
        }
    }

    private void setPaperFieldFromArticleAndInform(final String fieldName, final int articleValue, final BiConsumer<Paper, Integer> setter, final Paper p, final ProcessingRecord pr,
            final AjaxRequestTarget target, final FormComponent<?>... fcs) {
        setter.accept(p, articleValue);
        pr.addChangedField(fieldName);
        addTargets(target, fcs);
    }

    private void provideUserInfo(final ProcessingRecord pr) {
        if (pr.isDirty()) {
            info(new StringResourceModel("pubmedRetrieval.dirty.info", this, null).setParameters(pr.getModifiedFieldString()).getString());
        } else if (pr.allMatching()) {
            info(new StringResourceModel("pubmedRetrieval.no-difference.info", this, null).getString());
        }
    }

    private String getLocalizedFieldName(String fieldName) {
        return new StringResourceModel(fieldName + LABEL_RECOURCE_TAG, this, null).getString();
    }

    private void addTargets(AjaxRequestTarget target, FormComponent<?>... fcs) {
        if (fcs.length > 0)
            for (FormComponent<?> fc : fcs)
                target.add(fc);
    }

    /**
     * Compares the pubmed field with the scipamato article field. warns if it does not match.
     * @param fieldName the (localized) name of the field, used to construct the warn message
     * @param pmField field from pubmed article
     * @param paperField field from scipamato paper
     * @param ProcessingRecord pr
     */
    private void warnNonmatchingFields(String fieldName, String pmField, String paperField, ProcessingRecord pr) {
        if (pmField != null && !pmField.equals(paperField)) {
            warn("PubMed " + fieldName + ": " + pmField);
            pr.addDifferingField(fieldName);
        }
    }

    protected boolean hasPubMedId() {
        return getModelObject().getPmId() != null;
    }

    protected BootstrapButton newNavigationButton(String id, GlyphIconType icon, SerializableSupplier<Boolean> isEnabled, SerializableSupplier<Long> idSupplier) {
        final BootstrapButton btn = new BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                final Long id = idSupplier.get();
                if (id != null) {
                    final Optional<Paper> p = paperService.findById(id);
                    if (p.isPresent())
                        setResponsePage(new PaperEntryPage(Model.of(p.get()), getCallingPage(), searchOrderId, showingExclusions));
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(isEnabled.get());
            }
        };
        btn.setDefaultFormProcessing(false);
        btn.setIconType(icon);
        btn.add(new AttributeModifier(TITLE, new StringResourceModel("button." + id + ".title", this, null).getString()));
        btn.setType(Buttons.Type.Primary);
        return btn;
    }

    protected BootstrapButton newExcludeButton(String id) {
        BootstrapButton exclude = new BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                Long paperId = EditablePaperPanel.this.getModelObject().getId();
                if (paperId != null) {
                    if (showingExclusions) {
                        paperService.reincludeIntoSearchOrder(searchOrderId, paperId);
                    } else {
                        paperService.excludeFromSearchOrder(searchOrderId, paperId);
                    }
                }
                PageParameters pp = new PageParameters();
                pp.add(PageParameterNames.SEARCH_ORDER_ID, searchOrderId);
                pp.add(PageParameterNames.SHOW_EXCLUDED, showingExclusions);
                setResponsePage(new PaperSearchPage(pp));
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(searchOrderId != null);
                if (showingExclusions) {
                    setIconType(GlyphIconType.okcircle);
                    add(new AttributeModifier(TITLE, new StringResourceModel("button.exclude.title.reinclude", this, null).getString()));
                } else {
                    setIconType(GlyphIconType.bancircle);
                    add(new AttributeModifier(TITLE, new StringResourceModel("button.exclude.title.exclude", this, null).getString()));
                }
            }
        };
        exclude.setDefaultFormProcessing(false);
        return exclude;
    }

    protected DropZoneFileUpload newDropZoneFileUpload() {
        DropZoneFileUpload upload = new DropZoneFileUpload("dropzone") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpload(AjaxRequestTarget target, Map<String, List<FileItem>> fileMap) {
                if (fileMap != null && fileMap.containsKey("file")) {
                    Paper p = null;
                    for (final FileItem file : fileMap.get("file"))
                        p = paperService.saveAttachment(convertToPaperAttachment(file));
                    if (p != null) {
                        EditablePaperPanel.this.setModelObject(p);
                        target.add(getAttachments());
                    }
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(isEditMode());
            }

        };
        upload.getConfig().withMaxFileSize(4).withThumbnailHeight(80).withThumbnailWidth(80).withPreviewsContainer(".dropzone-previews").withParallelUploads(4).withAutoQueue(true);
        return upload;
    }

    private PaperAttachment convertToPaperAttachment(final FileItem file) {
        final PaperAttachment pa = new PaperAttachment();
        pa.setPaperId(EditablePaperPanel.this.getModelObject().getId());
        pa.setContent(file.get());
        pa.setContentType(file.getContentType());
        pa.setSize(file.getSize());
        pa.setName(sanitize(file.getName()));
        return pa;
    }

    private String sanitize(final String originalFileName) {
        return originalFileName.replace(" ", "_");
    }

    protected DataTable<PaperAttachment, String> newAttachmentTable(String id) {
        PropertyModel<List<PaperAttachment>> model = new PropertyModel<>(getModel(), Paper.ATTACHMENTS);
        PaperAttachmentProvider provider = new PaperAttachmentProvider(model);
        BootstrapDefaultDataTable<PaperAttachment, String> table = new BootstrapDefaultDataTable<PaperAttachment, String>(id, makeTableColumns(), provider, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Item<PaperAttachment> newRowItem(String id, int index, IModel<PaperAttachment> model) {
                final PaperAttachment pa = model.getObject();
                final Item<PaperAttachment> item = super.newRowItem(id, index, model);
                item.add(AttributeModifier.replace(TITLE, pa.getSizeKiloBytes() + " kB - " + pa.getContentType()));
                return item;
            }
        };
        table.setOutputMarkupId(true);
        return table;
    }

    private List<IColumn<PaperAttachment, String>> makeTableColumns() {
        final List<IColumn<PaperAttachment, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn(PaperAttachment.NAME, this::onTitleClick));
        columns.add(makeLinkIconColumn("removeAttachment"));
        return columns;
    }

    private void onTitleClick(IModel<PaperAttachment> m) {
        Integer id = m.getObject().getId();
        PaperAttachment pa = paperService.loadAttachmentWithContentBy(id);
        ByteArrayResource r = new ByteArrayResource(pa.getContentType(), pa.getContent(), pa.getName());
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceRequestHandler(r, new PageParameters()));
    }

    private ClickablePropertyColumn<PaperAttachment, String> makeClickableColumn(String propExpression, SerializableConsumer<IModel<PaperAttachment>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), null, propExpression, consumer);
    }

    private IColumn<PaperAttachment, String> makeLinkIconColumn(String id) {
        return new LinkIconColumn<PaperAttachment>(new StringResourceModel(COLUMN_HEADER + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<PaperAttachment> rowModel) {
                return Model.of("fa fa-fw fa-trash-o text-danger");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<PaperAttachment> rowModel) {
                return new StringResourceModel("column.title.removeAttachment", EditablePaperPanel.this, null).setParameters(rowModel.getObject().getName());
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<PaperAttachment> rowModel, AjaxLink<Void> link) {
                final Integer id = rowModel.getObject().getId();
                setModelObject(paperService.deleteAttachment(id));
                target.add(getAttachments());
            }
        };
    }
}
