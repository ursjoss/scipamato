package ch.difty.scipamato.core.sync.jobs.paper;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;

import ch.difty.scipamato.publ.db.public_.tables.pojos.Paper;

/**
 * Facade to the scipamato-public {@link Paper} so we can refer to it with a
 * name that is clearly distinct from the scipamato-core paper.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 * <p>
 * There are some main fields (result, population and method) that could alternatively
 * be represented with a number of Short field (Kurzerfassung). If the main field is populated, it will always
 * have precedence, regardless of whether there's content in the respective short fields. If the main field is null,
 * all respective short fields with content are concatenated into the respective field in SciPaMaTo-Public. Note
 * that there's a known deficiency: The labels that are included with the short fields are always in english, they
 * will not adapt to the browser locale of a viewer.
 *
 * @author u.joss
 */
public class PublicPaper extends Paper {

    private static final long serialVersionUID = 1L;

    @Delegate
    private final Paper delegate;

    @Builder
    private PublicPaper(final Long id, final Long number, final Integer pmId, final String authors, final String title,
        final String location, final Integer publicationYear, final String goals, final String methods,
        final String population, final String result, final String comment, final Short[] codesPopulation,
        final Short[] codesStudyDesign, final String[] codes, final Integer version, final Timestamp created,
        final Timestamp lastModified, final Timestamp lastSynched, final String methodStudyDesign,
        final String methodOutcome, final String exposurePollutant, final String exposureAssessment,
        final String methodStatistics, final String methodConfounders, final String populationPlace,
        final String populationParticipants, final String populationDuration, final String resultExposureRange,
        final String resultEffectEstimate, final String resultMeasuredOutcome) {
        delegate = new Paper();
        delegate.setId(id);
        delegate.setNumber(number);
        delegate.setPmId(pmId);
        delegate.setAuthors(authors);
        delegate.setTitle(title);
        delegate.setLocation(location);
        delegate.setPublicationYear(publicationYear);
        delegate.setGoals(goals);
        delegate.setMethods(determineAppropriate(new Tuple(null, methods), new Tuple("Study Design", methodStudyDesign),
            new Tuple("Outcome", methodOutcome), new Tuple("Pollutant", exposurePollutant),
            new Tuple("Exposure Assessment", exposureAssessment), new Tuple("Statistical Method", methodStatistics),
            new Tuple("Confounders", methodConfounders)));
        delegate.setPopulation(
            determineAppropriate(new Tuple(null, population), new Tuple("Place/Country", populationPlace),
                new Tuple("Participants", populationParticipants), new Tuple("Study Duration", populationDuration)));
        delegate.setResult(
            determineAppropriate(new Tuple(null, result), new Tuple("Exposure (Range)", resultExposureRange),
                new Tuple("Effect Estimate", resultEffectEstimate),
                new Tuple("Measured Outcome", resultMeasuredOutcome)));
        delegate.setComment(comment);
        delegate.setCodesPopulation(codesPopulation);
        delegate.setCodesStudyDesign(codesStudyDesign);
        delegate.setCodes(codes);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
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
                sb
                    .append(t.label)
                    .append(": ")
                    .append(t.content);
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
