package ch.difty.scipamato.core.persistence.keyword;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull
    List<Keyword> findAll(@NotNull String languageCode);

    /**
     * Find the {@link KeywordDefinition} with the provided id
     *
     * @param id
     *     the id of the record
     * @return the {@link KeywordDefinition} or null if not found
     */
    @Nullable
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
    @NotNull
    List<KeywordDefinition> findPageOfKeywordDefinitions(@Nullable KeywordFilter filter,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of {@link KeywordDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type KeywordFilter
     * @return keyword count
     */
    int countByFilter(@Nullable KeywordFilter filter);

    /**
     * @return the main language code as string
     */
    @NotNull
    String getMainLanguage();

    /**
     * Creates and returns an unpersisted instance of a KeywordDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    @NotNull
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
     */
    @Nullable
    KeywordDefinition insert(@NotNull KeywordDefinition entity);

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
     * @throws OptimisticLockingException
     *     if the record version has increased in the meantime
     */
    @Nullable
    KeywordDefinition update(@NotNull KeywordDefinition entity);

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
    @Nullable
    KeywordDefinition delete(@NotNull Integer id, int version);
}
