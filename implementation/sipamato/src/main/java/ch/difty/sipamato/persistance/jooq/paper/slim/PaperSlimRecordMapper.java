package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.AuditFields;
import ch.difty.sipamato.persistance.jooq.EntityRecordMapper;

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
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(), r.getVersion());
    }

    @Override
    protected void mapFields(PaperRecord from, PaperSlim to) {
        to.setId(from.getId());
        to.setFirstAuthor(from.getFirstAuthor());
        to.setTitle(from.getTitle());
        to.setPublicationYear(from.getPublicationYear());
    }

}
