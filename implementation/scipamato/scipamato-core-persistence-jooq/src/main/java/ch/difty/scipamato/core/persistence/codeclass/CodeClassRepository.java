package ch.difty.scipamato.core.persistence.codeclass;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.codeclass.CodeClassLikeRepository;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
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
    CodeClassDefinition findCodeClassDefinition(Integer id);

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
    List<CodeClassDefinition> findPageOfCodeClassDefinitions(CodeClassFilter filter,
        PaginationContext paginationContext);

    /**
     * Counts the number of {@link CodeClassDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type CodeClassFilter
     * @return entity count
     */
    int countByFilter(CodeClassFilter filter);

    /**
     * @return the main language code as string
     */
    String getMainLanguage();

    /**
     * Creates and returns an unpersisted instance of a CodeClassDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    CodeClassDefinition newUnpersistedCodeClassDefinition();

    /**
     * Persists the provided entity.
     *
     * @param entity
     *     the {@link CodeClassDefinition} to be persisted
     * @return the persisted entity.
     */
    CodeClassDefinition saveOrUpdate(CodeClassDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param id
     *     the code class id
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws NullArgumentException
     *     if the id is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    CodeClassDefinition delete(Integer id, int version);

}
