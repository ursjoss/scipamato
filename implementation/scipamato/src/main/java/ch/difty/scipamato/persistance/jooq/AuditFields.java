package ch.difty.scipamato.persistance.jooq;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Data transfer object to access the audit fields of the entity record class.
 */
public class AuditFields {
    final LocalDateTime created;
    final LocalDateTime lastModified;
    final Integer createdBy;
    final Integer lastModifiedBy;
    final int version;

    public AuditFields(final Timestamp c, final Integer cb, final Timestamp lm, final Integer lmb, final Integer v) {
        created = c != null ? c.toLocalDateTime() : null;
        createdBy = cb;
        lastModified = lm != null ? lm.toLocalDateTime() : null;
        lastModifiedBy = lmb;
        version = v != null ? v : 1;
    }
}