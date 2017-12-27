package ch.difty.scipamato.core.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.user.UserRepository;

/**
 * Abstract base repository class providing the fundamental functionality of a
 * jooq read-only Service
 *
 * @author u.joss
 *
 * @param <ID>
 *            the type of the id in the {@link CoreEntity}
 * @param <T>
 *            the entity type, extending {@link CoreEntity}
 * @param <F>
 *            the filter, extending {@link ScipamatoFilter}
 * @param <REPO>
 *            the entity repository (extending {@link EntityRepository}
 */
@Transactional(readOnly = true)
public abstract class JooqReadOnlyService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter, REPO extends ReadOnlyRepository<T, ID, F>>
        implements ReadOnlyService<ID, T, F> {

    private static final long serialVersionUID = 1L;

    private final REPO           repo;
    private final UserRepository userRepo;

    protected JooqReadOnlyService(final REPO repo, final UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    protected REPO getRepository() {
        return repo;
    }

    protected UserRepository getUserRepository() {
        return userRepo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<T> findById(ID id) {
        T entity = repo.findById(id);
        enrichAuditNamesOf(entity);
        return Optional.ofNullable(entity);
    }

    protected void enrichAuditNamesOf(T entity) {
        if (entity != null) {
            setCreatedMetaData(entity);
            setModifiedMeta(entity);
        }

    }

    private void setCreatedMetaData(T entity) {
        final Optional<User> user = getUserWithId(entity.getCreatedBy());
        entity.setCreatedByName(user.map(User::getDisplayValue)
            .orElse(null));
        entity.setCreatedByFullName(user.map(User::getFullName)
            .orElse(null));
    }

    private void setModifiedMeta(T entity) {
        entity.setLastModifiedByName(getUserWithId(entity.getLastModifiedBy()).map(User::getDisplayValue)
            .orElse(null));
    }

    private Optional<User> getUserWithId(final Integer id) {
        if (id != null && getUserRepository() != null) {
            return Optional.ofNullable(getUserRepository().findById(id));
        }
        return Optional.empty();

    }

    /** {@inheritDoc} */
    @Override
    public List<T> findPageByFilter(F filter, PaginationContext paginationContext) {
        final List<T> entities = repo.findPageByFilter(filter, paginationContext);
        entities.forEach(this::enrichAuditNamesOf);
        return entities;
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(F filter) {
        return repo.countByFilter(filter);
    }

    @Override
    public List<ID> findPageOfIdsByFilter(F filter, PaginationContext paginationContext) {
        return repo.findPageOfIdsByFilter(filter, paginationContext);
    }

}
