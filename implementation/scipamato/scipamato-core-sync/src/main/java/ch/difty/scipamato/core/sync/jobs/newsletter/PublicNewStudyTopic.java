package ch.difty.scipamato.core.sync.jobs.newsletter;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;

import ch.difty.scipamato.publ.db.public_.tables.pojos.NewStudyTopic;

/**
 * Facade to the scipamato-public {@link NewStudyTopic} so we can refer to it with a
 * name that is clearly distinct from the scipamato-core newsletter.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
class PublicNewStudyTopic {

    @Delegate
    private final NewStudyTopic delegate;

    @Builder
    private PublicNewStudyTopic(final Integer newsletterId, final Integer newsletterTopicId, final Integer sort,
        final Integer version, final Timestamp created, final Timestamp lastModified, final Timestamp lastSynched) {
        delegate = new NewStudyTopic();
        delegate.setNewsletterId(newsletterId);
        delegate.setNewsletterTopicId(newsletterTopicId);
        delegate.setSort(sort);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
    }
}