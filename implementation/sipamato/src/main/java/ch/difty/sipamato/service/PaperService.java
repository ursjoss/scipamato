package ch.difty.sipamato.service;

import java.util.List;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service methods.
 *
 * @author u.joss
 */
public interface PaperService extends EntityService<Paper, PaperFilter> {

    /**
     * Query by example: Find any paper matching the criteria. 
     *
     * {@link ch.difty.sipamato.persistance.jooq.paper.PaperRepository#findByExample}
     *
     * @param example
     * @return list of {@link Paper}s
     */
    List<Paper> findByExample(Paper example);
}
