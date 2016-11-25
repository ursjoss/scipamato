package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperSlimService;

/**
 * jOOQ specific implementation of the {@link PaperSlimService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperSlimService implements PaperSlimService {

    private static final long serialVersionUID = 1L;

    private PaperSlimRepository repo;

    @Autowired
    public void setRepository(PaperSlimRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<PaperSlim> findById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findByFilter(SimplePaperFilter filter, Pageable pageable) {
        return repo.findByFilter(filter, pageable).getContent();
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(SimplePaperFilter filter) {
        return repo.countByFilter(filter);
    }

    /** {@inhericDoc} */
    @Override
    public List<PaperSlim> findByFilter(CompositeComplexPaperFilter compositeFilter) {
        return repo.findByFilter(compositeFilter);
    }

    /** {@inhericDoc} */
    @Override
    public Page<PaperSlim> findByFilter(CompositeComplexPaperFilter compositeFilter, Pageable pageable) {
        return repo.findByFilter(compositeFilter, pageable);
    }

    /** {@inhericDoc} */
    @Override
    public int countByFilter(CompositeComplexPaperFilter compositeFilter) {
        return repo.countByFilter(compositeFilter);
    }
}
