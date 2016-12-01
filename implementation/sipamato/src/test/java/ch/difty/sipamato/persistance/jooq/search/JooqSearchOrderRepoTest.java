package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepoTest;

public class JooqSearchOrderRepoTest extends JooqEntityRepoTest<SearchOrderRecord, SearchOrder, Integer, ch.difty.sipamato.db.tables.SearchOrder, SearchOrderRecordMapper, SearchOrderFilter> {

    private static final Integer SAMPLE_ID = 3;

    private JooqSearchOrderRepo repo;

    @Override
    protected Integer getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqSearchOrderRepo getRepo() {
        if (repo == null) {
            repo = new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
        }
        return repo;
    }

    @Override
    protected EntityRepository<SearchOrderRecord, SearchOrder, Integer, SearchOrderRecordMapper, SearchOrderFilter> makeRepoFindingEntityById(SearchOrder searchOrder) {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig()) {
            private static final long serialVersionUID = 1L;

            @Override
            public SearchOrder findById(Integer id) {
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
    protected TableField<SearchOrderRecord, Integer> getTableId() {
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
            new JooqSearchOrderRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), null, getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("localization must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), null, getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), getInsertSetStepSetter(), null, getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("jooqConfig must not be null.");
        }
    }

}
