package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.Code.CODE;
import static ch.difty.scipamato.core.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.scipamato.core.db.tables.CodeTr.CODE_TR;
import static ch.difty.scipamato.core.db.tables.SearchCondition.SEARCH_CONDITION;
import static ch.difty.scipamato.core.db.tables.SearchConditionCode.SEARCH_CONDITION_CODE;
import static ch.difty.scipamato.core.db.tables.SearchExclusion.SEARCH_EXCLUSION;
import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.scipamato.core.db.tables.SearchTerm.SEARCH_TERM;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.core.db.tables.records.SearchConditionCodeRecord;
import ch.difty.scipamato.core.db.tables.records.SearchConditionRecord;
import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.db.tables.records.SearchTermRecord;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.search.*;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;
import ch.difty.scipamato.core.persistence.JooqEntityRepo;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;

/**
 * The repository to manage {@link SearchOrder}s - including the nested list of
 * {@link SearchCondition}s and excluded paper ids.
 *
 * @author u.joss
 */
@Repository
@Slf4j
public class JooqSearchOrderRepo extends
    JooqEntityRepo<SearchOrderRecord, SearchOrder, Long, ch.difty.scipamato.core.db.tables.SearchOrder, SearchOrderRecordMapper, SearchOrderFilter>
    implements SearchOrderRepository {

    public JooqSearchOrderRepo(@Qualifier("dslContext") DSLContext dsl, SearchOrderRecordMapper mapper,
        JooqSortMapper<SearchOrderRecord, SearchOrder, ch.difty.scipamato.core.db.tables.SearchOrder> sortMapper,
        GenericFilterConditionMapper<SearchOrderFilter> filterConditionMapper, DateTimeService dateTimeService,
        InsertSetStepSetter<SearchOrderRecord, SearchOrder> insertSetStepSetter,
        UpdateSetStepSetter<SearchOrderRecord, SearchOrder> updateSetStepSetter,
        ApplicationProperties applicationProperties) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, insertSetStepSetter, updateSetStepSetter,
            applicationProperties);
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    protected ch.difty.scipamato.core.db.tables.SearchOrder getTable() {
        return SEARCH_ORDER;
    }

    @Override
    protected TableField<SearchOrderRecord, Long> getTableId() {
        return SEARCH_ORDER.ID;
    }

    @Override
    protected TableField<SearchOrderRecord, Integer> getRecordVersion() {
        return SEARCH_ORDER.VERSION;
    }

    @Override
    protected Long getIdFrom(SearchOrderRecord record) {
        return record.getId();
    }

    @Override
    protected Long getIdFrom(SearchOrder entity) {
        return entity.getId();
    }

    /**
     * Enriches the plain {@link SearchOrder} with nested entities, i.e. the
     * {@link SearchCondition}s.
     */
    @Override
    public void enrichAssociatedEntitiesOf(final SearchOrder searchOrder, String languageCode) {
        if (searchOrder != null && searchOrder.getId() != null) {
            final Map<Long, SearchCondition> idToSc = findConditionsOf(searchOrder.getId())
                .stream()
                .collect(Collectors.toMap(SearchCondition::getSearchConditionId, Function.identity()));
            fillSearchTermsInto(searchOrder, mapSearchTermsToSearchConditions(searchOrder), idToSc, languageCode);
            addSearchTermLessConditionsOf(searchOrder, idToSc, languageCode);
            fillExcludedPaperIdsInto(searchOrder);
        }
    }

    protected List<SearchCondition> findConditionsOf(final Long searchOrderId) {
        return getDsl()
            .selectFrom(SEARCH_CONDITION)
            .where(SEARCH_CONDITION.SEARCH_ORDER_ID.eq(searchOrderId))
            .fetchInto(SearchCondition.class);
    }

    private Map<Long, List<SearchTerm>> mapSearchTermsToSearchConditions(final SearchOrder searchOrder) {
        return fetchSearchTermsForSearchOrderWithId(searchOrder.getId())
            .stream()
            .collect(groupingBy(SearchTerm::getSearchConditionId));
    }

    protected List<SearchTerm> fetchSearchTermsForSearchOrderWithId(final long searchOrderId) {
        return getSearchTermsWhere(SEARCH_CONDITION.SEARCH_ORDER_ID.equal(searchOrderId));
    }

    private List<SearchTerm> getSearchTermsWhere(final Condition condition) {
        return getDsl()
            .select(SEARCH_TERM.ID.as("id"), SEARCH_TERM.SEARCH_TERM_TYPE.as("stt"),
                SEARCH_TERM.SEARCH_CONDITION_ID.as("scid"), SEARCH_TERM.FIELD_NAME.as("fn"),
                SEARCH_TERM.RAW_VALUE.as("rv"))
            .from(SEARCH_TERM)
            .innerJoin(SEARCH_CONDITION)
            .on(SEARCH_CONDITION.SEARCH_CONDITION_ID.equal(SEARCH_TERM.SEARCH_CONDITION_ID))
            .where(condition)
            .fetch(r -> SearchTerm.newSearchTerm((long) r.get("id"), (int) r.get("stt"), (long) r.get("scid"),
                (String) r.get("fn"), (String) r.get("rv")));
    }

    /*
     * Note: This method only adds searchConditions that have searchTerms. It will
     * not add conditions that e.g. only have createdTerms or modifiedTerms.
     */
    private void fillSearchTermsInto(final SearchOrder searchOrder, final Map<Long, List<SearchTerm>> map,
        final Map<Long, SearchCondition> idToSc, final String languageCode) {
        for (final Entry<Long, List<SearchTerm>> entry : map.entrySet()) {
            final SearchCondition sc = idToSc.computeIfAbsent(entry.getKey(), SearchCondition::new);
            for (final SearchTerm st : entry.getValue())
                sc.addSearchTerm(st);
            fillCodesInto(sc, languageCode);
            searchOrder.add(sc);
        }
    }

    private Map<Long, List<SearchTerm>> mapSearchTermsToSearchConditions(final SearchCondition searchCondition) {
        final List<SearchTerm> searchTerms = fetchSearchTermsForSearchConditionWithId(
            searchCondition.getSearchConditionId());
        return searchTerms
            .stream()
            .collect(groupingBy(SearchTerm::getSearchConditionId));
    }

    private List<SearchTerm> fetchSearchTermsForSearchConditionWithId(final long searchConditionId) {
        return getSearchTermsWhere(SEARCH_CONDITION.SEARCH_CONDITION_ID.equal(searchConditionId));
    }

    private void fillSearchTermsInto(SearchCondition searchCondition, Map<Long, List<SearchTerm>> map) {
        for (final Entry<Long, List<SearchTerm>> entry : map.entrySet())
            for (final SearchTerm st : entry.getValue())
                searchCondition.addSearchTerm(st);
    }

    protected SearchCondition fetchSearchConditionWithId(final Long scId) {
        return getDsl()
            .selectFrom(SEARCH_CONDITION)
            .where(SEARCH_CONDITION.SEARCH_CONDITION_ID.eq(scId))
            .fetchOneInto(SearchCondition.class);
    }

    /*
     * Taking care of searchConditions that do not have searchTerms
     */
    private void addSearchTermLessConditionsOf(final SearchOrder searchOrder, final Map<Long, SearchCondition> idToSc,
        final String languageCode) {
        final Long searchOrderId = searchOrder.getId();
        final List<Long> conditionIdsWithSearchTerms = findConditionIdsWithSearchTerms(searchOrderId);
        final List<SearchCondition> termLessConditions = findTermLessConditions(idToSc, conditionIdsWithSearchTerms);
        for (final SearchCondition sc : termLessConditions) {
            fillCodesInto(sc, languageCode);
            searchOrder.add(sc);
        }
    }

    public List<Long> findConditionIdsWithSearchTerms(final Long searchOrderId) {
        return getDsl()
            .select(SEARCH_TERM.SEARCH_CONDITION_ID)
            .from(SEARCH_TERM)
            .innerJoin(SEARCH_CONDITION)
            .on(SEARCH_TERM.SEARCH_CONDITION_ID.eq(SEARCH_CONDITION.SEARCH_CONDITION_ID))
            .where(SEARCH_CONDITION.SEARCH_ORDER_ID.eq(searchOrderId))
            .fetchInto(Long.class);
    }

    private List<SearchCondition> findTermLessConditions(final Map<Long, SearchCondition> idToSc,
        final List<Long> conditionIdsWithSearchTerms) {
        return idToSc
            .values()
            .stream()
            .filter(sc -> sc.getSearchConditionId() != null && !conditionIdsWithSearchTerms.contains(
                sc.getSearchConditionId()))
            .collect(toList());
    }

    private void fillExcludedPaperIdsInto(SearchOrder searchOrder) {
        final List<Long> excludedPaperIds = fetchExcludedPaperIdsForSearchOrderWithId(searchOrder.getId());
        searchOrder.setExcludedPaperIds(excludedPaperIds);
    }

    protected List<Long> fetchExcludedPaperIdsForSearchOrderWithId(final long searchOrderId) {
        return getDsl()
            .select(SEARCH_EXCLUSION.PAPER_ID)
            .from(SEARCH_EXCLUSION)
            .where(SEARCH_EXCLUSION.SEARCH_ORDER_ID.equal(searchOrderId))
            .fetch(r -> (Long) r.get(0));
    }

    @Override
    protected void updateAssociatedEntities(final SearchOrder searchOrder, final String languageCode) {
        saveAssociatedEntitiesOf(searchOrder, languageCode);
    }

    @Override
    protected void saveAssociatedEntitiesOf(final SearchOrder searchOrder, final String languageCode) {
        storeSearchConditionsOf(searchOrder, languageCode);
        storeExcludedIdsOf(searchOrder);
    }

    private void storeSearchConditionsOf(final SearchOrder searchOrder, final String languageCode) {
        storeExistingConditionsOf(searchOrder, languageCode);
        deleteObsoleteConditionsFrom(searchOrder);
    }

    private void storeExistingConditionsOf(final SearchOrder searchOrder, final String languageCode) {
        final Long searchOrderId = searchOrder.getId();
        for (final SearchCondition sc : searchOrder.getSearchConditions()) {
            Long searchConditionId = sc.getSearchConditionId();
            if (searchConditionId == null)
                addSearchCondition(sc, searchOrderId, languageCode);
            else
                updateSearchCondition(sc, searchOrderId, languageCode);
        }
    }

    private void updateSearchTerm(final SearchTerm st, final Long searchTermId, final Long searchConditionId) {
        final Condition idMatches = SEARCH_TERM.ID.eq(searchTermId);
        getDsl()
            .update(SEARCH_TERM)
            .set(SEARCH_TERM.SEARCH_CONDITION_ID, searchConditionId)
            .set(SEARCH_TERM.SEARCH_TERM_TYPE, st
                .getSearchTermType()
                .getId())
            .set(SEARCH_TERM.FIELD_NAME, st.getFieldName())
            .set(SEARCH_TERM.RAW_VALUE, st.getRawSearchTerm())
            .set(SEARCH_TERM.LAST_MODIFIED, getTs())
            .set(SEARCH_TERM.LAST_MODIFIED_BY, getUserId())
            .set(SEARCH_TERM.VERSION, getDsl()
                                          .select(SEARCH_TERM.VERSION)
                                          .from(SEARCH_TERM)
                                          .where(idMatches)
                                          .fetchOneInto(Integer.class) + 1)
            .where(idMatches)
            .execute();
    }

    private void deleteObsoleteConditionsFrom(SearchOrder searchOrder) {
        final List<Long> conditionIds = searchOrder
            .getSearchConditions()
            .stream()
            .map(SearchCondition::getSearchConditionId)
            .collect(toList());
        getDsl()
            .deleteFrom(SEARCH_CONDITION)
            .where(SEARCH_CONDITION.SEARCH_ORDER_ID
                .equal(searchOrder.getId())
                .and(SEARCH_CONDITION.SEARCH_CONDITION_ID.notIn(conditionIds)))
            .execute();
        for (final SearchCondition sc : searchOrder.getSearchConditions())
            removeObsoleteSearchTerms(sc, sc.getSearchConditionId());
    }

    private void storeExcludedIdsOf(SearchOrder searchOrder) {
        storeExistingExclusionsOf(searchOrder);
        deleteObsoleteExclusionsOf(searchOrder);
    }

    private void storeExistingExclusionsOf(SearchOrder searchOrder) {
        final long searchOrderId = searchOrder.getId();
        final List<Long> saved = getDsl()
            .select(SEARCH_EXCLUSION.PAPER_ID)
            .from(SEARCH_EXCLUSION)
            .where(SEARCH_EXCLUSION.SEARCH_ORDER_ID.eq(searchOrderId))
            .and(SEARCH_EXCLUSION.PAPER_ID.in(searchOrder.getExcludedPaperIds()))
            .fetchInto(Long.class);
        final List<Long> unsaved = new ArrayList<>(searchOrder.getExcludedPaperIds());
        unsaved.removeAll(saved);
        final Integer userId = getUserId();
        for (final Long excludedId : unsaved) {
            getDsl()
                .insertInto(SEARCH_EXCLUSION, SEARCH_EXCLUSION.SEARCH_ORDER_ID, SEARCH_EXCLUSION.PAPER_ID,
                    SEARCH_EXCLUSION.CREATED_BY, SEARCH_EXCLUSION.LAST_MODIFIED_BY)
                .values(searchOrderId, excludedId, userId, userId)
                .execute();
        }
    }

    private void deleteObsoleteExclusionsOf(SearchOrder searchOrder) {
        getDsl()
            .deleteFrom(SEARCH_EXCLUSION)
            .where(SEARCH_EXCLUSION.SEARCH_ORDER_ID.eq(searchOrder.getId()))
            .and(SEARCH_EXCLUSION.PAPER_ID.notIn(searchOrder.getExcludedPaperIds()))
            .execute();
    }

    @Override
    public SearchCondition addSearchCondition(SearchCondition searchCondition, long searchOrderId,
        final String languageCode) {
        AssertAs.notNull(languageCode, "languageCode");
        final Optional<SearchCondition> optionalPersisted = findEquivalentPersisted(searchCondition, searchOrderId,
            languageCode);
        if (optionalPersisted.isPresent()) {
            final SearchCondition psc = optionalPersisted.get();
            if (hasDirtyNewsletterFields(searchCondition, psc)) {
                enrichNewsletterFields(searchCondition, psc);
                return updateSearchCondition(psc, searchOrderId, languageCode);
            } else {
                return psc;
            }
        } else {
            final Integer userId = getUserId();
            final SearchConditionRecord searchConditionRecord = getDsl()
                .insertInto(SEARCH_CONDITION, SEARCH_CONDITION.SEARCH_ORDER_ID, SEARCH_CONDITION.NEWSLETTER_TOPIC_ID,
                    SEARCH_CONDITION.NEWSLETTER_HEADLINE, SEARCH_CONDITION.NEWSLETTER_ISSUE,
                    SEARCH_CONDITION.CREATED_TERM, SEARCH_CONDITION.MODIFIED_TERM, SEARCH_CONDITION.CREATED_BY,
                    SEARCH_CONDITION.LAST_MODIFIED_BY)
                .values(searchOrderId, searchCondition.getNewsletterTopicId(), searchCondition.getNewsletterHeadline(),
                    searchCondition.getNewsletterIssue(), searchCondition.getCreatedDisplayValue(),
                    searchCondition.getModifiedDisplayValue(), userId, userId)
                .returning()
                .fetchOne();
            persistSearchTerms(searchCondition, searchConditionRecord.getSearchConditionId());
            persistCodes(searchCondition, searchConditionRecord.getSearchConditionId());
            final SearchCondition persistedSearchCondition = getDsl()
                .selectFrom(SEARCH_CONDITION)
                .where(SEARCH_CONDITION.SEARCH_CONDITION_ID.eq(searchConditionRecord.getSearchConditionId()))
                .fetchOneInto(SearchCondition.class);
            fillSearchTermsInto(persistedSearchCondition, mapSearchTermsToSearchConditions(persistedSearchCondition));
            fillCodesInto(persistedSearchCondition, languageCode);
            return persistedSearchCondition;
        }
    }

    private boolean hasDirtyNewsletterFields(final SearchCondition searchCondition, final SearchCondition psc) {
        //@formatter:off
        return !Objects.equals(psc.getNewsletterTopicId(), searchCondition.getNewsletterTopicId())
            || !Objects.equals(psc.getNewsletterHeadline(), searchCondition.getNewsletterHeadline())
            || !Objects.equals(psc.getNewsletterIssue(), searchCondition.getNewsletterIssue());
        //@formatter:on
    }

    private void enrichNewsletterFields(final SearchCondition searchCondition, final SearchCondition psc) {
        psc.setNewsletterTopic(
            new ch.difty.scipamato.core.entity.newsletter.NewsletterTopic(searchCondition.getNewsletterTopicId(), ""));
        psc.setNewsletterHeadline(searchCondition.getNewsletterHeadline());
        psc.setNewsletterIssue(searchCondition.getNewsletterIssue());
    }

    /**
     * Tries to load an already persisted instance of {@link SearchCondition} for
     * the given search order (identified by the {@code searchOrderId}) semantically
     * covering the same searchConditions.
     *
     * @param searchCondition
     *     the search condition we're trying to find the semantically
     *     identical persisted version for.
     * @param searchOrderId
     *     identifying the search order
     * @return optional of the persisted version (if found - empty otherwise)
     */
    private Optional<SearchCondition> findEquivalentPersisted(final SearchCondition searchCondition,
        final long searchOrderId, final String languageCode) {
        final List<SearchCondition> persisted = getDsl()
            .selectFrom(SEARCH_CONDITION)
            .where(SEARCH_CONDITION.SEARCH_ORDER_ID.eq(searchOrderId))
            .fetchInto(SearchCondition.class);
        for (final SearchCondition sc : persisted) {
            Long searchConditionId = sc.getSearchConditionId();
            fillSearchTermsInto(sc, mapSearchTermsToSearchConditions(sc));
            fillCodesInto(sc, languageCode);
            sc.setSearchConditionId(null);
            if (searchCondition.equals(sc)) {
                sc.setSearchConditionId(searchConditionId);
                return Optional.of(sc);
            }
        }
        return Optional.empty();
    }

    private void persistSearchTerms(final SearchCondition searchCondition, final Long searchConditionId) {
        saveOrUpdateValidTerms(searchCondition, searchConditionId);
        removeObsoleteSearchTerms(searchCondition, searchConditionId);
    }

    private void fillCodesInto(final SearchCondition searchCondition, final String languageCode) {
        final List<Code> codes = fetchCodesForSearchConditionWithId(searchCondition, languageCode);
        if (CollectionUtils.isNotEmpty(codes)) {
            searchCondition.addCodes(codes);
        }
    }

    protected List<Code> fetchCodesForSearchConditionWithId(final SearchCondition searchCondition,
        String languageCode) {
        return getDsl()
            .select(CODE.CODE_.as("C_ID"), DSL
                    .coalesce(CODE_TR.NAME, TranslationUtils.NOT_TRANSL)
                    .as("C_NAME"), CODE_TR.COMMENT.as("C_COMMENT"), CODE.INTERNAL.as("C_INTERNAL"),
                CODE_CLASS.ID.as("CC_ID"), DSL
                    .coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL)
                    .as("CC_NAME"), DSL
                    .coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL)
                    .as("CC_DESCRIPTION"), CODE.SORT)
            .from(SEARCH_CONDITION_CODE)
            .join(SEARCH_CONDITION)
            .on(SEARCH_CONDITION_CODE.SEARCH_CONDITION_ID.equal(SEARCH_CONDITION.SEARCH_CONDITION_ID))
            .join(CODE)
            .on(SEARCH_CONDITION_CODE.CODE.equal(CODE.CODE_))
            .join(CODE_CLASS)
            .on(CODE.CODE_CLASS_ID.equal(CODE_CLASS.ID))
            .leftOuterJoin(CODE_TR)
            .on(CODE.CODE_
                .equal(CODE_TR.CODE)
                .and(CODE_TR.LANG_CODE.equal(languageCode)))
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID
                .equal(CODE_CLASS_TR.CODE_CLASS_ID)
                .and(CODE_CLASS_TR.LANG_CODE.equal(languageCode)))
            .where(SEARCH_CONDITION_CODE.SEARCH_CONDITION_ID.equal(searchCondition.getSearchConditionId()))
            .fetchInto(Code.class);
    }

    private void saveOrUpdateValidTerms(SearchCondition searchCondition, Long searchConditionId) {
        InsertValuesStep6<SearchTermRecord, Long, Integer, String, String, Integer, Integer> insertStep = getDsl().insertInto(
            SEARCH_TERM, SEARCH_TERM.SEARCH_CONDITION_ID, SEARCH_TERM.SEARCH_TERM_TYPE, SEARCH_TERM.FIELD_NAME,
            SEARCH_TERM.RAW_VALUE, SEARCH_TERM.CREATED_BY, SEARCH_TERM.LAST_MODIFIED_BY);
        final String insertStepInitial = insertStep.toString();
        final Integer userId = getUserId();
        insertStep = doSearchTerm(searchConditionId, insertStep, userId, BooleanSearchTerm.class,
            searchCondition.getBooleanSearchTerms());
        insertStep = doSearchTerm(searchConditionId, insertStep, userId, IntegerSearchTerm.class,
            searchCondition.getIntegerSearchTerms());
        insertStep = doSearchTerm(searchConditionId, insertStep, userId, StringSearchTerm.class,
            searchCondition.getStringSearchTerms());
        insertStep = doSearchTerm(searchConditionId, insertStep, userId, AuditSearchTerm.class,
            searchCondition.getAuditSearchTerms());
        if (!insertStepInitial.equals(insertStep.toString()))
            insertStep.execute();
    }

    private InsertValuesStep6<SearchTermRecord, Long, Integer, String, String, Integer, Integer> doSearchTerm(
        final Long searchConditionId,
        InsertValuesStep6<SearchTermRecord, Long, Integer, String, String, Integer, Integer> insertStep,
        final Integer userId, final Class<? extends AbstractSearchTerm> clazz,
        final Collection<? extends AbstractSearchTerm> searchTerms) {
        for (final AbstractSearchTerm st : searchTerms) {
            final int typeId = st
                .getSearchTermType()
                .getId();
            final String fieldName = st.getFieldName();
            final AbstractSearchTerm pst = (AbstractSearchTerm) getPersistedTerm(searchConditionId, fieldName, clazz,
                typeId);
            if (pst != null) {
                updateSearchTerm(st, pst.getId(), searchConditionId);
            } else {
                insertStep = insertStep.values(searchConditionId, typeId, fieldName, st.getRawSearchTerm(), userId,
                    userId);
            }
        }
        return insertStep;
    }

    private SearchTerm getPersistedTerm(final Long searchConditionId, final String fieldName,
        final Class<? extends SearchTerm> termClass, final int typeId) {
        return getDsl()
            .select(SEARCH_TERM.ID, SEARCH_TERM.SEARCH_CONDITION_ID, SEARCH_TERM.FIELD_NAME, SEARCH_TERM.RAW_VALUE)
            .from(SEARCH_TERM)
            .where(SEARCH_TERM.SEARCH_CONDITION_ID.eq(searchConditionId))
            .and(SEARCH_TERM.SEARCH_TERM_TYPE.eq(typeId))
            .and(SEARCH_TERM.FIELD_NAME.eq(fieldName))
            .fetchOneInto(termClass);
    }

    private void removeObsoleteSearchTerms(SearchCondition searchCondition, Long searchConditionId) {
        if (!searchCondition
            .getRemovedKeys()
            .isEmpty()) {
            getDsl()
                .deleteFrom(SEARCH_TERM)
                .where(SEARCH_TERM.SEARCH_CONDITION_ID.eq(searchConditionId))
                .and(SEARCH_TERM.FIELD_NAME.in(searchCondition.getRemovedKeys()))
                .execute();
            searchCondition.clearRemovedKeys();
        }
    }

    private void persistCodes(SearchCondition searchCondition, Long searchConditionId) {
        saveOrUpdateCodes(searchCondition, searchConditionId);
        removeObsoleteCodesFrom(searchCondition, searchConditionId);
    }

    private void saveOrUpdateCodes(SearchCondition searchCondition, Long searchConditionId) {
        if (!CollectionUtils.isEmpty(searchCondition.getCodes())) {
            InsertValuesStep4<SearchConditionCodeRecord, Long, String, Integer, Integer> step = getDsl().insertInto(
                SEARCH_CONDITION_CODE, SEARCH_CONDITION_CODE.SEARCH_CONDITION_ID, SEARCH_CONDITION_CODE.CODE,
                SEARCH_CONDITION_CODE.CREATED_BY, SEARCH_CONDITION_CODE.LAST_MODIFIED_BY);
            final Integer userId = getUserId();
            for (final Code c : searchCondition.getCodes()) {
                step = step.values(searchConditionId, c.getCode(), userId, userId);
            }
            step
                .onDuplicateKeyIgnore()
                .execute();
        }
    }

    private void removeObsoleteCodesFrom(SearchCondition searchCondition, Long searchConditionId) {
        final List<String> codes = searchCondition
            .getCodes()
            .stream()
            .map(Code::getCode)
            .collect(toList());
        getDsl()
            .deleteFrom(SEARCH_CONDITION_CODE)
            .where(SEARCH_CONDITION_CODE.SEARCH_CONDITION_ID
                .equal(searchConditionId)
                .and(SEARCH_CONDITION_CODE.CODE.notIn(codes)))
            .execute();
    }

    @Override
    public SearchCondition updateSearchCondition(SearchCondition searchCondition, long searchOrderId,
        final String languageCode) {
        AssertAs.notNull(languageCode, "languageCode");
        final Condition idMatches = SEARCH_CONDITION.SEARCH_CONDITION_ID.eq(searchCondition.getSearchConditionId());
        getDsl()
            .update(SEARCH_CONDITION)
            .set(SEARCH_CONDITION.SEARCH_ORDER_ID, searchOrderId)
            .set(SEARCH_CONDITION.NEWSLETTER_TOPIC_ID, searchCondition.getNewsletterTopicId())
            .set(SEARCH_CONDITION.NEWSLETTER_HEADLINE, searchCondition.getNewsletterHeadline())
            .set(SEARCH_CONDITION.NEWSLETTER_ISSUE, searchCondition.getNewsletterIssue())
            .set(SEARCH_CONDITION.CREATED_TERM, searchCondition.getCreatedDisplayValue())
            .set(SEARCH_CONDITION.MODIFIED_TERM, searchCondition.getModifiedDisplayValue())
            .set(SEARCH_CONDITION.LAST_MODIFIED, getTs())
            .set(SEARCH_CONDITION.LAST_MODIFIED_BY, getUserId())
            .set(SEARCH_CONDITION.VERSION, getDsl()
                                               .select(SEARCH_CONDITION.VERSION)
                                               .from(SEARCH_CONDITION)
                                               .where(idMatches)
                                               .fetchOneInto(Integer.class) + 1)

            .where(idMatches)
            .execute();
        persistSearchTerms(searchCondition, searchCondition.getSearchConditionId());
        persistCodes(searchCondition, searchCondition.getSearchConditionId());
        SearchCondition persistedSearchCondition = fetchSearchConditionWithId(searchCondition.getSearchConditionId());
        fillSearchTermsInto(persistedSearchCondition, mapSearchTermsToSearchConditions(persistedSearchCondition));
        fillCodesInto(persistedSearchCondition, languageCode);
        return persistedSearchCondition;
    }

    @Override
    public void deleteSearchConditionWithId(long searchConditionId) {
        getDsl()
            .deleteFrom(SEARCH_CONDITION)
            .where(SEARCH_CONDITION.SEARCH_CONDITION_ID.eq(searchConditionId))
            .execute();
    }

}
