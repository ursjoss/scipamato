package ch.difty.scipamato.core.persistence.paper;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.EntityRepository;

/**
 * Repository to manage {@link Paper}s
 *
 * @author u.joss
 */
public interface PaperRepository extends EntityRepository<Paper, Long, PaperFilter> {

    /**
     * Find {@link Paper}s with the provided ids. The codes are not enriched.
     *
     * @param ids
     *     the list of ids of the papers to be returned
     * @return list of papers (codes not available, attachments without content)
     */
    @NotNull
    List<Paper> findByIds(@NotNull List<Long> ids);

    /**
     * Find {@link Paper}s (including codes) with the provided ids
     *
     * @param ids
     *     the list of ids for which to find the codes
     * @param languageCode
     *     the two character language code - must not be null
     * @return list of papers (attachments without content)
     */
    @NotNull
    List<Paper> findWithCodesByIds(@NotNull List<Long> ids, @NotNull String languageCode);

    /**
     * Finds all {@link Paper}s matching the provided {@link SearchOrder}
     * specification. The codes are enriched. The attachments are present but
     * without the actual content.
     *
     * @param searchOrder
     *     {@link SearchOrder} the search specification
     * @param languageCode
     *     - must not be null
     * @return list of entities (attachments without content)
     */
    @NotNull
    List<Paper> findBySearchOrder(@NotNull SearchOrder searchOrder, @NotNull String languageCode);

    /**
     * Finds a single page of {@link Paper}s matching the provided
     * {@link SearchOrder} and {@link PaginationContext}. The codes are enriched.
     * The attachments are present but without the actual content.
     *
     * @param searchOrder
     *     the search order with the search specification
     * @param paginationContext
     *     the pagination context
     * @param languageCode
     *     the two character language code
     * @return paged list of entities (attachments without content)
     */
    @NotNull
    List<Paper> findPageBySearchOrder(@NotNull SearchOrder searchOrder, @NotNull PaginationContext paginationContext, @NotNull String languageCode);

    /**
     * Counts all persisted entities of type {@code T} matching the provided
     * {@link SearchOrder} specification.
     *
     * @param searchOrder
     *     the search specification
     * @return T entity count
     */
    int countBySearchOrder(@NotNull SearchOrder searchOrder);

    /**
     * Finds all {@link Paper}s matching any of the provided {@code pmIds}. The
     * codes are enriched. The attachments are present but without the actual
     * content.
     *
     * @param pmIds
     *     list of pubmed ids
     * @param languageCode
     *     the two character language code
     * @return list of entities (codes enriched, attachments without content)
     */
    @NotNull
    List<Paper> findByPmIds(@NotNull List<Integer> pmIds, @NotNull String languageCode);

    /**
     * Returns the PMIDs out of the provided list that have actually been assigned
     * in persisted {@link Paper}s.
     *
     * @param pmIds
     *     list of pubmed ids
     * @return list of PMIDs
     */
    @NotNull
    List<Integer> findExistingPmIdsOutOf(@NotNull List<Integer> pmIds);

    /**
     * Finds all {@link Paper}s matching any of the provided {@code numbers}. The
     * codes are enriched. The attachments are present but without the actual
     * content.
     *
     * @param numbers
     *     list of numbers
     * @param languageCode
     *     the two character language code
     * @return list of entities (codes enriched, attachments without content)
     */
    @NotNull
    List<Paper> findByNumbers(@NotNull List<Long> numbers, @NotNull String languageCode);

    /**
     * Finds the lowest free number starting from the supplied minimum parameter.
     * Will find gaps if those numbers are equal to or larger than
     * {@code minimumPaperNumberToBeRecycled}.
     *
     * @param minimumPaperNumberToBeRecycled
     *     any gaps lower than this value will not be recycled
     * @return lowest free number ignoring any gaps below
     *     {@code minimumPaperNumberToBeRecycled}
     */
    long findLowestFreeNumberStartingFrom(long minimumPaperNumberToBeRecycled);

    /**
     * Finds a single page of entity ids matching the provided {@link SearchOrder}
     * and {@link PaginationContext}.
     *
     * @param searchOrder
     *     the search specification
     * @param paginationContext
     *     the pagination specification
     * @return paged list of entity ids
     */
    @NotNull
    List<Long> findPageOfIdsBySearchOrder(@NotNull SearchOrder searchOrder, @NotNull PaginationContext paginationContext);

    /**
     * Excludes the given paperId from the results of the searchOrder with given
     * searchOrderId.
     *
     * @param searchOrderId
     *     the id of the search order
     * @param paperId
     *     the id of the paper
     */
    void excludePaperFromSearchOrderResults(long searchOrderId, long paperId);

    /**
     * Re-includes the previously excluded paperId into the results of the
     * searchOrder with given searchOrderId.
     *
     * @param searchOrderId
     *     the id of the search order
     * @param paperId
     *     the id of the paper
     */
    void reincludePaperIntoSearchOrderResults(long searchOrderId, long paperId);

    /**
     * Saves the provided {@link PaperAttachment} including it's content.
     *
     * @param paperAttachment
     *     the attachment to save for the paper
     * @return the paper for which the attachment has been added
     */
    @Nullable
    Paper saveAttachment(@NotNull PaperAttachment paperAttachment);

    /**
     * Loads the {@link PaperAttachment} with provided id including it's content
     *
     * @param id
     *     the id of the paper attachment
     * @return the full attachment including the content.
     */
    @Nullable
    PaperAttachment loadAttachmentWithContentBy(@NotNull Integer id);

    /**
     * Deletes the attachment with given id
     *
     * @param id
     *     the id of the paper attachment to be deleted
     * @return the paper for which the attachment has been deleted
     */
    @Nullable
    Paper deleteAttachment(@NotNull Integer id);

    /**
     * Deletes all {@link Paper}s with the provided ids. No version check. More for
     * internal usage!
     *
     * @param ids
     *     the list of ids to delete
     */
    void delete(@NotNull List<Long> ids);

    /**
     * Checks if another (different id from idOfCurrentPaper) paper has the
     * specified doi assigned. If found, reports its/their number(s) as string.
     *
     * @param doi
     *     the doi which might be assigned to another paper. Must not be null
     * @param idOfCurrentPaper
     *     the id of the current paper which should not count as duplicate. Can be
     *     null for new Studies that have not been saved yet.
     * @return optional with numbers of violated papers - if found
     */
    @NotNull
    Optional<String> isDoiAlreadyAssigned(@NotNull String doi, @NotNull Long idOfCurrentPaper);

    /**
     * Checks if another (different id from idOfCurrentPaper) paper has the
     * specified pmId assigned.
     *
     * @param pmId
     *     the pmId which might be assigned to another paper
     * @param idOfCurrentPaper
     *     the id of the current paper which should not count as duplicate. Can be
     *     null for new Studies that have not been saved yet.
     * @return optional with numbers of violated papers - if found
     */
    @NotNull
    Optional<String> isPmIdAlreadyAssigned(int pmId, @Nullable Long idOfCurrentPaper);
}
