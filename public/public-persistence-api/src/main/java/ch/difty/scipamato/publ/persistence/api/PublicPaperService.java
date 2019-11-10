package ch.difty.scipamato.publ.persistence.api;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

public interface PublicPaperService {

    /**
     * Finds an individual paper by it's number (business key). Returns it as an
     * optional of {@link PublicPaper}
     *
     * @param number
     *     - must not be null
     * @return Optional
     */
    @NotNull
    Optional<PublicPaper> findByNumber(@NotNull Long number);

    /**
     * Finds a page full of {@link PublicPaper}s matching the provided filter and
     * pagination context.
     *
     * @param filter
     *     the filter - must not be null
     * @param paginationContext
     *     context defining paging and sorting - must not be null
     * @return a page of papers as list - never null
     */
    @NotNull
    List<PublicPaper> findPageByFilter(@NotNull PublicPaperFilter filter, @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     *     the filter specification - must not be null
     * @return entity count
     */
    int countByFilter(@NotNull PublicPaperFilter filter);

    /**
     * Finds the numbers (business key) of the persisted papers matching the
     * provided filter and pagination context.
     *
     * @param filter
     *     the filter specification - must not be null
     * @param paginationContext
     *     {@link PaginationContext} - must not be null
     * @return list of the numbers of matching papers - never null
     */
    @NotNull
    List<Long> findPageOfNumbersByFilter(@NotNull PublicPaperFilter filter,
        @NotNull PaginationContext paginationContext);

}
