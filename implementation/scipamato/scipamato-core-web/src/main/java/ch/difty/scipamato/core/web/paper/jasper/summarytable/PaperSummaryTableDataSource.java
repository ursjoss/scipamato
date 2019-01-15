package ch.difty.scipamato.core.web.paper.jasper.summarytable;

import net.sf.jasperreports.engine.JasperReport;

import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration;
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler;
import ch.difty.scipamato.core.web.resources.jasper.PaperSummaryTableReportResourceReference;

/**
 * DataSource for the PaperSummaryTableReport.
 * <p>
 * The report header fields are not contained within a paper instance and make
 * up e.g. localized labels, the brand or part of the header.
 *
 * @author u.joss
 */
public class PaperSummaryTableDataSource extends JasperPaperDataSource<PaperSummaryTable> {

    private static final long serialVersionUID = 1L;

    private static final String FILE_NAME = "paper_summary_table";

    private ReportHeaderFields reportHeaderFields;

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
    public PaperSummaryTableDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider,
        final ReportHeaderFields reportHeaderFields, ClusterablePdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), FILE_NAME, dataProvider);
        this.reportHeaderFields = reportHeaderFields;
    }

    @Override
    protected JasperReport getReport() {
        return PaperSummaryTableReportResourceReference
            .get()
            .getReport();
    }

    @Override
    protected PaperSummaryTable makeEntity(Paper p) {
        return new PaperSummaryTable(p, reportHeaderFields);
    }

}
