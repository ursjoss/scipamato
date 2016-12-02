package ch.difty.sipamato.service;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.search.SearchOrderFilter;

/**
 * The {@link SearchOrderService} interface - defining {@link SearchOrder} specific service methods.
 *
 * @author u.joss
 */
public interface SearchOrderService extends EntityService<Long, SearchOrder, SearchOrderFilter> {

}
