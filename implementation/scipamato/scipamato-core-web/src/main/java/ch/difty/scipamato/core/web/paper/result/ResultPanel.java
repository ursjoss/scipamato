package ch.difty.scipamato.core.web.paper.result;

import static ch.difty.scipamato.core.entity.Paper.PaperFields.COMMENT;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.EXPOSURE_ASSESSMENT;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.EXPOSURE_POLLUTANT;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.FIRST_AUTHOR;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.GOALS;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.METHODS;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.METHOD_CONFOUNDERS;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.METHOD_OUTCOME;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.METHOD_STATISTICS;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.METHOD_STUDY_DESIGN;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.NUMBER;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.POPULATION;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.POPULATION_DURATION;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.POPULATION_PARTICIPANTS;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.POPULATION_PLACE;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.PUBL_YEAR;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.RESULT;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.RESULT_EFFECT_ESTIMATE;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.RESULT_EXPOSURE_RANGE;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.RESULT_MEASURED_OUTCOME;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.TITLE;

import java.util.ArrayList;
import java.util.List;

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

import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.common.web.component.table.column.LinkIconColumn;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.common.BasePanel;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent;
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfExporterConfiguration;
import ch.difty.scipamato.core.web.paper.jasper.literaturereview.PaperLiteratureReviewDataSource;
import ch.difty.scipamato.core.web.paper.jasper.review.PaperReviewDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summarytable.PaperSummaryTableDataSource;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

/**
 * The result panel shows the results of searches (by filter or by search order)
 * which are provided by the instantiating page through the data provider
 * holding the filter specification.
 *
 * @author u.joss
 */
public abstract class ResultPanel extends BasePanel<Void> {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER        = "column.header.";
    private static final String TITLE_ATTR           = "title";
    private static final String LINK_RESOURCE_PREFIX = "link.";

    @SpringBean
    private PaperService paperService;

    private final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider;

    private DataTable<PaperSlim, String> results;

    /**
     * Instantiate the panel.
     *
     * @param id
     *            the id of the panel
     * @param dataProvider
     *            the data provider extending {@link AbstractPaperSlimProvider}
     *            holding the filter specs
     */
    public ResultPanel(String id, AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider) {
        super(id);
        this.dataProvider = dataProvider;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueTable("table");
        makeAndQueuePdfSummaryLink("summaryLink");
        makeAndQueuePdfSummaryShortLink("summaryShortLink");
        makeAndQueuePdfReviewLink("reviewLink");
        makeAndQueuePdfLiteratureReviewLink("literatureReviewLink");
        makeAndQueuePdfSummaryTableLink("summaryTableLink");
        makeAndQueuePdfSummaryTableWithoutResultsLink("summaryTableWithoutResultsLink");
    }

    private void makeAndQueueTable(String id) {
        results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, dataProvider.getRowsPerPage());
        results.setOutputMarkupId(true);
        results.add(new TableBehavior().striped()
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
        columns.add(makeLinkIconColumn("exclude"));
        return columns;
    }

    private void onTitleClick(IModel<PaperSlim> m) {
        getPaperIdManager().setFocusToItem(m.getObject()
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

    private IColumn<PaperSlim, String> makeLinkIconColumn(String id) {
        return new LinkIconColumn<PaperSlim>(new StringResourceModel(COLUMN_HEADER + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<PaperSlim> rowModel) {
                return Model.of(dataProvider.isShowExcluded() ? "fa fa-fw fa-check-circle-o" : "fa fa-fw fa-ban");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<PaperSlim> rowModel) {
                return new StringResourceModel(
                        dataProvider.isShowExcluded() ? "column.title.reinclude" : "column.title.exclude",
                        ResultPanel.this, null);
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<PaperSlim> rowModel, AjaxLink<Void> link) {
                final Long excludedId = rowModel.getObject()
                    .getId();
                target.add(results);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).withExcludedPaperId(excludedId));
            }
        };
    }

    private void makeAndQueuePdfSummaryLink(String id) {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summary", this, null).getString();
        final String pdfCaption = brand + "- "
                + new StringResourceModel("paper_summary.titlePart", this, null).getString();
        final ReportHeaderFields rhf = ReportHeaderFields.builder(headerPart, brand)
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

        queue(newResourceLink(id, "summary", new PaperSummaryDataSource(dataProvider, rhf, config)));
    }

    private void makeAndQueuePdfSummaryShortLink(String id) {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-"
                + new StringResourceModel("headerPart.summaryShort", this, null).getString();
        final String pdfCaption = brand + "- "
                + new StringResourceModel("paper_summary.titlePart", this, null).getString();
        final ReportHeaderFields rhf = ReportHeaderFields.builder(headerPart, brand)
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
        final String pdfCaption = brand + "- "
                + new StringResourceModel("paper_review.titlePart", this, null).getString();
        final ReportHeaderFields rhf = ReportHeaderFields.builder("", brand)
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
            .build();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(getActiveUser())
            .withCreator(brand)
            .withCompression()
            .build();

        queue(newResourceLink(id, "review", new PaperReviewDataSource(dataProvider, rhf, config)));
    }

    private void makeAndQueuePdfLiteratureReviewLink(final String id) {
        final String brand = getProperties().getBrand();
        final String pdfCaption = new StringResourceModel("paper_literature_review.caption", this, null)
            .setParameters(brand)
            .getString();
        final String url = getProperties().getPubmedBaseUrl();
        final ReportHeaderFields rhf = ReportHeaderFields.builder("", brand)
            .numberLabel(getLabelResourceFor(NUMBER.getName()))
            .captionLabel(pdfCaption)
            .pubmedBaseUrl(url)
            .build();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption)
            .withAuthor(getActiveUser())
            .withCreator(brand)
            .withCompression()
            .build();

        queue(newResourceLink(id, "literature_review", new PaperLiteratureReviewDataSource(dataProvider, rhf, config)));
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
        final ReportHeaderFields rhf = ReportHeaderFields.builder("", brand)
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
        reviewLink
            .add(new AttributeModifier(TITLE_ATTR, new StringResourceModel(tileResourceKey, this, null).getString()));
        return reviewLink;
    }

}
