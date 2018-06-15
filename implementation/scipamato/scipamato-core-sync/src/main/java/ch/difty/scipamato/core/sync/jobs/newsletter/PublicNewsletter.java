package ch.difty.scipamato.core.sync.jobs.newsletter;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;

import ch.difty.scipamato.publ.db.public_.tables.pojos.Newsletter;

/**
 * Facade to the scipamato-public {@link Newsletter} so we can refer to it with a
 * name that is clearly distinct from the scipamato-core newsletter.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
class PublicNewsletter {

    @Delegate
    private final Newsletter delegate;

    @Builder
    private PublicNewsletter(final Integer id, final String issue, final Date issueDate, final Integer version,
        final Timestamp created, final Timestamp lastModified, final Timestamp lastSynched) {
        delegate = new Newsletter();
        delegate.setId(id);
        delegate.setIssue(issue);
        delegate.setIssueDate(issueDate);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
    }
}