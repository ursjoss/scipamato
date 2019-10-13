package ch.difty.scipamato.common.persistence.paging;

import lombok.Value;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.paging.Sort.Direction;

/**
 * The {@link PaginationRequest} serves to define both pagination and sorting
 * specifications and pass it on towards the persistence layer.
 * <p>
 * It is currently only targeting offset pagination due to the limitations of
 * the presentation layer. It could be extended to hold information that could
 * be used for keyset pagination later on.
 * <p>
 * This class was inspired by spring data's Pageable, however it only tracks
 * offset and pageSize together with the (optional) sortSpecification.
 *
 * @author u.joss
 */
@Value
public class PaginationRequest implements PaginationContext {

    private static final long serialVersionUID = 1L;

    private final int  offset;
    private final int  pageSize;
    @Nullable
    private final Sort sort;

    /**
     * Creates a new {@link PaginationRequest}. Pages are zero indexed, thus
     * providing 0 for {@code offset} will return the first page.
     *
     * @param offset
     *     zero-based record index (over the entire un-paged set of records).
     * @param pageSize
     *     the page size
     */
    public PaginationRequest(final int offset, final int pageSize) {
        this(offset, pageSize, null);
    }

    /**
     * Creates a new {@link PaginationRequest} with sort parameters applied.
     *
     * @param offset
     *     zero-based record index (over the entire un-paged set of records).
     * @param pageSize
     *     the page size
     * @param direction
     *     the direction of the {@link Sort} to be specified, can be
     *     {@literal null}.
     * @param properties
     *     the properties to sort by, must not be {@literal null} or empty.
     */
    public PaginationRequest(final int offset, final int pageSize, final Direction direction,
        final String... properties) {
        this(offset, pageSize, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PaginationRequest} only applying sort, but retrieving
     * all records (no paging).
     *
     * @param direction
     *     the direction of the {@link Sort} to be specified, can be
     *     {@literal null}.
     * @param properties
     *     the properties to sort by, must not be {@literal null} or empty.
     */
    public PaginationRequest(final Direction direction, final String... properties) {
        this(0, Integer.MAX_VALUE, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PaginationRequest}.
     *
     * @param offset
     *     zero-based record index (over the entire un-paged set of records).
     * @param pageSize
     *     the page size
     * @param sort
     *     - can be {@literal null}.
     */
    public PaginationRequest(final int offset, final int pageSize, @Nullable final Sort sort) {
        checkConstraints(offset, pageSize);
        this.offset = offset;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    private void checkConstraints(final int offset, final int pageSize) {
        if (offset < 0)
            throw new IllegalArgumentException("offset must not be less than zero!");
        if (pageSize < 1)
            throw new IllegalArgumentException("Page size must not be less than one!");
    }

    @Override
    public String toString() {
        return String.format("Pagination request [offset: %d, size %d, sort: %s]", getOffset(), getPageSize(),
            sort == null ? null : sort.toString());
    }

}
