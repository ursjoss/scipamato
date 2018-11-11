package ch.difty.scipamato.core.persistence;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
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
    List<CodeDefinition> findPageOfCodeDefinitions(CodeFilter filter, PaginationContext paginationContext);

    /**
     * Counts the number of {@link CodeDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type CodeFilter
     * @return entity count
     */
    int countByFilter(CodeFilter filter);

    /**
     * Creates and returns an unpersisted instance of a CodeDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    CodeDefinition newUnpersistedCodeDefinition();

    /**
     * Persists the provided entity.
     *
     * @param entity
     *     the {@link CodeDefinition} to be persisted
     * @return the persisted entity.
     */
    CodeDefinition saveOrUpdate(CodeDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param code
     *     the code
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws NullArgumentException
     *     if the id is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    CodeDefinition delete(String code, int version);

    CodeClass getCodeClass1(String langCode);
}
