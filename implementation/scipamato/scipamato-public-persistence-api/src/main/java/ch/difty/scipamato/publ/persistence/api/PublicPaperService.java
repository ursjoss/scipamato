package ch.difty.scipamato.publ.persistence.api;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

public interface PublicPaperService extends Serializable {

    /**
     * Finds an individual paper by it's number (business key). Returns it as an
     * optional of {@link PublicPaper}
     *
     * @param number
     *            - must not be null
     * @return Optional
     * @throws NullArgumentException
     *             if id is null
     */
    Optional<PublicPaper> findByNumber(Long number);

    /**
     * Finds a page full of {@link PublicPaper}s matching the provided filter and
     * pagination context.
     *
     * @param filter
     *            the filter
     * @param paginationContext
     *            context defining paging and sorting
     * @return a page of papers as list
     */
    List<PublicPaper> findPageByFilter(PublicPaperFilter filter, PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     * @return entity count
     */
    int countByFilter(PublicPaperFilter filter);

    /**
     * Finds the numbers (business key) of the persisted papers matching the
     * provided filter and pagination context.
     *
     * @param filter
     * @param paginationContext
     *            {@link PaginationContext}
     * @return list of the numbers of matching papers
     */
    List<Long> findPageOfNumbersByFilter(PublicPaperFilter filter, PaginationContext paginationContext);

}
