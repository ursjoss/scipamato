package ch.difty.scipamato.persistance.jooq;

import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.entity.IdScipamatoEntity;
import ch.difty.scipamato.entity.ScipamatoEntity;
import ch.difty.scipamato.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.service.EntityService;

/**
 * Abstract base repository class providing the fundamental functionality of a JooqService
 *
 * @author u.joss
 *
 * @param <ID> the type of the id in the {@link ScipamatoEntity}
 * @param <T> the entity type, extending {@link ScipamatoEntity}
 * @param <F> the filter, extending {@link ScipamatoFilter}
 * @param <REPO> the entity repository (extending {@link EntityRepository}
 */
public abstract class JooqEntityService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter, REPO extends EntityRepository<T, ID, F>>
        extends JooqReadOnlyService<ID, T, F, REPO> implements EntityService<ID, T, F> {

    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Transactional(readOnly = false)
    @Override
    public T saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            T added = getRepository().add(entity);
            enrichAuditNamesOf(added);
            return added;
        } else {
            T updated = getRepository().update(entity);
            enrichAuditNamesOf(updated);
            return updated;
        }
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public void remove(T entity) {
        if (entity != null && entity.getId() != null) {
            getRepository().delete(entity.getId());
        }
    }

}
