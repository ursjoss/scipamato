package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.persistance.jooq.JooqSortMapper;
import ch.difty.scipamato.persistance.jooq.paper.slim.PaperSlimRecordMapper;

/**
 * {@link PaperSlim} specific repository returning those entities by SearchOrders.
 *
 * @author u.joss
 */
@Repository
public class JooqPaperSlimBySearchOrderRepo extends JooqBySearchOrderRepo<PaperSlim, PaperSlimRecordMapper> implements PaperSlimBackedSearchOrderRepository {

    public JooqPaperSlimBySearchOrderRepo(DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.scipamato.db.tables.Paper> sortMapper) {
        super(dsl, mapper, sortMapper);
    }

}
