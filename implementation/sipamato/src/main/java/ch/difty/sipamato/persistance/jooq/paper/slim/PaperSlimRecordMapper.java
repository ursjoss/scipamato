package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Mapper mapping {@link PaperRecord} into entity {@link PaperSlim}
 *
 * @author u.joss
 */
@Component
public class PaperSlimRecordMapper implements RecordMapper<PaperRecord, PaperSlim> {

    /** {@inheritDoc} */
    @Override
    public PaperSlim map(PaperRecord from) {
        AssertAs.notNull(from, "from");
        PaperSlim to = new PaperSlim();
        to.setId(from.getId());
        to.setFirstAuthor(from.getFirstAuthor());
        to.setTitle(from.getTitle());
        to.setPublicationYear(from.getPublicationYear());

        to.setCreated(from.getCreated() != null ? from.getCreated().toLocalDateTime() : null);
        to.setCreatedBy(from.getCreatedBy());
        to.setLastModified(from.getLastModified() != null ? from.getLastModified().toLocalDateTime() : null);
        to.setLastModifiedBy(from.getLastModifiedBy());
        to.setVersion(from.getVersion() != null ? from.getVersion() : 1);

        return to;
    }

}
