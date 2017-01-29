package ch.difty.sipamato.service;

import java.util.List;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

/**
 * The {@link PaperService} interface - defining {@link Paper} specific service methods.
 *
 * @author u.joss
 */
public interface PaperService extends EntityService<Long, Paper, PaperFilter> {

    /**
     * Find the papers with the given ids. (codes are not enriched)
     * @param ids
     * @return list of papers
     */
    List<Paper> findByIds(List<Long> ids);

    /**
     * Find the papers (enriched with codes) with the given ids.
     * @param ids
     * @return list of papers
     */
    List<Paper> findWithCodesByIds(List<Long> ids);

}
