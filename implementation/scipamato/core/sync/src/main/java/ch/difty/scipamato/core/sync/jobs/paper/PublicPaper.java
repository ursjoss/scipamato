package ch.difty.scipamato.core.sync.jobs.paper;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private PublicPaper(@NotNull final Long id, @NotNull final Long number, @Nullable final Integer pmId,
        @Nullable final String authors, @Nullable final String title, @Nullable final String location,
        @Nullable final Integer publicationYear, @Nullable final String goals, @Nullable final String methods,
        @Nullable final String population, @Nullable final String result, @Nullable final String comment,
        @NotNull final Short[] codesPopulation, @NotNull final Short[] codesStudyDesign, @NotNull final String[] codes,
        @Nullable final Integer version, @Nullable final Timestamp created, @Nullable final Timestamp lastModified,
        @NotNull final Timestamp lastSynched) {
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
