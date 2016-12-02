package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, PaperFilter> {

    /**
     * Finds all {@link PaperSlim}s matching the provided {@link SearchOrder} specification.
     *
     * @param searchOrder {@link SearchOrder} the search specification
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds all {@link PaperSlim}s matching the provided {@link SearchOrder} specification, returned in pages.
     *
     * @see #findBySearchOrder(SearchOrder)
     *
     * @return list of {@link PaperSlim}s
     */
    Page<PaperSlim> findBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * Counts all persisted entities matching the provided {@link SearchOrder} specification.
     *
     * @param searchOrder the search specification
     * @return paper count
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
