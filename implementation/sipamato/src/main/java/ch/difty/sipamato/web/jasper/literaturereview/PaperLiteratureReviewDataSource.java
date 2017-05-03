package ch.difty.sipamato.web.jasper.literaturereview;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.jasper.JasperPaperDataSource;
import ch.difty.sipamato.web.jasper.SipamatoPdfResourceHandler;
import ch.difty.sipamato.web.pages.paper.provider.AbstractPaperSlimProvider;
import ch.difty.sipamato.web.resources.jasper.PaperLiteratureReviewReportResourceReference;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * DataSource for the PaperLiteratureReviewReport.
 *
 * The meta fields are not contained within a paper instance and make up the caption
 *
 * @author u.joss
 */
public class PaperLiteratureReviewDataSource extends JasperPaperDataSource<PaperLiteratureReview> {

    private static final long serialVersionUID = 1L;

    private static final String FILE_NAME = "paper_literature_review";

    private final String caption;
    private final String brand;
    private final String numberLabel;

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the {@link PaperService} to retrieve the papers
     * based on the numbers of the {@link PaperSlim}s that are used in the dataProvider.
     * @param dataProvider
     *      the {@link AbstractPaperSlimProvider} - must not be null
     * @param caption
     *      localized caption
     * @param brand
     *      localized brand
     * @param numberLabel
     *      localized number
     * @param config
     *      PdfExporterConfiguration
     */
    public PaperLiteratureReviewDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider, final String caption, String brand, String numberLabel,
            PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config), FILE_NAME, dataProvider);
        this.caption = caption;
        this.brand = brand;
        this.numberLabel = numberLabel;
    }

    @Override
    protected JasperReport getReport() {
        return PaperLiteratureReviewReportResourceReference.get().getReport();
    }

    @Override
    protected PaperLiteratureReview makeEntity(Paper p) {
        return new PaperLiteratureReview(p, caption, brand, numberLabel);
    }

}
