package ch.difty.sipamato.web.jasper.summary_table;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.jasper.JasperPaperDataSource;
import ch.difty.sipamato.web.jasper.SipamatoPdfResourceHandler;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.resources.jasper.PaperSummaryReportResourceReference;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * DataSource for the PaperSummaryTableReport.
 *
 * The meta fields are not contained within a paper instance and make up the caption
 *
 * @author u.joss
 */
public class PaperSummaryTableDataSource extends JasperPaperDataSource<PaperSummaryTable> {

    private static final long serialVersionUID = 1L;

    private static final String FILE_NAME = "paper_summary_table";

    private String caption;

    @Override
    protected JasperReport getReport() {
        return PaperSummaryReportResourceReference.get().getReport();
    }

    @Override
    protected PaperSummaryTable makeEntity(Paper p) {
        return new PaperSummaryTable(p, caption);
    }

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the {@link PaperService} to retrieve the papers
     * based on the ids of the {@link PaperSlim}s that are used in the dataProvider.
     * @param dataProvider
     *      the {@link SortablePaperSlimProvider} - must not be null
     * @param paperService
     *      the {@link PaperService} - must not be null
     * @param caption
     *      localized caption
     */
    public PaperSummaryTableDataSource(final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider, final PaperService paperService, final String caption,
            PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config), FILE_NAME, dataProvider, paperService);
        this.caption = caption;
    }

}
