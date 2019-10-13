package ch.difty.scipamato.publ.persistence.api;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of papers as list
     */
    @NotNull
    List<PublicPaper> findPageByFilter(@Nullable PublicPaperFilter filter,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     *     the filter specification
     * @return entity count
     */
    int countByFilter(@Nullable PublicPaperFilter filter);

    /**
     * Finds the numbers (business key) of the persisted papers matching the
     * provided filter and pagination context.
     *
     * @param filter
     *     the filter specification
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of the numbers of matching papers
     */
    @NotNull
    List<Long> findPageOfNumbersByFilter(@Nullable PublicPaperFilter filter,
        @NotNull PaginationContext paginationContext);

}
