package ch.difty.sipamato.persistance.jooq.paper;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.persistance.jooq.GenericRepository;

public interface PaperRepository extends GenericRepository<PaperRecord, Paper, Long, PaperRecordMapper, PaperFilter> {

}
