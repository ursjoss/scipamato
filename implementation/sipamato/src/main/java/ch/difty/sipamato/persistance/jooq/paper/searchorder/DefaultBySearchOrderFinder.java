package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static ch.difty.sipamato.db.tables.PaperCode.PAPER_CODE;
import static ch.difty.sipamato.db.tables.User.USER;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.db.Tables;
import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.BooleanSearchTerm;
import ch.difty.sipamato.entity.filter.IntegerSearchTerm;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.StringSearchTerm;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.ConditionalSupplier;
import ch.difty.sipamato.persistance.jooq.EntityRecordMapper;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;

public class DefaultBySearchOrderFinder<T extends IdSipamatoEntity<Long>, M extends EntityRecordMapper<PaperRecord, T>> implements BySearchOrderFinder<T, M> {

    private final IntegerSearchTermEvaluator integerSearchTermEvaluator = new IntegerSearchTermEvaluator();
    private final StringSearchTermEvaluator stringSearchTermEvaluator = new StringSearchTermEvaluator();
    private final BooleanSearchTermEvaluator booleanSearchTermEvaluator = new BooleanSearchTermEvaluator();

    private final DSLContext dsl;
    private final M mapper;
    private final JooqSortMapper<PaperRecord, T, ch.difty.sipamato.db.tables.Paper> sortMapper;
    private final Class<? extends PaperRecord> recordClass;

    private DSLContext getDsl() {
        return dsl;
    }

    private M getMapper() {
        return mapper;
    }

    private JooqSortMapper<PaperRecord, T, ch.difty.sipamato.db.tables.Paper> getSortMapper() {
        return sortMapper;
    }

    private Class<? extends PaperRecord> getRecordClass() {
        return recordClass;
    }

    public DefaultBySearchOrderFinder(final DSLContext dsl, final M mapper, JooqSortMapper<PaperRecord, T, ch.difty.sipamato.db.tables.Paper> sortMapper, Class<? extends PaperRecord> recordClass) {
        this.dsl = dsl;
        this.mapper = mapper;
        this.sortMapper = sortMapper;
        this.recordClass = recordClass;
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findBySearchOrder(final SearchOrder searchOrder) {
        AssertAs.notNull(searchOrder, "searchOrder");

        final Condition paperMatches = getConditionsFrom(searchOrder);
        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(paperMatches).fetchInto(getRecordClass());
        final List<T> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());

        return entities;

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
        if (searchOrder.getExcludedPaperIds().isEmpty() || scConditions.toString().equals("1 = 0")) {
            return scConditions;
        } else {
            final Condition exclusionCondition = makeExclusionCondition(searchOrder);
            return scConditions.and(exclusionCondition);
        }
    }

    private Condition makeExclusionCondition(final SearchOrder searchOrder) {
        List<Long> excludedIds = searchOrder.getExcludedPaperIds();
        if (searchOrder.isInvertExclusions()) {
            return PAPER.ID.in(excludedIds);
        } else {
            return PAPER.ID.notIn(excludedIds);
        }
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
        if (searchCondition.getCreatedDisplayValue() != null) {
            conditions.add(() -> userCondition(PAPER.CREATED_BY, searchCondition.getCreatedDisplayValue()));
        }
        if (searchCondition.getModifiedDisplayValue() != null) {
            conditions.add(() -> userCondition(PAPER.LAST_MODIFIED_BY, searchCondition.getModifiedDisplayValue()));
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

    private Condition userCondition(final TableField<PaperRecord, Integer> userField, final String user) {
        final ConditionalSupplier c = new ConditionalSupplier();
        final String userName = "%" + user.toLowerCase() + "%";
        final SelectConditionStep<Record1<Long>> step = DSL.select(PAPER.ID).from(PAPER).innerJoin(USER).on(userField.eq(USER.ID)).where(USER.USER_NAME.lower().like(userName));
        c.add(() -> PAPER.ID.in(step));
        return c.combineWithAnd();
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findPagedBySearchOrder(SearchOrder searchOrder, Pageable pageable) {
        final Condition paperMatches = getConditionsFrom(searchOrder);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pageable.getSort(), PAPER);
        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER)
                .where(paperMatches)
                .orderBy(sortCriteria)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(getRecordClass());
        return queryResults.stream().map(getMapper()::map).collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        AssertAs.notNull(searchOrder, "searchOrder");

        final Condition paperMatches = getConditionsFrom(searchOrder);
        return getDsl().fetchCount(getDsl().selectOne().from(PAPER).where(paperMatches));
    }
}
