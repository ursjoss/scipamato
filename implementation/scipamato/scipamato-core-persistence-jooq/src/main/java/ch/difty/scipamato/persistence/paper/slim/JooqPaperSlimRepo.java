package ch.difty.scipamato.persistence.paper.slim;

import static ch.difty.scipamato.db.tables.Paper.*;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.PaperFilter;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.persistence.JooqReadOnlyRepo;
import ch.difty.scipamato.persistence.JooqSortMapper;
import ch.difty.scipamato.persistence.paging.PaginationContext;
import ch.difty.scipamato.persistence.paper.searchorder.PaperSlimBackedSearchOrderRepository;

@Repository
public class JooqPaperSlimRepo extends JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.scipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> implements PaperSlimRepository {

    private static final long serialVersionUID = 1L;

    private final PaperSlimBackedSearchOrderRepository searchOrderRepository;

    @Autowired
    public JooqPaperSlimRepo(@Qualifier("dslContext") DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.scipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<PaperFilter> filterConditionMapper, PaperSlimBackedSearchOrderRepository searchOrderRepository, ApplicationProperties applicationProperties) {
        super(dsl, mapper, sortMapper, filterConditionMapper, applicationProperties);
        this.searchOrderRepository = AssertAs.notNull(searchOrderRepository, "searchOrderRepository");
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
    protected ch.difty.scipamato.db.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    @Override
    protected TableField<PaperRecord, Integer> getRecordVersion() {
        return PAPER.VERSION;
    }

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findBySearchOrder(final SearchOrder searchOrder) {
        List<PaperSlim> papers = searchOrderRepository.findBySearchOrder(searchOrder);
        enrichAssociatedEntitiesOfAll(papers, null);
        return papers;
    }

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext) {
        final List<PaperSlim> entities = searchOrderRepository.findPageBySearchOrder(searchOrder, paginationContext);
        enrichAssociatedEntitiesOfAll(entities, null);
        return entities;
    }

    /** {@inheritDoc} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return searchOrderRepository.countBySearchOrder(searchOrder);
    }

}
