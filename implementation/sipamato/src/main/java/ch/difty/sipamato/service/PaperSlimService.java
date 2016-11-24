package ch.difty.sipamato.service;

import java.util.List;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;

/**
 * The {@link PaperSlimService} interface - defining {@link PaperSlim} specific service methods.
 *
 * @author u.joss
 */
public interface PaperSlimService extends ReadOnlyService<PaperSlim, Long, SimplePaperFilter> {

    /**
     * Query by example: Find any paper matching the provided {@link Paper} example. 
     *
     * {@link ch.difty.sipamato.persistance.jooq.paper.PaperSlimRepository#findByExample}
     *
     * @param example
     * @return list of {@link PaperSlim}s
     */
    List<PaperSlim> findByExample(Paper example);

}
