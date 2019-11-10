package ch.difty.scipamato.core.persistence.code;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.code.CodeLikeRepository;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

/**
 * Provides access to the localized {@link Code}s.
 *
 * @author u.joss
 */
public interface CodeRepository extends CodeLikeRepository<Code> {

    /**
     * Find the {@link CodeDefinition} with the provided id
     *
     * @param code
     *     the code of the record
     * @return the {@link CodeDefinition} or null if not found
     */
    @Nullable
    CodeDefinition findCodeDefinition(@NotNull String code);

    /**
     * Finds a page full of {@link CodeDefinition} records matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of {@link CodeDefinition}s as list
     */
    @NotNull
    List<CodeDefinition> findPageOfCodeDefinitions(@Nullable CodeFilter filter,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of {@link CodeDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type CodeFilter
     * @return keyword count
     */
    int countByFilter(@Nullable CodeFilter filter);

    /**
     * @return the main language code as string
     */
    @NotNull
    String getMainLanguage();

    /**
     * Creates and returns an unpersisted instance of a CodeDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    @NotNull
    CodeDefinition newUnpersistedCodeDefinition();

    /**
     * Inserts a new or updates an already persisted {@link CodeDefinition} including its
     * associated CodeTranslations in the database. Any associated translation will be updated
     * if it exists in the database (id != null) or will be added if it is new (id == null).
     *
     * @param entity
     *     - must not be null.
     * @return the updated persisted entity, including default values - or {@code null}
     *     if it can't be added. Only the version field is populated out of
     *     all the audit fields.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @Nullable
    CodeDefinition saveOrUpdate(@NotNull CodeDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param code
     *     the code as database id - must not be null
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity - or null
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @Nullable
    CodeDefinition delete(@NotNull String code, int version);

    /**
     * Returns code class 1 in the desired translation.
     * If the translation is not available in the database, we will still
     * receive a {@link CodeClass}, but with name/description 'not translated'
     *
     * @param langCode
     * @return {@link CodeClass} never null
     */
    @NotNull
    CodeClass getCodeClass1(@NotNull final String langCode);
}
