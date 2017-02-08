package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.BySearchOrderFinder;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.DefaultBySearchOrderFinder;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqPaperSlimRepo extends JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> implements PaperSlimRepository {

    private static final long serialVersionUID = 1L;

    private final BySearchOrderFinder<PaperSlim> bySearchOrderFinder;

    @Autowired
    public JooqPaperSlimRepo(DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<PaperFilter> filterConditionMapper, Localization localization) {
        super(dsl, mapper, sortMapper, filterConditionMapper, localization);
        bySearchOrderFinder = new DefaultBySearchOrderFinder<>(dsl, mapper, sortMapper, getRecordClass());
    }

    @Override
    protected Class<? extends PaperSlim> getEntityClass() {
        return PaperSlim.class;
    }

    @Override
    protected Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findBySearchOrder(final SearchOrder searchOrder) {
        List<PaperSlim> papers = bySearchOrderFinder.findBySearchOrder(searchOrder);
        enrichAssociatedEntitiesOfAll(papers);
        return papers;
    }

    /** {@inheritDoc} */
    @Override
    public Page<PaperSlim> findBySearchOrder(SearchOrder searchOrder, Pageable pageable) {
        final List<PaperSlim> entities = bySearchOrderFinder.findPagedBySearchOrder(searchOrder, pageable);
        enrichAssociatedEntitiesOfAll(entities);
        return new PageImpl<>(entities, pageable, (long) countBySearchOrder(searchOrder));
    }

    /** {@inheritDoc} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return bySearchOrderFinder.countBySearchOrder(searchOrder);
    }

}
