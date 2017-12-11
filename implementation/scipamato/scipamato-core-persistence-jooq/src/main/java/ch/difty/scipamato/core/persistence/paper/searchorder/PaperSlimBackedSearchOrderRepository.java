package ch.difty.scipamato.core.persistence.paper.searchorder;

import ch.difty.scipamato.core.entity.projection.PaperSlim;

/**
 * Repository to find {@link PaperSlim}s based on SearchOrder specifications.
 *
 * @author u.joss
 */
public interface PaperSlimBackedSearchOrderRepository extends BySearchOrderRepository<PaperSlim> {

}
