package ch.difty.sipamato.persistance.jooq;

import java.util.List;
import java.util.Optional;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.entity.filter.SipamatoFilter;
import ch.difty.sipamato.persistance.jooq.user.UserRepository;
import ch.difty.sipamato.service.ReadOnlyService;

/**
 * Abstract base repository class providing the fundamental functionality of a  jooq read-only Service
 *
 * @author u.joss
 *
 * @param <ID> the type of the id in the {@link SipamatoEntity}
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <M> the type of the record mapper mapping records of type <literal>R</literal> into entities of type <literal>T</literal>
 * @param <F> the filter, extending {@link SipamatoFilter}
 * @param <REPO> the entity repository (extending {@link EntityRepository}
 */
public abstract class JooqReadOnlyService<ID extends Number, R extends Record, T extends IdSipamatoEntity<ID>, F extends SipamatoFilter, M extends RecordMapper<R, T>, REPO extends ReadOnlyRepository<R, T, ID, M, F>>
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
        if (id != null && userRepo != null) {
            return Optional.ofNullable(userRepo.findById(id));
        }
        return Optional.empty();

    }

    /** {@inheritDoc} */
    @Override
    public Page<T> findByFilter(F filter, Pageable pageable) {
        final Page<T> page = repo.findByFilter(filter, pageable);
        page.forEach(this::enrichAuditNamesOf);
        return page;
    }

    protected void enrichAuditNamesOfAll(final List<T> entities) {
        for (final T e : entities) {
            enrichAuditNamesOf(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(F filter) {
        return repo.countByFilter(filter);
    }

}
