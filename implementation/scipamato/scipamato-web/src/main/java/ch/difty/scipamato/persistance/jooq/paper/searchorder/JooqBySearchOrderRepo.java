package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import static ch.difty.scipamato.db.tables.Paper.*;
import static ch.difty.scipamato.db.tables.PaperCode.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.db.Tables;
import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.IdScipamatoEntity;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.AuditSearchTerm;
import ch.difty.scipamato.entity.filter.BooleanSearchTerm;
import ch.difty.scipamato.entity.filter.IntegerSearchTerm;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.entity.filter.StringSearchTerm;
import ch.difty.scipamato.persistance.jooq.ConditionalSupplier;
import ch.difty.scipamato.persistance.jooq.EntityRecordMapper;
import ch.difty.scipamato.persistance.jooq.JooqSortMapper;
import ch.difty.scipamato.persistence.paging.PaginationContext;

/**
 * Common abstract base class for the paper or paperSlim specific repository implementations to find those by {@link SearchOrder}.
 *
 * @author u.joss
 *
 * @param <T>
 *      derivatives of {@link IdScipamatoEntity}, should actually be Paper or PaperSlims
 * @param <M>
 *      derivatives of {@link EntityRecordMapper} specific to Papers or PaperSlims
 */
public abstract class JooqBySearchOrderRepo<T extends IdScipamatoEntity<Long>, M extends EntityRecordMapper<PaperRecord, T>> implements BySearchOrderRepository<T> {

    private final IntegerSearchTermEvaluator integerSearchTermEvaluator = new IntegerSearchTermEvaluator();
    private final StringSearchTermEvaluator stringSearchTermEvaluator = new StringSearchTermEvaluator();
    private final BooleanSearchTermEvaluator booleanSearchTermEvaluator = new BooleanSearchTermEvaluator();
    private final AuditSearchTermEvaluator auditSearchTermEvaluator = new AuditSearchTermEvaluator();

    private final DSLContext dsl;
    private final M mapper;
    private final JooqSortMapper<PaperRecord, T, ch.difty.scipamato.db.tables.Paper> sortMapper;

    /**
     * @param dsl
     *      the {@link DSLContext}
     * @param mapper
     *      derivatives of {@link EntityRecordMapper} specific to type {@code Paper}s or {@code PaperSlim}s
     * @param sortMapper
     *      paper or paperSlim specific {@link JooqSortMapper}
     */
    public JooqBySearchOrderRepo(final DSLContext dsl, final M mapper, final JooqSortMapper<PaperRecord, T, ch.difty.scipamato.db.tables.Paper> sortMapper) {
        this.dsl = dsl;
        this.mapper = mapper;
        this.sortMapper = sortMapper;
    }

    private DSLContext getDsl() {
        return dsl;
    }

    private M getMapper() {
        return mapper;
    }

    private JooqSortMapper<PaperRecord, T, ch.difty.scipamato.db.tables.Paper> getSortMapper() {
        return sortMapper;
    }

    private Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findBySearchOrder(final SearchOrder searchOrder) {
        AssertAs.notNull(searchOrder, "searchOrder");

        final Condition paperMatches = getConditionsFrom(searchOrder);
        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(paperMatches).fetchInto(getRecordClass());

        return queryResults.stream().map(getMapper()::map).collect(Collectors.toList());
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
        if (searchOrder.isShowExcluded()) {
            return PAPER.ID.in(searchOrder.getExcludedPaperIds());
        } else {
            for (final SearchCondition sc : searchOrder.getSearchConditions())
                conditions.add(() -> getConditionFromSingleSearchCondition(sc));
            final Condition scConditions = conditions.combineWithOr();
            if (searchOrder.getExcludedPaperIds().isEmpty() || "1 = 0".equals(scConditions.toString())) {
                return scConditions;
            } else {
                return scConditions.and(PAPER.ID.notIn(searchOrder.getExcludedPaperIds()));
            }
        }
    }

    /**
     * Combines the individual search terms of a single {@link SearchCondition} using AND operators
     */
    private Condition getConditionFromSingleSearchCondition(final SearchCondition searchCondition) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final BooleanSearchTerm st : searchCondition.getBooleanSearchTerms())
            conditions.add(() -> booleanSearchTermEvaluator.evaluate(st));
        for (final IntegerSearchTerm st : searchCondition.getIntegerSearchTerms())
            conditions.add(() -> integerSearchTermEvaluator.evaluate(st));
        for (final StringSearchTerm st : searchCondition.getStringSearchTerms())
            conditions.add(() -> stringSearchTermEvaluator.evaluate(st));
        for (final AuditSearchTerm st : searchCondition.getAuditSearchTerms())
            conditions.add(() -> auditSearchTermEvaluator.evaluate(st));
        if (!searchCondition.getCodes().isEmpty()) {
            conditions.add(() -> codeConditions(searchCondition.getCodes()));
        }
        return conditions.combineWithAnd();
    }

    private Condition codeConditions(final List<Code> codes) {
        final ConditionalSupplier codeConditions = new ConditionalSupplier();
        for (final String code : codes.stream().map(Code::getCode).collect(Collectors.toList())) {
            final SelectConditionStep<Record1<Integer>> step = DSL.selectOne().from(PAPER_CODE).where(PAPER_CODE.PAPER_ID.eq(PAPER.ID));
            codeConditions.add(() -> DSL.exists(step.and(DSL.lower(PAPER_CODE.CODE).eq(code.toLowerCase()))));
        }
        return codeConditions.combineWithAnd();
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findPageBySearchOrder(final SearchOrder searchOrder, final PaginationContext pc) {
        final Condition paperMatches = getConditionsFrom(searchOrder);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), PAPER);
        final List<PaperRecord> tuples = getDsl().selectFrom(Tables.PAPER).where(paperMatches).orderBy(sortCriteria).limit(pc.getPageSize()).offset(pc.getOffset()).fetchInto(getRecordClass());
        return tuples.stream().map(getMapper()::map).collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public int countBySearchOrder(final SearchOrder searchOrder) {
        AssertAs.notNull(searchOrder, "searchOrder");

        final Condition paperMatches = getConditionsFrom(searchOrder);
        return getDsl().fetchCount(getDsl().selectOne().from(PAPER).where(paperMatches));
    }

    @Override
    public List<Long> findPageOfIdsBySearchOrder(final SearchOrder searchOrder, final PaginationContext pc) {
        final Condition conditions = getConditionsFrom(searchOrder);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), PAPER);
        return getDsl().select().from(Tables.PAPER).where(conditions).orderBy(sortCriteria).limit(pc.getPageSize()).offset(pc.getOffset()).fetch(PAPER.ID);
    }
}
