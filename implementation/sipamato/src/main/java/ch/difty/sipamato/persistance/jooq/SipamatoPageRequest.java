package ch.difty.sipamato.persistance.jooq;

import java.io.Serializable;

import ch.difty.sipamato.persistance.jooq.Sort.Direction;


/**
 * PageRequest implementing {@link Pageable} that accepts the record offset instead of the page index
 * as the default implementation of the spring data Pageable does.
 * <p/>
 * The offset calculation in the spring data Pageable implementation accepts the 0-based page
 * index and the page size. It later re-calculates the actual record offset from those two parameters. This
 * dictates that the page size is constant across  all pages (even the last one).
 * <p/>
 * The wicket implementation of DataTable is backed by the DataGridView. This component will adjust the page
 * size it uses to call iterator - depending on the actual number of records on the page. If the last page
 * is not full, the pageSize passed into iterator will be smaller than the theoretical page size. This screws
 * up the offset calculation of the spring data Pageable for the last page.
 * <p/>
 * This implementation of the {Pageable} interface deals with the offset directly and derives the pageNumber
 * as a dependent calculation instead.
 *
 * @see org.springframework.data.domain.AbstractPageRequest#getOffset
 * @see org.apache.wicket.markup.repeater.AbstractPageableView#getViewSize
 *
 * @author u.joss
 */
public class SipamatoPageRequest implements Pageable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Sort sort;

    private final int offset;
    private final int pageSize;
    private final int recordCount;

    /**
     * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing 0 for {@code offset} will return the first page.
     *
     * @param offset zero-based record index (over the entire unpaged set of records).
     * @param maxPageSize the maximum page size
     * @param recordCount number of records on the current page ({@code recordCount <= maxPageSize})
     */
    public SipamatoPageRequest(int offset, int pageSize, int recordCount) {
        this(offset, pageSize, recordCount, null);
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param offset zero-based record index (over the entire unpaged set of records).
     * @param maxPageSize the maximum page size
     * @param recordCount number of records on the current page ({@code recordCount <= maxPageSize})
     * @param direction the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
    public SipamatoPageRequest(int offset, int pageSize, int recordCount, Direction direction, String... properties) {
        this(offset, pageSize, recordCount, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param offset zero-based record index (over the entire unpaged set of records).
     * @param maxPageSize the maximum page size
     * @param recordCount number of records on the current page ({@code recordCount <= maxPageSize})
     * @param sort can be {@literal null}.
     */
    public SipamatoPageRequest(int offset, int pageSize, int recordCount, Sort sort) {
        checkConstraints(offset, pageSize, recordCount);
        this.offset = offset;
        this.pageSize = pageSize;
        this.recordCount = recordCount;
        this.sort = sort;
    }

    /**
     * Creates a new {@link PageRequest} only applying sort, but retrieving all records (no paging).
     * @param direction the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
    public SipamatoPageRequest(Direction direction, String... properties) {
        this(0, Integer.MAX_VALUE, Integer.MAX_VALUE, new Sort(direction, properties));
    }

    private void checkConstraints(int offset, int pageSize, int recordCount) {
        if (offset < 0)
            throw new IllegalArgumentException("offset must not be less than zero!");
        if (pageSize < 1)
            throw new IllegalArgumentException("Page size must not be less than one!");
        if (recordCount > pageSize)
            throw new IllegalArgumentException("RecordCount must not be greater than pageSize!");
    }

    @Override
    public int getPageNumber() {
        return getOffset() / getPageSize();
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return the actual number of records on the current page
     */
    public int getRecordCount() {
        return recordCount;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#next()
     */
    @Override
    public Pageable next() {
        return new SipamatoPageRequest(getOffset() + getPageSize(), getPageSize(), getPageSize(), getSort());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#hasPrevious()
     */
    @Override
    public boolean hasPrevious() {
        return offset > 0;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#previousOrFirst()
     */
    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.AbstractPageRequest#previous()
     */
    public Pageable previous() {
        return getOffset() == 0 ? this : new SipamatoPageRequest(getOffset() - getPageSize(), getPageSize(), getPageSize(), getSort());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#first()
     */
    @Override
    public Pageable first() {
        return new SipamatoPageRequest(0, getPageSize(), getPageSize(), getSort());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + offset;
        result = prime * result + pageSize;
        result = prime * result + recordCount;
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
        SipamatoPageRequest other = (SipamatoPageRequest) obj;
        if (offset != other.offset)
            return false;
        if (pageSize != other.pageSize)
            return false;
        if (recordCount != other.recordCount)
            return false;
        if (sort == null) {
            if (other.sort != null)
                return false;
        } else if (!sort.equals(other.sort))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Page request [offset: %d, size %d, records %d, sort: %s]", getOffset(), getPageSize(), getRecordCount(), sort == null ? null : sort.toString());
    }

}
