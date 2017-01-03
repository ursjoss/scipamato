package ch.difty.sipamato.persistance.jooq;

import java.util.List;
import java.util.Optional;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.SipamatoEntity;
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
        implements ReadOnlyService<T, ID, F> {

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
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findByFilter(F filter, Pageable pageable) {
        return repo.findByFilter(filter, pageable).getContent();
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(F filter) {
        return repo.countByFilter(filter);
    }

}
