package ch.difty.scipamato.core.persistence;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.CodeClassLikeService;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;

/**
 * Service operating with {@link CodeClass}es.
 *
 * @author u.joss
 */
public interface CodeClassService
    extends CodeClassLikeService<CodeClass>, DefinitionProviderService<CodeClassDefinition, CodeClassFilter> {

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
     *     the id of the code class record
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the meantime
     */
    @Nullable
    CodeClassDefinition delete(@NotNull Integer id, int version);
}
