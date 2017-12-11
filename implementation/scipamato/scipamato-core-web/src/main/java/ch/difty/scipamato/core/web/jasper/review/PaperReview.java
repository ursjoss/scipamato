package ch.difty.scipamato.core.web.jasper.review;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.web.jasper.JasperEntity;
import ch.difty.scipamato.core.web.jasper.ReportHeaderFields;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO to feed the PaperReviewDataSource
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperReview extends JasperEntity {
    private static final long serialVersionUID = 1L;

    private final String number;
    private final String authorYear;
    private final String populationPlace;
    private final String methodOutcome;
    private final String exposurePollutant;
    private final String methodStudyDesign;
    private final String populationDuration;
    private final String populationParticipants;
    private final String exposureAssessment;
    private final String resultExposureRange;
    private final String methodConfounders;
    private final String resultEffectEstimate;

    private final String numberLabel;
    private final String authorYearLabel;
    private final String populationPlaceLabel;
    private final String methodOutcomeLabel;
    private final String exposurePollutantLabel;
    private final String methodStudyDesignLabel;
    private final String populationDurationLabel;
    private final String populationParticipantsLabel;
    private final String exposureAssessmentLabel;
    private final String resultExposureRangeLabel;
    private final String methodConfoundersLabel;
    private final String resultEffectEstimateLabel;

    private final String brand;
    private final String createdBy;

    /**
     * Instantiation with a {@link Paper} and the {@link ReportHeaderFields}
     *
     * @param p
     *      the paper with the relevant fields
     * @param rhf
     *      the reportHeaderFields with the localized field headers
     */
    public PaperReview(final Paper p, final ReportHeaderFields rhf) {
        AssertAs.notNull(p, "p");
        AssertAs.notNull(rhf, "rhf");

        final Long no = p.getNumber();
        this.number = no != null ? String.valueOf(no) : "";
        AssertAs.notNull(p, "paper");
        this.authorYear = makeAuthorYearFrom(p);
        this.populationPlace = na(p.getPopulationPlace());
        this.methodOutcome = na(p.getMethodOutcome());
        this.exposurePollutant = na(p.getExposurePollutant());
        this.methodStudyDesign = na(p.getMethodStudyDesign());
        this.populationDuration = na(p.getPopulationDuration());
        this.populationParticipants = na(p.getPopulationParticipants());
        this.exposureAssessment = na(p.getExposureAssessment());
        this.resultExposureRange = na(p.getResultExposureRange());
        this.methodConfounders = na(p.getMethodConfounders());
        this.resultEffectEstimate = na(p.getResultEffectEstimate());
        this.numberLabel = na2(rhf.getNumberLabel(), String.valueOf(p.getNumber()));
        this.authorYearLabel = na(rhf.getAuthorYearLabel());
        this.populationPlaceLabel = na(rhf.getPopulationPlaceLabel());
        this.methodOutcomeLabel = na(rhf.getMethodOutcomeLabel());
        this.exposurePollutantLabel = na(rhf.getExposurePollutantLabel());
        this.methodStudyDesignLabel = na(rhf.getMethodStudyDesignLabel());
        this.populationDurationLabel = na(rhf.getPopulationDurationLabel());
        this.populationParticipantsLabel = na(rhf.getPopulationParticipantsLabel());
        this.exposureAssessmentLabel = na(rhf.getExposureAssessmentLabel());
        this.resultExposureRangeLabel = na(rhf.getResultExposureRangeLabel());
        this.methodConfoundersLabel = na(rhf.getMethodConfoundersLabel());
        this.resultEffectEstimateLabel = na(rhf.getResultEffectEstimateLabel());

        this.brand = na(rhf.getBrand());
        this.createdBy = na(p.getCreatedByName());
    }

    private String makeAuthorYearFrom(Paper p) {
        final String fa = p.getFirstAuthor();
        final Integer py = p.getPublicationYear();
        if (fa == null && (py == null || py.intValue() == 0)) {
            return "";
        } else {
            final StringBuilder sb = new StringBuilder();
            if (fa != null) {
                sb.append(fa);
                if (py != null)
                    sb.append(" ");
            }
            if (py != null && py.intValue() > 0)
                sb.append(py);
            return sb.toString();
        }
    }

}