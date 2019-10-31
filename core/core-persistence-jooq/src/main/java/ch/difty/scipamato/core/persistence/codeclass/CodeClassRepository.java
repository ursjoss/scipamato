package ch.difty.scipamato.core.persistence.codeclass;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.codeclass.CodeClassLikeRepository;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

/**
 * Provides access to the localized {@link CodeClass}es.
 *
 * @author u.joss
 */
public interface CodeClassRepository extends CodeClassLikeRepository<CodeClass> {

    /**
     * Find the {@link CodeClassDefinition} with the provided id
     *
     * @param id
     *     id of the record
     * @return the {@link CodeClassDefinition} or null if not found
     */
    @Nullable
    CodeClassDefinition findCodeClassDefinition(@NotNull Integer id);

    /**
     * Finds a page full of {@link CodeClassDefinition} records matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of CodeClassDefinition entities as list
     */
    @NotNull
    List<CodeClassDefinition> findPageOfCodeClassDefinitions(@Nullable CodeClassFilter filter,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of {@link CodeClassDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type CodeClassFilter
     * @return entity count
     */
    int countByFilter(@Nullable CodeClassFilter filter);

    /**
     * @return the main language code as string
     */
    @NotNull
    String getMainLanguage();

    /**
     * Creates and returns an unpersisted instance of a CodeClassDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    @NotNull
    CodeClassDefinition newUnpersistedCodeClassDefinition();

    /**
     * Persists the provided entity.
     *
     * @param entity
     *     the {@link CodeClassDefinition} to be persisted
     * @return the persisted entity.
     */
    @NotNull
    CodeClassDefinition saveOrUpdate(@NotNull CodeClassDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id
     *     the code class id
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @Nullable
    CodeClassDefinition delete(@NotNull Integer id, int version);
}
