package ch.difty.sipamato.web.jasper.review;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.web.jasper.JasperPaperDataSource;
import ch.difty.sipamato.web.jasper.SipamatoPdfResourceHandler;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.resources.jasper.PaperReviewReportResourceReference;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * DataSource for the PaperReviewReport.
 *
 * The meta fields are not contained within a paper instance and make up e.g. localized labels, the brand or part of the header.
 * @author u.joss
 */
public class PaperReviewDataSource extends JasperPaperDataSource<PaperReview> {

    private static final String FILE_BASE_NAME = "paper_review";

    private static final long serialVersionUID = 1L;

    private String idLabel;
    private String authorYearLabel;
    private String populationPlaceLabel;
    private String methodOutcomeLabel;
    private String exposurePollutantLabel;
    private String methodStudyDesignLabel;
    private String populationDurationLabel;
    private String populationParticipantsLabel;
    private String exposureAssessmentLabel;
    private String resultExposureRangeLabel;
    private String methodConfoundersLabel;
    private String resultEffectEstimateLabel;
    private String brand;
    private String createdBy;

    /**
     * Using the dataProvider for the Result Panel as record source.
     */
    public PaperReviewDataSource(final SortablePaperSlimProvider<? extends PaperSlimFilter> dataProvider, final String idLabel, final String authorYearLabel, final String populationPlaceLabel,
            final String methodOutcomeLabel, final String exposurePollutantLabel, final String methodStudyDesignLabel, final String populationDurationLabel, final String populationParticipantsLabel,
            final String exposureAssessmentLabel, final String resultExposureRangeLabel, final String methodConfoundersLabel, final String resultEffectEstimateLabel, final String brand,
            final String createdBy, PdfExporterConfiguration config) {
        super(new SipamatoPdfResourceHandler(config), FILE_BASE_NAME, dataProvider);

        this.idLabel = idLabel;
        this.authorYearLabel = authorYearLabel;
        this.populationPlaceLabel = populationPlaceLabel;
        this.methodOutcomeLabel = methodOutcomeLabel;
        this.exposurePollutantLabel = exposurePollutantLabel;
        this.methodStudyDesignLabel = methodStudyDesignLabel;
        this.populationDurationLabel = populationDurationLabel;
        this.populationParticipantsLabel = populationParticipantsLabel;
        this.exposureAssessmentLabel = exposureAssessmentLabel;
        this.resultExposureRangeLabel = resultExposureRangeLabel;
        this.methodConfoundersLabel = methodConfoundersLabel;
        this.resultEffectEstimateLabel = resultEffectEstimateLabel;
        this.brand = brand;
        this.createdBy = createdBy;
    }

    @Override
    protected JasperReport getReport() {
        return PaperReviewReportResourceReference.get().getReport();
    }

    @Override
    protected PaperReview makeEntity(final Paper p) {
        return new PaperReview(p, idLabel, authorYearLabel, populationPlaceLabel, methodOutcomeLabel, exposurePollutantLabel, methodStudyDesignLabel, populationDurationLabel,
                populationParticipantsLabel, exposureAssessmentLabel, resultExposureRangeLabel, methodConfoundersLabel, resultEffectEstimateLabel, brand, createdBy);
    }

}
