package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.SearchTerm;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepoTest;

public class JooqSearchOrderRepoTest extends JooqEntityRepoTest<SearchOrderRecord, SearchOrder, Long, ch.difty.sipamato.db.tables.SearchOrder, SearchOrderRecordMapper, SearchOrderFilter> {

    private static final Long SAMPLE_ID = 3l;

    private JooqSearchOrderRepo repo;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqSearchOrderRepo getRepo() {
        if (repo == null) {
            repo = new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(),
                    getUpdateSetStepSetter(), getJooqConfig());
        }
        return repo;
    }

    @Override
    protected EntityRepository<SearchOrderRecord, SearchOrder, Long, SearchOrderRecordMapper, SearchOrderFilter> makeRepoFindingEntityById(SearchOrder searchOrder) {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getJooqConfig()) {
            private static final long serialVersionUID = 1L;

            @Override
            public SearchOrder findById(Long id) {
                return searchOrder;
            }
        };
    }

    @Mock
    private SearchOrder unpersistedEntity, persistedEntity;

    @Override
    protected SearchOrder getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected SearchOrder getPersistedEntity() {
        return persistedEntity;
    }

    @Mock
    private SearchOrderRecord persistedRecord;

    @Override
    protected SearchOrderRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Mock
    private SearchOrderRecordMapper mapperMock;

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
    protected ch.difty.sipamato.db.tables.SearchOrder getTable() {
        return SEARCH_ORDER;
    }

    @Override
    protected TableField<SearchOrderRecord, Long> getTableId() {
        return SEARCH_ORDER.ID;
    }

    @Mock
    private SearchOrderFilter filterMock;

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
            new JooqSearchOrderRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dateTimeService must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("localization must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), null, getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), null, getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("jooqConfig must not be null.");
        }
    }

    @Test
    public void enrichingAssociatedEntities_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null);
    }

    @Test
    public void enrichingAssociatedEntities_withEntityWithNullId_doesNothing() {
        SearchOrder so = new SearchOrder();
        assertThat(so.getId()).isNull();

        repo.enrichAssociatedEntitiesOf(so);

        assertThat(so.getSearchConditions()).isEmpty();
    }

    private JooqSearchOrderRepo makeRepoFindingNestedEntities() {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getJooqConfig()) {
            private static final long serialVersionUID = 1L;

            SearchTerm<?> st1 = SearchTerm.of(1, 2, 3, "authors", "joss");
            SearchTerm<?> st2 = SearchTerm.of(2, 1, 3, "publication_year", "2014");
            SearchTerm<?> st3 = SearchTerm.of(3, 1, 4, "publication_year", "2014-2016");

            @Override
            protected List<SearchTerm<?>> fetchSearchTermsForSearchOrderWithId(long searchOrderId) {
                if (searchOrderId == SAMPLE_ID) {
                    return Arrays.asList(st1, st2, st3);
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
            protected List<Code> fetchCodesForSearchConditionWithId(SearchCondition searchCondition) {
                return Arrays.asList(new Code("1F", "Code 1F", "", false, 1, "CC 1", "", 0));
            }

            @Override
            protected SearchCondition fetchSearchConditionWithId(Long scId) {
                return new SearchCondition(scId);
            }
        };
    }

    @Test
    public void enrichingAssociatedEntities_withEntityId_fillsTheSearchConditionsAndTerms() {
        JooqSearchOrderRepo repoSpy = makeRepoFindingNestedEntities();
        SearchOrder so = new SearchOrder();
        so.setId(SAMPLE_ID);
        assertThat(so.getSearchConditions()).isEmpty();

        repoSpy.enrichAssociatedEntitiesOf(so);

        assertThat(so.getSearchConditions()).hasSize(2);

        SearchCondition so1 = so.getSearchConditions().get(0);
        assertThat(so1).isNotNull();
        assertThat(so1.getAuthors()).isEqualTo("joss");
        assertThat(so1.getPublicationYear()).isEqualTo("2014");
        assertThat(so1.getDisplayValue()).isEqualTo("joss AND 2014 AND 1F");

        SearchCondition so2 = so.getSearchConditions().get(1);
        assertThat(so2).isNotNull();
        assertThat(so2.getPublicationYear()).isEqualTo("2014-2016");
        assertThat(so2.getDisplayValue()).isEqualTo("2014-2016 AND 1F");
    }

    @Test
    public void enrichingAssociatedEntities_withEntityId_fillsTheExcludedPaperIds() {
        JooqSearchOrderRepo repoSpy = makeRepoFindingNestedEntities();
        SearchOrder so = new SearchOrder();
        so.setId(SAMPLE_ID);
        assertThat(so.getExcludedPaperIds()).isEmpty();

        repoSpy.enrichAssociatedEntitiesOf(so);

        assertThat(so.getExcludedPaperIds()).hasSize(3).containsExactly(17l, 33l, 42l);
    }

}
