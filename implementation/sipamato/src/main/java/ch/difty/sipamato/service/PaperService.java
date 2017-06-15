package ch.difty.sipamato.service;

import java.util.List;
import java.util.Optional;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service methods.
 *
 * @author u.joss
 */
public interface PaperService extends EntityService<Long, Paper, PaperFilter> {

    /**
     * Find any paper matching the provided {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return list of {@link Paper}s
     */
    List<Paper> findBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds a page full of papers as list matching the provided {@link SearchOrder} and {@link PaginationContext}.
     *
     * @param searchOrder the filter
     * @param paginationContext context defining paging and sorting
     * @return paged list of papers
     */
    List<Paper> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * Finds a page full of ids of papers as list matching the provided {@link SearchOrder} and {@link PaginationContext}.
     *
     * @param searchOrder the filter
     * @param paginationContext context defining paging and sorting
     * @return paged list of paper ids
     */
    List<Long> findPageOfIdsBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified {@link SearchOrder}.
     *
     * @param searchOrder {@link SearchOrder}
     * @return paper count
     */
    int countBySearchOrder(SearchOrder searchOrder);

    /**
     * Saves some minimally filled {@link Paper}s based on the information received
     * from PubMed via the {@link PubmedArticleFacade} entities.
     * 
     * @param articles list of {@link PubmedArticleFacade}
     * @param minimumNumber The lowest number that could be recycled
     * @return {@link ServiceResult}
     */
    ServiceResult dumpPubmedArticlesToDb(List<PubmedArticleFacade> articles, long minimumNumber);

    /**
     * Finds an individual paper by number. Returns it as an optional of type {@code T}
     *
     * @param number - must not be null
     * @return Optional
     * @throws NullArgumentException if number is null
     */
    Optional<Paper> findByNumber(Long number);

    /**
     * Finds the lowest free number starting from the supplied value.
     * @param minimumPaperNumberToBeRecycled any gaps lower than this value will not be recycled
     */
    long findLowestFreeNumberStartingFrom(long minimumPaperNumberToBeRecycled);

    /**
     * Deletes the papers with the given ids
     * @param ids list of ids
     */
    void deleteByIds(List<Long> ids);

}
