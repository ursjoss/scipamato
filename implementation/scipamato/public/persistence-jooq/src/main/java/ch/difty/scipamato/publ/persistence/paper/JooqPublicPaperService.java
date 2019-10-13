package ch.difty.scipamato.publ.persistence.paper;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.api.PublicPaperService;

@Service
public class JooqPublicPaperService implements PublicPaperService {

    private final PublicPaperRepository repository;

    public JooqPublicPaperService(@NotNull final PublicPaperRepository repository) {
        this.repository = repository;
    }

    private PublicPaperRepository getRepository() {
        return repository;
    }

    @NotNull
    @Override
    public Optional<PublicPaper> findByNumber(@NotNull final Long number) {
        final PublicPaper entity = getRepository().findByNumber(number);
        return Optional.ofNullable(entity);
    }

    @NotNull
    @Override
    public List<PublicPaper> findPageByFilter(@Nullable final PublicPaperFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return getRepository().findPageByFilter(filter, paginationContext);
    }

    @Override
    public int countByFilter(final PublicPaperFilter filter) {
        return getRepository().countByFilter(filter);
    }

    @NotNull
    @Override
    public List<Long> findPageOfNumbersByFilter(@Nullable PublicPaperFilter filter,
        @NotNull PaginationContext paginationContext) {
        return getRepository().findPageOfNumbersByFilter(filter, paginationContext);
    }
}
