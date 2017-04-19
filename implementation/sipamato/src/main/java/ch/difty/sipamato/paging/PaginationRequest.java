package ch.difty.sipamato.paging;

import java.io.Serializable;

import ch.difty.sipamato.paging.Sort.Direction;

/**
 * The {@link PaginationRequest} serves to define both pagination and sorting specifications and pass it
 * on towards the persistence layer.
 * 
 * It is currently only targeting offest pagination due to the limitations of the presentation layer.
 * It could be extended to hold information that could be used for keyset pagination later on.
 *
 * This class was inspired by spring datas Pageable, however it only tracks offset and pageSize together
 * with the (optional) sortSpecification.
 *
 * @author u.joss
 */
public class PaginationRequest implements PaginationContext, Serializable {

    private static final long serialVersionUID = 1L;

    private final int offset;
    private final int pageSize;
    private final Sort sort;

    /**
     * Creates a new {@link PaginationRequest}. Pages are zero indexed, thus providing 0 for {@code offset} will return the first page.
     *
     * @param offset zero-based record index (over the entire unpaged set of records).
     * @param pageSize the page size
     */
    public PaginationRequest(int offset, int pageSize) {
        this(offset, pageSize, null);
    }

    /**
     * Creates a new {@link PaginationRequest} with sort parameters applied.
     *
     * @param offset zero-based record index (over the entire unpaged set of records).
     * @param pageSize the page size
     * @param direction the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
    public PaginationRequest(int offset, int pageSize, Direction direction, String... properties) {
        this(offset, pageSize, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PaginationRequest} only applying sort, but retrieving all records (no paging).
     * @param direction the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
    public PaginationRequest(Direction direction, String... properties) {
        this(0, Integer.MAX_VALUE, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PaginationRequest}.
     *
     * @param offset zero-based record index (over the entire unpaged set of records).
     * @param pageSize the page size
     * @param sort - can be {@literal null}.
     */
    public PaginationRequest(int offset, int pageSize, Sort sort) {
        checkConstraints(offset, pageSize);
        this.offset = offset;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    private void checkConstraints(int offset, int pageSize) {
        if (offset < 0)
            throw new IllegalArgumentException("offset must not be less than zero!");
        if (pageSize < 1)
            throw new IllegalArgumentException("Page size must not be less than one!");
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + offset;
        result = prime * result + pageSize;
        result = prime * result + ((sort == null) ? 0 : sort.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PaginationRequest other = (PaginationRequest) obj;
        if (offset != other.offset)
            return false;
        if (pageSize != other.pageSize)
            return false;
        if (sort == null) {
            if (other.sort != null)
                return false;
        } else if (!sort.equals(other.sort))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Pagination request [offset: %d, size %d, sort: %s]", getOffset(), getPageSize(), sort == null ? null : sort.toString());
    }

}
