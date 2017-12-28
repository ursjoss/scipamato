package ch.difty.scipamato.common.persistence.paging;

import java.io.Serializable;

/**
 * The {@link PaginationContext} serves as interface for pagination
 * specifications.
 *
 * It is inspired by spring datas Pageable interface, but much simpler for the
 * use cases of scipamato.
 *
 * @author u.joss
 */
public interface PaginationContext extends Serializable {

    /**
     * @return zero based record offset relative to the index of the record in the
     *         entire unpaged recordset.
     */
    int getOffset();

    /**
     * @return the number of records in the page.
     */
    int getPageSize();

    /**
     * @return the {@link Sort} specification
     */
    Sort getSort();
}
