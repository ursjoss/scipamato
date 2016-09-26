package ch.difty.sipamato.persistance.jooq.paper;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.GenericRepository;

public interface PaperRepository extends GenericRepository<PaperRecord, Paper, Long, PaperRecordMapper> {

}
