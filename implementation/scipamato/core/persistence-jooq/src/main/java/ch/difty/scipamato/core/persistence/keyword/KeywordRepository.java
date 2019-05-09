package ch.difty.scipamato.core.persistence.keyword;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

public interface KeywordRepository {

    /**
     * Find the localized {@link Keyword}s
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of keywords
     */
    List<Keyword> findAll(String languageCode);

    /**
     * Find the {@link KeywordDefinition} with the provided id
     *
     * @param id
     *     the id of the record
     * @return the {@link KeywordDefinition} or null if not found
     */
    KeywordDefinition findKeywordDefinitionById(int id);

    /**
     * Finds a page full of {@link KeywordDefinition} records matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of {@link KeywordDefinition}s as list
     */
    List<KeywordDefinition> findPageOfKeywordDefinitions(KeywordFilter filter, PaginationContext paginationContext);

    /**
     * Counts the number of {@link KeywordDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type KeywordFilter
     * @return keyword count
     */
    int countByFilter(KeywordFilter filter);

    /**
     * @return the main language code as string
     */
    String getMainLanguage();

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
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    KeywordDefinition update(KeywordDefinition entity);

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
    KeywordDefinition delete(Integer id, int version);
}
