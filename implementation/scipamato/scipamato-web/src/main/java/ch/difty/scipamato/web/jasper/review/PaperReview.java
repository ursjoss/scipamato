package ch.difty.scipamato.web.jasper.review;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.JasperEntity;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

/**
 * DTO to feed the PaperReviewDataSource
 *
 * @author u.joss
 */
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

    public String getNumber() {
        return number;
    }

    public String getNumberLabel() {
        return numberLabel;
    }

    public String getAuthorYear() {
        return authorYear;
    }

    public String getAuthorYearLabel() {
        return authorYearLabel;
    }

    public String getPopulationPlace() {
        return populationPlace;
    }

    public String getPopulationPlaceLabel() {
        return populationPlaceLabel;
    }

    public String getMethodOutcome() {
        return methodOutcome;
    }

    public String getMethodOutcomeLabel() {
        return methodOutcomeLabel;
    }

    public String getExposurePollutant() {
        return exposurePollutant;
    }

    public String getExposurePollutantLabel() {
        return exposurePollutantLabel;
    }

    public String getMethodStudyDesign() {
        return methodStudyDesign;
    }

    public String getMethodStudyDesignLabel() {
        return methodStudyDesignLabel;
    }

    public String getPopulationDuration() {
        return populationDuration;
    }

    public String getPopulationDurationLabel() {
        return populationDurationLabel;
    }

    public String getPopulationParticipants() {
        return populationParticipants;
    }

    public String getPopulationParticipantsLabel() {
        return populationParticipantsLabel;
    }

    public String getExposureAssessment() {
        return exposureAssessment;
    }

    public String getExposureAssessmentLabel() {
        return exposureAssessmentLabel;
    }

    public String getResultExposureRange() {
        return resultExposureRange;
    }

    public String getResultExposureRangeLabel() {
        return resultExposureRangeLabel;
    }

    public String getMethodConfounders() {
        return methodConfounders;
    }

    public String getMethodConfoundersLabel() {
        return methodConfoundersLabel;
    }

    public String getResultEffectEstimate() {
        return resultEffectEstimate;
    }

    public String getResultEffectEstimateLabel() {
        return resultEffectEstimateLabel;
    }

    public String getBrand() {
        return brand;
    }

    public String getCreatedBy() {
        return createdBy;
    }

}