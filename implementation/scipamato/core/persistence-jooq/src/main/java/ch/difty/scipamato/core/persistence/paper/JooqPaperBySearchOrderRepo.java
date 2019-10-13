package ch.difty.scipamato.core.persistence.paper;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
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
@Profile("!wickettest")
public class JooqPaperBySearchOrderRepo extends JooqBySearchOrderRepo<Paper, PaperRecordMapper>
    implements PaperBackedSearchOrderRepository {

    public JooqPaperBySearchOrderRepo(@Qualifier("dslContext") @NotNull final DSLContext dsl,
        @NotNull final PaperRecordMapper mapper,
        @NotNull final JooqSortMapper<PaperRecord, Paper, ch.difty.scipamato.core.db.tables.Paper> sortMapper) {
        super(dsl, mapper, sortMapper);
    }

}
