package ch.difty.sipamato.web.jasper.summarytable;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.web.jasper.JasperPaperDataSource;
import ch.difty.sipamato.web.jasper.SipamatoPdfResourceHandler;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.resources.jasper.PaperSummaryTableReportResourceReference;
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

    private final String caption;
    private final String brand;
    private final boolean includeResults;

    /**
     * Using the dataProvider for the Result Panel as record source.
     * @param dataProvider
     *      the {@link SortablePaperSlimProvider} - must not be null
     * @param caption
     *      localized caption
     * @param includeResults
     *      whether or not to include the results field
     * @param brand
     *      localized brand
     * @param config
     *      PdfExporterConfiguration
     */
    public PaperSummaryTableDataSource(final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider, boolean includeResults, final String caption, String brand,
            PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config), FILE_NAME, dataProvider);
        this.caption = caption;
        this.brand = brand;
        this.includeResults = includeResults;
    }

    @Override
    protected JasperReport getReport() {
        return PaperSummaryTableReportResourceReference.get().getReport();
    }

    @Override
    protected PaperSummaryTable makeEntity(Paper p) {
        return new PaperSummaryTable(p, includeResults, caption, brand);
    }

}
