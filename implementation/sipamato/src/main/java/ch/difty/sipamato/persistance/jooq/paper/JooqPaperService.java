package ch.difty.sipamato.persistance.jooq.paper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.service.PaperService;

/**
 * jOOQ specific implementation of the {@link PaperService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperService implements PaperService {

    private static final long serialVersionUID = 1L;

    private PaperRepository repo;

    @Autowired
    public void setRepository(PaperRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Paper> findById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<Paper> findByFilter(PaperFilter filter, Pageable pageable) {
        return repo.findByFilter(filter, pageable).getContent();
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(PaperFilter filter) {
        return repo.countByFilter(filter);
    }

    /** {@inhericDoc} */
    @Override
    public Paper update(Paper paper) {
        return repo.update(paper);
    }

}
