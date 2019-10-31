package ch.difty.scipamato.core.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;

/**
 * Data transfer object to access the audit fields of the entity record class.
 */
public class AuditFields {
    final LocalDateTime created;
    final LocalDateTime lastModified;
    final Integer       createdBy;
    final Integer       lastModifiedBy;
    final int           version;

    public AuditFields(@Nullable final Timestamp c, @Nullable final Integer cb, @Nullable final Timestamp lm,
        @Nullable final Integer lmb, @Nullable final Integer v) {
        created = c != null ? c.toLocalDateTime() : null;
        createdBy = cb;
        lastModified = lm != null ? lm.toLocalDateTime() : null;
        lastModifiedBy = lmb;
        version = v != null ? v : 1;
    }
}