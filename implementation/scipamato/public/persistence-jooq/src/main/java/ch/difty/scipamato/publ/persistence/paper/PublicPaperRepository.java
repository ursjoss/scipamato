package ch.difty.scipamato.publ.persistence.paper;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

public interface PublicPaperRepository {

    /**
     * Finds the persisted {@link PublicPaper} with the provided number (business key).
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
     *     {@link PublicPaperFilter} - must not be null
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of all matching {@link PublicPaper}s
     */
    @NotNull
    List<PublicPaper> findPageByFilter(@NotNull PublicPaperFilter filter, @NotNull PaginationContext paginationContext);

    /**
     * Counts all persisted {@link PublicPaper}s matching the provided filter.
     *
     * @param filter
     *     {@link PublicPaperFilter} - must not be null
     * @return list of all matching {@link PublicPaper}s
     */
    int countByFilter(@NotNull PublicPaperFilter filter);

    /**
     * Finds the numbers (business key) of the persisted entities matching the
     * provided filter and pagination context.
     *
     * @param filter
     *     of type {@code PublicPaperFilter} - must not be null
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of the numbers of type {@code Long} of matching papers
     */
    @NotNull
    List<Long> findPageOfNumbersByFilter(@NotNull PublicPaperFilter filter,
        @NotNull PaginationContext paginationContext);
}
