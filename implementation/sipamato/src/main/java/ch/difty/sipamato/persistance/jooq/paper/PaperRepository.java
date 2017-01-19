package ch.difty.sipamato.persistance.jooq.paper;

import java.util.List;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface PaperRepository extends EntityRepository<PaperRecord, Paper, Long, PaperRecordMapper, PaperFilter> {

    /**
     * Find Papers with the provided ids.
     * @param ids
     * @return list of papers
     */
    List<Paper> findByIds(List<Long> ids);

}
