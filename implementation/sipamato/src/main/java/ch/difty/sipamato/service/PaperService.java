package ch.difty.sipamato.service;

import java.util.List;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
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
     * @param pagination context defining paging and sorting
     * @return paged list of papers
     */
    List<Paper> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

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
     * @return {@link ServiceResult}
     */
    ServiceResult dumpPubmedArticlesToDb(List<PubmedArticleFacade> articles);

}
