package ch.difty.scipamato.core.persistence.paper;

import java.util.List;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.filter.PaperFilter;
import ch.difty.scipamato.core.persistence.EntityRepository;
import ch.difty.scipamato.core.persistence.paper.searchorder.BySearchOrderRepository;

/**
 * Repository to manage {@link Paper}s
 *
 * @author u.joss
 *
 */
public interface PaperRepository extends EntityRepository<Paper, Long, PaperFilter> {

    /**
     * Find {@link Paper}s with the provided ids. The codes are not enriched.
     *
     * @param ids
     * @return list of papers (codes not available, attachments without content)
     */
    List<Paper> findByIds(List<Long> ids);

    /**
     * Find {@link Paper}s (including codes) with the provided ids
     *
     * @param ids
     * @param languageCode
     *            - must not be null
     * @return list of papers (attachments without content)
     */
    List<Paper> findWithCodesByIds(List<Long> ids, String languageCode);

    /**
     * Finds all {@link Paper}s matching the provided {@link SearchOrder}
     * specification. The codes are enriched. The attachments are present but
     * without the actual content.
     *
     * @param searchOrder
     *            {@link SearchOrder} the search specification
     * @param languageCode
     *            - must not be null
     * @return list of entities (attachments without content)
     */
    List<Paper> findBySearchOrder(SearchOrder searchOrder, String languageCode);

    /**
     * Finds a single page of {@link Paper}s matching the provided
     * {@link SearchOrder} and {@link PaginationContext}. The codes are enriched.
     * The attachments are present but without the actual content.
     *
     * @param searchOrder
     * @param paginationContext
     * @param languageCode
     * @return paged list of entities (attachments without content)
     */
    List<Paper> findPageBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext,
            String languageCode);

    /**
     * {@link BySearchOrderRepository#countBySearchOrder(SearchOrder)}
     */
    int countBySearchOrder(SearchOrder searchOrder);

    /**
     * Finds all {@link Paper}s matching any of the provided {@code pmIds}. The
     * codes are enriched. The attachments are present but without the actual
     * content.
     *
     * @param pmIds
     *            list of pubmed ids
     * @param languageCode
     * @return list of entities (codes enriched, attachments without content)
     */
    List<Paper> findByPmIds(List<Integer> pmIds, String languageCode);

    /**
     * Returns the PMIDs out of the provided list that have actually been assigned
     * in persisted {@link Paper}s.
     *
     * @param pmIds
     *            list of pubmed ids
     * @return list of PMIDs
     */
    List<Integer> findExistingPmIdsOutOf(List<Integer> pmIds);

    /**
     * Finds all {@link Paper}s matching any of the provided {@code numbers}. The
     * codes are enriched. The attachments are present but without the actual
     * content.
     *
     * @param numbers
     *            list of numbers
     * @param languageCode
     * @return list of entities (codes enriched, attachments without content)
     */
    List<Paper> findByNumbers(List<Long> numbers, String languageCode);

    /**
     * Finds the lowest free number starting from the supplied minimum parameter.
     * Will find gaps if those numbers are equal to or larger than
     * {@code minimumPaperNumberToBeRecycled}.
     *
     * @param minimumPaperNumberToBeRecycled
     *            any gaps lower than this value will not be recycled
     * @return lowest free number ignoring any gaps below
     *         {@code minimumPaperNumberToBeRecycled}
     */
    long findLowestFreeNumberStartingFrom(long minimumPaperNumberToBeRecycled);

    /**
     * {@link BySearchOrderRepository#findPageOfIdsBySearchOrder(SearchOrder, PaginationContext)}
     */
    List<Long> findPageOfIdsBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext);

    /**
     * Excludes the given paperId from the results of the searchOrder with given
     * searchOrderid.
     *
     * @param searchOrderId
     *            the id of the search order
     * @param paperId
     *            the id of the paper
     */
    void excludePaperFromSearchOrderResults(long searchOrderId, long paperId);

    /**
     * Re-includes the previously excluded paperId into the results of the
     * searchOrder with given searchOrderid.
     *
     * @param searchOrderId
     *            the id of the search order
     * @param paperId
     *            the id of the paper
     */
    void reincludePaperIntoSearchOrderResults(long searchOrderId, long paperId);

    /**
     * Saves the provided {@link PaperAttachment} including it's content.
     *
     * @param paperAttachment
     * @return the paper for which the attachment has been added
     */
    Paper saveAttachment(PaperAttachment paperAttachment);

    /**
     * Loads the {@link PaperAttachment} with provided id including it's content
     *
     * @param id
     *            the id of the paper attachment
     * @return the full attachment including the content.
     */
    PaperAttachment loadAttachmentWithContentBy(Integer id);

    /**
     * Deletes the attachment with given id
     *
     * @param id
     *            the id of the paper attachment to be deleted
     * @return the paper for which the attachment has been deleted
     */
    Paper deleteAttachment(Integer id);

    /**
     * Deletes all {@link Paper}s with the provided ids. No version check. More for
     * internal usage!
     *
     * @param ids
     */
    void delete(List<Long> ids);

}
