package ch.difty.scipamato.common.persistence;

import java.util.Iterator;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;

public interface DefinitionProviderService<T extends DefinitionEntity, F extends ScipamatoFilter> {

    /**
     * Find a page of entities of type {@code F}
     *
     * @param filter
     *     the filter specification
     * @param paginationContext
     *     the paginationContext describing paging and sorting options
     * @return iterator of the paged and sorted entities of type {@code F}
     */
    Iterator<T> findPageOfEntityDefinitions(F filter, PaginationContext paginationContext);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     *     filter of type {@code F}
     * @return entity count
     */
    int countByFilter(F filter);
}
