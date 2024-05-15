package ch.difty.scipamato.core.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * The generic repository interface for managing entities.
 *
 * @param <T>
 *     the type of the entity, extending {@link CoreEntity}
 * @param <ID>
 *     the type of the ID of the entity
 * @param <F>
 *     the type of the filter extending {@link ScipamatoFilter}
 * @author u.joss
 */
public interface EntityRepository<T extends CoreEntity, ID, F extends ScipamatoFilter> extends ReadOnlyRepository<T, ID, F> {

    /**
     * Add an entity {@code T} to the database.
     *
     * @param entity
     *     - must not be null
     * @return the added entity, including the generated default values - or
     *     {@code null} if it can't be added.
     */
    @Nullable
    T add(@NotNull T entity);

    /**
     * Add an entity {@code T} to the database.
     *
     * @param entity
     *     - must not be null
     * @param languageCode
     *     - must not be null
     * @return the added entity, including the generated default values - or
     *     {@code null} if it can't be added.
     */
    @Nullable
    T add(@NotNull T entity, @NotNull String languageCode);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id
     *     the database id - must not be null
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @NotNull
    T delete(@NotNull ID id, int version);

    /**
     * Searches the persistent entity {@code T} and modifies it according to the
     * values of the provided entity.
     *
     * @param entity
     *     the entity with some changed values - must not be null.
     * @return the modified persisted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @Nullable
    T update(@NotNull T entity);

    /**
     * Searches the persistent entity {@code T} and modifies it according to the
     * values of the provided entity.
     *
     * @param entity
     *     the entity with some changed values - must not be null.
     * @param languageCode
     *     the language code (e.g. 'en') - must not be null.
     * @return the modified persisted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @Nullable
    T update(@NotNull T entity, @NotNull String languageCode);
}
