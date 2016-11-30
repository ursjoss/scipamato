package ch.difty.sipamato.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.entity.filter.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;

/**
 * The {@link PaperSlimService} interface - defining {@link PaperSlim} specific service methods.
 *
 * @author u.joss
 */
public interface PaperSlimService extends ReadOnlyService<PaperSlim, Long, SimplePaperFilter> {

    /**
     * Find any paper matching the provided {@link CompositeComplexPaperFilter}.
     *
     * @param compositeFilter {@link CompositeComplexPaperFilter}
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findByFilter(CompositeComplexPaperFilter compositeFilter);

    /**
     * Finds a page of records of type <literal>T</literal> matching the provided {@link CompositeComplexPaperFilter},
     * returned in pages.
     *
     * @param compositeFilter the filter
     * @param pageable defining paging and sorting
     * @return a list of entities of type <literal>T</literal>
     */
    Page<PaperSlim> findByFilter(CompositeComplexPaperFilter compositeFilter, Pageable pageable);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param compositeFilter {@link CompositeComplexPaperFilter}
     * @return entity count
     */
    int countByFilter(CompositeComplexPaperFilter compositeFilter);

}
