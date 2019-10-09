package ch.difty.scipamato.core.web.paper.jasper.review;

import net.sf.jasperreports.engine.JasperReport;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.jasper.ClusterablePdfExporterConfiguration;
import ch.difty.scipamato.core.web.paper.jasper.JasperPaperDataSource;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;
import ch.difty.scipamato.core.web.paper.jasper.ScipamatoPdfResourceHandler;
import ch.difty.scipamato.core.web.resources.jasper.PaperReviewReportResourceReference;

/**
 * DataSource for the PaperReviewReport.
 * <p>
 * The meta fields are not contained within a paper instance and make up e.g.
 * localized labels, the brand or part of the header.
 *
 * @author u.joss
 */
public class PaperReviewDataSource extends JasperPaperDataSource<PaperReview> {

    private static final String FILE_BASE_NAME = "paper_review";

    private static final long serialVersionUID = 1L;

    private final ReportHeaderFields reportHeaderFields;

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the
     * {@link PaperService} to retrieve the papers based on the ids of the
     * {@link PaperSlim}s that are used in the dataProvider.
     *
     * @param dataProvider
     *     the {@link AbstractPaperSlimProvider} - must not be null
     * @param reportHeaderFields
     *     collection of localized labels for the report fields - must not be
     *     null
     * @param config
     *     {@link ClusterablePdfExporterConfiguration}
     */
    public PaperReviewDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider,
        final ReportHeaderFields reportHeaderFields, ClusterablePdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), FILE_BASE_NAME,
            AssertAs.INSTANCE.notNull(dataProvider, "dataProvider"));
        this.reportHeaderFields = AssertAs.INSTANCE.notNull(reportHeaderFields, "reportHeaderFields");
    }

    @Override
    protected JasperReport getReport() {
        return PaperReviewReportResourceReference
            .get()
            .getReport();
    }

    @Override
    protected PaperReview makeEntity(final Paper p) {
        return new PaperReview(p, reportHeaderFields);
    }

}
