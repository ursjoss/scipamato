package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapper;

/**
 * {@link Paper} specific repository returning those entities by searchOrders.
 *
 * @author u.joss
 */
@Repository
public class JooqPaperBySearchOrderRepo extends JooqBySearchOrderRepo<Paper, PaperRecordMapper> implements PaperBackedSearchOrderRepository {

    public JooqPaperBySearchOrderRepo(DSLContext dsl, PaperRecordMapper mapper, JooqSortMapper<PaperRecord, Paper, ch.difty.sipamato.db.tables.Paper> sortMapper) {
        super(dsl, mapper, sortMapper);
    }

}
