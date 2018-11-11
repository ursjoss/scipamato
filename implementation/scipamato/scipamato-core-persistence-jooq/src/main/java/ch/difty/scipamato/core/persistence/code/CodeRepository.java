package ch.difty.scipamato.core.persistence.code;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;
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
    CodeDefinition findCodeDefinition(String code);

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
    List<CodeDefinition> findPageOfCodeDefinitions(CodeFilter filter, PaginationContext paginationContext);

    /**
     * Counts the number of {@link CodeDefinition}s matching the specified filter.
     *
     * @param filter
     *     of type CodeFilter
     * @return keyword count
     */
    int countByFilter(CodeFilter filter);

    /**
     * @return the main language code as string
     */
    String getMainLanguage();

    /**
     * Creates and returns an unpersisted instance of a CodeDefinition
     * with translations in all relevant languages but no values.
     *
     * @return the unpersisted entity
     */
    CodeDefinition newUnpersistedCodeDefinition();

    /**
     * Inserts a new or pdates an already persisted {@link CodeDefinition} including its
     * associated CodeTranslations in the database. Any associated translation will be updated
     * if it exists in the database (id != null) or will be added if it is new (id == null).
     *
     * @param entity
     *     - must not be null.
     * @return the updated persisted entity, including default values - or {@code null}
     *     if it can't be added. Only the version field is populated out of
     *     all the audit fields.
     * @throws NullArgumentException
     *     if the entity is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    CodeDefinition saveOrUpdate(CodeDefinition entity);

    /**
     * Remove the persisted entity with the provided id.
     *
     * @param code
     *     the code as database id - must not be null
     * @param version
     *     the record version - used for optimistic locking
     * @return the deleted entity
     * @throws NullArgumentException
     *     if the id is null.
     * @throws OptimisticLockingException
     *     if the record version has increased in the mean time
     */
    CodeDefinition delete(String code, int version);

    CodeClass getCodeClass1(final String langCode);
}
