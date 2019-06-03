package ch.difty.scipamato.core.persistence;

import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service
 * methods.
 *
 * @author u.joss
 */
public interface PaperService extends EntityService<Long, Paper, PaperFilter> {

    /**
     * Find any paper matching the provided {@link SearchOrder}.
     *
     * @param searchOrder
     *     {@link SearchOrder}
     * @param languageCode
     *     the language code (e.g. 'en') to fetch the paper in
     * @return list of {@link Paper}s
     */
    List<Paper> findBySearchOrder(SearchOrder searchOrder, String languageCode);

    /**
     * Finds a page full of papers as list matching the provided {@link SearchOrder}
     * and {@link PaginationContext}.
     *
     * @param searchOrder
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @param languageCode
     *     the language code (e.g. 'en') to fetch the paper in
     * @return paged list of papers
     */
    List<Paper> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext,
        String languageCode);

    /**
     * Finds a page full of ids of papers as list matching the provided
     * {@link SearchOrder} and {@link PaginationContext}.
     *
     * @param searchOrder
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return paged list of paper ids
     */
    List<Long> findPageOfIdsBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified {@link SearchOrder}.
     *
     * @param searchOrder
     *     {@link SearchOrder}
     * @return paper count
     */
    int countBySearchOrder(SearchOrder searchOrder);

    /**
     * Saves some minimally filled {@link Paper}s based on the information received
     * from PubMed via the {@link PubmedArticleFacade} entities.
     *
     * @param articles
     *     list of {@link PubmedArticleFacade}
     * @param minimumNumber
     *     The lowest number that could be recycled
     * @return {@link ServiceResult}
     */
    ServiceResult dumpPubmedArticlesToDb(List<PubmedArticleFacade> articles, long minimumNumber);

    /**
     * Finds an individual paper by number. Returns it as an optional of type
     * {@code T}
     *
     * @param number
     *     - must not be null
     * @param languageCode
     *     the language code (e.g. 'en') to fetch the paper in
     * @return Optional paper. Codes are enriched and attachments present (but
     *     without content)
     * @throws NullArgumentException
     *     if number is null
     */
    Optional<Paper> findByNumber(Long number, String languageCode);

    /**
     * Finds the lowest free number starting from the supplied value.
     *
     * @param minimumPaperNumberToBeRecycled
     *     any gaps lower than this value will not be recycled
     * @return the lowest free number that was identified
     */
    long findLowestFreeNumberStartingFrom(long minimumPaperNumberToBeRecycled);

    /**
     * Exclude the paper from the result list of the specified search order id.
     *
     * @param searchOrderId
     *     the id of the search order from which the paper is to be excluded
     * @param paperId
     *     the id of the paper that shall be excluded
     */
    void excludeFromSearchOrder(long searchOrderId, long paperId);

    /**
     * Re-include the paper into the result list of the specified search order id.
     *
     * @param searchOrderId
     *     the id of the search order from which the paper is to be
     *     re-included
     * @param paperId
     *     the id of the paper that shall be re-included
     */
    void reincludeIntoSearchOrder(long searchOrderId, long paperId);

    /**
     * Saves the attachment including its content.
     *
     * @param paperAttachment
     *     the attachment to save into the paper
     * @return the paper for which the attachment was added
     */
    Paper saveAttachment(PaperAttachment paperAttachment);

    /**
     * Loads the {@link PaperAttachment} matching the provided id including its
     * content
     *
     * @param id
     *     the id of the paper attachment
     * @return paper attachment
     */
    PaperAttachment loadAttachmentWithContentBy(Integer id);

    /**
     * Deletes the attachment with given id
     *
     * @param id
     *     the id of the paper attachment to be deleted
     * @return the paper for which the attachment was deleted
     */
    Paper deleteAttachment(Integer id);

    /**
     * Deletes the papers with the provided ids.
     *
     * @param ids
     *     ids of the papers to be deleted.
     */
    void deletePapersWithIds(List<Long> ids);

    /**
     * Assigns the paper to the newsletter with the given topic by either adding a new
     * association or updating an existing one.
     *
     * @param paperId
     *     the id of the paper to assign
     * @param newsletterTopicId
     *     the id of the newsletter topic, may be null.
     * @param languageCode
     *     the two digit language Code, e.g. 'en' or 'de'
     * @return optional of NewsletterLink
     */
    Optional<Paper.NewsletterLink> mergePaperIntoWipNewsletter(long paperId, Integer newsletterTopicId,
        String languageCode);

    /**
     * Removes the paper with the specified id from the newsletter with the given id.
     *
     * @param newsletterId
     *     the id of the newsletter to assign the paper to
     * @param paperId
     *     the id of the paper to assign
     * @return the count of records that were removed
     */
    int removePaperFromNewsletter(int newsletterId, long paperId);

    /**
     * Used for validation purposes: Checks if (besides the paper record with the current id)
     * another record has the same value in the field specified with the provided name. If it finds
     * any violated papers that already have the currently validated fieldValue, those other papers
     * numbers are reported as string.
     *
     * @param fieldName
     *     the field name that will be checked. Currently supported: pmId, doi
     * @param fieldValue
     *     the field value that should not occur in other papers
     * @param idOfCurrentPaper
     *     the id of the current paper
     * @return optional of numbers of violated papers
     */
    Optional<String> hasDuplicateFieldNextToCurrent(String fieldName, Object fieldValue, Long idOfCurrentPaper);
}
