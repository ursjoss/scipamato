package ch.difty.scipamato.core.persistence.keyword;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;

@Service
class JooqKeywordService implements KeywordService {

    private final KeywordRepository repo;

    public JooqKeywordService(@NotNull final KeywordRepository repo) {
        this.repo = repo;
    }

    @NotNull
    public KeywordRepository getRepo() {
        return repo;
    }

    @NotNull
    @Override
    public List<Keyword> findAll(@NotNull final String languageCode) {
        return getRepo().findAll(languageCode);
    }

    @NotNull
    @Override
    public List<KeywordDefinition> findPageOfKeywordDefinitions(@Nullable final KeywordFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return getRepo().findPageOfKeywordDefinitions(filter, paginationContext);
    }

    @NotNull
    @Override
    public Iterator<KeywordDefinition> findPageOfEntityDefinitions(@Nullable final KeywordFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return findPageOfKeywordDefinitions(filter, paginationContext).iterator();
    }

    @Override
    public int countByFilter(@Nullable final KeywordFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @NotNull
    @Override
    public KeywordDefinition newUnpersistedKeywordDefinition() {
        return getRepo().newUnpersistedKeywordDefinition();
    }

    @Nullable
    @Override
    public KeywordDefinition insert(@NotNull final KeywordDefinition entity) {
        return getRepo().insert(entity);
    }

    @Nullable
    @Override
    public KeywordDefinition update(@NotNull final KeywordDefinition entity) {
        return getRepo().update(entity);
    }

    @Nullable
    @Override
    public KeywordDefinition saveOrUpdate(@NotNull final KeywordDefinition entity) {
        if (entity.getId() == null)
            return getRepo().insert(entity);
        else
            return getRepo().update(entity);
    }

    @Nullable
    @Override
    public KeywordDefinition delete(final int id, final int version) {
        return getRepo().delete(id, version);
    }
}
