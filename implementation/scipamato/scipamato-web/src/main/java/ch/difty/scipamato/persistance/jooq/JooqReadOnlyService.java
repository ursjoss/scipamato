package ch.difty.scipamato.persistance.jooq;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.entity.IdScipamatoEntity;
import ch.difty.scipamato.entity.ScipamatoEntity;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.persistance.jooq.user.UserRepository;
import ch.difty.scipamato.persistence.ReadOnlyService;
import ch.difty.scipamato.persistence.paging.PaginationContext;

/**
 * Abstract base repository class providing the fundamental functionality of a  jooq read-only Service
 *
 * @author u.joss
 *
 * @param <ID> the type of the id in the {@link ScipamatoEntity}
 * @param <T> the entity type, extending {@link ScipamatoEntity}
 * @param <F> the filter, extending {@link ScipamatoFilter}
 * @param <REPO> the entity repository (extending {@link EntityRepository}
 */
@Transactional(readOnly = true)
public abstract class JooqReadOnlyService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter, REPO extends ReadOnlyRepository<T, ID, F>>
        implements ReadOnlyService<ID, T, F> {

    private static final long serialVersionUID = 1L;

    private REPO repo;
    private UserRepository userRepo;

    @Autowired
    public void setRepository(REPO repo) {
        this.repo = repo;
    }

    protected REPO getRepository() {
        return repo;
    }

    protected UserRepository getUserRepository() {
        return userRepo;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
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
        entity.setCreatedByName(user.map(User::getDisplayValue).orElse(null));
        entity.setCreatedByFullName(user.map(User::getFullName).orElse(null));
    }

    private void setModifiedMeta(T entity) {
        entity.setLastModifiedByName(getUserWithId(entity.getLastModifiedBy()).map(User::getDisplayValue).orElse(null));
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
