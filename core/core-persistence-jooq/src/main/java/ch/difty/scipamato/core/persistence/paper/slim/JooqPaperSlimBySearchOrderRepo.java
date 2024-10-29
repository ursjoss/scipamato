package ch.difty.scipamato.core.persistence.paper.slim;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.db.tables.PaperNewsletter.PAPER_NEWSLETTER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.paper.searchorder.JooqBySearchOrderRepo;
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperSlimBackedSearchOrderRepository;

import org.springframework.util.StopWatch;

/**
 * {@link PaperSlim} specific repository returning those entities by
 * SearchOrders.
 *
 * @author u.joss
 */
@Repository
@Slf4j
@Profile("!wickettest")
public class JooqPaperSlimBySearchOrderRepo extends JooqBySearchOrderRepo<PaperSlim, PaperSlimRecordMapper>
    implements PaperSlimBackedSearchOrderRepository {

    public JooqPaperSlimBySearchOrderRepo(@NotNull @Qualifier("dslContext") final DSLContext dsl, @NotNull final PaperSlimRecordMapper mapper,
        @NotNull final JooqSortMapper<PaperRecord, PaperSlim, ch.difty.scipamato.core.db.tables.Paper> sortMapper) {
        super(dsl, mapper, sortMapper);
    }

    @NotNull
    @Override
    public List<PaperSlim> findPageBySearchOrder(@NotNull final SearchOrder searchOrder, @NotNull final PaginationContext pc) {
        final StopWatch watch = new StopWatch("JooqPaperSlimBySearchOrderRepo");
        watch.start();
        log.info("Start getting iterator page ({} of size {}) of PaperSlim entries for search order with id {}", pc.getOffset(), pc.getPageSize(), searchOrder.getId());

        final List<PaperSlim> results = new ArrayList<>();
        final Condition paperMatches = getConditionsFrom(searchOrder);
        final Collection<SortField<PaperSlim>> sortCriteria = getSortMapper().map(pc.getSort(), PAPER);
        for (final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> r : getBaseQuery()
            .where(paperMatches)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch())
            results.add(newPaperSlim(r));
        watch.stop();
        log.info("- iterator page ({} of size {}) of PaperSlim entries for search order with id {} in {} ms.", pc.getOffset(), pc.getPageSize(), searchOrder.getId(), watch.getTotalTimeMillis());
        return results;
    }

    private SelectOnConditionStep<Record9<Long, Long, String, Integer, String, Integer, String, Integer, String>> getBaseQuery() {
        return getDsl()
            .select(PAPER.ID, PAPER.NUMBER, PAPER.FIRST_AUTHOR, PAPER.PUBLICATION_YEAR, PAPER.TITLE, NEWSLETTER.ID, NEWSLETTER.ISSUE,
                NEWSLETTER.PUBLICATION_STATUS, PAPER_NEWSLETTER.HEADLINE)
            .from(PAPER)
            .leftOuterJoin(PAPER_NEWSLETTER)
            .on(PAPER.ID.eq(PAPER_NEWSLETTER.PAPER_ID))
            .leftOuterJoin(NEWSLETTER)
            .on(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(NEWSLETTER.ID));
    }

    private PaperSlim newPaperSlim(final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> r) {
        final Integer newsletterId = r.value6();
        if (newsletterId != null)
            return new PaperSlim(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), newsletterId, r.value7(), getStatusId(r), r.value9());
        else
            return new PaperSlim(r.value1(), r.value2(), r.value3(), r.value4(), r.value5());
    }

    private int getStatusId(final Record9<Long, Long, String, Integer, String, Integer, String, Integer, String> record) {
        return record.get(NEWSLETTER.PUBLICATION_STATUS.getName(), Integer.class);
    }
}
