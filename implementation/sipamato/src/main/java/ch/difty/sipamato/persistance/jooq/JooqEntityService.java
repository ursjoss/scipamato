package ch.difty.sipamato.persistance.jooq;

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
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <F> the filter, extending {@link SipamatoFilter}
 * @param <REPO> the entity repository (extending {@link EntityRepository}
 */
public abstract class JooqEntityService<ID extends Number, T extends IdSipamatoEntity<ID>, F extends SipamatoFilter, REPO extends EntityRepository<T, ID, F>>
        extends JooqReadOnlyService<ID, T, F, REPO> implements EntityService<ID, T, F> {

    private static final long serialVersionUID = 1L;

    /** {@inhericDoc} */
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
    public void remove(T entity) {
        if (entity != null && entity.getId() != null) {
            getRepository().delete(entity.getId());
        }
    }

}
