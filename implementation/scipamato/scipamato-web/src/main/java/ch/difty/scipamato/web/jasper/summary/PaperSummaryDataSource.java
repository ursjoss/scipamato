package ch.difty.scipamato.web.jasper.summary;

import java.util.Arrays;
import java.util.Collection;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.filter.PaperSlimFilter;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.service.PaperService;
import ch.difty.scipamato.web.jasper.JasperPaperDataSource;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;
import ch.difty.scipamato.web.jasper.ScipamatoPdfResourceHandler;
import ch.difty.scipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import ch.difty.scipamato.web.resources.jasper.PaperSummaryReportResourceReference;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * DataSource for the PaperSummaryReport.
 *
 * Can be instantiated in different ways, either by passing in
 *
 * <ul>
 * <li> a single {@link Paper} + report header fields + export config</li>
 * <li> a single {@link PaperSummary} + export config</li>
 * <li> a collection of {@link PaperSummary} entities + export config or</li>
 * <li> an instance of a {@link AbstractPaperSlimProvider} + report header fields + export config</li>
 * </ul>
 *
 * The report header fields are not contained within a paper instance and make up e.g. localized labels, the brand or part of the header.
 *
 * @author u.joss
 */
public class PaperSummaryDataSource extends JasperPaperDataSource<PaperSummary> {

    private static final long serialVersionUID = 1L;

    private static final String BASE_NAME_SINGLE = "paper_summary_no_";
    private static final String BASE_NAME_SINGLE_FALLBACK = "paper_summary";
    private static final String BASE_NAME_MULTIPLE = "paper_summaries";

    private ReportHeaderFields reportHeaderFields;

    /**
     * Build up the paper summary from a {@link Paper} and any additional information not contained within the paper
     * @param paper
     *      an instance of {@link Paper} - must not be null.
     * @param reportHeaderFields
     *      collection of localized labels for report fields
     * @param config {@link PdfExporterConfiguration}
     */
    public PaperSummaryDataSource(final Paper paper, final ReportHeaderFields reportHeaderFields, PdfExporterConfiguration config) {
        this(Arrays.asList(new PaperSummary(AssertAs.notNull(paper, "paper"), AssertAs.notNull(reportHeaderFields, "reportHeaderFields"))), config,
                makeSinglePaperBaseName(paper.getNumber() != null ? String.valueOf(paper.getNumber()) : null));
        this.reportHeaderFields = reportHeaderFields;
    }

    /**
     * Populate the report from a single {@link PaperSummary}, using a specific file name including the id of the paper.
     * @param paperSummary
     *     collection of {@link PaperSummary} instances - must not be null
     * @param config
     *     the {@link PdfExporterConfiguration}
     */
    public PaperSummaryDataSource(final PaperSummary paperSummary, PdfExporterConfiguration config) {
        this(Arrays.asList(AssertAs.notNull(paperSummary, "paperSummary")), config, makeSinglePaperBaseName(paperSummary.getNumber()));
    }

    private PaperSummaryDataSource(final Collection<PaperSummary> paperSummaries, PdfExporterConfiguration config, String baseName) {
        super(new ScipamatoPdfResourceHandler(config), baseName, paperSummaries);
    }

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the {@link PaperService} to retrieve the papers
     * based on the ids of the {@link PaperSlim}s that are used in the dataProvider.
     * @param dataProvider
     *      the {@link AbstractPaperSlimProvider} - must not be null
     * @param reportHeaderFields
     *      collection of localized labels for the report fields
     * @param config
     *      {@link PdfExporterConfiguration}
     */
    public PaperSummaryDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider, final ReportHeaderFields reportHeaderFields, PdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), BASE_NAME_MULTIPLE, dataProvider);
        this.reportHeaderFields = reportHeaderFields;
    }

    @Override
    protected JasperReport getReport() {
        return PaperSummaryReportResourceReference.get().getReport();
    }

    @Override
    protected PaperSummary makeEntity(Paper p) {
        return new PaperSummary(p, reportHeaderFields);
    }

    private static String makeSinglePaperBaseName(String number) {
        if (number != null && !number.isEmpty()) {
            return BASE_NAME_SINGLE + number;
        } else {
            return BASE_NAME_SINGLE_FALLBACK;
        }
    }

}
