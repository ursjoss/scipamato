package ch.difty.scipamato.core.sync.jobs.codeclass;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.publ.db.tables.pojos.CodeClass;

/**
 * Facade to the scipamato-public {@link CodeClass} so we can refer to it with a
 * name that is clearly distinct from the scipamato-core code class.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
class PublicCodeClass {

    @Delegate
    private final CodeClass delegate;

    @Builder
    private PublicCodeClass(@NotNull final Integer codeClassId, @NotNull final String langCode,
        @NotNull final String name, @NotNull final String description, @Nullable final Integer version,
        @Nullable final Timestamp created, @Nullable final Timestamp lastModified,
        @NotNull final Timestamp lastSynched) {
        delegate = new CodeClass();
        delegate.setCodeClassId(codeClassId);
        delegate.setLangCode(langCode);
        delegate.setName(name);
        delegate.setDescription(description);
        delegate.setVersion(version);
        delegate.setCreated(created);
        delegate.setLastModified(lastModified);
        delegate.setLastSynched(lastSynched);
    }
}
