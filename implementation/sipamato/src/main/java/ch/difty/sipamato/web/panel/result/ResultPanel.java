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

import ch.difty.sipamato.SipamatoSession;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.data.LinkIconColumn;
import ch.difty.sipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.sipamato.web.jasper.SipamatoPdfExporterConfiguration;
import ch.difty.sipamato.web.jasper.literaturereview.PaperLiteratureReviewDataSource;
import ch.difty.sipamato.web.jasper.review.PaperReviewDataSource;
import ch.difty.sipamato.web.jasper.summary.PaperSummaryDataSource;
import ch.difty.sipamato.web.jasper.summarytable.PaperSummaryTableDataSource;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
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
        SipamatoSession.get().getPaperIdManager().setFocusToItem(m.getObject().getId());
        setResponsePage(new PaperEntryPage(Model.of(paperService.findByNumber(m.getObject().getNumber()).orElse(new Paper()))));
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
                return Model.of("fa fa-fw fa-ban");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<PaperSlim> rowModel) {
                return new StringResourceModel("column.title.exclude", ResultPanel.this, null);
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
        ResourceLink<Void> summaryLink = new ResourceLink<>(id, new PaperSummaryDataSource(dataProvider, populationLabel, methodsLabel, resultLabel, commentLabel, headerPart, brand, config));
        summaryLink.setOutputMarkupId(true);
        summaryLink.setBody(new StringResourceModel("link.summary.label"));
        summaryLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.summary.title", this, null).getString()));
        queue(summaryLink);
    }

    private void makeAndQueuePdfReviewLink(String id) {
        final String numberLabel = new StringResourceModel("number" + LABEL_RECOURCE_TAG, this, null).getString();
        final String authorYearLabel = new StringResourceModel("authorYear" + LABEL_RECOURCE_TAG, this, null).getString();
        final String populationPlaceLabel = new StringResourceModel("populationPlace" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String methodOutcomeLabel = new StringResourceModel("methodOutcome" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String exposurePollutantLabel = new StringResourceModel("exposurePollutant" + LABEL_RECOURCE_TAG, this, null).getString();
        final String methodStudyDesignLabel = new StringResourceModel("methodStudyDesign" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String populationDurationLabel = new StringResourceModel("populationDuration" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String populationParticipantsLabel = new StringResourceModel("populationParticipants" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String exposureAssessmentLabel = new StringResourceModel("exposureAssessment" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String resultExposureRangeLabel = new StringResourceModel("resultExposureRange" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String methodConfoundersLabel = new StringResourceModel("methodConfounders" + LABEL_RECOURCE_TAG, this, null).getString();
        final String resultEffectEstimateLabel = new StringResourceModel("resultEffectEstimate" + SHORT_LABEL_RECOURCE_TAG, this, null).getString();
        final String brand = getProperties().getBrand();
        final String createdBy = getActiveUser().getFullName();

        final String pdfTitle = brand + "- " + new StringResourceModel("paper_review.titlePart", this, null).getString();
        final SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfTitle).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> reviewLink = new ResourceLink<>(id,
                new PaperReviewDataSource(dataProvider, numberLabel, authorYearLabel, populationPlaceLabel, methodOutcomeLabel, exposurePollutantLabel, methodStudyDesignLabel, populationDurationLabel,
                        populationParticipantsLabel, exposureAssessmentLabel, resultExposureRangeLabel, methodConfoundersLabel, resultEffectEstimateLabel, brand, createdBy, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.review.label"));
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.review.title", this, null).getString()));
        queue(reviewLink);
    }

    private void makeAndQueuePdfLiteratureReviewLink(String id) {
        final String brand = getProperties().getBrand();
        final String numberLabel = new StringResourceModel("number" + LABEL_RECOURCE_TAG, this, null).getString();
        final String pdfCaption = new StringResourceModel("paper_literature_review.caption", this, null).setParameters(brand).getString();
        final SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfCaption).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> reviewLink = new ResourceLink<>(id, new PaperLiteratureReviewDataSource(dataProvider, pdfCaption, brand, numberLabel, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.literature_review.label"));
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.literature_review.title", this, null).getString()));
        queue(reviewLink);
    }

    private void makeAndQueuePdfSummaryTableLink(String id) {
        final String pdfCaption = new StringResourceModel("paper_summary_table.titlePart", this, null).getString();
        final String brand = getProperties().getBrand();
        final String numberLabel = new StringResourceModel("number" + LABEL_RECOURCE_TAG, this, null).getString();
        final SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfCaption).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> reviewLink = new ResourceLink<>(id, new PaperSummaryTableDataSource(dataProvider, true, pdfCaption, brand, numberLabel, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.summary_table.label"));
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.summary_table.title", this, null).getString()));
        queue(reviewLink);
    }

    private void makeAndQueuePdfSummaryTableWithoutResultsLink(String id) {
        final String pdfCaption = new StringResourceModel("paper_summary_table.titlePart", this, null).getString();
        final String brand = getProperties().getBrand();
        final String numberLabel = new StringResourceModel("number" + LABEL_RECOURCE_TAG, this, null).getString();
        final SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(pdfCaption).withAuthor(getActiveUser()).withCreator(brand).withCompression().build();

        ResourceLink<Void> reviewLink = new ResourceLink<>(id, new PaperSummaryTableDataSource(dataProvider, false, pdfCaption, brand, numberLabel, config));
        reviewLink.setOutputMarkupId(true);
        reviewLink.setBody(new StringResourceModel("link.summary_table_wo_results.label"));
        reviewLink.add(new AttributeModifier(TITLE, new StringResourceModel("link.summary_table_wo_results.title", this, null).getString()));
        queue(reviewLink);
    }

}
