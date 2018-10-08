package ch.difty.scipamato.core.sync.jobs.keyword;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;

import ch.difty.scipamato.publ.db.public_.tables.pojos.Keyword;

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
    private PublicKeyword(final int id, final int keywordId, final String langCode, final String name,
        final Integer version, final Timestamp created, final Timestamp lastModified, final Timestamp lastSynched,
        final String searchOverride) {
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
