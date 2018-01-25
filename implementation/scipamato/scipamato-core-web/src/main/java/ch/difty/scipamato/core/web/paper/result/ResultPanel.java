package ch.difty.scipamato.core.web.paper.result;

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
import ch.difty.scipamato.core.ScipamatoSession;
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
    private static final String TITLE                = "title";
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
        columns.add(makePropertyColumn(Paper.ID));
        columns.add(makePropertyColumn(Paper.NUMBER));
        columns.add(makePropertyColumn(Paper.FIRST_AUTHOR));
        columns.add(makePropertyColumn(Paper.PUBL_YEAR));
        columns.add(makeClickableColumn(Paper.TITLE, this::onTitleClick));
        columns.add(makeLinkIconColumn("exclude"));
        return columns;
    }

    private void onTitleClick(IModel<PaperSlim> m) {
        ScipamatoSession.get()
            .getPaperIdManager()
            .setFocusToItem(m.getObject()
                .getId());
        String languageCode = ScipamatoSession.get()
            .getLocale()
            .getLanguage();
        setResponsePage(getResponsePage(m, languageCode, paperService, dataProvider));
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
            .populationLabel(getLabelResourceFor(Paper.POPULATION))
            .goalsLabel(getLabelResourceFor(Paper.GOALS))
            .methodsLabel(getLabelResourceFor(Paper.METHODS))
            .resultLabel(getLabelResourceFor(Paper.RESULT))
            .commentLabel(getLabelResourceFor(Paper.COMMENT))
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
            .goalsLabel(getLabelResourceFor(Paper.GOALS))
            .methodsLabel(getLabelResourceFor(Paper.METHODS))
            .methodOutcomeLabel(getLabelResourceFor(Paper.METHOD_OUTCOME))
            .resultMeasuredOutcomeLabel(getLabelResourceFor(Paper.RESULT_MEASURED_OUTCOME))
            .methodStudyDesignLabel(getLabelResourceFor(Paper.METHOD_STUDY_DESIGN))
            .populationPlaceLabel(getLabelResourceFor(Paper.POPULATION_PLACE))
            .populationParticipantsLabel(getLabelResourceFor(Paper.POPULATION_PARTICIPANTS))
            .populationDurationLabel(getLabelResourceFor(Paper.POPULATION_DURATION))
            .exposurePollutantLabel(getLabelResourceFor(Paper.EXPOSURE_POLLUTANT))
            .exposureAssessmentLabel(getLabelResourceFor(Paper.EXPOSURE_ASSESSMENT))
            .resultExposureRangeLabel(getLabelResourceFor(Paper.RESULT_EXPOSURE_RANGE))
            .methodStatisticsLabel(getLabelResourceFor(Paper.METHOD_STATISTICS))
            .methodConfoundersLabel(getLabelResourceFor(Paper.METHOD_CONFOUNDERS))
            .resultEffectEstimateLabel(getLabelResourceFor(Paper.RESULT_EFFECT_ESTIMATE))
            .commentLabel(getLabelResourceFor(Paper.COMMENT))
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
            .numberLabel(getLabelResourceFor(Paper.NUMBER))
            .authorYearLabel(getLabelResourceFor("authorYear"))
            .populationPlaceLabel(getShortLabelResourceFor(Paper.POPULATION_PLACE))
            .methodOutcomeLabel(getShortLabelResourceFor(Paper.METHOD_OUTCOME))
            .exposurePollutantLabel(getLabelResourceFor(Paper.EXPOSURE_POLLUTANT))
            .methodStudyDesignLabel(getShortLabelResourceFor(Paper.METHOD_STUDY_DESIGN))
            .populationDurationLabel(getShortLabelResourceFor(Paper.POPULATION_DURATION))
            .populationParticipantsLabel(getShortLabelResourceFor(Paper.POPULATION_PARTICIPANTS))
            .exposureAssessmentLabel(getShortLabelResourceFor(Paper.EXPOSURE_ASSESSMENT))
            .resultExposureRangeLabel(getShortLabelResourceFor(Paper.RESULT_EXPOSURE_RANGE))
            .methodConfoundersLabel(getLabelResourceFor(Paper.METHOD_CONFOUNDERS))
            .resultEffectEstimateLabel(getShortLabelResourceFor(Paper.RESULT_EFFECT_ESTIMATE))
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
            .numberLabel(getLabelResourceFor(Paper.NUMBER))
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
            .numberLabel(getLabelResourceFor(Paper.NUMBER))
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
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel(tileResourceKey, this, null).getString()));
        return reviewLink;
    }

}
