package ch.difty.sipamato.web.panel.result;

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

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.data.LinkIconColumn;
import ch.difty.sipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.sipamato.web.jasper.SipamatoPdfExporterConfiguration;
import ch.difty.sipamato.web.jasper.review.PaperReviewDataSource;
import ch.difty.sipamato.web.jasper.summary_sp.PaperSummaryDataSource;
import ch.difty.sipamato.web.jasper.summary_table.PaperSummaryTableDataSource;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.AbstractPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderChangeEvent;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

/**
 * The result panel shows the results of searches (by filter or by search order) which are provided
 * by the instantiating page through the data provider holding the filter specification.
 *
 * @author u.joss
 */
public class ResultPanel extends AbstractPanel<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperService paperService;

    private final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider;

    private DataTable<PaperSlim, String> results;

    /**
     * Instantiate the panel.
     *
     * @param id the id of the panel
     * @param dataProvider the data provider extending {@link SortablePaperSlimProvider} holding the filter specs
     */
    public ResultPanel(String id, SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider) {
        super(id);
        this.dataProvider = dataProvider;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueTable("table");
        makeAndQueuePdfSummaryLink("summaryLink");
        makeAndQueuePdfReviewLink("reviewLink");
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
        columns.add(makePropertyColumn(Paper.ID, Paper.FLD_ID));
        columns.add(makePropertyColumn(Paper.FIRST_AUTHOR, Paper.FLD_FIRST_AUTHOR));
        columns.add(makePropertyColumn(Paper.PUBL_YEAR, Paper.FLD_PUBL_YEAR));
        columns.add(makeClickableColumn(Paper.TITLE, Paper.FLD_TITLE,
                (IModel<PaperSlim> m) -> setResponsePage(new PaperEntryPage(Model.of(paperService.findById(m.getObject().getId()).orElse(new Paper()))))));
        columns.add(makeLinkIconColumn("exclude"));
        return columns;
    }

    private PropertyColumn<PaperSlim, String> makePropertyColumn(String propExpression, String sortProperty) {
        return new PropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression);
    }

    private ClickablePropertyColumn<PaperSlim, String> makeClickableColumn(String propExpression, String sortProperty, SerializableConsumer<IModel<PaperSlim>> consumer) {
        return new ClickablePropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression, consumer);
    }

    private IColumn<PaperSlim, String> makeLinkIconColumn(String id) {
        return new LinkIconColumn<PaperSlim>(new StringResourceModel("column.header." + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<PaperSlim> rowModel) {
                return Model.of("fa fa-fw fa-trash-o text-danger");
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<PaperSlim> rowModel, AjaxLink<Void> link) {
                final Long excludedId = rowModel.getObject().getId();
                info("Excluded " + rowModel.getObject().getDisplayValue());
                target.add(results);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).withExcludedPaperId(excludedId));
            }
        };
    }

    private void makeAndQueuePdfSummaryLink(String id) {
        String populationLabel = new StringResourceModel("population" + LABEL_RECOURCE_TAG, this, null).getString();
        String methodsLabel = new StringResourceModel("methods" + LABEL_RECOURCE_TAG, this, null).getString();
        String resultLabel = new StringResourceModel("result" + LABEL_RECOURCE_TAG, this, null).getString();
        String commentLabel = new StringResourceModel("comment" + LABEL_RECOURCE_TAG, this, null).getString();
        String brand = getProperties().getBrand();
        String headerPart = brand + "-" + new StringResourceModel("headerPart", this, null).getString();

        String pdfTitle = brand + "- " + new StringResourceModel("paper_summary.titlePart", this, null).getString();
        SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfTitle).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();
        ResourceLink<Void> summaryLink = new ResourceLink<Void>(id,
                new PaperSummaryDataSource(dataProvider, paperService, populationLabel, methodsLabel, resultLabel, commentLabel, headerPart, brand, config));
        summaryLink.setOutputMarkupId(true);
        summaryLink.setBody(new StringResourceModel("link.summary.label"));
        summaryLink.add(new AttributeModifier("title", new StringResourceModel("link.summary.title", this, null).getString()));
        queue(summaryLink);
    }

    private void makeAndQueuePdfReviewLink(String id) {
        final String idLabel = new StringResourceModel("id" + LABEL_RECOURCE_TAG, this, null).getString();
        final String authorYearLabel = new StringResourceModel("authorYear" + LABEL_RECOURCE_TAG, this, null).getString();
        final String populationPlaceLabel = new StringResourceModel("populationPlace" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String methodOutcomeLabel = new StringResourceModel("methodOutcome" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String exposurePollutantLabel = new StringResourceModel("exposurePollutant" + LABEL_RECOURCE_TAG, this, null).getString();
        final String methodStudyDesignLabel = new StringResourceModel("methodStudyDesign" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String populationDurationLabel = new StringResourceModel("populationDuration" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String methodStatisticsLabel = new StringResourceModel("methodStatistics" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String populationParticipantsLabel = new StringResourceModel("populationParticipants" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String exposureAssessmentLabel = new StringResourceModel("exposureAssessment" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String resultExposureRangeLabel = new StringResourceModel("resultExposureRange" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String methodConfoundersLabel = new StringResourceModel("methodConfounders" + LABEL_RECOURCE_TAG, this, null).getString();
        final String resultEffectEstimateLabel = new StringResourceModel("resultEffectEstimate" + HORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String brand = getProperties().getBrand();
        final String createdBy = getActiveUser().getFullName();

        final String pdfTitle = brand + "- " + new StringResourceModel("paper_review.titlePart", this, null).getString();
        final SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfTitle).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> reviewLink = new ResourceLink<Void>(id,
                new PaperReviewDataSource(dataProvider, paperService, idLabel, authorYearLabel, populationPlaceLabel, methodOutcomeLabel, exposurePollutantLabel, methodStudyDesignLabel,
                        populationDurationLabel, methodStatisticsLabel, populationParticipantsLabel, exposureAssessmentLabel, resultExposureRangeLabel, methodConfoundersLabel,
                        resultEffectEstimateLabel, brand, createdBy, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.review.label"));
        reviewLink.add(new AttributeModifier("title", new StringResourceModel("link.review.title", this, null).getString()));
        queue(reviewLink);
    }

    private void makeAndQueuePdfSummaryTableLink(String id) {
        final String pdfCaption = new StringResourceModel("paper_summary_table.titlePart", this, null).getString();
        final String brand = getProperties().getBrand();
        final SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfCaption).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> reviewLink = new ResourceLink<Void>(id, new PaperSummaryTableDataSource(dataProvider, paperService, true, pdfCaption, brand, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.summary_table.label"));
        reviewLink.add(new AttributeModifier("title", new StringResourceModel("link.summary_table.title", this, null).getString()));
        queue(reviewLink);
    }

    private void makeAndQueuePdfSummaryTableWithoutResultsLink(String id) {
        final String pdfCaption = new StringResourceModel("paper_summary_table.titlePart", this, null).getString();
        final String brand = getProperties().getBrand();
        final SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfCaption).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> reviewLink = new ResourceLink<Void>(id, new PaperSummaryTableDataSource(dataProvider, paperService, false, pdfCaption, brand, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.summary_table_wo_results.label"));
        reviewLink.add(new AttributeModifier("title", new StringResourceModel("link.summary_table_wo_results.title", this, null).getString()));
        queue(reviewLink);
    }

}
