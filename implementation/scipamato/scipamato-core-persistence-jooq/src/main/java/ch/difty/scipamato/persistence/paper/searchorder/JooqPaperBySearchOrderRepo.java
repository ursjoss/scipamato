package ch.difty.scipamato.persistence.paper.searchorder;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.persistence.JooqSortMapper;
import ch.difty.scipamato.persistence.paper.PaperRecordMapper;

/**
 * {@link Paper} specific repository returning those entities by searchOrders.
 *
 * @author u.joss
 */
@Repository
public class JooqPaperBySearchOrderRepo extends JooqBySearchOrderRepo<Paper, PaperRecordMapper> implements PaperBackedSearchOrderRepository {

    public JooqPaperBySearchOrderRepo(@Qualifier("dslContext") DSLContext dsl, PaperRecordMapper mapper, JooqSortMapper<PaperRecord, Paper, ch.difty.scipamato.db.tables.Paper> sortMapper) {
        super(dsl, mapper, sortMapper);
    }

}
