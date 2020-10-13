package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.DefinitionEntity
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import ch.difty.scipamato.common.persistence.paging.PaginationContext

interface DefinitionProviderService<T : DefinitionEntity<*, *>, F : ScipamatoFilter> {

    /**
     * Find a page of entities of type [T]
     *
     * @param [filter] the filter specification of type [F]. Can be null.
     * @param [paginationContext] the paginationContext describing paging and sorting options
     * @return iterator of the paged and sorted entities of type [T]
     */
    fun findPageOfEntityDefinitions(filter: F?, paginationContext: PaginationContext): Iterator<T>

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param [filter] filter of type `F`. May be null.
     * @return entity count
     */
    fun countByFilter(filter: F?): Int
}
