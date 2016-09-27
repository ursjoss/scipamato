package ch.difty.sipamato.service;

import java.util.List;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service methods.
 * 
 * @author u.joss
 *
 */
public interface PaperService extends SipamatoService<Paper, PaperFilter> {

    /**
     * Finds a page of records of type <literal>T</literal> matching the provided filter.
     *
     * @param filter the filter
     * @param first the start of the page
     * @param count the page count.
     * @return a list of entities of type <literal>T</literal>
     */
    List<Paper> findByFilter(PaperFilter filter, long first, long count);

    /**
     * Counts the number of entities matching the specified filter.
     *
     * @param filter
     * @return entity count
     */
    int countByFilter(PaperFilter filter);

}
