package ch.difty.scipamato.web.jasper;

import org.apache.wicket.util.io.IClusterable;

/**
 * Context holder for localized jasper report captions. Uses builder pattern
 * in order to avoid having constructor with numerous String arguments.
 *
 * This class can be used to serve various jasper reports with different needs
 * for localized labels. Consequently hardly any validation occurs in this class.
 * It is up to the DTOs that are passed into the reports to validate the required
 * labels are non-null.
 *
 * @author u.joss
 */
public class ReportHeaderFields implements IClusterable {

    private static final long serialVersionUID = 1L;

    // Avoiding the Tag 'Label' in the variables for brevity
    private final String goals;
    private final String methods;
    private final String methodOutcome;
    private final String resultMeasuredOutcome;
    private final String methodStudyDesign;
    private final String populationPlace;
    private final String populationPariticpants;
    private final String populationDuration;
    private final String exposurePollutant;
    private final String exposureAssessment;
    private final String resultExposureRange;
    private final String methodStatistics;
    private final String methodConfounders;
    private final String resultEffectEstimate;
    private final String comment;
    private final String headerPart;
    private final String brand;

    private final String population;
    private final String result;

    private final String caption;
    private final String number;

    private final String authorYear;

    private ReportHeaderFields(final Builder b) {
        this.goals = b.goals;
        this.methods = b.methods;
        this.methodOutcome = b.methodOutcome;
        this.resultMeasuredOutcome = b.resultMeasuredOutcome;
        this.methodStudyDesign = b.methodStudyDesign;
        this.populationPlace = b.populationPlace;
        this.populationPariticpants = b.populationPariticpants;
        this.populationDuration = b.populationDuration;
        this.exposurePollutant = b.exposurePollutant;
        this.exposureAssessment = b.exposureAssessment;
        this.resultExposureRange = b.resultExposureRange;
        this.methodStatistics = b.methodStatistics;
        this.methodConfounders = b.methodConfounders;
        this.resultEffectEstimate = b.resultEffectEstimate;
        this.comment = b.comment;
        this.headerPart = b.headerPart;
        this.brand = b.brand;
        this.population = b.population;
        this.result = b.result;
        this.caption = b.caption;
        this.number = b.number;
        this.authorYear = b.authorYear;
    }

    public String getGoalsLabel() {
        return goals;
    }

    public String getMethodsLabel() {
        return methods;
    }

    public String getMethodOutcomeLabel() {
        return methodOutcome;
    }

    public String getResultMeasuredOutcomeLabel() {
        return resultMeasuredOutcome;
    }

    public String getMethodStudyDesignLabel() {
        return methodStudyDesign;
    }

    public String getPopulationPlaceLabel() {
        return populationPlace;
    }

    public String getPopulationParticipantsLabel() {
        return populationPariticpants;
    }

    public String getPopulationDurationLabel() {
        return populationDuration;
    }

    public String getExposurePollutantLabel() {
        return exposurePollutant;
    }

    public String getExposureAssessmentLabel() {
        return exposureAssessment;
    }

    public String getResultExposureRangeLabel() {
        return resultExposureRange;
    }

    public String getMethodStatisticsLabel() {
        return methodStatistics;
    }

    public String getMethodConfoundersLabel() {
        return methodConfounders;
    }

    public String getResultEffectEstimateLabel() {
        return resultEffectEstimate;
    }

    public String getCommentLabel() {
        return comment;
    }

    public String getHeaderPart() {
        return headerPart;
    }

    public String getBrand() {
        return brand;
    }

    public String getPopulationLabel() {
        return population;
    }

    public String getResultLabel() {
        return result;
    }

    public String getCaptionLabel() {
        return caption;
    }

    public String getNumberLabel() {
        return number;
    }

    public String getAuthorYearLabel() {
        return authorYear;
    }

    /**
     * Local builder to provide named arguments for the various labels.
     * @author u.joss
     */
    public static class Builder {
        private final String headerPart;
        private final String brand;

        private String goals;
        private String methods;
        private String methodOutcome;
        private String resultMeasuredOutcome;
        private String methodStudyDesign;
        private String populationPlace;
        private String populationPariticpants;
        private String populationDuration;
        private String exposurePollutant;
        private String exposureAssessment;
        private String resultExposureRange;
        private String methodStatistics;
        private String methodConfounders;
        private String resultEffectEstimate;
        private String comment;
        private String population;
        private String result;
        private String caption;
        private String number;
        private String authorYear;

        public Builder(final String headerPart, final String brand) {
            this.headerPart = headerPart;
            this.brand = brand;
        }

        public Builder withGoals(final String goals) {
            this.goals = goals;
            return this;
        }

        public Builder withMethods(final String methods) {
            this.methods = methods;
            return this;
        }

        public Builder withMethodOutcome(final String methodOutcome) {
            this.methodOutcome = methodOutcome;
            return this;
        }

        public Builder withResultMeasuredOutcome(final String resultMeasuredOutcome) {
            this.resultMeasuredOutcome = resultMeasuredOutcome;
            return this;
        }

        public Builder withMethodStudyDesign(final String methodStudyDesign) {
            this.methodStudyDesign = methodStudyDesign;
            return this;
        }

        public Builder withPopulationPlace(final String populationPlace) {
            this.populationPlace = populationPlace;
            return this;
        }

        public Builder withPopulationPariticpants(final String populationPariticpants) {
            this.populationPariticpants = populationPariticpants;
            return this;
        }

        public Builder withPopulationDuration(final String populationDuration) {
            this.populationDuration = populationDuration;
            return this;
        }

        public Builder withExposurePollutant(final String exposurePollutant) {
            this.exposurePollutant = exposurePollutant;
            return this;
        }

        public Builder withExposureAssessment(final String exposureAssessment) {
            this.exposureAssessment = exposureAssessment;
            return this;
        }

        public Builder withResultExposureRange(final String resultExposureRange) {
            this.resultExposureRange = resultExposureRange;
            return this;
        }

        public Builder withMethodStatistics(final String methodStatistics) {
            this.methodStatistics = methodStatistics;
            return this;
        }

        public Builder withMethodConfounders(final String methodConfounders) {
            this.methodConfounders = methodConfounders;
            return this;
        }

        public Builder withResultEffectEstimate(final String resultEffectEstimate) {
            this.resultEffectEstimate = resultEffectEstimate;
            return this;
        }

        public Builder withComment(final String comment) {
            this.comment = comment;
            return this;
        }

        public Builder withPopulation(final String population) {
            this.population = population;
            return this;
        }

        public Builder withResult(final String result) {
            this.result = result;
            return this;
        }

        public Builder withCaption(final String caption) {
            this.caption = caption;
            return this;
        }

        public Builder withNumber(final String number) {
            this.number = number;
            return this;
        }

        public Builder withAuthorYear(final String authorYear) {
            this.authorYear = authorYear;
            return this;
        }

        public ReportHeaderFields build() {
            validate();
            return new ReportHeaderFields(this);
        }

        private void validate() {
        }

    }

}
