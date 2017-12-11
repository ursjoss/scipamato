package ch.difty.scipamato.core.web.jasper.literaturereview;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.filter.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.jasper.ScipamatoPdfResourceHandler;
import ch.difty.scipamato.core.web.pages.paper.provider.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.resources.jasper.PaperLiteratureReviewReportResourceReference;
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

    private final ReportHeaderFields reportHeaderFields;

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
    public PaperLiteratureReviewDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider, final ReportHeaderFields reportHeaderFields, PdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), FILE_NAME, AssertAs.notNull(dataProvider, "dataProvider"));
        this.reportHeaderFields = AssertAs.notNull(reportHeaderFields, "reportHeaderFields");
    }

    @Override
    protected JasperReport getReport() {
        return PaperLiteratureReviewReportResourceReference.get().getReport();
    }

    @Override
    protected PaperLiteratureReview makeEntity(Paper p) {
        return new PaperLiteratureReview(p, reportHeaderFields);
    }

}
