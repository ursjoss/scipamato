package ch.difty.scipamato.web.panel.result;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.ScipamatoSession;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.filter.PaperSlimFilter;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.persistence.PaperService;
import ch.difty.scipamato.web.component.SerializableConsumer;
import ch.difty.scipamato.web.component.data.LinkIconColumn;
import ch.difty.scipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;
import ch.difty.scipamato.web.jasper.ScipamatoPdfExporterConfiguration;
import ch.difty.scipamato.web.jasper.literaturereview.PaperLiteratureReviewDataSource;
import ch.difty.scipamato.web.jasper.review.PaperReviewDataSource;
import ch.difty.scipamato.web.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.web.jasper.summaryshort.PaperSummaryShortDataSource;
import ch.difty.scipamato.web.jasper.summarytable.PaperSummaryTableDataSource;
import ch.difty.scipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.scipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import ch.difty.scipamato.web.panel.BasePanel;
import ch.difty.scipamato.web.panel.search.SearchOrderChangeEvent;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

/**
 * The result panel shows the results of searches (by filter or by search order) which are provided
 * by the instantiating page through the data provider holding the filter specification.
 *
 * @author u.joss
 */
public class ResultPanel extends BasePanel<Void> {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER = "column.header.";
    private static final String TITLE = "title";

    @SpringBean
    private PaperService paperService;

    private final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider;

    private DataTable<PaperSlim, String> results;

    /**
     * Instantiate the panel.
     *
     * @param id the id of the panel
     * @param dataProvider the data provider extending {@link AbstractPaperSlimProvider} holding the filter specs
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
        results.add(new TableBehavior().striped().hover());
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
        ScipamatoSession.get().getPaperIdManager().setFocusToItem(m.getObject().getId());
        String languageCode = ScipamatoSession.get().getLocale().getLanguage();
        setResponsePage(new PaperEntryPage(Model.of(paperService.findByNumber(m.getObject().getNumber(), languageCode).orElse(new Paper())), getPage().getPageReference(),
                dataProvider.getSearchOrderId(), dataProvider.isShowExcluded()));
    }

    private PropertyColumn<PaperSlim, String> makePropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, propExpression);
    }

    private ClickablePropertyColumn<PaperSlim, String> makeClickableColumn(String propExpression, SerializableConsumer<IModel<PaperSlim>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, propExpression, consumer);
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
                return new StringResourceModel(dataProvider.isShowExcluded() ? "column.title.reinclude" : "column.title.exclude", ResultPanel.this, null);
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<PaperSlim> rowModel, AjaxLink<Void> link) {
                final Long excludedId = rowModel.getObject().getId();
                target.add(results);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).withExcludedPaperId(excludedId));
            }
        };
    }

    private void makeAndQueuePdfSummaryLink(String id) {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summary", this, null).getString();
        ReportHeaderFields rhf = ReportHeaderFields
            .builder(headerPart, brand)
            .populationLabel(new StringResourceModel("population" + LABEL_RECOURCE_TAG, this, null).getString())
            .goalsLabel(new StringResourceModel("goals" + LABEL_RECOURCE_TAG, this, null).getString())
            .methodsLabel(new StringResourceModel("methods" + LABEL_RECOURCE_TAG, this, null).getString())
            .resultLabel(new StringResourceModel("result" + LABEL_RECOURCE_TAG, this, null).getString())
            .commentLabel(new StringResourceModel("comment" + LABEL_RECOURCE_TAG, this, null).getString())
            .build();
        final String pdfTitle = brand + "- " + new StringResourceModel("paper_summary.titlePart", this, null).getString();
        ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfTitle).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> summaryLink = new ResourceLink<>(id, new PaperSummaryDataSource(dataProvider, rhf, config));
        summaryLink.setOutputMarkupId(true);
        summaryLink.setBody(new StringResourceModel("link.summary.label"));
        summaryLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.summary.title", this, null).getString()));
        queue(summaryLink);
    }

    private void makeAndQueuePdfSummaryShortLink(String id) {
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart.summaryShort", this, null).getString();
        ReportHeaderFields rhf = ReportHeaderFields
            .builder(headerPart, brand)
            .goalsLabel(new StringResourceModel(Paper.GOALS + LABEL_RECOURCE_TAG, this, null).getString())
            .methodsLabel(new StringResourceModel(Paper.METHODS + LABEL_RECOURCE_TAG, this, null).getString())
            .methodOutcomeLabel(new StringResourceModel(Paper.METHOD_OUTCOME + LABEL_RECOURCE_TAG, this, null).getString())
            .resultMeasuredOutcomeLabel(new StringResourceModel(Paper.RESULT_MEASURED_OUTCOME + LABEL_RECOURCE_TAG, this, null).getString())
            .methodStudyDesignLabel(new StringResourceModel(Paper.METHOD_STUDY_DESIGN + LABEL_RECOURCE_TAG, this, null).getString())
            .populationPlaceLabel(new StringResourceModel(Paper.POPULATION_PLACE + LABEL_RECOURCE_TAG, this, null).getString())
            .populationParticipantsLabel(new StringResourceModel(Paper.POPULATION_PARTICIPANTS + LABEL_RECOURCE_TAG, this, null).getString())
            .populationDurationLabel(new StringResourceModel(Paper.POPULATION_DURATION + LABEL_RECOURCE_TAG, this, null).getString())
            .exposurePollutantLabel(new StringResourceModel(Paper.EXPOSURE_POLLUTANT + LABEL_RECOURCE_TAG, this, null).getString())
            .exposureAssessmentLabel(new StringResourceModel(Paper.EXPOSURE_ASSESSMENT + LABEL_RECOURCE_TAG, this, null).getString())
            .resultExposureRangeLabel(new StringResourceModel(Paper.RESULT_EXPOSURE_RANGE + LABEL_RECOURCE_TAG, this, null).getString())
            .methodStatisticsLabel(new StringResourceModel(Paper.METHOD_STATISTICS + LABEL_RECOURCE_TAG, this, null).getString())
            .methodConfoundersLabel(new StringResourceModel(Paper.METHOD_CONFOUNDERS + LABEL_RECOURCE_TAG, this, null).getString())
            .resultEffectEstimateLabel(new StringResourceModel(Paper.RESULT_EFFECT_ESTIMATE + LABEL_RECOURCE_TAG, this, null).getString())
            .commentLabel(new StringResourceModel(Paper.COMMENT + LABEL_RECOURCE_TAG, this, null).getString())
            .build();
        String pdfTitle = brand + "- " + new StringResourceModel("paper_summary.titlePart", this, null).getString();
        ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfTitle).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> summaryShortLink = new ResourceLink<>(id, new PaperSummaryShortDataSource(dataProvider, rhf, config));
        summaryShortLink.setOutputMarkupId(true);
        summaryShortLink.setBody(new StringResourceModel("link.summary-short.label"));
        summaryShortLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.summary-short.title", this, null).getString()));
        queue(summaryShortLink);
    }

    private void makeAndQueuePdfReviewLink(String id) {
        final String brand = getProperties().getBrand();
        final String pdfTitle = brand + "- " + new StringResourceModel("paper_review.titlePart", this, null).getString();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfTitle).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();
        ReportHeaderFields rhf = ReportHeaderFields
            .builder("", brand)
            .numberLabel(new StringResourceModel("number" + LABEL_RECOURCE_TAG, this, null).getString())
            .authorYearLabel(new StringResourceModel("authorYear" + LABEL_RECOURCE_TAG, this, null).getString())
            .populationPlaceLabel(new StringResourceModel("populationPlace" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .methodOutcomeLabel(new StringResourceModel("methodOutcome" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .exposurePollutantLabel(new StringResourceModel("exposurePollutant" + LABEL_RECOURCE_TAG, this, null).getString())
            .methodStudyDesignLabel(new StringResourceModel("methodStudyDesign" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .populationDurationLabel(new StringResourceModel("populationDuration" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .populationParticipantsLabel(new StringResourceModel("populationParticipants" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .exposureAssessmentLabel(new StringResourceModel("exposureAssessment" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .resultExposureRangeLabel(new StringResourceModel("resultExposureRange" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .methodConfoundersLabel(new StringResourceModel("methodConfounders" + LABEL_RECOURCE_TAG, this, null).getString())
            .resultEffectEstimateLabel(new StringResourceModel("resultEffectEstimate" + SHORT_LABEL_RECOURCE_TAG, this, null).getString())
            .build();

        ResourceLink<Void> reviewLink = new ResourceLink<>(id, new PaperReviewDataSource(dataProvider, rhf, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.review.label"));
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.review.title", this, null).getString()));
        queue(reviewLink);
    }

    private void makeAndQueuePdfLiteratureReviewLink(String id) {
        final String brand = getProperties().getBrand();
        final String pdfCaption = new StringResourceModel("paper_literature_review.caption", this, null).setParameters(brand).getString();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();
        ReportHeaderFields rhf = ReportHeaderFields.builder("", brand).numberLabel(new StringResourceModel("number" + LABEL_RECOURCE_TAG, this, null).getString()).captionLabel(pdfCaption).build();
        ResourceLink<Void> reviewLink = new ResourceLink<>(id, new PaperLiteratureReviewDataSource(dataProvider, rhf, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.literature_review.label"));
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.literature_review.title", this, null).getString()));
        queue(reviewLink);
    }

    private void makeAndQueuePdfSummaryTableLink(String id) {
        queue(newPdfSummaryTable(id, true, "link.summary_table.label", "link.summary_table.title"));
    }

    private void makeAndQueuePdfSummaryTableWithoutResultsLink(String id) {
        queue(newPdfSummaryTable(id, false, "link.summary_table_wo_results.label", "link.summary_table_wo_results.title"));
    }

    private ResourceLink<Void> newPdfSummaryTable(String id, final boolean includeResults, final String bodyResourceKey, final String titleResourceKey) {
        final String pdfCaption = new StringResourceModel("paper_summary_table.titlePart", this, null).getString();
        final String brand = getProperties().getBrand();
        final ScipamatoPdfExporterConfiguration config = new ScipamatoPdfExporterConfiguration.Builder(pdfCaption).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();
        ReportHeaderFields rhf = ReportHeaderFields.builder("", brand).numberLabel(new StringResourceModel("number" + LABEL_RECOURCE_TAG, this, null).getString()).captionLabel(pdfCaption).build();

        ResourceLink<Void> reviewLink = new ResourceLink<>(id, new PaperSummaryTableDataSource(dataProvider, rhf, includeResults, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel(bodyResourceKey));
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel(titleResourceKey, this, null).getString()));
        return reviewLink;
    }

}
