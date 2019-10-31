package ch.difty.scipamato.core.sync.jobs.keyword;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.publ.db.tables.pojos.Keyword;

/**
 * Facade to the scipamato-public {@link Keyword} so we can refer to it with a name
 * that is clearly distinct from the scipamato-core keyword.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
class PublicKeyword {

    @Delegate
    private final Keyword delegate;

    @Builder
    private PublicKeyword(final int id, final int keywordId, @NotNull final String langCode, @NotNull final String name,
        @Nullable final Integer version, @Nullable final Timestamp created, @Nullable final Timestamp lastModified,
        @NotNull final Timestamp lastSynched, @Nullable final String searchOverride) {
        delegate = new Keyword();
        delegate.setId(id);
        delegate.setKeywordId(keywordId);
        delegate.setLangCode(langCode);
        delegate.setName(name);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
        delegate.setSearchOverride(searchOverride);
    }
}
