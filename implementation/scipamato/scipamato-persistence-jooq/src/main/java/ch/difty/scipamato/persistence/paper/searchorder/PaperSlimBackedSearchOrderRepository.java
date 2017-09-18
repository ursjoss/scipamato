package ch.difty.scipamato.persistence.paper.searchorder;

import ch.difty.scipamato.entity.projection.PaperSlim;

/**
 * Repository to find {@link PaperSlim}s based on SearchOrder specifications.
 *
 * @author u.joss
 */
public interface PaperSlimBackedSearchOrderRepository extends BySearchOrderRepository<PaperSlim> {

}
