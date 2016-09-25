package ch.difty.sipamato.persistance.repository;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapper;

public interface PaperRepository extends GenericRepository<PaperRecord, Paper, Long, PaperRecordMapper> {

}
