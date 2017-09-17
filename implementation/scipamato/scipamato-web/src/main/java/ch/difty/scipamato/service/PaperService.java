package ch.difty.scipamato.service;

import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.PaperAttachment;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.scipamato.pubmed.entity.PubmedArticleFacade;

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
     * @param languageCode
     * @return list of {@link Paper}s
     */
    List<Paper> findBySearchOrder(SearchOrder searchOrder, String languageCode);

    /**
     * Finds a page full of papers as list matching the provided {@link SearchOrder} and {@link PaginationContext}.
     *
     * @param searchOrder the filter
     * @param paginationContext context defining paging and sorting
     * @param languageCode
     * @return paged list of papers
     */
    List<Paper> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext, String languageCode);

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
     * @param languageCode
     * @return Optional paper. Codes are enriched and attachments present (but without content)
     * @throws NullArgumentException if number is null
     */
    Optional<Paper> findByNumber(Long number, String languageCode);

    /**
     * Finds the lowest free number starting from the supplied value.
     * @param minimumPaperNumberToBeRecycled any gaps lower than this value will not be recycled
     */
    long findLowestFreeNumberStartingFrom(long minimumPaperNumberToBeRecycled);

    /**
     * Exclude the paper from the result list of the specified search order id.
     * @param searchOrderId the id of the search order from which the paper is to be excluded
     * @param paperId the id of the paper that shall be excluded
     */
    void excludeFromSearchOrder(long searchOrderId, long paperId);

    /**
     * Re-include the paper into the result list of the specified search order id.
     * @param searchOrderId the id of the search order from which the paper is to be re-included
     * @param paperId the id of the paper that shall be re-included
     */
    void reincludeIntoSearchOrder(long searchOrderId, long paperId);

    /**
     * Saves the attachment including its content.
     * @param paperAttachment
     * @return the paper for which the attachment was added
     */
    Paper saveAttachment(PaperAttachment paperAttachment);

    /**
     * Loads the {@link PaperAttachment} matching the provided id including its content
     * @param id the id of the paper attachment
     * @return paper attachment
     */
    PaperAttachment loadAttachmentWithContentBy(Integer id);

    /**
     * Deletes the attachment with given id
     * @param id the id of the paper attachment to be deleted
     * @return the paper for which the attachment was deleted
     */
    Paper deleteAttachment(Integer id);
}
