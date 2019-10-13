package ch.difty.scipamato.core.persistence;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.CodeLikeService;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;

/**
 * Service operating with {@link Code}s.
 *
 * @author u.joss
 */
public interface CodeService extends CodeLikeService<Code>, DefinitionProviderService<CodeDefinition, CodeFilter> {

    /**
     * Finds a page full of {@link CodeDefinition} records matching the provided filter
     * and pagination context.
     *
     * @param filter
     *     the filter
     * @param paginationContext
     *     context defining paging and sorting
     * @return a page of CodeDefinitions as list
     */
    @NotNull
    List<CodeDefinition> findPageOfCodeDefinitions(@Nullable CodeFilter filter,
        @NotNull PaginationContext paginationContext);

    /**
     * Counts the number of {@link CodeDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type CodeFilter
     * @return entity count
     */
    int countByFilter(@Nullable CodeFilter filter);

    /**
     * Creates and returns an unpersisted instance of a CodeDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    @NotNull
    CodeDefinition newUnpersistedCodeDefinition();

    /**
     * Persists the provided entity.
     *
     * @param entity
     *     the {@link CodeDefinition} to be persisted
     * @return the persisted entity.
     */
    @Nullable
    CodeDefinition saveOrUpdate(@NotNull CodeDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param code
     *     the code
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    @Nullable
    CodeDefinition delete(@NotNull String code, int version);

    @NotNull
    CodeClass getCodeClass1(@NotNull String langCode);
}
