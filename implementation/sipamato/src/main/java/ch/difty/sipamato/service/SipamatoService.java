package ch.difty.sipamato.service;

import java.io.Serializable;
import java.util.List;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;

/**
 * The generic {@link SipamatoService} interface, defining the common methods.
 *
 * @author u.joss
 *
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <F> the filter, extending {@link SipamatoFilter}
 */
public interface SipamatoService<T extends SipamatoEntity, F extends SipamatoFilter> extends Serializable {

    /**
     * Finds a page of records of type <literal>T</literal> matching the provided filter.
     *
     * @param filter the filter
     * @param first the start of the page
     * @param count the page count.
     * @return a list of entities of type <literal>T</literal>
     */
    List<T> findByFilter(F filter, long first, long count);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     * @return entity count
     */
    int countByFilter(F filter);
}
