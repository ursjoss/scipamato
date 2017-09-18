package ch.difty.scipamato.web.jasper.summarytable;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.filter.PaperSlimFilter;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.persistence.PaperService;
import ch.difty.scipamato.web.jasper.JasperPaperDataSource;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;
import ch.difty.scipamato.web.jasper.ScipamatoPdfResourceHandler;
import ch.difty.scipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import ch.difty.scipamato.web.resources.jasper.PaperSummaryTableReportResourceReference;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * DataSource for the PaperSummaryTableReport.
 *
 * Can be instantiated in different ways, either by passing in
 *
 * The report header fields are not contained within a paper instance and make up e.g. localized labels, the brand or part of the header.
 *
 * @author u.joss
 */
public class PaperSummaryTableDataSource extends JasperPaperDataSource<PaperSummaryTable> {

    private static final long serialVersionUID = 1L;

    private static final String FILE_NAME = "paper_summary_table";

    private ReportHeaderFields reportHeaderFields;
    private boolean includeResults;

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the {@link PaperService} to retrieve the papers
     * based on the ids of the {@link PaperSlim}s that are used in the dataProvider.
     * @param dataProvider
     *      the {@link AbstractPaperSlimProvider} - must not be null
     * @param reportHeaderFields
     *      collection of localized labels for the report fields
     * @param includeResults
     *      true: show results in pdf, false: hide it
     * @param config
     *      {@link PdfExporterConfiguration}
     */
    public PaperSummaryTableDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider, final ReportHeaderFields reportHeaderFields, final boolean includeResults,
            PdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), FILE_NAME, dataProvider);
        this.reportHeaderFields = reportHeaderFields;
        this.includeResults = includeResults;
    }

    @Override
    protected JasperReport getReport() {
        return PaperSummaryTableReportResourceReference.get().getReport();
    }

    @Override
    protected PaperSummaryTable makeEntity(Paper p) {
        return new PaperSummaryTable(p, reportHeaderFields, includeResults);
    }

}
