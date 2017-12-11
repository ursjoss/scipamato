package ch.difty.scipamato.public_.persistence.paper;

import java.io.Serializable;
import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.public_.entity.PublicPaper;
import ch.difty.scipamato.public_.entity.filter.PublicPaperFilter;

public interface PublicPaperRepository extends Serializable {

    /**
     * Finds the persisted {@link PublicPaper} with the provided number (business key).
     *
     * @param number - must not be null
     * @return the persisted {@link PublicPaper} or null if it can't be found.
     * @throws NullArgumentException if the number is null.
     */
    PublicPaper findByNumber(Long number);

    /**
     * Finds the persisted {@link PublicPaper}s matching the provided filter and pagination context.
     *
     * @param filter {@link PublicPaperFilter}
     * @param paginationContext {@link PaginationContext}
     * @return list of all matching {@link PublicPaper}s
     */
    List<PublicPaper> findPageByFilter(PublicPaperFilter filter, PaginationContext paginationContext);

    /**
     * Counts all persisted {@link PublicPaper}s matching the provided filter. 
     *
     * @param filter {@link PublicPaper}s
     * @return list of all matching {@link PublicPaper}s
     */
    int countByFilter(PublicPaperFilter filter);

    /**
     * Finds the numbers (business key) of the persisted entities matching the provided filter and pagination context.
     *
     * @param filter of type {@code F}
     * @param paginationContext {@link PaginationContext}
     * @return list of the numbers of type {@code ID} of matching entities {@code T}
     */
    List<Long> findPageOfNumbersByFilter(PublicPaperFilter filter, PaginationContext paginationContext);

}
