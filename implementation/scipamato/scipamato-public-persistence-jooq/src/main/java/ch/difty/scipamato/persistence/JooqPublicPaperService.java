package ch.difty.scipamato.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.paging.PaginationContext;

@Service
public class JooqPublicPaperService implements PublicPaperService {

    private static final long serialVersionUID = 1L;

    private PublicPaperRepository repository;

    private PublicPaperRepository getRepository() {
        return repository;
    }

    @Autowired
    public void setRepository(final PublicPaperRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PublicPaper> findById(final Long id) {
        final PublicPaper entity = getRepository().findById(id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<PublicPaper> findPageByFilter(final PublicPaperFilter filter, final PaginationContext paginationContext) {
        return getRepository().findPageByFilter(filter, paginationContext);
    }

    @Override
    public int countByFilter(final PublicPaperFilter filter) {
        return getRepository().countByFilter(filter);
    }

}
