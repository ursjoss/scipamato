package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.core.db.Tables.PAPER_ATTACHMENT;
import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.db.tables.PaperCode.PAPER_CODE;
import static ch.difty.scipamato.core.db.tables.PaperNewsletter.PAPER_NEWSLETTER;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.db.Tables;
import ch.difty.scipamato.core.db.tables.Newsletter;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.search.*;
import ch.difty.scipamato.core.persistence.ConditionalSupplier;
import ch.difty.scipamato.core.persistence.EntityRecordMapper;

/**
 * Common abstract base class for the paper or paperSlim specific repository
 * implementations to find those by {@link SearchOrder}.
 *
 * @param <T>
 *     derivatives of {@link IdScipamatoEntity}, should actually be Paper
 *     or PaperSlims
 * @param <M>
 *     derivatives of {@link EntityRecordMapper} specific to Papers or
 *     PaperSlims
 * @author u.joss
 */
@SuppressWarnings("WeakerAccess")
public abstract class JooqBySearchOrderRepo<T extends IdScipamatoEntity<Long>, M extends EntityRecordMapper<PaperRecord, T>>
    implements BySearchOrderRepository<T> {

    private final IntegerSearchTermEvaluator integerSearchTermEvaluator = new IntegerSearchTermEvaluator();
    private final StringSearchTermEvaluator  stringSearchTermEvaluator  = new StringSearchTermEvaluator();
    private final BooleanSearchTermEvaluator booleanSearchTermEvaluator = new BooleanSearchTermEvaluator();
    private final AuditSearchTermEvaluator   auditSearchTermEvaluator   = new AuditSearchTermEvaluator();

    private final DSLContext dsl;
    private final M          mapper;

    private final JooqSortMapper<PaperRecord, T, ch.difty.scipamato.core.db.tables.Paper> sortMapper;

    /**
     * @param dsl
     *     the {@link DSLContext}
     * @param mapper
     *     derivatives of {@link EntityRecordMapper} specific to type
     *     {@code Paper}s or {@code PaperSlim}s
     * @param sortMapper
     *     paper or paperSlim specific {@link JooqSortMapper}
     */
    public JooqBySearchOrderRepo(@Qualifier("dslContext") @NotNull final DSLContext dsl, @NotNull final M mapper,
        @NotNull final JooqSortMapper<PaperRecord, T, ch.difty.scipamato.core.db.tables.Paper> sortMapper) {
        this.dsl = dsl;
        this.mapper = mapper;
        this.sortMapper = sortMapper;
    }

    @NotNull
    protected DSLContext getDsl() {
        return dsl;
    }

    @NotNull
    protected M getMapper() {
        return mapper;
    }

    @NotNull
    protected JooqSortMapper<PaperRecord, T, ch.difty.scipamato.core.db.tables.Paper> getSortMapper() {
        return sortMapper;
    }

    @NotNull
    protected Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @NotNull
    @Override
    public List<T> findBySearchOrder(@NotNull final SearchOrder searchOrder) {
        final Condition paperMatches = getConditionsFrom(searchOrder);
        final List<PaperRecord> queryResults = getDsl()
            .selectFrom(Tables.PAPER)
            .where(paperMatches)
            .fetchInto(getRecordClass());

        return queryResults
            .stream()
            .map(getMapper()::map)
            .collect(Collectors.toList());
    }

    /**
     * Combines the search terms of different {@link SearchOrder} using OR
     * operators.
     * <p>
     * Note: searchOrder must not be null. this is to be guarded from the public
     * entry methods.
     * <p>
     * public for test purposes
     *
     * @param searchOrder
     *     the {@link SearchOrder} for which to return the conditions
     * @return {@link Condition}
     */
    @NotNull
    public Condition getConditionsFrom(@NotNull final SearchOrder searchOrder) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        if (searchOrder.isShowExcluded()) {
            return PAPER.ID.in(searchOrder.getExcludedPaperIds());
        } else {
            for (final SearchCondition sc : searchOrder.getSearchConditions())
                conditions.add(() -> getConditionFromSingleSearchCondition(sc));
            final Condition scConditions = conditions.combineWithOr();
            if (searchOrder
                    .getExcludedPaperIds()
                    .isEmpty() || "false".equals(scConditions.toString())) {
                return scConditions;
            } else {
                return scConditions.and(PAPER.ID.notIn(searchOrder.getExcludedPaperIds()));
            }
        }
    }

    /**
     * Combines the individual search terms of a single {@link SearchCondition}
     * using AND operators
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
        if (!searchCondition
            .getCodes()
            .isEmpty()) {
            conditions.add(() -> codeConditions(searchCondition.getCodes()));
        }
        if (searchCondition.getNewsletterHeadline() != null || searchCondition.getNewsletterTopicId() != null
            || searchCondition.getNewsletterIssue() != null) {
            conditions.add(() -> newsletterConditions(searchCondition));
        }
        if (searchCondition.getHasAttachments() != null || searchCondition.getAttachmentNameMask() != null) {
            conditions.add(() -> attachmentConditions(searchCondition));
        }
        return conditions.combineWithAnd();
    }

    private Condition codeConditions(final List<Code> codes) {
        final ConditionalSupplier codeConditions = new ConditionalSupplier();
        for (final String code : codes
            .stream()
            .map(Code::getCode)
            .collect(Collectors.toList())) {
            final SelectConditionStep<Record1<Integer>> step = DSL
                .selectOne()
                .from(PAPER_CODE)
                .where(PAPER_CODE.PAPER_ID.eq(PAPER.ID));
            codeConditions.add(() -> DSL.exists(step.and(DSL
                .lower(PAPER_CODE.CODE)
                .eq(code.toLowerCase()))));
        }
        return codeConditions.combineWithAnd();
    }

    private Condition newsletterConditions(final SearchCondition sc) {
        final ConditionalSupplier nlConditions = new ConditionalSupplier();
        final SelectConditionStep<Record1<Integer>> step0 = DSL
            .selectOne()
            .from(PAPER_NEWSLETTER)
            .innerJoin(Newsletter.NEWSLETTER)
            .on(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(Newsletter.NEWSLETTER.ID))
            .where(PAPER_NEWSLETTER.PAPER_ID.eq(PAPER.ID));

        final SelectConditionStep<Record1<Integer>> step1 = (sc.getNewsletterTopicId() != null) ?
            step0.and(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(sc.getNewsletterTopicId())) :
            step0;
        final SelectConditionStep<Record1<Integer>> step2 = (sc.getNewsletterHeadline() != null) ?
            step1.and(PAPER_NEWSLETTER.HEADLINE.likeIgnoreCase("%" + sc.getNewsletterHeadline() + "%")) :
            step1;
        final SelectConditionStep<Record1<Integer>> step3 = (sc.getNewsletterIssue() != null) ?
            step2.and(Newsletter.NEWSLETTER.ISSUE.likeIgnoreCase("%" + sc.getNewsletterIssue() + "%")) :
            step2;
        nlConditions.add(() -> DSL.exists(step3));
        return nlConditions.combineWithAnd();
    }

    private Condition attachmentConditions(final SearchCondition sc) {
        final ConditionalSupplier attConditions = new ConditionalSupplier();
        final SelectConditionStep<Record1<Integer>> step0 = DSL
            .selectOne()
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(PAPER.ID));
        if (sc.getAttachmentNameMask() != null) {
            final SelectConditionStep<Record1<Integer>> step1 = step0.and(PAPER_ATTACHMENT.NAME.containsIgnoreCase(sc.getAttachmentNameMask()));
            attConditions.add(() -> DSL.exists(step1));
        } else if (sc.getHasAttachments() != null) {
            if (sc.getHasAttachments())
                attConditions.add(() -> DSL.exists(step0));
            else
                attConditions.add(() -> DSL.notExists(step0));
        }
        return attConditions.combineWithAnd();
    }

    @NotNull
    @Override
    public List<T> findPageBySearchOrder(@NotNull final SearchOrder searchOrder, @NotNull final PaginationContext pc) {
        final Condition paperMatches = getConditionsFrom(searchOrder);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), PAPER);
        final List<PaperRecord> tuples = getDsl()
            .selectFrom(Tables.PAPER)
            .where(paperMatches)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetchInto(getRecordClass());
        return tuples
            .stream()
            .map(getMapper()::map)
            .collect(Collectors.toList());
    }

    @Override
    public int countBySearchOrder(@NotNull final SearchOrder searchOrder) {
        final Condition paperMatches = getConditionsFrom(searchOrder);
        return getDsl().fetchCount(getDsl()
            .selectOne()
            .from(PAPER)
            .where(paperMatches));
    }

    @NotNull
    @Override
    public List<Long> findPageOfIdsBySearchOrder(@NotNull final SearchOrder searchOrder, @NotNull final PaginationContext pc) {
        final Condition conditions = getConditionsFrom(searchOrder);
        final Collection<SortField<T>> sortCriteria = getSortMapper().map(pc.getSort(), PAPER);
        return getDsl()
            .select()
            .from(Tables.PAPER)
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch(PAPER.ID);
    }
}
