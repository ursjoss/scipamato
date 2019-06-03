package ch.difty.scipamato.core.persistence.keyword;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;

@Service
class JooqKeywordService implements KeywordService {

    private final KeywordRepository repo;

    public JooqKeywordService(final KeywordRepository repo) {
        this.repo = repo;
    }

    public KeywordRepository getRepo() {
        return repo;
    }

    @Override
    public List<Keyword> findAll(final String languageCode) {
        return getRepo().findAll(languageCode);
    }

    @Override
    public List<KeywordDefinition> findPageOfKeywordDefinitions(final KeywordFilter filter,
        final PaginationContext paginationContext) {
        return getRepo().findPageOfKeywordDefinitions(filter, paginationContext);
    }

    @Override
    public Iterator<KeywordDefinition> findPageOfEntityDefinitions(final KeywordFilter filter,
        final PaginationContext paginationContext) {
        return findPageOfKeywordDefinitions(filter, paginationContext).iterator();
    }

    @Override
    public int countByFilter(final KeywordFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @Override
    public KeywordDefinition newUnpersistedKeywordDefinition() {
        return getRepo().newUnpersistedKeywordDefinition();
    }

    @Override
    public KeywordDefinition insert(final KeywordDefinition entity) {
        return getRepo().insert(entity);
    }

    @Override
    public KeywordDefinition update(final KeywordDefinition entity) {
        return getRepo().update(entity);
    }

    @Override
    public KeywordDefinition saveOrUpdate(final KeywordDefinition entity) {
        AssertAs.notNull(entity, "entity");
        if (entity.getId() == null)
            return getRepo().insert(entity);
        else
            return getRepo().update(entity);
    }

    @Override
    public KeywordDefinition delete(final int id, final int version) {
        return getRepo().delete(id, version);
    }

}
