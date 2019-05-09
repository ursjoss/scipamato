package ch.difty.scipamato.core.persistence;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;

/**
 * Service Interface as gateway to manage persisting {@link Keyword} instances.
 *
 * @author Urs Joss
 */
public interface KeywordService extends DefinitionProviderService<KeywordDefinition, KeywordFilter> {

    /**
     * Find the localized {@link Keyword}s
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of keywords
     */
    List<Keyword> findAll(String languageCode);

    /**
     * Finds a page full of {@link KeywordDefinition} records matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of entities of type {@code T} as list
     */
    List<KeywordDefinition> findPageOfKeywordDefinitions(KeywordFilter filter, PaginationContext paginationContext);

    /**
     * Counts the number of {@link KeywordDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type KeywordFilter
     * @return entity count
     */
    int countByFilter(KeywordFilter filter);

    /**
     * Creates and returns an unpersisted instance of a KeywordDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    KeywordDefinition newUnpersistedKeywordDefinition();

    /**
     * Persist an unpersisted {@link KeywordDefinition} including its associated
     * KeywordTranslations to the database.
     *
     * @param entity
     *     - must not be null, id must be null
     * @return the persisted entity, including the generated id and default values - or
     *     {@code null} if it can't be added. Only the version field is populated out of
     *     all the audit fields.
     * @throws NullArgumentException
     *     if the entity is null.
     */
    KeywordDefinition insert(KeywordDefinition entity);

    /**
     * Update an already persisted {@link KeywordDefinition} including its associated
     * KeywordTranslations in the database. Any associated translation will be updated
     * if it exists in the database (id != null) or will be added if it is new (id == null).
     *
     * @param entity
     *     - must not be null, id must not be null.
     * @return the updated persisted entity, including the generated id and default values - or
     *     {@code null} if it can't be added. Only the version field is populated out of
     *     all the audit fields.
     * @throws NullArgumentException
     *     if the entity is null.
     */
    KeywordDefinition update(KeywordDefinition entity);

    /**
     * Persists the provided entity. Depending on the absence or presence of an ID, the
     * entity will be added to the database or updated.
     *
     * @param entity
     *     the {@link KeywordDefinition} to be persisted
     * @return the persisted entity.
     */
    KeywordDefinition saveOrUpdate(KeywordDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id
     *     the database id - must not be null
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws NullArgumentException
     *     if the id is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    KeywordDefinition delete(int id, int version);

}
