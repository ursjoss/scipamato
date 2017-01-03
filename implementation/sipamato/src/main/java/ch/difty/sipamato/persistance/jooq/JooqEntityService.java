package ch.difty.sipamato.persistance.jooq;

import org.jooq.Record;
import org.jooq.RecordMapper;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.filter.SipamatoFilter;
import ch.difty.sipamato.service.EntityService;

/**
 * Abstract base repository class providing the fundamental functionality of a JooqService
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
public abstract class JooqEntityService<ID extends Number, R extends Record, T extends IdSipamatoEntity<ID>, F extends SipamatoFilter, M extends RecordMapper<R, T>, REPO extends EntityRepository<R, T, ID, M, F>>
        extends JooqReadOnlyService<ID, R, T, F, M, REPO> implements EntityService<ID, T, F> {

    private static final long serialVersionUID = 1L;

    /** {@inhericDoc} */
    @Override
    public T saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            return getRepository().add(entity);
        } else {
            return getRepository().update(entity);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void remove(T entity) {
        if (entity != null && entity.getId() != null) {
            getRepository().delete(entity.getId());
        }
    }

}
