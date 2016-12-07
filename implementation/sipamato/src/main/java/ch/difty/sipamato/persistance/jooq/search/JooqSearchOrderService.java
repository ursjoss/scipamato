package ch.difty.sipamato.persistance.jooq.search;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.service.SearchOrderService;

/**
 * jOOQ specific implementation of the {@link SearchOrderService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqSearchOrderService implements SearchOrderService {

    private static final long serialVersionUID = 1L;

    private SearchOrderRepository repo;

    @Autowired
    public void setRepository(SearchOrderRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<SearchOrder> findById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<SearchOrder> findByFilter(SearchOrderFilter filter, Pageable pageable) {
        return repo.findByFilter(filter, pageable).getContent();
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(SearchOrderFilter filter) {
        return repo.countByFilter(filter);
    }

    /** {@inheritDoc} */
    @Override
    public SearchOrder saveOrUpdate(SearchOrder searchOrder) {
        if (searchOrder.getId() == null) {
            return repo.add(searchOrder);
        } else {
            return repo.update(searchOrder);
        }
    }

    /** {@inheritDoc} */
    @Override
    public SearchCondition saveOrUpdateSearchCondition(SearchCondition searchCondition, long searchOrderId) {
        if (searchCondition.getConditionId() == null) {
            return repo.addSearchCondition(searchCondition, searchOrderId);
        } else {
            return repo.updateSearchCondition(searchCondition, searchOrderId);
        }
    }
}
