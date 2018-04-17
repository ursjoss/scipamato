package ch.difty.scipamato.core.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.User;

/**
 * Abstract base repository class providing the fundamental functionality of a
 * jooq read-only Service
 *
 * @param <ID>
 *     the type of the id in the {@link CoreEntity}
 * @param <T>
 *     the entity type, extending {@link CoreEntity}
 * @param <F>
 *     the filter, extending {@link ScipamatoFilter}
 * @param <REPO>
 *     the entity repository (extending {@link EntityRepository}
 * @author u.joss
 */
@Transactional(readOnly = true)
public abstract class JooqReadOnlyService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter, REPO extends ReadOnlyRepository<T, ID, F>>
    implements ReadOnlyService<ID, T, F> {

    private final REPO           repo;
    private final UserRepository userRepo;

    protected JooqReadOnlyService(final REPO repo, final UserRepository userRepo) {
        this.repo = AssertAs.notNull(repo, "repo");
        this.userRepo = AssertAs.notNull(userRepo, "userRepo");
    }

    protected REPO getRepository() {
        return repo;
    }

    protected UserRepository getUserRepository() {
        return userRepo;
    }

    @Override
    public Optional<T> findById(final ID id) {
        final T entity = repo.findById(id);
        enrichAuditNamesOf(entity);
        return Optional.ofNullable(entity);
    }

    protected void enrichAuditNamesOf(final T entity) {
        if (entity != null) {
            setCreatedMetaData(entity);
            setModifiedMeta(entity);
        }
    }

    private void setCreatedMetaData(final T entity) {
        final Optional<User> user = getUserWithId(entity.getCreatedBy());
        entity.setCreatedByName(user
            .map(User::getDisplayValue)
            .orElse(null));
        entity.setCreatedByFullName(user
            .map(User::getFullName)
            .orElse(null));
    }

    private void setModifiedMeta(final T entity) {
        entity.setLastModifiedByName(getUserWithId(entity.getLastModifiedBy())
            .map(User::getDisplayValue)
            .orElse(null));
    }

    private Optional<User> getUserWithId(final Integer id) {
        if (id != null)
            return Optional.ofNullable(getUserRepository().findById(id));
        return Optional.empty();

    }

    @Override
    public List<T> findPageByFilter(final F filter, final PaginationContext paginationContext) {
        final List<T> entities = repo.findPageByFilter(filter, paginationContext);
        entities.forEach(this::enrichAuditNamesOf);
        return entities;
    }

    @Override
    public int countByFilter(final F filter) {
        return repo.countByFilter(filter);
    }

    @Override
    public List<ID> findPageOfIdsByFilter(final F filter, final PaginationContext paginationContext) {
        return repo.findPageOfIdsByFilter(filter, paginationContext);
    }

}
