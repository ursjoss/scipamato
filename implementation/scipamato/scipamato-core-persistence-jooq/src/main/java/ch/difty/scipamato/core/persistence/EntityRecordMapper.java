package ch.difty.scipamato.core.persistence;

import org.jooq.Record;
import org.jooq.RecordMapper;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * Abstract base class for entity to record mappers that have audit fields.
 * 
 * We need this workaround in order to access the audit fields from the record,
 * a class which is generated, so we can't simply implement an interface that
 * offers the audit methods for the record. Hence the dto AuditFields class that
 * needs to provide those fields from the concrete class.
 *
 * @author u.joss
 *
 * @param <R>
 *            the type of the {@link Record}
 * @param <T>
 *            the type of the {@link CoreEntity}
 */
public abstract class EntityRecordMapper<R extends Record, T extends CoreEntity> implements RecordMapper<R, T> {

    /** {@inheritDoc} */
    @Override
    public T map(R from) {
        AssertAs.notNull(from, "from");
        T to = makeEntity();
        mapFields(from, to);
        mapAuditFields(from, to);
        return to;
    }

    /**
     * @return an empty instance of entity {@code T}
     */
    protected abstract T makeEntity();

    /**
     * @param record
     *            the record to provide the audit fields for
     * @return an instance of the {@link AuditFields} dto
     *
     *         {@code return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(), r.getVersion())}
     */
    protected abstract AuditFields getAuditFieldsOf(R record);

    /**
     * Implement the mapping of the normal (i.e. non-audit) fields from record into
     * entity.
     *
     * @param from
     *            the record to provide the fields from db
     * @param to
     *            the entity to fill the fields into
     */
    protected abstract void mapFields(R from, T to);

    /**
     * Maps the audit fields form the record to the entity.
     *
     * @param from
     *            the record to provide the audit fields from db
     * @param to
     *            the entity to fill the audit fields into
     */
    private void mapAuditFields(R from, T to) {
        final AuditFields af = getAuditFieldsOf(from);
        to.setCreated(af.created);
        to.setCreatedBy(af.createdBy);
        to.setLastModified(af.lastModified);
        to.setLastModifiedBy(af.lastModifiedBy);
        to.setVersion(af.version);
    }

}
