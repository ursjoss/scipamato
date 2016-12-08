package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchCondition.SEARCH_CONDITION;
import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.sipamato.db.tables.SearchTerm.SEARCH_TERM;
import static org.jooq.impl.DSL.row;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep4;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.SearchConditionRecord;
import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.db.tables.records.SearchTermRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.BooleanSearchTerm;
import ch.difty.sipamato.entity.filter.IntegerSearchTerm;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.SearchTerm;
import ch.difty.sipamato.entity.filter.StringSearchTerm;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqSearchOrderRepo extends JooqEntityRepo<SearchOrderRecord, SearchOrder, Long, ch.difty.sipamato.db.tables.SearchOrder, SearchOrderRecordMapper, SearchOrderFilter>
        implements SearchOrderRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqSearchOrderRepo.class);

    @Autowired
    public JooqSearchOrderRepo(DSLContext dsl, SearchOrderRecordMapper mapper, JooqSortMapper<SearchOrderRecord, SearchOrder, ch.difty.sipamato.db.tables.SearchOrder> sortMapper,
            GenericFilterConditionMapper<SearchOrderFilter> filterConditionMapper, Localization localization, InsertSetStepSetter<SearchOrderRecord, SearchOrder> insertSetStepSetter,
            UpdateSetStepSetter<SearchOrderRecord, SearchOrder> updateSetStepSetter, Configuration jooqConfig) {
        super(dsl, mapper, sortMapper, filterConditionMapper, localization, insertSetStepSetter, updateSetStepSetter, jooqConfig);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Class<? extends SearchOrder> getEntityClass() {
        return SearchOrder.class;
    }

    @Override
    protected Class<? extends SearchOrderRecord> getRecordClass() {
        return SearchOrderRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.SearchOrder getTable() {
        return SEARCH_ORDER;
    }

    @Override
    protected TableField<SearchOrderRecord, Long> getTableId() {
        return SEARCH_ORDER.ID;
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
     * Enriches the plain {@link SearchOrder} with nested entities, i.e. the {@link SearchCondition}s.
     */
    @Override
    protected void enrichAssociatedEntitiesOf(final SearchOrder searchOrder) {
        if (searchOrder != null && searchOrder.getId() != null) {
            fillSearchTermsInto(searchOrder, mapSearchTermsToSearchConditions(searchOrder));
        }
    }

    private Map<Long, List<SearchTerm<?>>> mapSearchTermsToSearchConditions(final SearchOrder searchOrder) {
        final List<SearchTerm<?>> searchTerms = fetchSearchTermsForSearchOrderWithId(searchOrder.getId());
        return searchTerms.stream().collect(Collectors.groupingBy(st -> st.getSearchConditionId()));
    }

    protected List<SearchTerm<?>> fetchSearchTermsForSearchOrderWithId(final long searchOrderId) {
        // @formatter:off
        return getDsl()
                .select(
                        SEARCH_TERM.ID.as("id"),
                        SEARCH_TERM.SEARCH_TERM_TYPE.as("stt"),
                        SEARCH_TERM.SEARCH_CONDITION_ID.as("scid"),
                        SEARCH_TERM.FIELD_NAME.as("fn"),
                        SEARCH_TERM.RAW_VALUE.as("rv"))
                .from(SEARCH_TERM)
                .innerJoin(SEARCH_CONDITION)
                .on(SEARCH_CONDITION.SEARCH_CONDITION_ID.equal(SEARCH_TERM.SEARCH_CONDITION_ID))
                .where(SEARCH_CONDITION.SEARCH_ORDER_ID.equal(searchOrderId))
                .fetch(r -> SearchTerm.of((long) r.get("id"), (int) r.get("stt"), (long) r.get("scid"), (String) r.get("fn"), (String) r.get("rv")));
        // @formatter:on
    }

    private void fillSearchTermsInto(SearchOrder searchOrder, Map<Long, List<SearchTerm<?>>> map) {
        for (final Entry<Long, List<SearchTerm<?>>> entry : map.entrySet()) {
            final SearchCondition sc = new SearchCondition(entry.getKey());
            for (final SearchTerm<?> st : entry.getValue()) {
                sc.addSearchTerm(st);
            }
            searchOrder.add(sc);
        }
    }

    private Map<Long, List<SearchTerm<?>>> mapSearchTermsToSearchConditions(final SearchCondition searchCondition) {
        final List<SearchTerm<?>> searchTerms = fetchSearchTermsForSearchConditionWithId(searchCondition.getSearchConditionId());
        return searchTerms.stream().collect(Collectors.groupingBy(st -> st.getSearchConditionId()));
    }
    
    protected List<SearchTerm<?>> fetchSearchTermsForSearchConditionWithId(final long searchConditionId) {
        // @formatter:off
        return getDsl()
                .select(
                        SEARCH_TERM.ID.as("id"),
                        SEARCH_TERM.SEARCH_TERM_TYPE.as("stt"),
                        SEARCH_TERM.SEARCH_CONDITION_ID.as("scid"),
                        SEARCH_TERM.FIELD_NAME.as("fn"),
                        SEARCH_TERM.RAW_VALUE.as("rv"))
                .from(SEARCH_TERM)
                .innerJoin(SEARCH_CONDITION)
                .on(SEARCH_CONDITION.SEARCH_CONDITION_ID.equal(SEARCH_TERM.SEARCH_CONDITION_ID))
                .where(SEARCH_CONDITION.SEARCH_CONDITION_ID.equal(searchConditionId))
                .fetch(r -> SearchTerm.of((long) r.get("id"), (int) r.get("stt"), (long) r.get("scid"), (String) r.get("fn"), (String) r.get("rv")));
        // @formatter:on
    }

    private void fillSearchTermsInto(SearchCondition searchCondition, Map<Long, List<SearchTerm<?>>> map) {
        for (final Entry<Long, List<SearchTerm<?>>> entry : map.entrySet()) {
            for (final SearchTerm<?> st : entry.getValue()) {
                searchCondition.addSearchTerm(st);
            }
        }
    }

    @Override
    protected void updateAssociatedEntities(final SearchOrder searchOrder) {
        storeSearchConditionsOf(searchOrder);
    }

    @Override
    protected void saveAssociatedEntitiesOf(final SearchOrder searchOrder) {
        storeSearchConditionsOf(searchOrder);
    }

    private void storeSearchConditionsOf(SearchOrder searchOrder) {
        storeExistingConditionsOf(searchOrder);
        deleteObsoleteConditionsFrom(searchOrder);

    }

    private void storeExistingConditionsOf(SearchOrder searchOrder) {
        final Long searchOrderId = searchOrder.getId();
        for (final SearchCondition sc : searchOrder.getSearchConditions()) {
            Long searchConditionId = sc.getSearchConditionId();
            if (searchConditionId == null) {
                addSearchCondition(sc, searchOrderId);
            } else {
                updateSearchCondition(sc, searchOrderId);
            }
        }
    }

    private void updateSearchTerm(SearchTerm<?> st, Long searchConditionId) {
        getDsl().update(SEARCH_TERM)
                .set(row(SEARCH_TERM.SEARCH_CONDITION_ID, SEARCH_TERM.SEARCH_TERM_TYPE, SEARCH_TERM.FIELD_NAME, SEARCH_TERM.RAW_VALUE),
                        row(searchConditionId, st.getSearchTermType().getId(), st.getFieldName(), st.getRawSearchTerm()))
                .where(SEARCH_TERM.ID.eq(st.getId()))
                .execute();
    }

    private void deleteObsoleteConditionsFrom(SearchOrder searchOrder) {
        final List<Long> conditionIds = searchOrder.getSearchConditions().stream().map(SearchCondition::getSearchConditionId).collect(Collectors.toList());
        getDsl().deleteFrom(SEARCH_CONDITION).where(SEARCH_CONDITION.SEARCH_ORDER_ID.equal(searchOrder.getId()).and(SEARCH_CONDITION.SEARCH_CONDITION_ID.notIn(conditionIds))).execute();
        // TODO delete search terms of remaining conditions
    }

    @Override
    public SearchCondition addSearchCondition(SearchCondition searchCondition, long searchOrderId) {
        final Optional<SearchCondition> optionalPersisted = findEquivalentPersisted(searchCondition, searchOrderId);
        if (optionalPersisted.isPresent()) {
            return optionalPersisted.get();
        } else {
            final SearchConditionRecord searchConditionRecord = getDsl().insertInto(SEARCH_CONDITION, SEARCH_CONDITION.SEARCH_ORDER_ID).values(searchOrderId).returning().fetchOne();
            persistSearchTerms(searchCondition, searchConditionRecord.getSearchConditionId());
            final SearchCondition persistedSearchCondition = getDsl().selectFrom(SEARCH_CONDITION).where(SEARCH_CONDITION.SEARCH_CONDITION_ID.eq(searchConditionRecord.getSearchConditionId())).fetchOneInto(SearchCondition.class);
            fillSearchTermsInto(persistedSearchCondition, mapSearchTermsToSearchConditions(persistedSearchCondition));
            return persistedSearchCondition;
        }
    }

    /**
     * Tries to load an already persisted instance of {@link SearchCondition} for the given search order (identified by the 
     * <code>searchOrderId</code>) semantically covering the same searchConditions.
     * @param searchCondition the search condition we're trying to find the semantically identical persisted version for.
     * @param searchOrderId identifying the search order
     * @return optional of the persisted version (if found - empty othewise)
     */
    private Optional<SearchCondition> findEquivalentPersisted(final SearchCondition searchCondition, final long searchOrderId) {
        final List<SearchCondition> persisted = getDsl().selectFrom(SEARCH_CONDITION).where(SEARCH_CONDITION.SEARCH_ORDER_ID.eq(searchOrderId)).fetchInto(SearchCondition.class);
        for (final SearchCondition sc : persisted) {
            Long searchConditionId = sc.getSearchConditionId();
            fillSearchTermsInto(sc, mapSearchTermsToSearchConditions(sc));
            sc.setSearchConditionId(null);
            if (searchCondition.equals(sc)) {
                sc.setSearchConditionId(searchConditionId);
                return Optional.ofNullable(sc);
            }
        }
        return Optional.empty();
    }

    private void persistSearchTerms(SearchCondition searchCondition, Long searchConditionId) {
        saveOrUpdateValidSearchConditions(searchCondition, searchConditionId);
        removeObsoleteSearchConditions(searchCondition, searchConditionId);
    }

    private void saveOrUpdateValidSearchConditions(SearchCondition searchCondition, Long searchConditionId) {
        InsertValuesStep4<SearchTermRecord, Long, Integer, String, String> insertStep = getDsl().insertInto(SEARCH_TERM, SEARCH_TERM.SEARCH_CONDITION_ID, SEARCH_TERM.SEARCH_TERM_TYPE,
                SEARCH_TERM.FIELD_NAME, SEARCH_TERM.RAW_VALUE);
        for (BooleanSearchTerm bst : searchCondition.getBooleanSearchTerms()) {
            if (bst.getId() == null) {
                insertStep = insertStep.values(searchConditionId, bst.getSearchTermType().getId(), bst.getFieldName(), bst.getRawSearchTerm());
            } else {
                updateSearchTerm(bst, searchConditionId);
            }
        }
        for (IntegerSearchTerm ist : searchCondition.getIntegerSearchTerms()) {
            if (ist.getId() == null) {
                insertStep = insertStep.values(searchConditionId, ist.getSearchTermType().getId(), ist.getFieldName(), ist.getRawSearchTerm());
            } else {
                updateSearchTerm(ist, searchConditionId);
            }
        }
        for (StringSearchTerm sst : searchCondition.getStringSearchTerms()) {
            if (sst.getId() == null) {
                insertStep = insertStep.values(searchConditionId, sst.getSearchTermType().getId(), sst.getFieldName(), sst.getRawSearchTerm());
            } else {
                updateSearchTerm(sst, searchConditionId);
            }
        }
        insertStep.onDuplicateKeyIgnore().execute();
    }

    private void removeObsoleteSearchConditions(SearchCondition searchCondition, Long searchConditionId) {
        // TODO need to implement
    }

    @Override
    public SearchCondition updateSearchCondition(SearchCondition searchCondition, long searchOrderId) {
        getDsl().update(SEARCH_CONDITION)
                .set(row(SEARCH_CONDITION.SEARCH_ORDER_ID, SEARCH_CONDITION.LAST_MODIFIED, SEARCH_CONDITION.LAST_MODIFIED_BY), row(searchOrderId, getTs(), getOwner()))
                .where(SEARCH_CONDITION.SEARCH_CONDITION_ID.eq(searchCondition.getSearchConditionId()))
                .execute();
        persistSearchTerms(searchCondition, searchCondition.getSearchConditionId());
        SearchCondition persistedSearchCondition = getDsl().selectFrom(SEARCH_CONDITION).where(SEARCH_CONDITION.SEARCH_CONDITION_ID.eq(searchCondition.getSearchConditionId())).fetchOneInto(SearchCondition.class);
        fillSearchTermsInto(persistedSearchCondition, mapSearchTermsToSearchConditions(persistedSearchCondition));
        return persistedSearchCondition;
    }

    // TODO replace with currentDate bean
    private java.sql.Timestamp getTs() {
        return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    // TODO replace with real ID from session
    private int getOwner() {
        return 1;
    }
}
