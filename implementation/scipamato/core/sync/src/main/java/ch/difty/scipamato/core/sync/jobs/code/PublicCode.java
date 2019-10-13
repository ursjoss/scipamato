package ch.difty.scipamato.core.sync.jobs.code;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.publ.db.tables.pojos.Code;

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
    private PublicCode(@NotNull final String code, @NotNull final String langCode, @NotNull final Integer codeClassId,
        @NotNull final String name, @Nullable final String comment, @NotNull final Integer sort,
        @Nullable final Integer version, @Nullable final Timestamp created, @Nullable final Timestamp lastModified,
        @NotNull final Timestamp lastSynched) {
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
