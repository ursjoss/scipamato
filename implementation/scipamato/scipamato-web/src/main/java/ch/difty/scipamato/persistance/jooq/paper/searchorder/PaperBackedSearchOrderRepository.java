package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import ch.difty.scipamato.entity.Paper;

/**
 * Repository to find {@link Paper}s based on SearchOrder specifications.
 *
 * @author u.joss
 */
public interface PaperBackedSearchOrderRepository extends BySearchOrderRepository<Paper> {

}
