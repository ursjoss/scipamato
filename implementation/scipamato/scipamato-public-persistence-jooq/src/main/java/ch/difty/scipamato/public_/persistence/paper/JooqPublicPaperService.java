package ch.difty.scipamato.public_.persistence.paper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.public_.entity.PublicPaper;
import ch.difty.scipamato.public_.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.public_.persistence.api.PublicPaperService;

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
    public Optional<PublicPaper> findByNumber(final Long number) {
        final PublicPaper entity = getRepository().findByNumber(number);
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

    @Override
    public List<Long> findPageOfNumbersByFilter(PublicPaperFilter filter, PaginationContext paginationContext) {
        return getRepository().findPageOfNumbersByFilter(filter, paginationContext);
    }

}
