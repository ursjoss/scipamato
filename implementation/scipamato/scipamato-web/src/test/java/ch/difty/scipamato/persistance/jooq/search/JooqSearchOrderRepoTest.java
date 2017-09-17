package ch.difty.scipamato.persistance.jooq.search;

import static ch.difty.scipamato.db.tables.SearchOrder.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.entity.filter.SearchTerm;
import ch.difty.scipamato.entity.filter.SearchTermType;
import ch.difty.scipamato.persistance.jooq.EntityRepository;
import ch.difty.scipamato.persistance.jooq.JooqEntityRepoTest;

public class JooqSearchOrderRepoTest extends JooqEntityRepoTest<SearchOrderRecord, SearchOrder, Long, ch.difty.scipamato.db.tables.SearchOrder, SearchOrderRecordMapper, SearchOrderFilter> {

    private static final Long SAMPLE_ID = 3l;
    private static final String LC = "de";

    private JooqSearchOrderRepo repo;

    @Mock
    private SearchOrder unpersistedEntity;
    @Mock
    private SearchOrder persistedEntity;
    @Mock
    private SearchOrderRecord persistedRecord;
    @Mock
    private SearchOrderRecordMapper mapperMock;
    @Mock
    private SearchOrderFilter filterMock;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqSearchOrderRepo getRepo() {
        if (repo == null) {
            repo = new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getApplicationProperties());
        }
        return repo;
    }

    @Override
    protected EntityRepository<SearchOrder, Long, SearchOrderFilter> makeRepoFindingEntityById(SearchOrder searchOrder) {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getApplicationProperties()) {
            private static final long serialVersionUID = 1L;

            @Override
            public SearchOrder findById(Long id, int version) {
                return searchOrder;
            }
        };
    }

    @Override
    protected SearchOrder getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected SearchOrder getPersistedEntity() {
        return persistedEntity;
    }

    @Override
    protected SearchOrderRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Override
    protected SearchOrderRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected Class<SearchOrder> getEntityClass() {
        return SearchOrder.class;
    }

    @Override
    protected Class<SearchOrderRecord> getRecordClass() {
        return SearchOrderRecord.class;
    }

    @Override
    protected ch.difty.scipamato.db.tables.SearchOrder getTable() {
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
    protected SearchOrderFilter getFilter() {
        return filterMock;
    }

    @Override
    protected void expectEntityIdsWithValues() {
        when(unpersistedEntity.getId()).thenReturn(SAMPLE_ID);
        when(persistedRecord.getId()).thenReturn(SAMPLE_ID);
    }

    @Override
    protected void expectUnpersistedEntityIdNull() {
        when(unpersistedEntity.getId()).thenReturn(null);
    }

    @Override
    protected void verifyUnpersistedEntityId() {
        verify(getUnpersistedEntity()).getId();
    }

    @Override
    protected void verifyPersistedRecordId() {
        verify(persistedRecord).getId();
    }

    @Test
    public void degenerateConstruction() {
        try {
            new JooqSearchOrderRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getApplicationProperties());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dateTimeService must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), null, getUpdateSetStepSetter(), getApplicationProperties());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), null, getApplicationProperties());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("applicationProperties must not be null.");
        }
    }

    @Test
    public void enrichingAssociatedEntities_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null, LC);
    }

    @Test
    public void enrichingAssociatedEntities_withEntityWithNullId_doesNothing() {
        SearchOrder so = new SearchOrder();
        assertThat(so.getId()).isNull();

        repo.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so.getSearchConditions()).isEmpty();
    }

    private JooqSearchOrderRepo makeRepoFindingNestedEntities() {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getApplicationProperties()) {
            private static final long serialVersionUID = 1L;

            SearchTerm st1 = SearchTerm.of(1, SearchTermType.STRING.getId(), 3, Paper.AUTHORS, "joss");
            SearchTerm st2 = SearchTerm.of(2, SearchTermType.INTEGER.getId(), 3, Paper.PUBL_YEAR, "2014");
            SearchTerm st3 = SearchTerm.of(3, SearchTermType.INTEGER.getId(), 4, Paper.PUBL_YEAR, "2014-2016");
            SearchTerm st4 = SearchTerm.of(4, SearchTermType.AUDIT.getId(), 5, Paper.CREATED_BY, "mkj");

            @Override
            protected List<SearchTerm> fetchSearchTermsForSearchOrderWithId(long searchOrderId) {
                if (searchOrderId == SAMPLE_ID) {
                    return Arrays.asList(st1, st2, st3, st4);
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            protected List<Long> fetchExcludedPaperIdsForSearchOrderWithId(long searchOrderId) {
                if (searchOrderId == SAMPLE_ID) {
                    return Arrays.asList(17l, 33l, 42l);
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            protected List<Code> fetchCodesForSearchConditionWithId(SearchCondition searchCondition, String languageCode) {
                return Arrays.asList(new Code("1F", "Code 1F", "", false, 1, "CC 1", "", 0));
            }

            @Override
            protected SearchCondition fetchSearchConditionWithId(Long scId) {
                return new SearchCondition(scId);
            }

            @Override
            protected List<Long> findConditionIdsWithSearchTerms(Long searchOrderId) {
                return new ArrayList<Long>();
            }

            @Override
            protected List<SearchCondition> findTermLessConditions(Long searchOrderId, List<Long> conditionIdsWithSearchTerms) {
                return new ArrayList<SearchCondition>();
            }
        };
    }

    @Test
    public void enrichingAssociatedEntities_withEntityId_fillsTheSearchConditionsAndTerms() {
        JooqSearchOrderRepo repoSpy = makeRepoFindingNestedEntities();
        SearchOrder so = new SearchOrder();
        so.setId(SAMPLE_ID);
        assertThat(so.getSearchConditions()).isEmpty();

        repoSpy.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so.getSearchConditions()).hasSize(3);

        SearchCondition so1 = so.getSearchConditions().get(0);
        assertThat(so1).isNotNull();
        assertThat(so1.getAuthors()).isEqualTo("joss");
        assertThat(so1.getPublicationYear()).isEqualTo("2014");
        assertThat(so1.getDisplayValue()).isEqualTo("joss AND 2014 AND 1F");

        SearchCondition so2 = so.getSearchConditions().get(1);
        assertThat(so2).isNotNull();
        assertThat(so2.getPublicationYear()).isEqualTo("2014-2016");
        assertThat(so2.getDisplayValue()).isEqualTo("2014-2016 AND 1F");

        SearchCondition so3 = so.getSearchConditions().get(2);
        assertThat(so3).isNotNull();
        assertThat(so3.getCreatedBy()).isEqualTo("mkj");
        assertThat(so3.getDisplayValue()).isEqualTo("mkj AND 1F");
    }

    @Test
    public void enrichingAssociatedEntities_withEntityId_fillsTheExcludedPaperIds() {
        JooqSearchOrderRepo repoSpy = makeRepoFindingNestedEntities();
        SearchOrder so = new SearchOrder();
        so.setId(SAMPLE_ID);
        assertThat(so.getExcludedPaperIds()).isEmpty();

        repoSpy.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so.getExcludedPaperIds()).hasSize(3).containsExactly(17l, 33l, 42l);
    }

}
