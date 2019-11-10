package ch.difty.scipamato.core.web.paper.jasper.summaryshort;

import java.util.Collection;
import java.util.Collections;

import net.sf.jasperreports.engine.JasperReport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration;
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler;
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummary;
import ch.difty.scipamato.core.web.resources.jasper.PaperSummaryShortReportResourceReference;

/**
 * DataSource for the PaperSummaryShortReport.
 * <p>
 * Can be instantiated in different ways, either by passing in
 *
 * <ul>
 * <li>a single {@link Paper} + report header fields + export config</li>
 * <li>a single {@link PaperSummaryShort} + export config</li>
 * <li>a collection of {@link PaperSummaryShort} entities + export config
 * or</li>
 * <li>an instance of a {@link AbstractPaperSlimProvider} + report header fields
 * + export config</li>
 * </ul>
 * <p>
 * The report header fields are not contained within a paper instance and make
 * up e.g. localized labels, the brand or part of the header.
 *
 * @author u.joss
 */
public class PaperSummaryShortDataSource extends JasperPaperDataSource<PaperSummaryShort> {

    private static final long serialVersionUID = 1L;

    private static final String BASE_NAME_SINGLE          = "paper_summary_short_no_";
    private static final String BASE_NAME_SINGLE_FALLBACK = "paper_summary_short";
    private static final String BASE_NAME_MULTIPLE        = "paper_summaries_short";

    private ReportHeaderFields reportHeaderFields;

    /**
     * Build up the paper summary short from a {@link Paper} and any additional
     * information not contained within the paper
     *
     * @param paper
     *     an instance of {@link Paper} - must not be null.
     * @param reportHeaderFields
     *     collection of localized labels for report fields
     * @param config
     *     {@link ClusterablePdfExporterConfiguration}
     */
    public PaperSummaryShortDataSource(@NotNull final Paper paper, @NotNull final ReportHeaderFields reportHeaderFields,
        @Nullable ClusterablePdfExporterConfiguration config) {
        this(Collections.singletonList(new PaperSummaryShort(paper, reportHeaderFields)), config,
            makeSinglePaperBaseName(paper.getNumber() != null ? String.valueOf(paper.getNumber()) : null));
        this.reportHeaderFields = reportHeaderFields;
    }

    /**
     * Populate the report from a single {@link PaperSummaryShort}, using a specific
     * file name including the id of the paper.
     *
     * @param paperSummaryShort
     *     single {@link PaperSummaryShort} - must not be null
     * @param config
     *     the {@link ClusterablePdfExporterConfiguration}
     */
    PaperSummaryShortDataSource(@NotNull final PaperSummaryShort paperSummaryShort,
        @Nullable ClusterablePdfExporterConfiguration config) {
        this(Collections.singletonList(paperSummaryShort), config,
            makeSinglePaperBaseName(paperSummaryShort.getNumber()));
    }

    /**
     * Populate the report from a collection of {@link PaperSummaryShort
     * PaperSummaryShorts}, using a specific file name including the id of the
     * paper.
     *
     * @param paperSummaryShorts
     *     collection of {@link PaperSummary} instances
     * @param config
     *     the {@link ClusterablePdfExporterConfiguration}
     * @param baseName
     *     the file name without the extension (.pdf)
     */
    private PaperSummaryShortDataSource(@NotNull final Collection<PaperSummaryShort> paperSummaryShorts,
        @Nullable ClusterablePdfExporterConfiguration config, @NotNull String baseName) {
        super(new ScipamatoPdfResourceHandler(config), baseName, paperSummaryShorts);
    }

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the
     * {@link PaperService} to retrieve the papers based on the ids of the
     * {@link PaperSlim}s that are used in the dataProvider.
     *
     * @param dataProvider
     *     the {@link AbstractPaperSlimProvider} - must not be null
     * @param reportHeaderFields
     *     collection of localized labels for the report fields
     * @param config
     *     {@link ClusterablePdfExporterConfiguration}
     */
    public PaperSummaryShortDataSource(@NotNull final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider,
        @NotNull final ReportHeaderFields reportHeaderFields, @Nullable ClusterablePdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), BASE_NAME_MULTIPLE, dataProvider);
        this.reportHeaderFields = reportHeaderFields;
    }

    @NotNull
    @Override
    protected JasperReport getReport() {
        return PaperSummaryShortReportResourceReference
            .get()
            .getReport();
    }

    @NotNull
    @Override
    protected PaperSummaryShort makeEntity(@NotNull Paper p) {
        return new PaperSummaryShort(p, reportHeaderFields);
    }

    private static String makeSinglePaperBaseName(String number) {
        if (number != null && !number.isEmpty()) {
            return BASE_NAME_SINGLE + number;
        } else {
            return BASE_NAME_SINGLE_FALLBACK;
        }
    }
}
