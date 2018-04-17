package ch.difty.scipamato.core.persistence;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * The generic repository interface for reading-only entity repository methods.
 *
 * @param <T>
 *     the type of the entity, extending {@link CoreEntity}
 * @param <ID>
 *     the type of the ID of the entity
 * @param <F>
 *     the type of the filter extending {@link ScipamatoFilter}
 * @author u.joss
 */
public interface ReadOnlyRepository<T extends CoreEntity, ID, F extends ScipamatoFilter> {

    /**
     * Finds all persisted entities. If a repo requires the language code, the
     * default language code will be used.
     *
     * @return list of all entities {@code T}
     */
    List<T> findAll();

    /**
     * Finds all persisted entities.
     *
     * @param languageCode
     *     required for some repos only.
     * @return list of all entities {@code T}
     */
    List<T> findAll(String languageCode);

    /**
     * Finds the persistent entities {@code T} with the provided id. If a repo
     * requires the language code, the default language code will be used.
     *
     * @param id
     *     - must not be null
     * @return the persisted entity {@code T} or null if it can't be found.
     * @throws NullArgumentException
     *     if the id is null.
     */
    T findById(ID id);

    /**
     * Finds the persistent entities {@code T} with the provided id.
     *
     * @param id
     *     - must not be null
     * @param languageCode
     *     required for some repos only.
     * @return the persisted entity {@code T} or null if it can't be found.
     * @throws NullArgumentException
     *     if the id is null.
     */
    T findById(ID id, String languageCode);

    /**
     * Finds the persistent entities {@code T} with the provided id. If a repo
     * requires the language code, the default language code will be used.
     *
     * @param id
     *     the database id - must not be null
     * @param version
     *     the record version - implementing optimistic locking
     * @return the persisted entity {@code T} or null if it can't be found.
     * @throws NullArgumentException
     *     if the id is null.
     */
    T findById(ID id, int version);

    /**
     * Finds the persistent entities {@code T} with the provided id.
     *
     * @param id
     *     the database id - must not be null
     * @param version
     *     the record version - implementing optimistic locking
     * @param languageCode
     *     required for some repos only.
     * @return the persisted entity {@code T} or null if it can't be found.
     * @throws NullArgumentException
     *     if the id is null.
     */
    T findById(ID id, int version, String languageCode);

    /**
     * Finds the persisted entities matching the provided filter and pagination
     * context. If a repo requires the language code, the default language code will
     * be used.
     *
     * @param filter
     *     of type {@code F}
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of all matching entities {@code T}
     */
    List<T> findPageByFilter(F filter, PaginationContext paginationContext);

    /**
     * Finds the persisted entities matching the provided filter and pagination
     * context.
     *
     * @param filter
     *     of type {@code F}
     * @param paginationContext
     *     {@link PaginationContext}
     * @param languageCode
     *     required for some repos only.
     * @return list of all matching entities {@code T}
     */
    List<T> findPageByFilter(F filter, PaginationContext paginationContext, String languageCode);

    /**
     * Counts all persisted entities matching the provided filter.
     *
     * @param filter
     *     of type {@code F}
     * @return list of all matching entities {@code T}
     */
    int countByFilter(F filter);

    /**
     * Finds the ids of the persisted entities matching the provided filter and
     * pagination context. If a repo requires the language code, the default
     * language code will be used.
     *
     * @param filter
     *     of type {@code F}
     * @param paginationContext
     *     {@link PaginationContext}
     * @return list of the ids of type {@code ID} of matching entities {@code T}
     */
    List<ID> findPageOfIdsByFilter(F filter, PaginationContext paginationContext);

}
