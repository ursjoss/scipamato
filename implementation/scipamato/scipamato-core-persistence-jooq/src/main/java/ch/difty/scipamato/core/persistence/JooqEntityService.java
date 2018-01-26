package ch.difty.scipamato.core.persistence;

import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

/**
 * Abstract base repository class providing the fundamental functionality of a
 * JooqService
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
public abstract class JooqEntityService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter, REPO extends EntityRepository<T, ID, F>>
        extends JooqReadOnlyService<ID, T, F, REPO> implements EntityService<ID, T, F> {

    private static final long serialVersionUID = 1L;

    protected JooqEntityService(final REPO repo, final UserRepository userRepo) {
        super(repo, userRepo);
    }

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

    @Override
    @Transactional(readOnly = false)
    public void remove(T entity) {
        if (entity != null && entity.getId() != null) {
            getRepository().delete(entity.getId(), entity.getVersion());
        }
    }

}
