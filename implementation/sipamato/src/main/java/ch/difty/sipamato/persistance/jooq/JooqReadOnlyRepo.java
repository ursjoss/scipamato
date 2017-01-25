package ch.difty.sipamato.persistance.jooq;

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
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.filter.SipamatoFilter;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.service.Localization;

/**
 * The generic jOOQ entity repository for read-only data retrieval.
 *
 * @author u.joss
 *
 * @param <R> the type of the record, extending {@link Record}
 * @param <T> the type of the entity, extending {@link SipamatoEntity}
 * @param <ID> the type of the ID of the entity <literal>T</literal>
 * @param <TI> the type of the table implementation of record <literal>R</literal>
 * @param <M> the type of the record mapper, mapping record <literal>R</literal> into the entity <literal>T</literal>
 * @param <F> the type of the filter, extending {@link SipamatoFilter}
 */
@Profile("DB_JOOQ")
@Transactional(readOnly = true)
public abstract class JooqReadOnlyRepo<R extends Record, T extends SipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends SipamatoFilter>
        implements ReadOnlyRepository<R, T, ID, M, F> {

    private static final long serialVersionUID = 1L;

    private final DSLContext dsl;
    private final M mapper;
    private final JooqSortMapper<R, T, TI> sortMapper;
    private final GenericFilterConditionMapper<F> filterConditionMapper;
    private final Localization localization;

    /**
     * @param dsl the {@link DSLContext}
     * @param mapper record mapper mapping record <literal>R</literal> into entity <literal>T</literal>
     * @param sortMapper {@link JooqSortMapper} mapping spring data sort specifications into jOOQ specific sort specs
     * @param filterConditionMapper the {@link GenericFilterConditionMapper} mapping a derivative of {@link SipamatoFilter} into jOOC {@link Condition}s
     * @param localization {@link Localization} bean providing the information about the requested localization code.
     */
    protected JooqReadOnlyRepo(final DSLContext dsl, final M mapper, final JooqSortMapper<R, T, TI> sortMapper, GenericFilterConditionMapper<F> filterConditionMapper, Localization localization) {
        this.dsl = AssertAs.notNull(dsl, "dsl");
        this.mapper = AssertAs.notNull(mapper, "mapper");
        this.sortMapper = AssertAs.notNull(sortMapper, "sortMapper");
        this.filterConditionMapper = AssertAs.notNull(filterConditionMapper, "filterConditionMapper");
        this.localization = AssertAs.notNull(localization, "localization");
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

    protected Localization getLocalization() {
        return localization;
    }

    /**
     * @return the entity class <code>T.class</code>
     */
    protected abstract Class<? extends T> getEntityClass();

    /**
     * @return the Record Class <code>R.class</code>
     */
    protected abstract Class<? extends R> getRecordClass();

    /**
     * @return the jOOQ generated table of type <code>T</code>
     */
    protected abstract TI getTable();

    /**
     * @return the jOOQ generated {@link TableField} representing the <code>ID</code>
     */
    protected abstract TableField<R, ID> getTableId();

    /** {@inheritDoc} */
    @Override
    public List<T> findAll() {
        final List<T> entities = getDsl().selectFrom(getTable()).fetchInto(getEntityClass());
        enrichAssociatedEntitiesOfAll(entities);
        return entities;
    }

    protected void enrichAssociatedEntitiesOfAll(final List<T> entities) {
        for (final T e : entities) {
            enrichAssociatedEntitiesOf(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public T findById(final ID id) {
        AssertAs.notNull(id, "id");
        T entity = getDsl().selectFrom(getTable()).where(getTableId().equal(id)).fetchOneInto(getEntityClass());
        enrichAssociatedEntitiesOf(entity);
        return entity;
    }

    /**
     * Implement if associated entities need separate saving.
     * @param entity entity of type <code>T</code> with sub entities to be enriched.
     */
    protected void enrichAssociatedEntitiesOf(final T entity) {
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(final F filter) {
        final Condition conditions = filterConditionMapper.map(filter);
        return getDsl().fetchCount(getDsl().selectOne().from(getTable()).where(conditions));
    }

    /** {@inheritDoc} */
    @Override
    public Page<T> findByFilter(final F filter, final Pageable pageable) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pageable.getSort(), getTable());
        final List<R> queryResults = getDsl().selectFrom(getTable()).where(conditions).orderBy(sortCriteria).limit(pageable.getPageSize()).offset(pageable.getOffset()).fetchInto(getRecordClass());

        final List<T> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());

        enrichAssociatedEntitiesOfAll(entities);

        return new PageImpl<>(entities, pageable, (long) countByFilter(filter));
    }

}
