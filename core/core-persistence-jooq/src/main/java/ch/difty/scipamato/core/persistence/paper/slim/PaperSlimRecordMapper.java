package ch.difty.scipamato.core.persistence.paper.slim;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.AuditFields;
import ch.difty.scipamato.core.persistence.EntityRecordMapper;

/**
 * Mapper mapping {@link PaperRecord} into entity {@link PaperSlim}
 *
 * @author u.joss
 */
@Component
public class PaperSlimRecordMapper extends EntityRecordMapper<PaperRecord, PaperSlim> {

    @NotNull
    @Override
    protected PaperSlim makeEntity() {
        return new PaperSlim();
    }

    @NotNull
    @Override
    protected AuditFields getAuditFieldsOf(@NotNull PaperRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(),
            r.getVersion());
    }

    @Override
    protected void mapFields(@NotNull PaperRecord from, @NotNull PaperSlim to) {
        to.setId(from.getId());
        to.setNumber(from.getNumber());
        to.setFirstAuthor(from.getFirstAuthor());
        to.setTitle(from.getTitle());
        to.setPublicationYear(from.getPublicationYear());
    }
}
