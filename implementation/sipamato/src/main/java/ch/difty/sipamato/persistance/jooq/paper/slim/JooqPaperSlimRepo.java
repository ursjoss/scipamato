package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.Tables;
import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.BooleanSearchTerm;
import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.entity.IntegerSearchTerm;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.StringSearchTerm;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.ConditionalSupplier;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqPaperSlimRepo extends JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, SimplePaperFilter> implements PaperSlimRepository {

    private static final long serialVersionUID = 1L;

    @Autowired
    public JooqPaperSlimRepo(DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<SimplePaperFilter> filterConditionMapper, Localization localization) {
        super(dsl, mapper, sortMapper, filterConditionMapper, localization);
    }

    @Override
    protected Class<? extends PaperSlim> getEntityClass() {
        return PaperSlim.class;
    }

    @Override
    protected Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    /** {@inheritDoc} */
    @Override
    public List<PaperSlim> findByFilter(final CompositeComplexPaperFilter compositeFilter) {
        AssertAs.notNull(compositeFilter, "filter");

        final Condition compositeCondition = getConditionsFrom(compositeFilter);
        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(compositeCondition).fetchInto(getRecordClass());
        final List<PaperSlim> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());
        enrichAssociatedEntitiesOfAll(entities);

        return entities;
    }

    /** {@inheritDoc} */
    @Override
    public Page<PaperSlim> findByFilter(CompositeComplexPaperFilter filter, Pageable pageable) {
        final List<PaperSlim> entities = findByFilter(filter);
        return new PageImpl<>(entities, pageable, (long) countByFilter(filter));
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(CompositeComplexPaperFilter compositeFilter) {
        final Condition conditions = getConditionsFrom(compositeFilter);
        return getDsl().fetchCount(getDsl().selectOne().from(getTable()).where(conditions));
    }

    /*
     * Combines the search terms of different ComplexPaperFilters using OR operators.
     */
    private Condition getConditionsFrom(final CompositeComplexPaperFilter filter) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final ComplexPaperFilter cpf : filter.getFilters())
            conditions.add(() -> getConditionFromSingleComplexFilter(cpf));
        return conditions.combineWithOr();
    }

    /*
     * Combines the individual search terms of a single ComplexPaperFilter using AND operators
     */
    private Condition getConditionFromSingleComplexFilter(ComplexPaperFilter filter) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final BooleanSearchTerm st : filter.getBooleanSearchTerms())
            conditions.add(() -> applyBooleanSearchLogic(st));
        for (final IntegerSearchTerm st : filter.getIntegerSearchTerms())
            conditions.add(() -> applyIntegerSearchLogic(st));
        for (final StringSearchTerm st : filter.getStringSearchTerms())
            conditions.add(() -> applyStringSearchLogic(st));
        return conditions.combineWithAnd();
    }

    /**
     * Evaluates the raw search term for boolean fields and applies the actual condition. 
     */
    private Condition applyBooleanSearchLogic(final BooleanSearchTerm st) {
        return DSL.field(st.key).equal(DSL.val(st.rawValue));
    }

    /**
     * Evaluates the raw search term for integer fields and applies the actual condition
     */
    protected Condition applyIntegerSearchLogic(final IntegerSearchTerm st) {
        switch (st.getType()) {
        case EXACT:
            return DSL.field(st.getKey()).equal(DSL.val(st.getValue()));
        case LESS_OR_EQUAL:
            return DSL.field(st.getKey()).le(DSL.val(st.getValue()));
        case LESS_THAN:
            return DSL.field(st.getKey()).lt(DSL.val(st.getValue()));
        case GREATER_OR_EQUAL:
            return DSL.field(st.getKey()).ge(DSL.val(st.getValue()));
        case GREATER_THAN:
            return DSL.field(st.getKey()).gt(DSL.val(st.getValue()));
        case RANGE:
            return DSL.field(st.getKey()).between(DSL.val(st.getValue()), DSL.val(st.getValue2()));
        default:
            throw new UnsupportedOperationException("Unable to handle type " + st.getType());
        }
    }

    /**
     * Evaluates the raw search term for string fields and applies the actual condition
     *
     * <ul>
     * <li> <literal>foo</literal> --- <code>likeIgnoreCase '%foo%'</code></li>
     * <li> TODO <literal>*foo*</literal> --- <code>likeIgnoreCase '%foo%'</code></li>
     * <li> TODO <literal>"foo"</literal> --- <code>equalsIgnoreCase 'foo'</code></li>
     * <li> TODO <literal>foo*</literal> --- <code>likeIgnoreCase 'foo%'</code></li>
     * <li> TODO <literal>*foo</literal> --- <code>likeIgnoreCase '%foo'</code></li>
     * </ul>
     */
    private Condition applyStringSearchLogic(final StringSearchTerm st) {
        return DSL.field(st.key).lower().contains(DSL.val(st.rawValue).lower());
    }

}
