package ch.difty.scipamato.web.jasper.summaryshort;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.web.jasper.PaperSummaryCommon;
import ch.difty.scipamato.web.jasper.ReportHeaderFields;

/**
 * DTO to feed the PaperSummaryShortDataSource
 *
 * @author u.joss
 */
public class PaperSummaryShort extends PaperSummaryCommon {
    private static final long serialVersionUID = 1L;

    private final String methodOutcomeLabel;
    private final String methodOutcome;
    private final String resultMeasuredOutcomeLabel;
    private final String resultMeasuredOutcome;
    private final String methodStudyDesignLabel;
    private final String methodStudyDesign;
    private final String populationPlaceLabel;
    private final String populationPlace;
    private final String populationParticipantsLabel;
    private final String populationParticipants;
    private final String populationDurationLabel;
    private final String populationDuration;
    private final String exposurePollutantLabel;
    private final String exposurePollutant;
    private final String exposureAssessmentLabel;
    private final String exposureAssessment;
    private final String resultExposureRangeLabel;
    private final String resultExposureRange;
    private final String methodStatisticsLabel;
    private final String methodStatistics;
    private final String methodConfoundersLabel;
    private final String methodConfounders;
    private final String resultEffectEstimateLabel;
    private final String resultEffectEstimate;

    /**
     * Instantiation with a {@link Paper} and the {@link ReportHeaderFields}
     *
     * @param p
     *      the paper with the relevant fields
     * @param rhf
     *      the reportHeaderFields with the localized field headers
     */
    public PaperSummaryShort(final Paper p, final ReportHeaderFields rhf) {
        super(AssertAs.notNull(p, "p"), AssertAs.notNull(rhf, "rhf"));

        this.methodOutcome = na(p.getMethodOutcome());
        this.resultMeasuredOutcome = na(p.getResultMeasuredOutcome());
        this.methodStudyDesign = na(p.getMethodStudyDesign());
        this.populationPlace = na(p.getPopulationPlace());
        this.populationParticipants = na(p.getPopulationParticipants());
        this.populationDuration = na(p.getPopulationDuration());
        this.exposurePollutant = na(p.getExposurePollutant());
        this.exposureAssessment = na(p.getExposureAssessment());
        this.resultExposureRange = na(p.getResultExposureRange());
        this.methodStatistics = na(p.getMethodStatistics());
        this.methodConfounders = na(p.getMethodConfounders());
        this.resultEffectEstimate = na(p.getResultEffectEstimate());

        this.methodOutcomeLabel = na2(rhf.getMethodOutcomeLabel(), methodOutcome);
        this.resultMeasuredOutcomeLabel = na2(rhf.getResultMeasuredOutcomeLabel(), resultMeasuredOutcome);
        this.methodStudyDesignLabel = na2(rhf.getMethodStudyDesignLabel(), methodStudyDesign);
        this.populationPlaceLabel = na2(rhf.getPopulationPlaceLabel(), populationPlace);
        this.populationParticipantsLabel = na2(rhf.getPopulationParticipantsLabel(), populationParticipants);
        this.populationDurationLabel = na2(rhf.getPopulationDurationLabel(), populationDuration);
        this.exposurePollutantLabel = na2(rhf.getExposurePollutantLabel(), exposurePollutant);
        this.exposureAssessmentLabel = na2(rhf.getExposureAssessmentLabel(), exposureAssessment);
        this.resultExposureRangeLabel = na2(rhf.getResultExposureRangeLabel(), resultExposureRange);
        this.methodStatisticsLabel = na2(rhf.getMethodStatisticsLabel(), methodStatistics);
        this.methodConfoundersLabel = na2(rhf.getMethodConfoundersLabel(), methodConfounders);
        this.resultEffectEstimateLabel = na2(rhf.getResultEffectEstimateLabel(), resultEffectEstimate);
    }

    public String getMethodOutcomeLabel() {
        return methodOutcomeLabel;
    }

    public String getMethodOutcome() {
        return methodOutcome;
    }

    public String getResultMeasuredOutcomeLabel() {
        return resultMeasuredOutcomeLabel;
    }

    public String getResultMeasuredOutcome() {
        return resultMeasuredOutcome;
    }

    public String getMethodStudyDesignLabel() {
        return methodStudyDesignLabel;
    }

    public String getMethodStudyDesign() {
        return methodStudyDesign;
    }

    public String getPopulationPlaceLabel() {
        return populationPlaceLabel;
    }

    public String getPopulationPlace() {
        return populationPlace;
    }

    public String getPopulationParticipantsLabel() {
        return populationParticipantsLabel;
    }

    public String getPopulationParticipants() {
        return populationParticipants;
    }

    public String getPopulationDurationLabel() {
        return populationDurationLabel;
    }

    public String getPopulationDuration() {
        return populationDuration;
    }

    public String getExposurePollutantLabel() {
        return exposurePollutantLabel;
    }

    public String getExposurePollutant() {
        return exposurePollutant;
    }

    public String getExposureAssessmentLabel() {
        return exposureAssessmentLabel;
    }

    public String getExposureAssessment() {
        return exposureAssessment;
    }

    public String getResultExposureRangeLabel() {
        return resultExposureRangeLabel;
    }

    public String getResultExposureRange() {
        return resultExposureRange;
    }

    public String getMethodStatisticsLabel() {
        return methodStatisticsLabel;
    }

    public String getMethodStatistics() {
        return methodStatistics;
    }

    public String getMethodConfoundersLabel() {
        return methodConfoundersLabel;
    }

    public String getMethodConfounders() {
        return methodConfounders;
    }

    public String getResultEffectEstimateLabel() {
        return resultEffectEstimateLabel;
    }

    public String getResultEffectEstimate() {
        return resultEffectEstimate;
    }

}