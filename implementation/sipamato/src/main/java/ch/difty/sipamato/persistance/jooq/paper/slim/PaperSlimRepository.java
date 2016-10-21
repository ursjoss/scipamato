package ch.difty.sipamato.persistance.jooq.paper.slim;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, PaperFilter> {

}
