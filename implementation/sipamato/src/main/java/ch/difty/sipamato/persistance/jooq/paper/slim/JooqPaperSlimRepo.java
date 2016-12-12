package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static ch.difty.sipamato.db.tables.PaperCode.PAPER_CODE;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.Tables;
import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.BooleanSearchTerm;
import ch.difty.sipamato.entity.filter.IntegerSearchTerm;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.StringSearchTerm;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.ConditionalSupplier;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqPaperSlimRepo extends JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> implements PaperSlimRepository {

    private static final long serialVersionUID = 1L;

    private final IntegerSearchTermEvaluator integerSearchTermEvaluator = new IntegerSearchTermEvaluator();
    private final StringSearchTermEvaluator stringSearchTermEvaluator = new StringSearchTermEvaluator();
    private final BooleanSearchTermEvaluator booleanSearchTermEvaluator = new BooleanSearchTermEvaluator();

    @Autowired
    public JooqPaperSlimRepo(DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<PaperFilter> filterConditionMapper, Localization localization) {
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
    public List<PaperSlim> findBySearchOrder(final SearchOrder searchOrder) {
        AssertAs.notNull(searchOrder, "searchOrder");

        final Condition paperMatches = getConditionsFrom(searchOrder);
        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(paperMatches).fetchInto(getRecordClass());
        final List<PaperSlim> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());
        enrichAssociatedEntitiesOfAll(entities);

        return entities;
    }

    private Condition makeExclusionCondition(final SearchOrder searchOrder) {
        List<Long> excludedIds = searchOrder.getExcludedPaperIds();
        if (searchOrder.isInvertExclusions()) {
            return PAPER.ID.in(excludedIds);
        } else {
            return PAPER.ID.notIn(excludedIds);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Page<PaperSlim> findBySearchOrder(SearchOrder searchOrder, Pageable pageable) {
        final List<PaperSlim> entities = findBySearchOrder(searchOrder);
        return new PageImpl<>(entities, pageable, (long) countBySearchOrder(searchOrder));
    }

    /** {@inheritDoc} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        AssertAs.notNull(searchOrder, "searchOrder");

        final Condition paperMatches = getConditionsFrom(searchOrder);
        return getDsl().fetchCount(getDsl().selectOne().from(getTable()).where(paperMatches));
    }

    /**
     * Combines the search terms of different {@link SearchOrder} using OR operators.
     *
     * Note: searchOrder must not be null. this is to be guarded from the public entry methods.
     *
     * protected for test purposes
     */
    protected Condition getConditionsFrom(final SearchOrder searchOrder) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final SearchCondition sc : searchOrder.getSearchConditions())
            conditions.add(() -> getConditionFromSingleSearchCondition(sc));
        final Condition scConditions = conditions.combineWithOr();
        final Condition exclusionCondition = makeExclusionCondition(searchOrder);
        return scConditions.and(exclusionCondition);
    }

    /**
     * Combines the individual search terms of a single {@link SearchCondition} using AND operators
     */
    private Condition getConditionFromSingleSearchCondition(SearchCondition searchCondition) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final BooleanSearchTerm st : searchCondition.getBooleanSearchTerms())
            conditions.add(() -> booleanSearchTermEvaluator.evaluate(st));
        for (final IntegerSearchTerm st : searchCondition.getIntegerSearchTerms())
            conditions.add(() -> integerSearchTermEvaluator.evaluate(st));
        for (final StringSearchTerm st : searchCondition.getStringSearchTerms())
            conditions.add(() -> stringSearchTermEvaluator.evaluate(st));
        if (!searchCondition.getCodes().isEmpty()) {
            conditions.add(() -> codeConditions(searchCondition.getCodes()));
        }
        return conditions.combineWithAnd();
    }

    private Condition codeConditions(List<Code> codes) {
        final ConditionalSupplier codeConditions = new ConditionalSupplier();
        for (final String code : codes.stream().map(Code::getCode).collect(Collectors.toList())) {
            final SelectConditionStep<Record1<Integer>> step = DSL.selectOne().from(PAPER_CODE).where(PAPER_CODE.PAPER_ID.eq(PAPER.ID));
            codeConditions.add(() -> DSL.exists(step.and(DSL.lower(PAPER_CODE.CODE).eq(code.toLowerCase()))));
        }
        return codeConditions.combineWithAnd();
    }

}
