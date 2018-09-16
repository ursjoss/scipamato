package ch.difty.scipamato.core.persistence.paper.slim;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.db.tables.PaperNewsletter.PAPER_NEWSLETTER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.JooqReadOnlyRepo;
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperSlimBackedSearchOrderRepository;

@Repository
public class JooqPaperSlimRepo extends
    JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.scipamato.core.db.tables.Paper, PaperSlimRecordMapper, PaperFilter>
    implements PaperSlimRepository {

    private final PaperSlimBackedSearchOrderRepository searchOrderRepository;

    public JooqPaperSlimRepo(@Qualifier("dslContext") DSLContext dsl, PaperSlimRecordMapper mapper,
        JooqSortMapper<PaperRecord, PaperSlim, ch.difty.scipamato.core.db.tables.Paper> sortMapper,
        GenericFilterConditionMapper<PaperFilter> filterConditionMapper,
        PaperSlimBackedSearchOrderRepository searchOrderRepository, DateTimeService dateTimeService,
        ApplicationProperties applicationProperties) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, applicationProperties);
        this.searchOrderRepository = AssertAs.notNull(searchOrderRepository, "searchOrderRepository");
    }

    @Override
    protected ch.difty.scipamato.core.db.tables.Paper getTable() {
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

    @Override
    public PaperSlim findById(final Long id, final String languageCode) {
        AssertAs.notNull(id, "id");
        final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> record = getBaseQuery()
            .where(getTableId().equal(id))
            .fetchOne();
        if (record != null)
            return new PaperSlim(record.value1(), record.value2(), record.value3(), record.value4(), record.value5(),
                record.value6(), record.value7(), getStatusId(record), record.value9());
        else
            return null;
    }

    private SelectOnConditionStep<Record9<Long, Long, String, Integer, String, Integer, String, Integer, String>> getBaseQuery() {
        return getDsl()
            .select(PAPER.ID, PAPER.NUMBER, PAPER.FIRST_AUTHOR, PAPER.PUBLICATION_YEAR, PAPER.TITLE, NEWSLETTER.ID,
                NEWSLETTER.ISSUE, NEWSLETTER.PUBLICATION_STATUS, PAPER_NEWSLETTER.HEADLINE)
            .from(PAPER)
            .leftOuterJoin(PAPER_NEWSLETTER)
            .on(PAPER.ID.eq(PAPER_NEWSLETTER.PAPER_ID))
            .leftOuterJoin(NEWSLETTER)
            .on(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(NEWSLETTER.ID));
    }

    private int getStatusId(
        final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> record) {
        return record.get(NEWSLETTER.PUBLICATION_STATUS.getName(), Integer.class);
    }

    @Override
    public PaperSlim findById(final Long id, final int version, String languageCode) {
        AssertAs.notNull(id, "id");
        final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> record = getBaseQuery()
            .where(getTableId().equal(id))
            .and(getRecordVersion().equal(version))
            .fetchOne();
        if (record != null)
            return newPaperSlim(record);
        else
            return null;
    }

    @Override
    public List<PaperSlim> findAll(String languageCode) {
        final List<PaperSlim> results = new ArrayList<>();
        for (final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> r : getBaseQuery().fetch())
            results.add(newPaperSlim(r));
        return results;
    }

    private PaperSlim newPaperSlim(
        final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> r) {
        final Integer newsletterId = r.value6();
        if (newsletterId != null)
            return new PaperSlim(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), newsletterId, r.value7(),
                getStatusId(r), r.value9());
        else
            return new PaperSlim(r.value1(), r.value2(), r.value3(), r.value4(), r.value5());
    }

    @Override
    public List<PaperSlim> findPageByFilter(final PaperFilter filter, final PaginationContext pc,
        final String languageCode) {
        final List<PaperSlim> results = new ArrayList<>();
        final Condition conditions = getFilterConditionMapper().map(filter);
        final Collection<SortField<PaperSlim>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        for (final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> r : getBaseQuery()
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch())
            results.add(newPaperSlim(r));
        return results;
    }

    @Override
    public List<PaperSlim> findBySearchOrder(final SearchOrder searchOrder) {
        List<PaperSlim> papers = searchOrderRepository.findBySearchOrder(searchOrder);
        enrichAssociatedEntitiesOfAll(papers, null);
        return papers;
    }

    @Override
    public List<PaperSlim> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext) {
        final List<PaperSlim> entities = searchOrderRepository.findPageBySearchOrder(searchOrder, paginationContext);
        enrichAssociatedEntitiesOfAll(entities, null);
        return entities;
    }

    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return searchOrderRepository.countBySearchOrder(searchOrder);
    }

}
