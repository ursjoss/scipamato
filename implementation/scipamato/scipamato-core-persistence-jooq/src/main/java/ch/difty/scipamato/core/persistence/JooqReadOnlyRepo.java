package ch.difty.scipamato.core.persistence;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * The generic jOOQ entity repository for read-only data retrieval.
 *
 * @author u.joss
 *
 * @param <R>
 *            the type of the record, extending {@link Record}
 * @param <T>
 *            the type of the entity, extending {@link CoreEntity}
 * @param <ID>
 *            the type of the ID of the entity {@code T}
 * @param <TI>
 *            the type of the table implementation of record {@code R}
 * @param <M>
 *            the type of the record mapper, mapping record {@code R} into the
 *            entity {@code T}
 * @param <F>
 *            the type of the filter, extending {@link ScipamatoFilter}
 */
public abstract class JooqReadOnlyRepo<R extends Record, T extends CoreEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends ScipamatoFilter>
        implements ReadOnlyRepository<T, ID, F> {

    private static final long serialVersionUID = 1L;

    private final DSLContext                      dsl;
    private final M                               mapper;
    private final JooqSortMapper<R, T, TI>        sortMapper;
    private final GenericFilterConditionMapper<F> filterConditionMapper;
    private final ApplicationProperties           applicationProperties;

    /**
     * @param dsl
     *            the {@link DSLContext}
     * @param mapper
     *            record mapper mapping record {@code R} into entity {@code T}
     * @param sortMapper
     *            {@link JooqSortMapper} mapping spring data sort specifications
     *            into jOOQ specific sort specs
     * @param filterConditionMapper
     *            the {@link GenericFilterConditionMapper} mapping a derivative of
     *            {@link ScipamatoFilter} into jOOC {@link Condition}s
     * @param applicationProperties
     */
    protected JooqReadOnlyRepo(final DSLContext dsl, final M mapper, final JooqSortMapper<R, T, TI> sortMapper,
            GenericFilterConditionMapper<F> filterConditionMapper, ApplicationProperties applicationProperties) {
        this.dsl = AssertAs.notNull(dsl, "dsl");
        this.mapper = AssertAs.notNull(mapper, "mapper");
        this.sortMapper = AssertAs.notNull(sortMapper, "sortMapper");
        this.filterConditionMapper = AssertAs.notNull(filterConditionMapper, "filterConditionMapper");
        this.applicationProperties = AssertAs.notNull(applicationProperties, "applicationProperties");

    }

    protected DSLContext getDsl() {
        return dsl;
    }

    protected M getMapper() {
        return mapper;
    }

    protected JooqSortMapper<R, T, TI> getSortMapper() {
        return sortMapper;
    }

    protected ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    /**
     * @return the entity class {@code T.class}
     */
    protected abstract Class<? extends T> getEntityClass();

    /**
     * @return the Record Class {@code R.class}
     */
    protected abstract Class<? extends R> getRecordClass();

    /**
     * @return the jOOQ generated table of type {@code T}
     */
    protected abstract TI getTable();

    /**
     * @return the jOOQ generated {@link TableField} representing the {@code ID}
     */
    protected abstract TableField<R, ID> getTableId();

    protected abstract TableField<R, Integer> getRecordVersion();

    /** {@inheritDoc} */
    @Override
    public List<T> findAll() {
        return findAll(getApplicationProperties().getDefaultLocalization());
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findAll(String languageCode) {
        final List<T> entities = getDsl().selectFrom(getTable())
            .fetchInto(getEntityClass());
        enrichAssociatedEntitiesOfAll(entities, languageCode);
        return entities;
    }

    protected void enrichAssociatedEntitiesOfAll(final List<T> entities, final String languageCode) {
        for (final T e : entities) {
            enrichAssociatedEntitiesOf(e, languageCode);
        }
    }

    /** {@inheritDoc} */
    @Override
    public T findById(final ID id) {
        return findById(id, getApplicationProperties().getDefaultLocalization());
    }

    /** {@inheritDoc} */
    @Override
    public T findById(final ID id, final String languageCode) {
        AssertAs.notNull(id, "id");
        T entity = getDsl().selectFrom(getTable())
            .where(getTableId().equal(id))
            .fetchOneInto(getEntityClass());
        enrichAssociatedEntitiesOf(entity, languageCode);
        return entity;
    }

    /** {@inheritDoc} */
    @Override
    public T findById(final ID id, final int version) {
        return findById(id, version, getApplicationProperties().getDefaultLocalization());
    }

    /** {@inheritDoc} */
    @Override
    public T findById(final ID id, final int version, String languageCode) {
        AssertAs.notNull(id, "id");
        T entity = getDsl().selectFrom(getTable())
            .where(getTableId().equal(id))
            .and(getRecordVersion().equal(version))
            .fetchOneInto(getEntityClass());
        enrichAssociatedEntitiesOf(entity, languageCode);
        return entity;
    }

    /**
     * Implement if associated entities need separate saving.
     *
     * @param entity
     *            entity of type {@code T} with sub entities to be enriched.
     * @param languageCode
     */
    protected void enrichAssociatedEntitiesOf(final T entity, final String languageCode) {
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(final F filter) {
        final Condition conditions = filterConditionMapper.map(filter);
        return getDsl().fetchCount(getDsl().selectOne()
            .from(getTable())
            .where(conditions));
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findPageByFilter(final F filter, final PaginationContext pc) {
        return findPageByFilter(filter, pc, getApplicationProperties().getDefaultLocalization());
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findPageByFilter(final F filter, final PaginationContext pc, final String languageCode) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        final List<R> tuples = getDsl().selectFrom(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetchInto(getRecordClass());

        final List<T> entities = tuples.stream()
            .map(getMapper()::map)
            .collect(Collectors.toList());

        enrichAssociatedEntitiesOfAll(entities, languageCode);

        return entities;
    }

    /** {@inheritDoc} */
    @Override
    public List<ID> findPageOfIdsByFilter(final F filter, final PaginationContext pc) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        return getDsl().select()
            .from(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch(getTableId());
    }

}
