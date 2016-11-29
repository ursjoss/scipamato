package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableField;
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

    private final IntegerSearchTermEvaluator integerSearchTermEvaluator = new IntegerSearchTermEvaluator();
    private final StringSearchTermEvaluator stringSearchTermEvaluator = new StringSearchTermEvaluator();
    private final BooleanSearchTermEvaluator booleanSearchTermEvaluator = new BooleanSearchTermEvaluator();

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
            conditions.add(() -> booleanSearchTermEvaluator.evaluate(st));
        for (final IntegerSearchTerm st : filter.getIntegerSearchTerms())
            conditions.add(() -> integerSearchTermEvaluator.evaluate(st));
        for (final StringSearchTerm st : filter.getStringSearchTerms())
            conditions.add(() -> stringSearchTermEvaluator.evaluate(st));
        return conditions.combineWithAnd();
    }

}
