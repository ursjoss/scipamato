package ch.difty.scipamato.core.web.paper.result;

import static ch.difty.scipamato.core.entity.Paper.PaperFields.*;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.common.web.component.table.column.LinkIconColumn;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.NewsletterService;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.common.BasePanel;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent;
import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent;
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfExporterConfiguration;
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator;
import ch.difty.scipamato.core.web.paper.jasper.literaturereview.PaperLiteratureReviewDataSource;
import ch.difty.scipamato.core.web.paper.jasper.literaturereview.PaperLiteratureReviewPlusDataSource;
import ch.difty.scipamato.core.web.paper.jasper.review.PaperReviewDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summarytable.PaperSummaryTableDataSource;

/**
 * The result panel shows the results of searches (by filter or by search order)
 * which are provided by the instantiating page through the data provider
 * holding the filter specification.
 *
 * @author u.joss
 */
@SuppressWarnings("SameParameterValue")
public abstract class ResultPanel extends BasePanel<Void> {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER        = "column.header.";
    private static final String TITLE_ATTR           = "title";
    private static final String LINK_RESOURCE_PREFIX = "link.";

    @SpringBean
    private PaperService paperService;

    @SpringBean
    private NewsletterService newsletterService;

    @SpringBean
    private CoreShortFieldConcatenator shortFieldConcatenator;

    private final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider;

    private DataTable<PaperSlim, String> results;

    private final Mode mode;

    /**
     * Instantiate the panel.
     *
     * @param id
     *     the id of the panel
     * @param dataProvider
     *     the data provider extending {@link AbstractPaperSlimProvider}
     *     holding the filter specs
     */
    protected ResultPanel(String id, AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider, Mode mode) {
        super(id);
        this.dataProvider = dataProvider;
        this.mode = mode;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueTable("table");
        makeAndQueuePdfSummaryLink("summaryLink");
        makeAndQueuePdfSummaryShortLink("summaryShortLink");
        makeAndQueuePdfReviewLink("reviewLink");
        makeAndQueuePdfLiteratureReviewLink("literatureReviewLink", false);
        makeAndQueuePdfLiteratureReviewLink("literatureReviewPlusLink", true);
        makeAndQueuePdfSummaryTableLink("summaryTableLink");
        makeAndQueuePdfSummaryTableWithoutResultsLink("summaryTableWithoutResultsLink");
    }

    private void makeAndQueueTable(String id) {
        results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, dataProvider.getRowsPerPage()) {
            @Override
            protected void onAfterRender() {
                super.onAfterRender();
                getPaperIdManager().initialize(dataProvider.findAllPaperIdsByFilter());
            }
        };
        results.setOutputMarkupId(true);
        results.add(new TableBehavior()
            .striped()
            .hover());
        queue(results);
    }

    private List<IColumn<PaperSlim, String>> makeTableColumns() {
        final List<IColumn<PaperSlim, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(Paper.IdScipamatoEntityFields.ID.getName()));
        columns.add(makePropertyColumn(NUMBER.getName()));
        columns.add(makePropertyColumn(FIRST_AUTHOR.getName()));
        columns.add(makePropertyColumn(PUBL_YEAR.getName()));
        columns.add(makeClickableColumn(TITLE.getName(), this::onTitleClick));
        if (mode != Mode.VIEW && isOfferingSearchComposition())
            columns.add(makeExcludeLinkIconColumn("exclude"));
        if (mode != Mode.VIEW)
            columns.add(makeNewsletterLinkIconColumn("newsletter"));
        return columns;
    }

    /**
     * Determines if the result panel is embedded into a page that offers composing complex searches.
     * If so, the table offers an icon column to exclude papers from searches. Otherwise it will not.
     *
     * @return whether search composition is to be offered or not
     */
    protected abstract boolean isOfferingSearchComposition();

    private void onTitleClick(IModel<PaperSlim> m) {
        getPaperIdManager().setFocusToItem(m
            .getObject()
            .getId());
        setResponsePage(getResponsePage(m, getLocalization(), paperService, dataProvider));
    }

    protected abstract GenericWebPage<Paper> getResponsePage(IModel<PaperSlim> m, String languageCode,
        PaperService paperService, AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider);

    private PropertyColumn<PaperSlim, String> makePropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression);
    }

    private ClickablePropertyColumn<PaperSlim, String> makeClickableColumn(String propExpression,
        SerializableConsumer<IModel<PaperSlim>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
            propExpression, propExpression, consumer);
    }

    private IColumn<PaperSlim, String> makeExcludeLinkIconColumn(String id) {
        return new LinkIconColumn<>(new StringResourceModel(COLUMN_HEADER + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<PaperSlim> rowModel) {
                return Model.of(dataProvider.isShowExcluded() ? "fa fa-fw fa-check-circle-o" : "fa fa-fw fa-ban");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<PaperSlim> rowModel) {
                return new StringResourceModel(
                    dataProvider.isShowExcluded() ? "column.title.reinclude" : "column.title.exclude", ResultPanel.this,
                    null);
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<PaperSlim> rowModel, AjaxLink<Void> link) {
                final Long excludedId = rowModel
                    .getObject()
                    .getId();
                target.add(results);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).withExcludedPaperId(excludedId));
            }
        };
    }

    /**
     * Icon indicating whether the paper
     * <ul>
     * <li>can be added to the current newsletter</li>
     * <li>has been added to the current newsletter</li>
     * <li>had been added previously to a newsletter that had been closed already</li>
     * </ul>
     * and providing the option of changing the association between paper and newsletter:
     * <ul>
     * <li>If the paper had been added to a newsletter, the association can only be removed if
     * the newsletter in question is still in status work in progress. </li>
     * <li>>Otherwise the association is read only.</li
     * </ul>
     */
    private IColumn<PaperSlim, String> makeNewsletterLinkIconColumn(String id) {
        return new LinkIconColumn<>(new StringResourceModel(COLUMN_HEADER + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(final IModel<PaperSlim> rowModel) {
                String icon;
                final PaperSlim paper = rowModel.getObject();
                if (hasNoNewsletter(paper))
                    icon = isThereOneNewsletterInStatusWip() ? "fa fa-fw fa-plus-square-o" : "";
                else if (hasNewsletterWip(paper))
                    icon = "fa fa-fw fa-envelope-open-o";
                else
                    icon = "fa fa-fw fa-envelope-o";
                return Model.of(icon);
            }

            private boolean hasNoNewsletter(final PaperSlim paper) {
                return paper.getNewsletterAssociation() == null;
            }

            private boolean isThereOneNewsletterInStatusWip() {
                return !newsletterService.canCreateNewsletterInProgress();
            }

            private boolean hasNewsletterWip(final PaperSlim paper) {
                return PublicationStatus
                    .byId(paper
                        .getNewsletterAssociation()
                        .getPublicationStatusId())
                    .isInProgress();
            }

            @Override
            protected IModel<String> createTitleModel(final IModel<PaperSlim> rowModel) {
                final PaperSlim paper = rowModel.getObject();
                if (hasNoNewsletter(paper)) {
                    if (isThereOneNewsletterInStatusWip())
                        return new StringResourceModel("column.title.newsletter.add", ResultPanel.this, null);
                    else
                        return Model.of("");
                } else if (hasNewsletterWip(paper)) {
                    return new StringResourceModel("column.title.newsletter.remove", ResultPanel.this, null);
                } else {
                    return new StringResourceModel("column.title.newsletter.closed", ResultPanel.this,
                        Model.of(paper.getNewsletterAssociation()));
                }
            }

            @Override
            protected void onClickPerformed(final AjaxRequestTarget target, final IModel<PaperSlim> rowModel,
                final AjaxLink<Void> link) {
                final PaperSlim paper = rowModel.getObject();

                if (hasNoNewsletter(paper)) {
                    if (isThereOneNewsletterInStatusWip())
                        newsletterService.mergePaperIntoWipNewsletter(paper.getId());
                    else
                        warn(new StringResourceModel("newsletter.noneInProgress", ResultPanel.this, null).getString());
                } else if (hasNewsletterWip(paper)) {
                    newsletterService.removePaperFromWipNewsletter(paper.getId());
                } else {
                    warn(new StringResourceModel("newsletter.readonly", ResultPanel.this,
                        Model.of(paper.getNewsletterAssociation())).getString());
                }

                target.add(results);
                send(getPage(), Broadcast.BREADTH, new NewsletterChangeEvent(target));
            }
        };
    }

    private void makeAndQueuePdfSummaryLink(String id) {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summary", this, null).getString();
        final String pdfCaption =
            brand + "- " + new StringResourceModel("paper_summary.titlePart", this, null).getString();
        final ReportHeaderFields rhf = ReportHeaderFields
            .builder(headerPart, brand)
            .populationLabel(getLabelResourceFor(POPULATION.getName()))
            .goalsLabel(getLabelResourceFor(GOALS.getName()))
            .methodsLabel(getLabelResourceFor(METHODS.getName()))
            .resultLabel(getLabelResourceFor(RESULT.getName()))
            .commentLabel(getLabelResourceFor(COMMENT.getName()))
            .build();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(getActiveUser())
            .withCreator(brand)
            .withCompression()
            .build();

        queue(newResourceLink(id, "summary",
            new PaperSummaryDataSource(dataProvider, rhf, shortFieldConcatenator, config)));
    }

    private void makeAndQueuePdfSummaryShortLink(String id) {
        final String brand = getProperties().getBrand();
        final String headerPart =
            brand + "-" + new StringResourceModel("headerPart.summaryShort", this, null).getString();
        final String pdfCaption =
            brand + "- " + new StringResourceModel("paper_summary.titlePart", this, null).getString();
        final ReportHeaderFields rhf = ReportHeaderFields
            .builder(headerPart, brand)
            .goalsLabel(getLabelResourceFor(GOALS.getName()))
            .methodsLabel(getLabelResourceFor(METHODS.getName()))
            .methodOutcomeLabel(getLabelResourceFor(METHOD_OUTCOME.getName()))
            .resultMeasuredOutcomeLabel(getLabelResourceFor(RESULT_MEASURED_OUTCOME.getName()))
            .methodStudyDesignLabel(getLabelResourceFor(METHOD_STUDY_DESIGN.getName()))
            .populationPlaceLabel(getLabelResourceFor(POPULATION_PLACE.getName()))
            .populationParticipantsLabel(getLabelResourceFor(POPULATION_PARTICIPANTS.getName()))
            .populationDurationLabel(getLabelResourceFor(POPULATION_DURATION.getName()))
            .exposurePollutantLabel(getLabelResourceFor(EXPOSURE_POLLUTANT.getName()))
            .exposureAssessmentLabel(getLabelResourceFor(EXPOSURE_ASSESSMENT.getName()))
            .resultExposureRangeLabel(getLabelResourceFor(RESULT_EXPOSURE_RANGE.getName()))
            .methodStatisticsLabel(getLabelResourceFor(METHOD_STATISTICS.getName()))
            .methodConfoundersLabel(getLabelResourceFor(METHOD_CONFOUNDERS.getName()))
            .resultEffectEstimateLabel(getLabelResourceFor(RESULT_EFFECT_ESTIMATE.getName()))
            .conclusionLabel(getLabelResourceFor(CONCLUSION.getName()))
            .commentLabel(getLabelResourceFor(COMMENT.getName()))
            .build();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(getActiveUser())
            .withCreator(brand)
            .withCompression()
            .build();

        queue(newResourceLink(id, "summary-short", new PaperSummaryShortDataSource(dataProvider, rhf, config)));
    }

    private void makeAndQueuePdfReviewLink(String id) {
        final String brand = getProperties().getBrand();
        final String pdfCaption =
            brand + "- " + new StringResourceModel("paper_review.titlePart", this, null).getString();
        final ReportHeaderFields rhf = ReportHeaderFields
            .builder("", brand)
            .numberLabel(getLabelResourceFor(NUMBER.getName()))
            .authorYearLabel(getLabelResourceFor("authorYear"))
            .populationPlaceLabel(getShortLabelResourceFor(POPULATION_PLACE.getName()))
            .methodOutcomeLabel(getShortLabelResourceFor(METHOD_OUTCOME.getName()))
            .exposurePollutantLabel(getLabelResourceFor(EXPOSURE_POLLUTANT.getName()))
            .methodStudyDesignLabel(getShortLabelResourceFor(METHOD_STUDY_DESIGN.getName()))
            .populationDurationLabel(getShortLabelResourceFor(POPULATION_DURATION.getName()))
            .populationParticipantsLabel(getShortLabelResourceFor(POPULATION_PARTICIPANTS.getName()))
            .exposureAssessmentLabel(getShortLabelResourceFor(EXPOSURE_ASSESSMENT.getName()))
            .resultExposureRangeLabel(getShortLabelResourceFor(RESULT_EXPOSURE_RANGE.getName()))
            .methodConfoundersLabel(getLabelResourceFor(METHOD_CONFOUNDERS.getName()))
            .resultEffectEstimateLabel(getShortLabelResourceFor(RESULT_EFFECT_ESTIMATE.getName()))
            .conclusionLabel(getShortLabelResourceFor(CONCLUSION.getName()))
            .build();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(getActiveUser())
            .withCreator(brand)
            .withCompression()
            .build();

        queue(newResourceLink(id, "review", new PaperReviewDataSource(dataProvider, rhf, config)));
    }

    private void makeAndQueuePdfLiteratureReviewLink(final String id, final boolean plus) {
        final String brand = getProperties().getBrand();
        final String pdfCaption = new StringResourceModel("paper_literature_review.caption", this, null)
            .setParameters(brand)
            .getString();
        final String url = getProperties().getPubmedBaseUrl();
        final ReportHeaderFields rhf = ReportHeaderFields
            .builder("", brand)
            .numberLabel(getLabelResourceFor(NUMBER.getName()))
            .captionLabel(pdfCaption)
            .pubmedBaseUrl(url)
            .build();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(getActiveUser())
            .withCreator(brand)
            .withCompression()
            .build();

        if (plus) {
            queue(newResourceLink(id, "literature_review_plus",
                new PaperLiteratureReviewPlusDataSource(dataProvider, rhf, config)));
        } else {
            queue(newResourceLink(id, "literature_review",
                new PaperLiteratureReviewDataSource(dataProvider, rhf, config)));
        }
    }

    private void makeAndQueuePdfSummaryTableLink(final String id) {
        queue(newPdfSummaryTable(id, true, "summary_table"));
    }

    private void makeAndQueuePdfSummaryTableWithoutResultsLink(final String id) {
        queue(newPdfSummaryTable(id, false, "summary_table_wo_results"));
    }

    private ResourceLink<Void> newPdfSummaryTable(final String id, final boolean includeResults,
        final String resourceKeyPart) {
        final String pdfCaption = new StringResourceModel("paper_summary_table.titlePart", this, null).getString();
        final String brand = getProperties().getBrand();
        final ReportHeaderFields rhf = ReportHeaderFields
            .builder("", brand)
            .numberLabel(getLabelResourceFor(NUMBER.getName()))
            .captionLabel(pdfCaption)
            .build();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(getActiveUser())
            .withCreator(brand)
            .withCompression()
            .build();
        return newResourceLink(id, resourceKeyPart,
            new PaperSummaryTableDataSource(dataProvider, rhf, includeResults, config));
    }

    private ResourceLink<Void> newResourceLink(String id, final String resourceKeyPart,
        final JasperPaperDataSource<?> resource) {
        final String bodyResourceKey = LINK_RESOURCE_PREFIX + resourceKeyPart + LABEL_RESOURCE_TAG;
        final String tileResourceKey = LINK_RESOURCE_PREFIX + resourceKeyPart + TITLE_RESOURCE_TAG;

        ResourceLink<Void> reviewLink = new ResourceLink<>(id, resource);
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel(bodyResourceKey));
        reviewLink.add(
            new AttributeModifier(TITLE_ATTR, new StringResourceModel(tileResourceKey, this, null).getString()));
        return reviewLink;
    }

}
