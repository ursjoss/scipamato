package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.entity.filter.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, SimplePaperFilter> {

    /**
     * Finds all {@link PaperSlim}s matching the provided {@link CompositeComplexPaperFilter}.
     *
     * <ul>
     * <li>strings are matched <code>contain</code> like ignoring case</li>
     * <li>numbers are matched exactly</li>
     * </ul>
     *
     * @param compositeFilter {@link CompositeComplexPaperFilter} the combined search specification
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findByFilter(CompositeComplexPaperFilter compositeFilter);

    /**
     * Finds all {@link PaperSlim}s matching the provided {@link CompositeComplexPaperFilter}, returned in pages.
     *
     * @see #findByFilter(CompositeComplexPaperFilter)
     *
     * @return list of {@link PaperSlim}s
     */
    Page<PaperSlim> findByFilter(CompositeComplexPaperFilter filter, Pageable pageable);

    /**
     * Counts all persisted entities matching the provided {@link CompositeComplexPaperFilter}.
     *
     * @param compositeFilter {@link CompositeComplexPaperFilter} the combined search specification
     * @return paper count
     */
    int countByFilter(CompositeComplexPaperFilter filter);

}
