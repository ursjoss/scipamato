package ch.difty.scipamato.web.jasper;

public class ReportHeaderFields {

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

    private ReportHeaderFields(Builder b) {
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

        public Builder(final String headerPart, final String brand) {
            this.headerPart = headerPart;
            this.brand = brand;
        }

        public Builder withGoals(String goals) {
            this.goals = goals;
            return this;
        }

        public Builder withMethods(String methods) {
            this.methods = methods;
            return this;
        }

        public Builder withMethodOutcome(String methodOutcome) {
            this.methodOutcome = methodOutcome;
            return this;
        }

        public Builder withResultMeasuredOutcome(String resultMeasuredOutcome) {
            this.resultMeasuredOutcome = resultMeasuredOutcome;
            return this;
        }

        public Builder withMethodStudyDesign(String methodStudyDesign) {
            this.methodStudyDesign = methodStudyDesign;
            return this;
        }

        public Builder withPopulationPlace(String populationPlace) {
            this.populationPlace = populationPlace;
            return this;
        }

        public Builder withPopulationPariticpants(String populationPariticpants) {
            this.populationPariticpants = populationPariticpants;
            return this;
        }

        public Builder withPopulationDuration(String populationDuration) {
            this.populationDuration = populationDuration;
            return this;
        }

        public Builder withExposurePollutant(String exposurePollutant) {
            this.exposurePollutant = exposurePollutant;
            return this;
        }

        public Builder withExposureAssessment(String exposureAssessment) {
            this.exposureAssessment = exposureAssessment;
            return this;
        }

        public Builder withResultExposureRange(String resultExposureRange) {
            this.resultExposureRange = resultExposureRange;
            return this;
        }

        public Builder withMethodStatistics(String methodStatistics) {
            this.methodStatistics = methodStatistics;
            return this;
        }

        public Builder withMethodConfounders(String methodConfounders) {
            this.methodConfounders = methodConfounders;
            return this;
        }

        public Builder withResultEffectEstimate(String resultEffectEstimate) {
            this.resultEffectEstimate = resultEffectEstimate;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
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
