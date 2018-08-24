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
        final String methodOutcome, final String methodStatistics, final String methodConfounders,
        final String populationPlace, final String populationParticipants, final String populationDuration,
        final String resultExposureRange, final String resultEffectEstimate, final String resultMeasuredOutcome) {
        delegate = new Paper();
        delegate.setId(id);
        delegate.setNumber(number);
        delegate.setPmId(pmId);
        delegate.setAuthors(authors);
        delegate.setTitle(title);
        delegate.setLocation(location);
        delegate.setPublicationYear(publicationYear);
        delegate.setGoals(goals);
        delegate.setMethods(
            determineAppropriate(methods, methodStudyDesign, methodOutcome, methodStatistics, methodConfounders));
        delegate.setPopulation(
            determineAppropriate(population, populationPlace, populationParticipants, populationDuration));
        delegate.setResult(
            determineAppropriate(result, resultExposureRange, resultEffectEstimate, resultMeasuredOutcome));
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
     * (separated by ' - ') otherwise.
     *
     * @param mainField
     *     the main field with precedence
     * @param shortFields
     *     one or more short fields to be used in case of a null/blank main field
     * @return appropriate field value
     */
    private String determineAppropriate(final String mainField, final String... shortFields) {
        if (mainField != null)
            return mainField;
        return concatShortFields(shortFields);
    }

    private String concatShortFields(final String[] shortFields) {
        final StringBuilder sb = new StringBuilder();
        for (final String sf : shortFields) {
            if (StringUtils.isNotBlank(sf)) {
                if (sb.length() > 0)
                    sb.append(" - ");
                sb.append(sf);
            }
        }
        return sb.toString();
    }

}
