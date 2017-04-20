package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import ch.difty.sipamato.entity.projection.PaperSlim;

/**
 * Repository to find {@link PaperSlim}s based on SearchOrder specifications.
 *
 * @author u.joss
 */
public interface PaperSlimBackedSearchOrderRepository extends BySearchOrderRepository<PaperSlim> {

}
