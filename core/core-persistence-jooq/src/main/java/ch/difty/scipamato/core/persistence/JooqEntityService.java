package ch.difty.scipamato.core.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.core.entity.CoreEntity;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

/**
 * Abstract base repository class providing the fundamental functionality of a
 * JooqService
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
public abstract class JooqEntityService<ID extends Number, T extends IdScipamatoEntity<ID>, F extends ScipamatoFilter, REPO extends EntityRepository<T, ID, F>>
    extends JooqReadOnlyService<ID, T, F, REPO> implements EntityService<ID, T, F> {

    protected JooqEntityService(@NotNull final REPO repo, @NotNull final UserRepository userRepo) {
        super(repo, userRepo);
    }

    @Nullable
    @Transactional
    @Override
    public T saveOrUpdate(@NotNull final T entity) {
        if (entity.getId() == null) {
            final T added = getRepository().add(entity);
            enrichAuditNamesOf(added);
            return added;
        } else {
            final T updated = getRepository().update(entity);
            enrichAuditNamesOf(updated);
            return updated;
        }
    }

    @Override
    @Transactional
    public void remove(@Nullable final T entity) {
        if (entity != null && entity.getId() != null) {
            getRepository().delete(entity.getId(), entity.getVersion());
        }
    }
}
