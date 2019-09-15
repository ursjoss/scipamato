package ch.difty.scipamato.core.web.paper.jasper.literaturereview;

import java.util.HashMap;

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
import ch.difty.scipamato.core.web.resources.jasper.PaperLiteratureReviewReportResourceReference;

/**
 * DataSource for the PaperLiteratureReviewReport.
 * <p>
 * The meta fields are not contained within a paper instance and make up the
 * caption
 *
 * @author u.joss
 */
public class PaperLiteratureReviewDataSource extends JasperPaperDataSource<PaperLiteratureReview> {

    private static final long serialVersionUID = 1L;

    private static final String FILE_NAME = "paper_literature_review";

    private final ReportHeaderFields reportHeaderFields;

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
    public PaperLiteratureReviewDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider,
        final ReportHeaderFields reportHeaderFields, ClusterablePdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), FILE_NAME, AssertAs.INSTANCE.notNull(dataProvider, "dataProvider"));
        this.reportHeaderFields = AssertAs.INSTANCE.notNull(reportHeaderFields, "reportHeaderFields");
    }

    @Override
    protected HashMap<String, Object> getParameterMap() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("show_goal", false);
        return map;
    }

    @Override
    protected JasperReport getReport() {
        return PaperLiteratureReviewReportResourceReference
            .get()
            .getReport();
    }

    @Override
    protected PaperLiteratureReview makeEntity(Paper p) {
        return new PaperLiteratureReview(p, reportHeaderFields);
    }

}
