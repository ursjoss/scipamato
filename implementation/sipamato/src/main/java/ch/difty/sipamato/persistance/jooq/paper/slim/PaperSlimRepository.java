package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, SimplePaperFilter> {

    /**
     * Finds all {@link PaperSlim}s matching the provided {@link ComplexPaperFilter}.
     *
     * <ul>
     * <li>strings are matched <code>contain</code> like ignoring case</li>
     * <li>numbers are matched exactly</li>
     * </ul>
     *
     * @param {@link ComplexPaperFilter}
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findByFilter(ComplexPaperFilter filter);

    /**
     * Finds all persisted entities matching the provided filter.
     *
     * @return list of all matching entities <code>T</code>
     */
    Page<PaperSlim> findByFilter(ComplexPaperFilter filter, Pageable pageable);

    /**
     * Counts all persisted entities matching the provided filter.
     *
     * @return list of all matching entities <code>T</code>
     */
    int countByFilter(ComplexPaperFilter filter);

}
