package ch.difty.scipamato.publ.persistence.paper;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

public interface PublicPaperRepository {

    /**
     * Finds the persisted {@link PublicPaper} with the provided number (business
     * key).
     *
     * @param number
     *     - must not be null
     * @return the persisted {@link PublicPaper} or null if it can't be found.
     */
    @Nullable
    PublicPaper findByNumber(@NotNull Long number);

    /**
     * Finds the persisted {@link PublicPaper}s matching the provided filter and
     * pagination context.
     *
     * @param filter
     *     {@link PublicPaperFilter}
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of all matching {@link PublicPaper}s
     */
    @NotNull
    List<PublicPaper> findPageByFilter(@Nullable PublicPaperFilter filter,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts all persisted {@link PublicPaper}s matching the provided filter.
     *
     * @param filter
     *     {@link PublicPaper}s
     * @return list of all matching {@link PublicPaper}s
     */
    int countByFilter(@Nullable PublicPaperFilter filter);

    /**
     * Finds the numbers (business key) of the persisted entities matching the
     * provided filter and pagination context.
     *
     * @param filter
     *     of type {@code F}
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of the numbers of type {@code ID} of matching entities {@code T}
     */
    @NotNull
    List<Long> findPageOfNumbersByFilter(@Nullable PublicPaperFilter filter,
        @NotNull PaginationContext paginationContext);
}
