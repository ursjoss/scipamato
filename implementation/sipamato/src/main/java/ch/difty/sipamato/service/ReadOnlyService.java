package ch.difty.sipamato.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;

/**
 * The generic {@link ReadOnlyService} interface, defining the common methods.
 *
 * @author u.joss
 *
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <F> the filter, extending {@link SipamatoFilter}
 */
public interface ReadOnlyService<T extends SipamatoEntity, F extends SipamatoFilter> extends Serializable {

    /**
     * Finds a page of records of type <literal>T</literal> matching the provided filter.
     *
     * @param filter the filter
     * @param pageable defining paging and sorting
     * @return a list of entities of type <literal>T</literal>
     */
    List<T> findByFilter(F filter, Pageable pageable);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     * @return entity count
     */
    int countByFilter(F filter);

}
