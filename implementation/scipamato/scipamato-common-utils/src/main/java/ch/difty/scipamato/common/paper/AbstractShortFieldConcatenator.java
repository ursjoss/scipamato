package ch.difty.scipamato.common.paper;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractShortFieldConcatenator {

    private final boolean includingLabels;

    protected AbstractShortFieldConcatenator(final boolean includingLabels) {
        this.includingLabels = includingLabels;
    }

    protected String methodsFrom(final String method, final String methodStudyDesign, final String methodOutcome,
        final String populationPlace, final String exposurePollutant, final String exposureAssessment,
        final String methodStatistics, final String methodConfounders) {
        return determineAppropriate(new Tuple(null, method), new Tuple("Study Design", methodStudyDesign),
            new Tuple("Outcome", methodOutcome), new Tuple("Place", populationPlace),
            new Tuple("Pollutant", exposurePollutant), new Tuple("Exposure Assessment", exposureAssessment),
            new Tuple("Statistical Method", methodStatistics), new Tuple("Confounders", methodConfounders));
    }

    protected String populationFrom(final String population, final String populationPlace,
        final String populationParticipants, final String populationDuration) {
        return determineAppropriate(new Tuple(null, population), new Tuple("Place", populationPlace),
            new Tuple("Participants", populationParticipants), new Tuple("Study Duration", populationDuration));
    }

    protected String resultFrom(final String result, final String resultExposureRange,
        final String resultEffectEstimate, final String resultMeasuredOutcome, final String conclusion) {
        return determineAppropriate(new Tuple(null, result), new Tuple("Exposure (Range)", resultExposureRange),
            new Tuple("Effect Estimate", resultEffectEstimate), new Tuple("Measured Outcome", resultMeasuredOutcome),
            new Tuple("Conclusion", conclusion));
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
                if (t.label != null && includingLabels)
                    sb
                        .append(t.label)
                        .append(": ");
                sb.append(t.content);
            }
        }
        return sb.toString();
    }

    private static class Tuple {
        private final String label;
        private final String content;

        private Tuple(final String label, final String content) {
            this.label = label;
            this.content = content;
        }
    }

}
