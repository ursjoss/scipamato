package ch.difty.scipamato.common.paper;

import org.apache.commons.lang3.StringUtils;

/**
 * Abstract base class for ShortFieldConcatenator implementations, enforcing the same short fields to be concatenated
 * into the respective main fields in different places in the application.
 * <p>
 * Each of the three methods (for method, population and result) has two variants, one passing in the values only,
 * prepending an English static label to each value. The other one with dynamic labels that can be localized according
 * to the users language preference.
 */
public abstract class AbstractShortFieldConcatenator {

    protected String methodsFrom(final String method, final String methodStudyDesign, final String methodOutcome,
        final String populationPlace, final String exposurePollutant, final String exposureAssessment,
        final String methodStatistics, final String methodConfounders) {
        return methodsFrom(method, new Tuple("Study Design", methodStudyDesign), new Tuple("Outcome", methodOutcome),
            new Tuple("Place", populationPlace), new Tuple("Pollutant", exposurePollutant),
            new Tuple("Exposure Assessment", exposureAssessment), new Tuple("Statistical Method", methodStatistics),
            new Tuple("Confounders", methodConfounders));
    }

    protected String methodsFrom(final String method, final Tuple methodStudyDesign, final Tuple methodOutcome,
        final Tuple populationPlace, final Tuple exposurePollutant, final Tuple exposureAssessment,
        final Tuple methodStatistics, final Tuple methodConfounders) {
        return determineAppropriate(new Tuple(null, method), methodStudyDesign, methodOutcome, populationPlace,
            exposurePollutant, exposureAssessment, methodStatistics, methodConfounders);
    }

    protected String populationFrom(final String population, final String populationPlace,
        final String populationParticipants, final String populationDuration) {
        return populationFrom(population, new Tuple("Place", populationPlace),
            new Tuple("Participants", populationParticipants), new Tuple("Study Duration", populationDuration));
    }

    protected String populationFrom(final String population, final Tuple populationPlace,
        final Tuple populationParticipants, final Tuple populationDuration) {
        return determineAppropriate(new Tuple(null, population), populationPlace, populationParticipants,
            populationDuration);
    }

    protected String resultFrom(final String result, final String resultExposureRange,
        final String resultEffectEstimate, final String resultMeasuredOutcome, final String conclusion) {
        return resultFrom(result, new Tuple("Exposure (Range)", resultExposureRange),
            new Tuple("Effect Estimate", resultEffectEstimate), new Tuple("Measured Outcome", resultMeasuredOutcome),
            new Tuple("Conclusion", conclusion));
    }

    protected String resultFrom(final String result, final Tuple resultExposureRange, final Tuple resultEffectEstimate,
        final Tuple resultMeasuredOutcome, final Tuple conclusion) {
        return determineAppropriate(new Tuple(null, result), resultExposureRange, resultEffectEstimate,
            resultMeasuredOutcome, conclusion);
    }

    /**
     * Use the mainField if non-null and not blank. Concatenate all other fields
     * (separated by ' - ') otherwise. Working with tuples of a label (English only)
     * and the value
     *
     * @param mainField
     *     the main field with precedence
     * @param shortFields
     *     one or more short fields to be used in case of a null/blank main field
     * @return appropriate field value
     */
    private String determineAppropriate(final Tuple mainField, final Tuple... shortFields) {
        if (mainField.content != null)
            return mainField.content;
        return concatShortFields(shortFields);
    }

    private String concatShortFields(final Tuple[] shortFields) {
        final StringBuilder sb = new StringBuilder();
        for (final Tuple t : shortFields) {
            if (StringUtils.isNotBlank(t.content)) {
                if (sb.length() > 0)
                    sb.append(" / ");
                if (t.label != null)
                    sb
                        .append(t.label)
                        .append(": ");
                sb.append(t.content);
            }
        }
        return sb.toString();
    }

    protected static class Tuple {
        private final String label;
        private final String content;

        public Tuple(final String label, final String content) {
            this.label = label;
            this.content = content;
        }
    }

}
