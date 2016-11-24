package ch.difty.sipamato.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;

/**
 * The {@link PaperSlimService} interface - defining {@link PaperSlim} specific service methods.
 *
 * @author u.joss
 */
public interface PaperSlimService extends ReadOnlyService<PaperSlim, Long, SimplePaperFilter> {

    /**
     * Find any paper matching the provided {@link ComplexPaperFilter}.
     *
     * @param filter {@link ComplexPaperFilter}
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findByFilter(ComplexPaperFilter filter);

    /**
     * Finds a page of records of type <literal>T</literal> matching the provided filter.
     *
     * @param filter the filter
     * @param pageable defining paging and sorting
     * @return a list of entities of type <literal>T</literal>
     */
    Page<PaperSlim> findByFilter(ComplexPaperFilter filter, Pageable pageable);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     * @return entity count
     */
    int countByFilter(ComplexPaperFilter filter);

}
