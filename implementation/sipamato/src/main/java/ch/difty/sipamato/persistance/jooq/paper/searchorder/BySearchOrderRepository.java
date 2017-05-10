package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import java.util.List;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.paging.PaginationContext;

/**
 * Repository to find Papers or PaperSlims by {@link SearchOrder} specifications.
 *
 * @author u.joss
 *
 * @param <T>
 *      derivatives of {@link IdSipamatoEntity<Long>}, should actually be Paper or PaperSlim
 */
public interface BySearchOrderRepository<T extends IdSipamatoEntity<Long>> {

    /**
     * Finds all entities of type {@code T} matching the provided {@link SearchOrder} specification.
     *
     * @param searchOrder {@link SearchOrder} the search specification
     * @return list of entities
     */
    List<T> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds a single page of entities of type {@code T} matching the provided {@link SearchOrder} and {@link PaginationContext}.
     *
     * @see #findBySearchOrder(SearchOrder)
     *
     * @return paged list of entities 
     */
    List<T> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * Counts all persisted entities of type {@code T} matching the provided {@link SearchOrder} specification.
     *
     * @param searchOrder the search specification
     * @return T entity count
     */
    int countBySearchOrder(SearchOrder searchOrder);

}
