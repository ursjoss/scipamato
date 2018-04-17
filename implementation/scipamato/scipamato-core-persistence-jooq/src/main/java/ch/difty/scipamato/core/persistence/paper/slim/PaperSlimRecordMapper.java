package ch.difty.scipamato.core.persistence.paper.slim;

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

    @Override
    protected PaperSlim makeEntity() {
        return new PaperSlim();
    }

    @Override
    protected AuditFields getAuditFieldsOf(PaperRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(),
            r.getVersion());
    }

    @Override
    protected void mapFields(PaperRecord from, PaperSlim to) {
        to.setId(from.getId());
        to.setNumber(from.getNumber());
        to.setFirstAuthor(from.getFirstAuthor());
        to.setTitle(from.getTitle());
        to.setPublicationYear(from.getPublicationYear());
    }

}
