package ch.difty.scipamato.core.web.paper.entry;

import static ch.difty.scipamato.core.entity.Paper.PaperFields.*;
import static ch.difty.scipamato.core.entity.PaperAttachment.PaperAttachmentFields.NAME;
import static ch.difty.scipamato.core.web.CorePageParameters.*;
import static ch.difty.scipamato.core.web.paper.entry.ExtensionsKt.isAheadOfPrint;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.SerializableSupplier;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.common.web.component.table.column.LinkIconColumn;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.logic.parsing.AuthorParser;
import ch.difty.scipamato.core.logic.parsing.AuthorParserFactory;
import ch.difty.scipamato.core.persistence.NewsletterService;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;
import ch.difty.scipamato.core.pubmed.PubmedArticleResult;
import ch.difty.scipamato.core.pubmed.PubmedArticleService;
import ch.difty.scipamato.core.web.common.BasePage;
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent;
import ch.difty.scipamato.core.web.paper.PageFactory;
import ch.difty.scipamato.core.web.paper.PaperAttachmentProvider;
import ch.difty.scipamato.core.web.paper.common.PaperPanel;
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfExporterConfiguration;
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource;

/**
 * The {@link EditablePaperPanel} is backed by the {@link Paper} entity. The
 * purpose is to create new papers or edit existing papers.
 *
 * @author u.joss
 */
@SuppressWarnings("ALL")
@Slf4j
public abstract class EditablePaperPanel extends PaperPanel<Paper> {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER = "column.header.";

    private static final int ATTACHMENT_PAGE_SIZE = 10;

    private final Long    searchOrderId;
    private final boolean showingExclusions;

    @SpringBean
    private PaperService paperService;

    @SpringBean
    private AuthorParserFactory authorParserFactory;

    @SpringBean
    private PubmedArticleService pubmedArticleService;

    @SpringBean
    private PageFactory pageFactory;

    @SpringBean
    private NewsletterService newsletterService;

    @SpringBean
    private ScipamatoWebSessionFacade sessionFacade;

    @SpringBean
    private CoreShortFieldConcatenator shortFieldConcatenator;

    @SpringBean
    private Environment env;

    EditablePaperPanel(@NotNull String id, @NotNull IModel<Paper> model, @Nullable PageReference previousPage, @Nullable Long searchOrderId,
        boolean showingExclusions, @NotNull Mode mode, @NotNull IModel<Integer> tabIndexModel) {
        super(id, model, mode, previousPage, tabIndexModel);
        if (!(mode == Mode.EDIT || mode == Mode.VIEW))
            throw new IllegalArgumentException("Mode " + mode + " is not enabled in " + getClass());
        this.searchOrderId = searchOrderId;
        this.showingExclusions = showingExclusions;
    }

    /**
     * If the user does not choose to override the first author, the field can be disabled.
     */
    @NotNull
    @Override
    protected TextField<String> makeFirstAuthor(@NotNull String firstAuthorId, @NotNull CheckBox firstAuthorOverridden) {
        TextField<String> firstAuthor = new TextField<>(firstAuthorId) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(firstAuthorOverridden.getModelObject());
            }
        };
        firstAuthor.add(new PropertyValidator<>());
        return firstAuthor;
    }

    /**
     * Add the behavior to derive the first author from the authors field (unless
     * overridden).
     */
    @Override
    protected void addAuthorBehavior(@NotNull TextArea<String> authors, @NotNull CheckBox firstAuthorOverridden,
        @NotNull TextField<String> firstAuthor) {
        firstAuthorOverridden.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
        authors.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
    }

    /*
     * Behavior to parse the authors string and populate the firstAuthor field - but
     * only if not overridden.
     */
    private OnChangeAjaxBehavior makeFirstAuthorChangeBehavior(TextArea<String> authors, CheckBox overridden, TextField<String> firstAuthor) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(@NotNull AjaxRequestTarget target) {
                if (Boolean.FALSE.equals(overridden.getModelObject())) {
                    AuthorParser p = authorParserFactory.createParser(authors.getValue());
                    firstAuthor.setModelObject(p.getFirstAuthor());
                }
                target.add(firstAuthor);
            }
        };
    }

    /**
     * Prepares the {@link PaperSummaryDataSource} for exporting the current entity
     * into the pdf.
     */
    @NotNull
    @Override
    protected PaperSummaryDataSource getSummaryDataSource() {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summary", this, null).getString();
        final ReportHeaderFields rhf = new ReportHeaderFields(headerPart, brand, getLabelResourceFor(GOALS.getFieldName()),
            getLabelResourceFor(METHODS.getFieldName()), getLabelResourceFor(METHOD_OUTCOME.getFieldName()),
            getLabelResourceFor(RESULT_MEASURED_OUTCOME.getFieldName()), getLabelResourceFor(METHOD_STUDY_DESIGN.getFieldName()),
            getLabelResourceFor(POPULATION_PLACE.getFieldName()), getLabelResourceFor(POPULATION_PARTICIPANTS.getFieldName()),
            getLabelResourceFor(POPULATION_DURATION.getFieldName()), getLabelResourceFor(EXPOSURE_POLLUTANT.getFieldName()),
            getLabelResourceFor(EXPOSURE_ASSESSMENT.getFieldName()), getLabelResourceFor(RESULT_EXPOSURE_RANGE.getFieldName()),
            getLabelResourceFor(METHOD_STATISTICS.getFieldName()), getLabelResourceFor(METHOD_CONFOUNDERS.getFieldName()),
            getLabelResourceFor(RESULT_EFFECT_ESTIMATE.getFieldName()), getLabelResourceFor(CONCLUSION.getFieldName()),
            getLabelResourceFor(COMMENT.getFieldName()), getLabelResourceFor(POPULATION.getFieldName()), getLabelResourceFor(RESULT.getFieldName()));
        final Paper p = getModelObject();
        final ScipamatoPdfExporterConfiguration config = makeExporterConfig(brand, headerPart, p);
        return new PaperSummaryDataSource(p, rhf, shortFieldConcatenator, config);
    }

    private ScipamatoPdfExporterConfiguration makeExporterConfig(final String brand, final String headerPart, final Paper p) {
        return new ScipamatoPdfExporterConfiguration.Builder(headerPart, p.getNumber())
            .withCreator(brand)
            .withPaperTitle(p.getTitle())
            .withPaperAuthor(p.getFirstAuthor())
            .withSubject(p.getMethods())
            .withAuthor(p.getCreatedByFullName())
            .withCodes(p.getCodes())
            .withCompression()
            .build();
    }

    /**
     * Prepares the {@link PaperSummaryDataSource} for exporting the current entity
     * into the pdf.
     */
    @NotNull
    @Override
    protected PaperSummaryShortDataSource getSummaryShortDataSource() {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summaryShort", this, null).getString();
        final ReportHeaderFields rhf = new ReportHeaderFields(headerPart, brand, getLabelResourceFor(GOALS.getFieldName()),
            getLabelResourceFor(METHODS.getFieldName()), getLabelResourceFor(METHOD_OUTCOME.getFieldName()),
            getLabelResourceFor(RESULT_MEASURED_OUTCOME.getFieldName()), getLabelResourceFor(METHOD_STUDY_DESIGN.getFieldName()),
            getLabelResourceFor(POPULATION_PLACE.getFieldName()), getLabelResourceFor(POPULATION_PARTICIPANTS.getFieldName()),
            getLabelResourceFor(POPULATION_DURATION.getFieldName()), getLabelResourceFor(EXPOSURE_POLLUTANT.getFieldName()),
            getLabelResourceFor(EXPOSURE_ASSESSMENT.getFieldName()), getLabelResourceFor(RESULT_EXPOSURE_RANGE.getFieldName()),
            getLabelResourceFor(METHOD_STATISTICS.getFieldName()), getLabelResourceFor(METHOD_CONFOUNDERS.getFieldName()),
            getLabelResourceFor(RESULT_EFFECT_ESTIMATE.getFieldName()), getLabelResourceFor(CONCLUSION.getFieldName()),
            getLabelResourceFor(COMMENT.getFieldName()));
        final Paper p = getModelObject();
        final ScipamatoPdfExporterConfiguration config = makeExporterConfig(brand, headerPart, p);
        return new PaperSummaryShortDataSource(p, rhf, config);
    }

    /**
     * Enable the codeClass1 field to update the mainCodeOfCodeClass1 field
     * automatically.
     */
    @Override
    protected void addCodeClass1ChangeBehavior(@NotNull final TextField<String> mainCodeOfCodeClass1,
        @NotNull final BootstrapMultiSelect<Code> codeClass1) {
        codeClass1.add(makeCodeClass1ChangeBehavior(codeClass1, mainCodeOfCodeClass1));
    }

    /*
     * Behavior to set the field mainCodeOfCodeClass1 to the first selection of the
     * codeClass1 field. Needs to be capable of handling the situation where the
     * initial first choice is removed from the codeClass1 field.
     */
    private OnChangeAjaxBehavior makeCodeClass1ChangeBehavior(final BootstrapMultiSelect<Code> codeClass1,
        final TextField<String> mainCodeOfCodeClass1) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(@NotNull final AjaxRequestTarget target) {
                if (codeClass1 != null && codeClass1.getModelObject() != null) {
                    final Collection<Code> codesOfClass1 = codeClass1.getModelObject();
                    switch (codesOfClass1.size()) {
                    case 0:
                        setMainCodeOfClass1(null, mainCodeOfCodeClass1, target);
                        break;
                    case 1:
                        setMainCodeOfClass1(codesOfClass1
                            .iterator()
                            .next()
                            .getCode(), mainCodeOfCodeClass1, target);
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

            private void ensureMainCodeIsPartOfCodes(Collection<Code> codesOfClass1, TextField<String> mainCodeOfCodeClass1,
                AjaxRequestTarget target) {
                final Optional<String> main = Optional.ofNullable(mainCodeOfCodeClass1.getModelObject());
                final Optional<String> match = codesOfClass1
                    .stream()
                    .map(Code::getCode)
                    .filter(c -> main.isPresent() && main
                        .get()
                        .equals(c))
                    .findFirst();
                if (main.isPresent() && match.isEmpty()) {
                    mainCodeOfCodeClass1.setModelObject(null);
                    target.add(mainCodeOfCodeClass1);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getPubmedArticleAndCompare(@NotNull AjaxRequestTarget target) {
        final Paper paper = getModelObject();
        final Integer pmId = paper.getPmId();
        final String apiKey = getProperties().getPubmedApiKey();
        if (pmId != null) {
            final PubmedArticleResult par = (apiKey == null) ?
                pubmedArticleService.getPubmedArticleWithPmid(pmId) :
                pubmedArticleService.getPubmedArticleWithPmidAndApiKey(pmId, apiKey);
            if (par.getPubmedArticleFacade() != null) {
                final PubmedArticleFacade pa = par.getPubmedArticleFacade();
                if (String
                    .valueOf(pmId)
                    .equals(pa.getPmId())) {
                    setFieldsIfNotSetCompareOtherwise(paper, pa, target);
                }
            } else {
                error(new StringResourceModel("pubmedRetrieval.pmid.error", this, getModel())
                    .setParameters(par.getErrorMessage())
                    .getString());
            }
        } else {
            error(new StringResourceModel("pubmedRetrieval.pmid.missing", this, null).getString());
        }
        if (getPage() instanceof BasePage) {
            target.add(((BasePage<Paper>) getPage()).getFeedbackPanel());
        }
    }

    private static class ProcessingRecord {
        private final List<String> modifiedFields  = new ArrayList<>();
        private final List<String> differingFields = new ArrayList<>();

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
            return String.join(", ", modifiedFields);
        }

    }

    /**
     * If a field in the scipamato paper is null or filled with a default value
     * indicating 'not set', the value from the pubmed paper is inserted. Otherwise
     * the two values are compared and differences are alerted.
     * If the paper has been added to SciPaMaTo as 'ahead of print', we overwrite
     * the fields (except for title and first author).
     */
    private void setFieldsIfNotSetCompareOtherwise(Paper p, PubmedArticleFacade a, AjaxRequestTarget target) {
        final ProcessingRecord pr = new ProcessingRecord();

        boolean aheadOfPrint = isAheadOfPrint(p, a);

        processStringField(AUTHORS.getFieldName(), a.getAuthors(), Paper::getAuthors, Paper::setAuthors, p, aheadOfPrint, pr, target, getAuthors(),
            getFirstAuthor());
        processStringField(FIRST_AUTHOR.getFieldName(), a.getFirstAuthor(), Paper::getFirstAuthor, Paper::setFirstAuthor, p, aheadOfPrint, pr, target,
            getFirstAuthor());
        processStringField(TITLE.getFieldName(), a.getTitle(), Paper::getTitle, Paper::setTitle, p, aheadOfPrint, pr, target, getTitle());
        processIntegerField(PUBL_YEAR.getFieldName(), a.getPublicationYear(), Paper::getPublicationYear, Paper::setPublicationYear,
            "year.parse.error", p, aheadOfPrint, pr, target, getPublicationYear());
        processStringField(LOCATION.getFieldName(), a.getLocation(), Paper::getLocation, Paper::setLocation, p, aheadOfPrint, pr, target,
            getLocation());
        processStringField(DOI.getFieldName(), a.getDoi(), Paper::getDoi, Paper::setDoi, p, aheadOfPrint, pr, target, getDoi());
        processStringField(ORIGINAL_ABSTRACT.getFieldName(), a.getOriginalAbstract(), Paper::getOriginalAbstract, Paper::setOriginalAbstract, p,
            aheadOfPrint, pr, target, getOriginalAbstract());

        provideUserInfo(pr, aheadOfPrint);
    }

    /**
     * Sets the paper's {@code fieldName} value if it has been null - and informs
     * about the change. If the value is not null, it compares the paper's value
     * with the article's value and informs about mismatches.
     *
     * @param fieldName
     *     the name of the field (as defined in the entity)
     * @param articleValue
     *     the string value of the article field
     * @param getter
     *     the lambda of the getter for the paper field
     * @param setter
     *     the lambda of the setter for the paper field
     * @param p
     *     the Paper
     * @param pr
     *     the processing record to capture the resulting flags
     * @param target
     *     AjaxRequestTarget
     * @param fcs
     *     the form components that need to be added to the AjaxTargetRequest
     *     in case of changed values
     */
    private void processStringField(final String fieldName, final String articleValue, final Function<Paper, String> getter,
        final BiConsumer<Paper, String> setter, final Paper p, final boolean aheadOfPrint, final ProcessingRecord pr, final AjaxRequestTarget target,
        final FormComponent<?>... fcs) {
        final String localizedFieldName = getLabelResourceFor(fieldName);
        final String paperValue = getter.apply(p);
        if (paperValue == null || Paper.NA_AUTHORS.equals(paperValue) || Paper.NA_STRING.equals(paperValue) || aheadOfPrint) {
            setPaperFieldFromArticleAndInform(localizedFieldName, articleValue, setter, p, pr, target, fcs);
        } else {
            warnNonMatchingFields(localizedFieldName, articleValue, paperValue, pr);
        }
    }

    private void setPaperFieldFromArticleAndInform(final String fieldName, final String articleValue, final BiConsumer<Paper, String> setter,
        final Paper p, final ProcessingRecord pr, final AjaxRequestTarget target, final FormComponent<?>... fcs) {
        setter.accept(p, articleValue);
        pr.addChangedField(fieldName);
        addTargets(target, fcs);
    }

    /**
     * Sets the paper's integer {@code fieldName} value if it has been null - and
     * informs about the change. If the value is not null, it compares the paper's
     * value with the article's value and informs about mismatches. If the paper's
     * value cannot be converted to integer, an error message is issued.
     *
     * @param fieldName
     *     the name of the field (as defined in the entity)
     * @param rawArticleValue
     *     the string value of the article field
     * @param getter
     *     the lambda of the getter for the paper field
     * @param setter
     *     the lambda of the setter for the paper field
     * @param conversionResourceString
     *     the resource string for the error message to be issued if the
     *     article value cannot be converted to integer.
     * @param p
     *     the Paper
     * @param pr
     *     the processing record to capture the resulting flags
     * @param target
     *     the AjaxRequestTarget
     * @param fcs
     *     the form components that need to be added to the AjaxTargetRequest
     *     in case of changed values
     */
    @SuppressWarnings("SameParameterValue")
    private void processIntegerField(final String fieldName, final String rawArticleValue, final Function<Paper, Integer> getter,
        final ObjIntConsumer<Paper> setter, final String conversionResourceString, final Paper p, final boolean aheadOfPrint,
        final ProcessingRecord pr, final AjaxRequestTarget target, final FormComponent<?>... fcs) {
        final String localizedFieldName = getLabelResourceFor(fieldName);
        final Integer paperValue = getter.apply(p);
        if (paperValue == null || Paper.NA_PUBL_YEAR == paperValue || aheadOfPrint) {
            try {
                final int articleValue = Integer.parseInt(rawArticleValue);
                setPaperFieldFromArticleAndInform(localizedFieldName, articleValue, setter, p, pr, target, fcs);
            } catch (final Exception ex) {
                error(new StringResourceModel(conversionResourceString, this, null)
                    .setParameters(rawArticleValue)
                    .getString());
            }
        } else {
            warnNonMatchingFields(localizedFieldName, rawArticleValue, String.valueOf(paperValue), pr);
        }
    }

    private void setPaperFieldFromArticleAndInform(final String fieldName, final int articleValue, final ObjIntConsumer<Paper> setter, final Paper p,
        final ProcessingRecord pr, final AjaxRequestTarget target, final FormComponent<?>... fcs) {
        setter.accept(p, articleValue);
        pr.addChangedField(fieldName);
        addTargets(target, fcs);
    }

    private void provideUserInfo(final ProcessingRecord pr, final boolean aheadOfPrint) {
        if (pr.isDirty()) {
            if (aheadOfPrint)
                info(new StringResourceModel("pubmedRetrieval.aheadofprint.dirty.info", this, null).getString());
            else
                info(new StringResourceModel("pubmedRetrieval.dirty.info", this, null)
                    .setParameters(pr.getModifiedFieldString())
                    .getString());
        } else if (pr.allMatching()) {
            info(new StringResourceModel("pubmedRetrieval.no-difference.info", this, null).getString());
        }
    }

    private void addTargets(@NotNull AjaxRequestTarget target, @NotNull FormComponent<?>... fcs) {
        if (fcs.length > 0)
            for (FormComponent<?> fc : fcs)
                if (fc != null)
                    target.add(fc);
    }

    /**
     * Compares the pubmed field with the scipamato article field. warns if it does not match.
     *
     * @param fieldName
     *     the (localized) name of the field, used to construct the warn message
     * @param pmField
     *     field from pubmed article
     * @param paperField
     *     field from scipamato paper
     * @param pr
     *     ProcessingRecord
     */
    private void warnNonMatchingFields(String fieldName, String pmField, String paperField, ProcessingRecord pr) {
        if (pmField != null && paperField != null && !normalizeLineEnds(pmField).equals(normalizeLineEnds(paperField))) {
            warn("PubMed " + fieldName + ": " + pmField);
            pr.addDifferingField(fieldName);
        }
    }

    // Thanks to Roland Illig - https://codereview.stackexchange.com/questions/140048/comparing-strings-with-different-newlines
    @SuppressWarnings("SpellCheckingInspection")
    private String normalizeLineEnds(final String s) {
        return s
            .replace("\r\n", "\n")
            .replace('\r', '\n');
    }

    @Override
    protected boolean hasPubMedId() {
        return getModelObject().getPmId() != null;
    }

    @NotNull
    @Override
    protected BootstrapButton newNavigationButton(@NotNull String id, @NotNull IconType icon, @NotNull SerializableSupplier<Boolean> isEnabled,
        @NotNull SerializableSupplier<Long> idSupplier) {
        final BootstrapButton btn = new BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                final Long id = idSupplier.get();
                if (id != null) {
                    final Optional<Paper> p = paperService.findById(id);
                    p.ifPresent(paper -> setResponsePage(getResponsePage(paper, searchOrderId, showingExclusions)));
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
        btn.add(new AttributeModifier(TITLE_ATTR, new StringResourceModel("button." + id + ".title", this, null).getString()));
        btn.setType(Buttons.Type.Primary);
        return btn;
    }

    @Nullable
    protected abstract GenericWebPage<Paper> getResponsePage(@NotNull final Paper p, @Nullable Long searchOrderId, boolean showingExclusions);

    @NotNull
    @Override
    protected BootstrapButton newExcludeButton(@NotNull String id) {
        BootstrapButton exclude = new BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                final Long paperId = EditablePaperPanel.this
                    .getModelObject()
                    .getId();
                if (paperId != null) {
                    if (showingExclusions)
                        paperService.reincludeIntoSearchOrder(searchOrderId, paperId);
                    else
                        paperService.excludeFromSearchOrder(searchOrderId, paperId);
                    getPaperIdManager().remove(paperId);
                    final Long idWithFocus = getPaperIdManager().getItemWithFocus();
                    if (idWithFocus != null) {
                        final Optional<Paper> p = paperService.findById(idWithFocus);
                        p.ifPresent(paper -> setResponsePage(getResponsePage(paper, searchOrderId, showingExclusions)));
                    } else {
                        restartSearchInPaperSearchPage();
                    }
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(searchOrderId != null && !isViewMode());
                if (showingExclusions) {
                    setIconType(GlyphIconType.okcircle);
                    add(new AttributeModifier(TITLE_ATTR, new StringResourceModel("button.exclude.title.reinclude", this, null).getString()));
                } else {
                    setIconType(GlyphIconType.bancircle);
                    add(new AttributeModifier(TITLE_ATTR, new StringResourceModel("button.exclude.title.exclude", this, null).getString()));
                }
            }
        };
        exclude.setDefaultFormProcessing(false);
        return exclude;
    }

    @Override
    protected void restartSearchInPaperSearchPage() {
        PageParameters pp = new PageParameters();
        pp.add(SEARCH_ORDER_ID.getName(), searchOrderId);
        pp.add(SHOW_EXCLUDED.getName(), showingExclusions);
        pp.add(MODE.getName(), getMode());
        pp.add(TAB_INDEX.getName(), getTabIndexModel().getObject());
        pageFactory
            .setResponsePageToPaperSearchPageConsumer(this)
            .accept(pp);
    }

    @NotNull
    @Override
    public DropZoneFileUpload newDropZoneFileUpload() {
        DropZoneFileUpload upload = new DropZoneFileUpload("dropzone") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpload(@NotNull AjaxRequestTarget target, @NotNull Map<String, List<FileItem>> fileMap) {
                if (fileMap.containsKey("file")) {
                    Paper p = null;
                    for (final FileItem file : fileMap.get("file")) {
                        try {
                            p = paperService.saveAttachment(convertToPaperAttachment(file));
                        } catch (Exception ex) {
                            log.error("Unexpected error when uploading file {}: {}", file.getName(), ex.getMessage());
                            error("Unexpected error: " + ex.getMessage());
                        }
                    }
                    if (p != null) {
                        EditablePaperPanel.this.setModelObject(p);
                        target.add(getAttachments());
                    }
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(isEditMode() && getModelObject().getId() != null);
            }
        };
        upload
            .getConfig()
            .withMaxFileSize(getMaxFileSize())
            .withThumbnailHeight(80)
            .withThumbnailWidth(80)
            .withPreviewsContainer(".dropzone-previews")
            .withParallelUploads(4);
        return upload;
    }

    private int getMaxFileSize() {
        final String prop = env.getProperty("spring.servlet.multipart.max-file-size");
        final String unit = "MB";
        try {
            if (prop != null && prop.length() > unit.length() && prop
                .substring(prop.length() - unit.length())
                .equalsIgnoreCase(unit))
                return Integer.parseInt(prop
                    .substring(0, prop.length() - unit.length())
                    .trim());
        } catch (Exception ex) {
            log.error("Unexpected exception when evaluating the max-file-size for file uploads ", ex);
        }
        return -1;
    }

    private PaperAttachment convertToPaperAttachment(final FileItem file) {
        final PaperAttachment pa = new PaperAttachment();
        pa.setPaperId(EditablePaperPanel.this
            .getModelObject()
            .getId());
        pa.setContent(file.get());
        pa.setContentType(file.getContentType());
        pa.setSize(file.getSize());
        pa.setName(sanitize(file.getName()));
        return pa;
    }

    private String sanitize(final String originalFileName) {
        return originalFileName.replace(" ", "_");
    }

    @NotNull
    @Override
    public DataTable<PaperAttachment, String> newAttachmentTable(@NotNull String id) {
        PropertyModel<List<PaperAttachment>> model = new PropertyModel<>(getModel(), ATTACHMENTS.getFieldName());
        PaperAttachmentProvider provider = new PaperAttachmentProvider(model);
        BootstrapDefaultDataTable<PaperAttachment, String> table = new BootstrapDefaultDataTable<>(id, makeTableColumns(), provider,
            ATTACHMENT_PAGE_SIZE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Item<PaperAttachment> newRowItem(@NotNull String id, int index, @NotNull IModel<PaperAttachment> model) {
                final PaperAttachment pa = model.getObject();
                final Item<PaperAttachment> item = super.newRowItem(id, index, model);
                item.add(AttributeModifier.replace(TITLE_ATTR, pa.getSizeKiloBytes() + " kB - " + pa.getContentType()));
                return item;
            }
        };
        table.setOutputMarkupId(true);
        return table;
    }

    private List<IColumn<PaperAttachment, String>> makeTableColumns() {
        final List<IColumn<PaperAttachment, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn(NAME, this::onTitleClick));
        columns.add(makeLinkIconColumn("removeAttachment"));
        return columns;
    }

    private void onTitleClick(IModel<PaperAttachment> m) {
        Integer id = m
            .getObject()
            .getId();
        PaperAttachment pa = paperService.loadAttachmentWithContentBy(id);
        if (pa != null) {
            ByteArrayResource r = new ByteArrayResource(pa.getContentType(), pa.getContent(), pa.getName());
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceRequestHandler(r, new PageParameters()));
        } else {
            log.warn("Unexpected condition with paperService unable to load attachment with content by id {}", id);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private ClickablePropertyColumn<PaperAttachment, String> makeClickableColumn(FieldEnumType propExpression,
        SerializableConsumer<IModel<PaperAttachment>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression.getFieldName(), this, null),
            propExpression.getFieldName(), consumer, null);
    }

    @SuppressWarnings("SameParameterValue")
    private IColumn<PaperAttachment, String> makeLinkIconColumn(String id) {
        final FontAwesome5IconType trash = FontAwesome5IconTypeBuilder
            .on(FontAwesome5IconTypeBuilder.FontAwesome5Solid.trash_alt)
            .fixedWidth()
            .build();
        return new LinkIconColumn<>(new StringResourceModel(COLUMN_HEADER + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(@NotNull IModel<PaperAttachment> rowModel) {
                return Model.of(trash.cssClassName() + " text-danger");
            }

            @Override
            public IModel<String> createTitleModel(@NotNull IModel<PaperAttachment> rowModel) {
                return new StringResourceModel("column.title.removeAttachment", EditablePaperPanel.this, null).setParameters(rowModel
                    .getObject()
                    .getName());
            }

            @Override
            protected void onClickPerformed(@NotNull AjaxRequestTarget target, @NotNull IModel<PaperAttachment> rowModel,
                @NotNull AjaxLink<Void> link) {
                final Integer id = rowModel
                    .getObject()
                    .getId();
                setModelObject(paperService.deleteAttachment(id));
                target.add(getAttachments());
            }
        };
    }

    @Override
    public boolean isAssociatedWithNewsletter() {
        return getModelObject().getNewsletterLink() != null;
    }

    @Override
    public boolean isAssociatedWithWipNewsletter() {
        final Paper.NewsletterLink nl = getModelObject().getNewsletterLink();
        return nl != null && PublicationStatus.Companion
            .byId(nl.getPublicationStatusId())
            .isInProgress();
    }

    @Override
    public void modifyNewsletterAssociation(@NotNull final AjaxRequestTarget target) {
        final Paper p = getModelObject();
        if (!isAssociatedWithNewsletter()) {
            if (p.getId() != null) {
                final Optional<Paper.NewsletterLink> nlo = paperService.mergePaperIntoWipNewsletter(p.getId(), null, sessionFacade.getLanguageCode());
                nlo.ifPresent(getModelObject()::setNewsletterLink);
            }
        } else if (isAssociatedWithWipNewsletter()) {
            final Paper.NewsletterLink nl = p.getNewsletterLink();
            paperService.removePaperFromNewsletter(nl.getNewsletterId(), p.getId());
            getModelObject().setNewsletterLink(null);
        }
        send(getPage(), Broadcast.BREADTH, new NewsletterChangeEvent(target));
    }

    @Override
    protected void considerAddingMoreValidation() {
        if (isEditMode()) {
            getForm().add(new TextFieldValueMustBeUniqueValidator("doi", getDoi()));
            getForm().add(new TextFieldValueMustBeUniqueValidator("pmId", getPmId()));
        }
    }

    class TextFieldValueMustBeUniqueValidator extends AbstractFormValidator {

        private static final long serialVersionUID = 1L;

        private final String             label;
        private final FormComponent<?>[] components;

        TextFieldValueMustBeUniqueValidator(@NotNull final String label, @NotNull final TextField<Object> field) {
            this.label = label;
            this.components = new FormComponent<?>[] { field };
        }

        @NotNull
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return components;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void validate(@NotNull final Form<?> form) {
            final TextField<String> field = (TextField<String>) components[0];
            final Object value = field.getConvertedInput();
            final Long id = EditablePaperPanel.this
                .getModelObject()
                .getId();
            final Optional<String> violatedPaperNumber = paperService.hasDuplicateFieldNextToCurrent(label, value, id);
            if (violatedPaperNumber.isPresent()) {
                final Map<String, Object> vars = Map.of("input", value, "numbers", violatedPaperNumber.get());
                error(field, resourceKey() + "." + label + "MustBeUnique", vars);
            }
        }
    }

    @Override
    public boolean isaNewsletterInStatusWip() {
        return !newsletterService.canCreateNewsletterInProgress();
    }

    @Override
    protected void doOnSubmit() {
        // no-op - handled via onFormSubmit
    }
}
