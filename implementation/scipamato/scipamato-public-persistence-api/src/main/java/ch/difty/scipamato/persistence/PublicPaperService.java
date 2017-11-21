package ch.difty.scipamato.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.paging.PaginationContext;

public interface PublicPaperService extends Serializable {

    /**
     * Finds an individual entity by id. Returns it as an optional of {@link PublicPaper}
     *
     * @param id - must not be null
     * @return Optional
     * @throws NullArgumentException if id is null
     */
    Optional<PublicPaper> findById(Long id);

    /**
     * Finds a page full of {@link PublicPaper}s matching the provided filter and pagination context.
     *
     * @param filter the filter
     * @param paginationContext context defining paging and sorting
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
     * Finds the ids of the persisted papers matching the provided filter and pagination context.
     *
     * @param filter
     * @param paginationContext {@link PaginationContext}
     * @return list of the ids of matching papers
     */
    List<Long> findPageOfIdsByFilter(PublicPaperFilter filter, PaginationContext paginationContext);

}
