package ch.difty.scipamato.publ.persistence.api

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter

interface PublicPaperService {

    /**
     * Finds an individual paper by its [number] (business key). Returns a [PublicPaper] or null.
     */
    fun findByNumber(number: Long): PublicPaper?

    /**
     * Finds a page full of [PublicPaper]s matching the provided [filter] and [paginationContext].
     */
    fun findPageByFilter(filter: PublicPaperFilter, paginationContext: PaginationContext): List<PublicPaper>

    /**
     * Counts the number of entities matching the specified [filter].
     */
    fun countByFilter(filter: PublicPaperFilter): Int

    /**
     * Finds the numbers (business key) of the persisted papers matching the
     * provided [filter] and [paginationContext].
     */
    fun findPageOfNumbersByFilter(
        filter: PublicPaperFilter,
        paginationContext: PaginationContext,
    ): List<Long>
}
