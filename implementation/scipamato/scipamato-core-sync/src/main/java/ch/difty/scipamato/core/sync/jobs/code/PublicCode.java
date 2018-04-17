package ch.difty.scipamato.core.sync.jobs.code;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;

import ch.difty.scipamato.publ.db.public_.tables.pojos.Code;

/**
 * Facade to the scipamato-public {@link Code} so we can refer to it with a name
 * that is clearly distinct from the scipamato-core code.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
class PublicCode {

    @Delegate
    private final Code delegate;

    @Builder
    private PublicCode(final String code, final String langCode, final Integer codeClassId, final String name,
        final String comment, final Integer sort, final Integer version, final Timestamp created,
        final Timestamp lastModified, final Timestamp lastSynched) {
        delegate = new Code();
        delegate.setCode(code);
        delegate.setLangCode(langCode);
        delegate.setCodeClassId(codeClassId);
        delegate.setName(name);
        delegate.setComment(comment);
        delegate.setSort(sort);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
    }

}
