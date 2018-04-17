package ch.difty.scipamato.core.persistence.paper;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.persistence.paper.searchorder.JooqBySearchOrderRepo;
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperBackedSearchOrderRepository;

/**
 * {@link Paper} specific repository returning those entities by searchOrders.
 *
 * @author u.joss
 */
@Repository
public class JooqPaperBySearchOrderRepo extends JooqBySearchOrderRepo<Paper, PaperRecordMapper>
    implements PaperBackedSearchOrderRepository {

    public JooqPaperBySearchOrderRepo(@Qualifier("dslContext") DSLContext dsl, PaperRecordMapper mapper,
        JooqSortMapper<PaperRecord, Paper, ch.difty.scipamato.core.db.tables.Paper> sortMapper) {
        super(dsl, mapper, sortMapper);
    }

}
