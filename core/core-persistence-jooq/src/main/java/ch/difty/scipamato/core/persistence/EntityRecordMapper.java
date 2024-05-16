package ch.difty.scipamato.core.persistence;

import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.RecordMapper;

import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * Abstract base class for entity to record mappers that have audit fields.
 * <p>
 * We need this workaround in order to access the audit fields from the record,
 * a class which is generated, so we can't simply implement an interface that
 * offers the audit methods for the record. Hence the dto AuditFields class that
 * needs to provide those fields from the concrete class.
 *
 * @param <R>
 *     the type of the {@link Record}
 * @param <T>
 *     the type of the {@link CoreEntity}
 * @author u.joss
 */
public abstract class EntityRecordMapper<R extends Record, T extends CoreEntity> implements RecordMapper<R, T> {

    @NotNull
    @Override
    public T map(@NotNull final R from) {
        final T to = makeEntity();
        mapFields(from, to);
        mapAuditFields(from, to);
        return to;
    }

    /**
     * @return an empty instance of entity {@code T}
     */
    @NotNull
    protected abstract T makeEntity();

    /**
     * @param record
     *     the record to provide the audit fields for
     * @return an instance of the {@link AuditFields} dto
     *     <p>
     *     {@code return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(), r.getVersion())}
     */
    @NotNull
    protected abstract AuditFields getAuditFieldsOf(@NotNull final R record);

    /**
     * Implement the mapping of the normal (i.e. non-audit) fields from record into
     * entity.
     *
     * @param from
     *     the record to provide the fields from db
     * @param to
     *     the entity to fill the fields into
     */
    protected abstract void mapFields(@NotNull final R from, @NotNull final T to);

    /**
     * Maps the audit fields form the record to the entity.
     *
     * @param from
     *     the record to provide the audit fields from db
     * @param to
     *     the entity to fill the audit fields into
     */
    private void mapAuditFields(@NotNull final R from, @NotNull final T to) {
        final AuditFields af = getAuditFieldsOf(from);
        to.setCreated(af.created);
        to.setCreatedBy(af.createdBy);
        to.setLastModified(af.lastModified);
        to.setLastModifiedBy(af.lastModifiedBy);
        to.setVersion(af.version);
    }
}
