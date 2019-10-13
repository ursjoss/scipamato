package ch.difty.scipamato.core.persistence.paper.searchorder;

import ch.difty.scipamato.core.entity.Paper;

/**
 * Repository to find {@link Paper}s based on SearchOrder specifications.
 *
 * @author u.joss
 */
public interface PaperBackedSearchOrderRepository extends BySearchOrderRepository<Paper> {
}
