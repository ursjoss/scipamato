package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter

interface PublicPaperRepository {

    /**
     * Finds the persisted [PublicPaper] with the provided [number] (business key).
     */
    fun findByNumber(number: Long): PublicPaper?

    /**
     * Finds the persisted [PublicPaper]s matching the provided [filter] and [paginationContext].
     */
    fun findPageByFilter(filter: PublicPaperFilter, paginationContext: PaginationContext): List<PublicPaper>

    /**
     * Counts all persisted [PublicPaper]s matching the provided [filter].
     */
    fun countByFilter(filter: PublicPaperFilter): Int

    /**
     * Finds the numbers (business key) of the persisted entities matching the
     * provided [filter] and [paginationContext].
     */
    fun findPageOfNumbersByFilter(
        filter: PublicPaperFilter,
        paginationContext: PaginationContext,
    ): List<Long>
}
