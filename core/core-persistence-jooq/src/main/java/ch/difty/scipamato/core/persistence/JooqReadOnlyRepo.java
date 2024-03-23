package ch.difty.scipamato.core.persistence;

import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.*;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * The generic jOOQ entity repository for read-only data retrieval.
 *
 * @param <R>
 *     the type of the record, extending {@link Record}
 * @param <T>
 *     the type of the entity, extending {@link CoreEntity}
 * @param <ID>
 *     the type of the ID of the entity {@code T}
 * @param <TI>
 *     the type of the table implementation of record {@code R}
 * @param <M>
 *     the type of the record mapper, mapping record {@code R} into the
 *     entity {@code T}
 * @param <F>
 *     the type of the filter, extending {@link ScipamatoFilter}
 * @author u.joss
 */
@SuppressWarnings("WeakerAccess")
public abstract class JooqReadOnlyRepo<R extends Record, T extends CoreEntity, ID, TI extends Table<R>, M extends RecordMapper<R, T>, F extends ScipamatoFilter>
    extends AbstractRepo implements ReadOnlyRepository<T, ID, F> {

    private final M                               mapper;
    private final JooqSortMapper<R, T, TI>        sortMapper;
    private final GenericFilterConditionMapper<F> filterConditionMapper;
    private final ApplicationProperties           applicationProperties;

    /**
     * @param dsl
     *     the {@link DSLContext}
     * @param mapper
     *     record mapper mapping record {@code R} into entity {@code T}
     * @param sortMapper
     *     {@link JooqSortMapper} mapping spring data sort specifications
     *     into jOOQ specific sort specs
     * @param filterConditionMapper
     *     the {@link GenericFilterConditionMapper} mapping a derivative of
     *     {@link ScipamatoFilter} into jOOC {@link Condition}s
     * @param dateTimeService
     *     the {@link DateTimeService} providing access to the system time
     * @param applicationProperties
     *     the object providing the application properties
     */
    protected JooqReadOnlyRepo(@NotNull final DSLContext dsl, @NotNull final M mapper, @NotNull final JooqSortMapper<R, T, TI> sortMapper,
        @NotNull final GenericFilterConditionMapper<F> filterConditionMapper, @NotNull final DateTimeService dateTimeService,
        @NotNull final ApplicationProperties applicationProperties) {
        super(dsl, dateTimeService);

        this.mapper = mapper;
        this.sortMapper = sortMapper;
        this.filterConditionMapper = filterConditionMapper;
        this.applicationProperties = applicationProperties;

    }

    @NotNull
    protected M getMapper() {
        return mapper;
    }

    @NotNull
    protected GenericFilterConditionMapper<F> getFilterConditionMapper() {
        return filterConditionMapper;
    }

    @NotNull
    protected JooqSortMapper<R, T, TI> getSortMapper() {
        return sortMapper;
    }

    @NotNull
    protected ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    /**
     * @return the jOOQ generated table of type {@code T}
     */
    @NotNull
    protected abstract TI getTable();

    /**
     * @return the jOOQ generated {@link TableField} representing the {@code ID}
     */
    @NotNull
    protected abstract TableField<R, ID> getTableId();

    @NotNull
    protected abstract TableField<R, Integer> getRecordVersion();

    @NotNull
    @Override
    public List<T> findAll() {
        return findAll(getApplicationProperties().getDefaultLocalization());
    }

    @NotNull
    @Override
    public List<T> findAll(@Nullable final String languageCode) {
        final List<T> entities = getDsl()
            .selectFrom(getTable())
            .fetch(getMapper());
        enrichAssociatedEntitiesOfAll(entities, languageCode);
        return entities;
    }

    protected void enrichAssociatedEntitiesOfAll(@NotNull final List<T> entities, @Nullable final String languageCode) {
        for (final T e : entities) {
            enrichAssociatedEntitiesOf(e, languageCode);
        }
    }

    @Nullable
    @Override
    public T findById(@NotNull final ID id) {
        return findById(id, getApplicationProperties().getDefaultLocalization());
    }

    @Nullable
    @Override
    public T findById(@NotNull final ID id, @Nullable final String languageCode) {
        final T entity = getDsl()
            .selectFrom(getTable())
            .where(getTableId().equal(id))
            .fetchOne(getMapper());
        enrichAssociatedEntitiesOf(entity, languageCode);
        return entity;
    }

    @Nullable
    @Override
    public T findById(@NotNull final ID id, final int version) {
        return findById(id, version, getApplicationProperties().getDefaultLocalization());
    }

    @Nullable
    @Override
    public T findById(@NotNull final ID id, final int version, @Nullable String languageCode) {
        final T entity = getDsl()
            .selectFrom(getTable())
            .where(getTableId().equal(id))
            .and(getRecordVersion().equal(version))
            .fetchOne(getMapper());
        enrichAssociatedEntitiesOf(entity, languageCode);
        return entity;
    }

    /**
     * Implement if associated entities need separate saving.
     *
     * @param entity
     *     entity of type {@code T} with sub entities to be enriched.
     * @param languageCode
     *     the two character language code
     */
    protected void enrichAssociatedEntitiesOf(@Nullable final T entity, @Nullable final String languageCode) {
    }

    @Override
    public int countByFilter(@Nullable final F filter) {
        final Condition conditions = filterConditionMapper.map(filter);
        return getDsl().fetchCount(getDsl()
            .selectOne()
            .from(getTable())
            .where(conditions));
    }

    @NotNull
    @Override
    public List<T> findPageByFilter(@Nullable final F filter, @NotNull final PaginationContext pc) {
        return findPageByFilter(filter, pc, getApplicationProperties().getDefaultLocalization());
    }

    @NotNull
    @Override
    public List<T> findPageByFilter(@Nullable final F filter, @NotNull final PaginationContext pc, @Nullable final String languageCode) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        final List<T> entities = getDsl()
            .selectFrom(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch(getMapper());

        enrichAssociatedEntitiesOfAll(entities, languageCode);

        return entities;
    }

    @NotNull
    @Override
    public List<ID> findPageOfIdsByFilter(@Nullable final F filter, @NotNull final PaginationContext pc) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        return getDsl()
            .select()
            .from(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch(getTableId());
    }
}
