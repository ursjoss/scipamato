package ch.difty.scipamato.common.persistence.paging

import java.io.Serializable

/**
 * The [PaginationContext] serves as interface for pagination specifications.
 * It is inspired by spring data's Pageable interface, but much simpler for the use cases of scipamato.
 */
interface PaginationContext : Serializable {
    /** zero based record offset relative to the index of the record in the entire un-paged record-set. */
    val offset: Int
    val pageSize: Int
    val sort: Sort
}
