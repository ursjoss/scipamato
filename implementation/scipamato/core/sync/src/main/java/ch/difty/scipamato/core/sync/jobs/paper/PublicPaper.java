package ch.difty.scipamato.core.sync.jobs.paper;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;

import ch.difty.scipamato.publ.db.tables.pojos.Paper;

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
        final Timestamp lastModified, final Timestamp lastSynched) {
        delegate = new Paper();
        delegate.setId(id);
        delegate.setNumber(number);
        delegate.setPmId(pmId);
        delegate.setAuthors(authors);
        delegate.setTitle(title);
        delegate.setLocation(location);
        delegate.setPublicationYear(publicationYear);
        delegate.setGoals(goals);
        delegate.setMethods(methods);
        delegate.setPopulation(population);
        delegate.setResult(result);
        delegate.setComment(comment);
        delegate.setCodesPopulation(codesPopulation);
        delegate.setCodesStudyDesign(codesStudyDesign);
        delegate.setCodes(codes);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
    }

}
