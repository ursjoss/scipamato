package ch.difty.sipamato.web.jasper.summary;

import java.util.Arrays;
import java.util.Collection;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.jasper.JasperPaperDataSource;
import ch.difty.sipamato.web.jasper.SipamatoPdfResourceHandler;
import ch.difty.sipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import ch.difty.sipamato.web.resources.jasper.PaperSummaryReportResourceReference;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * DataSource for the PaperSummaryReport.
 *
 * Can be instantiated in different ways, either by passing in
 *
 * <ul>
 * <li> a single {@link Paper} + meta fields</li>
 * <li> a single {@link PaperSummary}</li>
 * <li> a collection of {@link PaperSummary} entities or</li>
 * <li> an instance of a {@link AbstractPaperSlimProvider} + meta fields</li>
 * </ul>
 *
 * The meta fields are not contained within a paper instance and make up e.g. localized labels, the brand or part of the header.
 *
 * @author u.joss
 */
public class PaperSummaryDataSource extends JasperPaperDataSource<PaperSummary> {

    private static final long serialVersionUID = 1L;

    private static final String BASE_NAME_SINGLE = "paper_summary_id_";
    private static final String BASE_NAME_MULTIPLE = "paper_summaries";

    private String populationLabel;
    private String methodsLabel;
    private String resultLabel;
    private String commentLabel;
    private String headerPart;
    private String brand;

    /**
     * Build up the paper summary from a {@link Paper} and any additional information not contained within the paper
     * @param paper
     *      an instance of {@link Paper} - must not be null.
     * @param populationLabel
     *      localized label for the population field
     * @param methodsLabel
     *      localized label for the methods field
     * @param resultLabel
     *      localized label for the result field
     * @param commentLabel
     *      localized label for the comment field
     * @param headerPart
     *      Static part of the header - will be supplemented with the id
     * @param brand
     *      Brand of the application
     * @param config {@link PdfExporterConfiguration}
     */
    public PaperSummaryDataSource(final Paper paper, final String populationLabel, final String methodsLabel, final String resultLabel, final String commentLabel, final String headerPart,
            final String brand, PdfExporterConfiguration config) {
        this(Arrays.asList(new PaperSummary(AssertAs.notNull(paper, "paper"), populationLabel, methodsLabel, resultLabel, commentLabel, headerPart, brand)), config, makeSinglePaperBaseName(paper));
    }

    /**
     * Uses as single {@link PaperSummary} transparently as data source
     * @param paperSummary an instance of {@link PaperSummary} - must not be null
     */
    public PaperSummaryDataSource(final PaperSummary paperSummary, PdfExporterConfiguration config) {
        this(Arrays.asList(AssertAs.notNull(paperSummary, "paperSummary")), config, makeSinglePaperBaseName(paperSummary));
    }

    /**
     * Using a collection of {@link PaperSummary} instances as data source
     * @param paperSummaries collection of {@link PaperSummary} instances - must not be null
     */
    public PaperSummaryDataSource(final Collection<PaperSummary> paperSummaries, PdfExporterConfiguration config) {
        this(paperSummaries, config, BASE_NAME_MULTIPLE);
    }

    /**
     * Using a collection of {@link PaperSummary} instances as data source
     * @param paperSummaries collection of {@link PaperSummary} instances - must not be null
     */
    private PaperSummaryDataSource(final Collection<PaperSummary> paperSummaries, PdfExporterConfiguration config, String baseName) {
        super(new SipamatoPdfResourceHandler(config), baseName, paperSummaries);
    }

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the {@link PaperService} to retrieve the papers
     * based on the ids of the {@link PaperSlim}s that are used in the dataProvider.
     * @param dataProvider
     *      the {@link AbstractPaperSlimProvider} - must not be null
     * @param populationLabel
     *      localized label for the population field
     * @param methodsLabel
     *      localized label for the methods field
     * @param resultLabel
     *      localized label for the result field
     * @param commentLabel
     *      localized label for the comment field
     * @param headerPart
     *      Static part of the header - will be supplemented with the id
     * @param brand
     *      Brand of the application
     * @param config
     *      PdfExporterConfiguration
     */
    public PaperSummaryDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider, final String populationLabel, final String methodsLabel, final String resultLabel,
            final String commentLabel, final String headerPart, final String brand, PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config), BASE_NAME_MULTIPLE, dataProvider);
        this.populationLabel = populationLabel;
        this.methodsLabel = methodsLabel;
        this.resultLabel = resultLabel;
        this.commentLabel = commentLabel;
        this.headerPart = headerPart;
        this.brand = brand;
    }

    @Override
    protected JasperReport getReport() {
        return PaperSummaryReportResourceReference.get().getReport();
    }

    @Override
    protected PaperSummary makeEntity(Paper p) {
        return new PaperSummary(p, populationLabel, methodsLabel, resultLabel, commentLabel, headerPart, brand);
    }

    private static String makeSinglePaperBaseName(final Paper paper) {
        if (paper != null && paper.getId() != null) {
            return BASE_NAME_SINGLE + paper.getId();
        } else {
            return BASE_NAME_MULTIPLE;
        }
    }

    private static String makeSinglePaperBaseName(final PaperSummary paperSummary) {
        if (paperSummary != null && paperSummary.getId() != null) {
            return BASE_NAME_SINGLE + paperSummary.getId();
        } else {
            return BASE_NAME_MULTIPLE;
        }
    }

}
