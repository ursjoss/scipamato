package ch.difty.sipamato.web.jasper.summary_table;

import java.util.List;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
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

    @Override
    protected JasperReport getReport() {
        return PaperSummaryTableReportResourceReference.get().getReport();
    }

    @Override
    protected PaperSummaryTable makeEntity(Paper p) {
        return new PaperSummaryTable(p, caption, brand);
    }

    /**
     * We need the codes here too...
     */
    protected List<Paper> findPapersById(final List<Long> ids) {
        return getPaperService().findWithCodesByIds(ids);
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
     * @param brand
     *      localized brand
     */
    public PaperSummaryTableDataSource(final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider, final PaperService paperService, final String caption, String brand,
            PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config), FILE_NAME, dataProvider, paperService);
        this.caption = caption;
        this.brand = brand;
    }

}
