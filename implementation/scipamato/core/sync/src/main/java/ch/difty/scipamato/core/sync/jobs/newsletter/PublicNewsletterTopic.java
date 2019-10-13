package ch.difty.scipamato.core.sync.jobs.newsletter;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.publ.db.tables.pojos.NewsletterTopic;

/**
 * Facade to the scipamato-public {@link NewsletterTopic} so we can refer to it with a
 * name that is clearly distinct from the scipamato-core newsletter.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
class PublicNewsletterTopic {

    @Delegate
    private final NewsletterTopic delegate;

    @Builder
    private PublicNewsletterTopic(@NotNull final Integer id, @NotNull final String langCode,
        @NotNull final String title, @Nullable final Integer version, @Nullable final Timestamp created,
        @Nullable final Timestamp lastModified, @NotNull final Timestamp lastSynched) {
        delegate = new NewsletterTopic();
        delegate.setId(id);
        delegate.setLangCode(langCode);
        delegate.setTitle(title);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
    }
}