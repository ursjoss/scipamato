package ch.difty.scipamato.common.persistence.paging

/**
 * The [PaginationRequest] serves to define both
 * - pagination (with [offset] (default 0) and [pageSize] (default [Int.MAX_VALUE]))
 * - and sorting specifications (either passing [Sort] (or `direction`/`properties`)
 * and pass it on towards the persistence layer.
 *
 * It is currently only targeting offset pagination due to the limitations of
 * the presentation layer. It could be extended to hold information that could
 * be used for key-set pagination later on.
 *
 * This class was inspired by spring data's Pageable, however it only tracks
 * offset and pageSize together with the (optional) sortSpecification.
 */
data class PaginationRequest(
    override val offset: Int = 0,
    override val pageSize: Int = Int.MAX_VALUE,
    override val sort: Sort,
) : PaginationContext {

    /**
     * Creates a new [PaginationRequest] with sort parameters applied.
     *
     * @param [offset] zero-based record index (over the entire un-paged set of records).
     * @param [pageSize] the page size
     * @param [direction] the direction of the [Sort] to be specified.
     * @param [properties] the properties to sort by, must not be null or empty.
     */
    @JvmOverloads
    constructor(
        offset: Int,
        pageSize: Int,
        direction: Sort.Direction = Sort.Direction.ASC,
        vararg properties: String,
    ) : this(
        offset = offset,
        pageSize = pageSize,
        sort = Sort(
            direction = direction,
            propertyNames = properties
        )
    )

    constructor(
        direction: Sort.Direction = Sort.Direction.ASC,
        vararg properties: String,
    ) : this(offset = 0, pageSize = Int.MAX_VALUE, direction, properties = properties)

    init {
        require(offset >= 0) { "offset must not be less than zero!" }
        require(pageSize >= 1) { "Page size must not be less than one!" }
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
