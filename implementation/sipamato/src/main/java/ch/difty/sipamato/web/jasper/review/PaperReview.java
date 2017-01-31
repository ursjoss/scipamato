package ch.difty.sipamato.web.jasper.review;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.web.jasper.JasperEntity;

/**
 * DTO to feed the PaperSummaryDataSource
 *
 * @author u.joss
 */
public class PaperReview extends JasperEntity {
    private static final long serialVersionUID = 1L;

    private final String id;
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

    private final String idLabel;
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
     * Instantiation with a {@link Paper} and additional fields
     * @param p paper
     * @param idLabel
     * @param authorYearLabel
     * @param populationPlaceLabel
     * @param methodOutcomeLabel
     * @param exposurePollutantLabel
     * @param methodStudyDesignLabel
     * @param populationDurationLabel
     * @param populationParticipantsLabel
     * @param exposureAssessmentLabel
     * @param resultExposureRangeLabel
     * @param methodConfoundersLabel
     * @param resultEffectEstimateLabel
     * @param brand
     * @param createdBy
     */
    public PaperReview(final Paper p, final String idLabel, final String authorYearLabel, final String populationPlaceLabel, final String methodOutcomeLabel, final String exposurePollutantLabel,
            final String methodStudyDesignLabel, final String populationDurationLabel, final String populationParticipantsLabel, final String exposureAssessmentLabel,
            final String resultExposureRangeLabel, final String methodConfoundersLabel, final String resultEffectEstimateLabel, final String brand, final String createdBy) {
        super();
        AssertAs.notNull(p, "paper");
        this.id = p.getId() != null ? String.valueOf(p.getId()) : "";
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
        this.idLabel = na(idLabel, id);
        this.authorYearLabel = na(authorYearLabel);
        this.populationPlaceLabel = na(populationPlaceLabel);
        this.methodOutcomeLabel = na(methodOutcomeLabel);
        this.exposurePollutantLabel = na(exposurePollutantLabel);
        this.methodStudyDesignLabel = na(methodStudyDesignLabel);
        this.populationDurationLabel = na(populationDurationLabel);
        this.populationParticipantsLabel = na(populationParticipantsLabel);
        this.exposureAssessmentLabel = na(exposureAssessmentLabel);
        this.resultExposureRangeLabel = na(resultExposureRangeLabel);
        this.methodConfoundersLabel = na(methodConfoundersLabel);
        this.resultEffectEstimateLabel = na(resultEffectEstimateLabel);

        this.brand = na(brand);
        this.createdBy = na(createdBy);
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

    public String getId() {
        return id;
    }

    public String getIdLabel() {
        return idLabel;
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