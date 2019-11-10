package ch.difty.scipamato.core.sync.jobs.newsletter;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.publ.db.tables.pojos.NewStudyTopic;

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
    private PublicNewStudyTopic(@NotNull final Integer newsletterId, @NotNull final Integer newsletterTopicId,
        @NotNull final Integer sort, @Nullable final Integer version, @Nullable final Timestamp created,
        @Nullable final Timestamp lastModified, @NotNull final Timestamp lastSynched) {
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