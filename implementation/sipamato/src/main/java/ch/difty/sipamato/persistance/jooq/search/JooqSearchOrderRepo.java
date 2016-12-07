package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchCondition.SEARCH_CONDITION;
import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;
import static ch.difty.sipamato.db.tables.SearchTerm.SEARCH_TERM;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.SearchTerm;
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
        final List<SearchTerm<?>> searchTerms = fetchSearchTermsFor(searchOrder.getId());
        return searchTerms.stream().collect(Collectors.groupingBy(st -> st.getSearchConditionId()));
    }

    protected List<SearchTerm<?>> fetchSearchTermsFor(final long searchOrderId) {
        // @formatter:off
        return getDsl()
                .select(
                        SEARCH_TERM.SEARCH_TERM_TYPE.as("stt"),
                        SEARCH_TERM.SEARCH_CONDITION_ID.as("scid"),
                        SEARCH_TERM.FIELD_NAME.as("fn"),
                        SEARCH_TERM.RAW_VALUE.as("rv"))
                .from(SEARCH_TERM)
                .innerJoin(SEARCH_CONDITION)
                .on(SEARCH_CONDITION.ID.equal(SEARCH_TERM.SEARCH_CONDITION_ID))
                .where(SEARCH_CONDITION.SEARCH_ORDER_ID.equal(searchOrderId))
                .fetch(r -> SearchTerm.of((int) r.get("stt"), (long) r.get("scid"), (String) r.get("fn"), (String) r.get("rv")));
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

}
