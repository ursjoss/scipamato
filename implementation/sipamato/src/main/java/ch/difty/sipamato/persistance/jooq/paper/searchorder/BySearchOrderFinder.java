package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import java.util.List;

import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.SearchOrder;

/**
 * Helper to find papers or paperSlims based on SearchOrder specifications.
 *
 * @author u.joss
 *
 * @param <T>
 *      derivatives of {@link IdSipamatoEntity<Long>}, should actually be Paper or PaperSlim
 */
public interface BySearchOrderFinder<T extends IdSipamatoEntity<Long>> {

    /**
     * Finds all entities of type <code>T</code> matching the provided {@link SearchOrder} specification.
     *
     * @param searchOrder {@link SearchOrder} the search specification
     * @return list of entities
     */
    List<T> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds all entities of type <code>T</code> matching the provided {@link SearchOrder} specification, returned in pages.
     * Should be wrapped into a Pageable by the caller before returning to its clients
     *
     * @see #findBySearchOrder(SearchOrder)
     *
     * @return list of entities
     */
    List<T> findPagedBySearchOrder(SearchOrder searchOrder, Pageable pageable);

    /**
     * Counts all persisted entities of type <code>T</code> matching the provided {@link SearchOrder} specification.
     *
     * @param searchOrder the search specification
     * @return T entity count
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
