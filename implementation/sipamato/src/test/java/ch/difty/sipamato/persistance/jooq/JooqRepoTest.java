package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.fail;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertResultStep;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSeekStepN;
import org.jooq.SelectSelectStep;
import org.jooq.SelectWhereStep;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateResultStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.TableImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public abstract class JooqRepoTest<R extends Record, T extends SipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends SipamatoFilter> {

    private GenericRepository<R, T, ID, M, F> repo;

    private final List<T> entities = new ArrayList<>();
    private final List<R> records = new ArrayList<>();

    private final ID id = getSampleId();

    @Mock
    private DSLContext dslMock;
    @Mock
    private InsertSetStepSetter<R, T> insertSetStepSetterMock;
    @Mock
    private UpdateSetStepSetter<R, T> updateSetStepSetterMock;

    @Mock
    private R unpersistedRecord;

    protected DSLContext getDsl() {
        return dslMock;
    }

    protected InsertSetStepSetter<R, T> getInsertSetStepSetter() {
        return insertSetStepSetterMock;
    }

    protected UpdateSetStepSetter<R, T> getUpdateSetStepSetter() {
        return updateSetStepSetterMock;
    }

    @Mock
    private JooqSortMapper<R, T, TI> sortFieldExtractorMock;

    protected JooqSortMapper<R, T, TI> getSortFieldExtractor() {
        return sortFieldExtractorMock;
    }

    @Mock
    private Configuration jooqConfig;

    protected Configuration getJooqConfig() {
        return jooqConfig;
    }

    @Mock
    private SelectWhereStep<R> selectWhereStepMock;
    @Mock
    private SelectConditionStep<R> selectConditionStepMock;

    @Mock
    private InsertSetStep<R> insertSetStepMock;
    @Mock
    private InsertSetMoreStep<R> insertSetMoreStepMock;
    @Mock
    private InsertResultStep<R> insertResultStepMock;
    @Mock
    private DeleteWhereStep<R> deleteWhereStepMock;
    @Mock
    private DeleteConditionStep<R> deleteConditionStepMock;
    @Mock
    private UpdateSetFirstStep<R> updateSetFirstStepMock;
    @Mock
    private UpdateConditionStep<R> updateConditionStepMock;
    @Mock
    private UpdateSetMoreStep<R> updateSetMoreStepMock;
    @Mock
    private UpdateResultStep<R> updateResultStepMock;

    @Mock
    private SelectSelectStep<Record1<Integer>> selectSelectStepMock;
    @Mock
    private SelectJoinStep<Record1<Integer>> selectJoinStepMock;
    @Mock
    private SelectConditionStep<Record1<Integer>> selectConditionStepMock2;

    @Mock
    private Pageable pageableMock;
    @Mock
    private Sort sortMock;
    @Mock
    private Collection<SortField<T>> sortFieldsMock;
    @Mock
    private SelectSeekStepN<R> selectSeekStepNMock;

    protected abstract ID getSampleId();

    /**
     * @return the specific repository instantiated 
     */
    protected abstract GenericRepository<R, T, ID, M, F> getRepo();

    /**
     * Hand-rolled spy that returns the provided entity in the method <code>findById(ID id)</code>
     *
     * @param entity the entity to be found.
     * @return the entity
     */
    protected abstract GenericRepository<R, T, ID, M, F> makeRepoFindingEntityById(T entity);

    protected abstract T getPersistedEntity();

    protected abstract T getUnpersistedEntity();

    protected abstract R getPersistedRecord();

    protected abstract M getMapper();

    protected abstract Class<T> getEntityClass();

    protected abstract TI getTable();

    protected abstract Class<R> getRecordClass();

    protected abstract TableField<R, ID> getTableId();

    protected abstract void expectEntityIdsWithValues();

    protected abstract void expectUnpersistedEntityIdNull();

    protected abstract void verifyUnpersistedEntityId();

    protected abstract void verifyPersistedRecordId();

    private F filterMock = getFilter();

    protected abstract F getFilter();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        repo = getRepo();

        entities.add(getPersistedEntity());
        entities.add(getPersistedEntity());

        records.add(getPersistedRecord());
        records.add(getPersistedRecord());

        when(dslMock.selectFrom(getTable())).thenReturn(selectWhereStepMock);
        when(selectWhereStepMock.fetchInto(getEntityClass())).thenReturn(entities);
        when(selectWhereStepMock.where(getTableId().equal(id))).thenReturn(selectConditionStepMock);

        when(dslMock.insertInto(getTable())).thenReturn(insertSetStepMock);
        when(insertSetStepSetterMock.setNonKeyFieldsFor(insertSetStepMock, getUnpersistedEntity())).thenReturn(insertSetMoreStepMock);
        when(insertSetStepMock.set(isA(TableField.class), eq(getUnpersistedEntity()))).thenReturn(insertSetMoreStepMock);
        when(insertSetMoreStepMock.returning()).thenReturn(insertResultStepMock);

        when(getMapper().map(getPersistedRecord())).thenReturn(getPersistedEntity());

        when(dslMock.delete(getTable())).thenReturn(deleteWhereStepMock);
        when(deleteWhereStepMock.where(getTableId().equal(id))).thenReturn(deleteConditionStepMock);

        when(dslMock.update(getTable())).thenReturn(updateSetFirstStepMock);
        when(updateSetStepSetterMock.setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity())).thenReturn(updateSetMoreStepMock);
        when(updateConditionStepMock.returning()).thenReturn(updateResultStepMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dslMock, getMapper(), sortFieldExtractorMock);
        verifyNoMoreInteractions(getUnpersistedEntity(), getPersistedEntity(), unpersistedRecord, getPersistedRecord());
        verifyNoMoreInteractions(selectWhereStepMock, selectConditionStepMock);
        verifyNoMoreInteractions(insertSetStepMock, insertSetMoreStepMock, insertResultStepMock, insertSetStepSetterMock);
        verifyNoMoreInteractions(deleteWhereStepMock, deleteConditionStepMock);
        verifyNoMoreInteractions(updateSetFirstStepMock, updateConditionStepMock, updateSetMoreStepMock, updateResultStepMock, updateSetStepSetterMock);
        verifyNoMoreInteractions(selectSelectStepMock, selectJoinStepMock);
        verifyNoMoreInteractions(pageableMock, sortMock, sortFieldsMock, selectSeekStepNMock);
    }

    @Test
    public void nullCheck() {
        assertThat(getRepo()).isNotNull();
        assertThat(getDsl()).isNotNull();
        assertThat(getMapper()).isNotNull();
        assertThat(getInsertSetStepSetter()).isNotNull();
        assertThat(getUpdateSetStepSetter()).isNotNull();
        assertThat(getSortFieldExtractor()).isNotNull();
        assertThat(getJooqConfig()).isNotNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findingAll_returnsPersistedEntities() {
        assertThat(repo.findAll()).hasSize(2).containsExactly(getPersistedEntity(), getPersistedEntity());

        verify(dslMock).selectFrom(getTable());
        verify(selectWhereStepMock).fetchInto(getEntityClass());
    }

    @Test(expected = NullArgumentException.class)
    public void findingByIdNull_throws() {
        repo.findById(null);
    }

    @Test
    public void findingById() {
        when(selectConditionStepMock.fetchOneInto(getEntityClass())).thenReturn(getPersistedEntity());

        repo.findById(id);

        verify(dslMock).selectFrom(getTable());
        verify(selectWhereStepMock).where(getTableId().equal(id));
        verify(selectConditionStepMock).fetchOneInto(getEntityClass());
    }

    @Test(expected = NullArgumentException.class)
    public void addingNull_throws() {
        repo.add(null);
    }

    @Test
    public void adding_pesistsPaperToDb_andReturnsAsEntity() {
        when(insertResultStepMock.fetchOne()).thenReturn(getPersistedRecord());

        assertThat(repo.add(getUnpersistedEntity())).isEqualTo(getPersistedEntity());

        verify(dslMock).insertInto(getTable());
        verify(insertSetStepSetterMock).setNonKeyFieldsFor(insertSetStepMock, getUnpersistedEntity());
        verify(insertSetStepSetterMock).considerSettingKeyOf(insertSetMoreStepMock, getUnpersistedEntity());
        verify(insertSetMoreStepMock).returning();
        verify(insertResultStepMock).fetchOne();
        verify(getMapper()).map(getPersistedRecord());

        verifyPersistedRecordId();
    }

    @Test
    public void adding_pesistsPaperToDb_andReturnsAsEntity1() {
        when(insertResultStepMock.fetchOne()).thenReturn(null);

        assertThat(repo.add(getUnpersistedEntity())).isNull();

        verify(dslMock).insertInto(getTable());
        verify(insertSetStepSetterMock).setNonKeyFieldsFor(insertSetStepMock, getUnpersistedEntity());
        verify(insertSetStepSetterMock).considerSettingKeyOf(insertSetMoreStepMock, getUnpersistedEntity());
        verify(insertSetMoreStepMock).returning();
        verify(insertResultStepMock).fetchOne();
    }

    @Test(expected = NullArgumentException.class)
    public void deleting_withIdNull_throws() {
        repo.delete(null);
    }

    @Test
    public void deleting_withIdNotFoundInDb_returnsNull() {
        repo = makeRepoFindingEntityById(null);
        assertThat(repo.delete(id)).isNull();
    }

    @Test
    public void deleting_validPersistentEntity_returnsDeletedEntity() {
        repo = makeRepoFindingEntityById(getPersistedEntity());

        when(deleteConditionStepMock.execute()).thenReturn(1);

        assertThat(repo.delete(id)).isEqualTo(getPersistedEntity());

        verify(dslMock).delete(getTable());
        verify(deleteWhereStepMock).where(getTableId().equal(id));
        verify(deleteConditionStepMock).execute();
    }

    @Test
    public void deleting_validPersistentEntity_withFailingDelete_returnsDeletedEntity() {
        repo = makeRepoFindingEntityById(getPersistedEntity());
        when(deleteConditionStepMock.execute()).thenReturn(0);

        assertThat(repo.delete(id)).isEqualTo(getPersistedEntity());

        verify(dslMock).delete(getTable());
        verify(deleteWhereStepMock).where(getTableId().equal(id));
        verify(deleteConditionStepMock).execute();
    }

    @Test(expected = NullArgumentException.class)
    public void updating_withEntityNull_throws() {
        repo.update(null);
    }

    @Test
    public void updating_withEntityIdNull_throws() {
        expectUnpersistedEntityIdNull();
        try {
            repo.update(getUnpersistedEntity());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("entity.id must not be null.");
        }
        verifyUnpersistedEntityId();
    }

    @Test
    public void updating_withValidEntity_changesAndPersistsEntityToDb_andReturnsFetchedEntity() {
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(getPersistedRecord());
        when(getMapper().map(getPersistedRecord())).thenReturn(getPersistedEntity());

        assertThat(repo.update(getUnpersistedEntity())).isEqualTo(getPersistedEntity());

        verifyUnpersistedEntityId();
        verify(dslMock).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();
        verify(getMapper()).map(getPersistedRecord());
    }

    @Test
    public void updating_withNonH2Db_withUnsuccessfulRetrievalAfterPersistingAttempt_returnsNull() {
        when(jooqConfig.dialect()).thenReturn(SQLDialect.POSTGRES_9_5);
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(null);

        assertThat(repo.update(getUnpersistedEntity())).isNull();

        verifyUnpersistedEntityId();
        verify(dslMock).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();
    }

    @Test
    public void updating_withH2Db_withUnsuccessfulRetrievalAfterPersistingAttempt_andWithUnsuccessfulH2Retrieval_returnsNull() {
        when(jooqConfig.dialect()).thenReturn(SQLDialect.H2);
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(null);
        when(selectConditionStepMock.fetchOneInto(getEntityClass())).thenReturn(null);

        assertThat(repo.update(getUnpersistedEntity())).isNull();

        verifyUnpersistedEntityId();
        verify(dslMock).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();

        verify(dslMock).selectFrom(getTable());
        verify(selectWhereStepMock).where(getTableId().equal(id));
        verify(selectConditionStepMock).fetchOneInto(getEntityClass());
    }

    @Test
    public void updating_withH2Db_withUnsuccessfulRetrievalAfterPersistingAttempt_butSuccessfulFind_returnsEntity() {
        when(jooqConfig.dialect()).thenReturn(SQLDialect.H2);
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(null);
        when(selectConditionStepMock.fetchOneInto(getEntityClass())).thenReturn(getPersistedEntity());

        assertThat(repo.update(getUnpersistedEntity())).isEqualTo(getPersistedEntity());

        verifyUnpersistedEntityId();
        verify(dslMock).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();

        verify(dslMock).selectFrom(getTable());
        verify(selectWhereStepMock).where(getTableId().equal(id));
        verify(selectConditionStepMock).fetchOneInto(getEntityClass());
    }

    protected String makeWhereClause(String pattern, String... fieldNames) {
        int fields = fieldNames.length;
        final StringBuilder sb = new StringBuilder();
        sb.append("(").append("\n  ");
        for (final String fieldName : fieldNames) {
            sb.append("lower(\"PUBLIC\".\"").append(getTable().getName()).append("\".\"").append(fieldName).append("\") like lower('%").append(pattern).append("%')").append("\n");
            if (fields-- > 1)
                sb.append("  or ");
        }
        return sb.append(")").toString();
    }

    @Test
    public void countingByFilter() {
        when(dslMock.selectOne()).thenReturn(selectSelectStepMock);
        when(selectSelectStepMock.from(getTable())).thenReturn(selectJoinStepMock);
        when(selectJoinStepMock.where(isA(Condition.class))).thenReturn(selectConditionStepMock2);
        when(dslMock.fetchCount(selectConditionStepMock2)).thenReturn(2);

        assertThat(repo.countByFilter(filterMock)).isEqualTo(2);

        verify(dslMock).selectOne();
        verify(selectSelectStepMock).from(getTable());
        verify(selectJoinStepMock).where(isA(Condition.class));
        verify(dslMock).fetchCount(selectConditionStepMock2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findingByFilter() {
        when(selectWhereStepMock.where(isA(Condition.class))).thenReturn(selectConditionStepMock);
        when(pageableMock.getSort()).thenReturn(sortMock);
        when(sortFieldExtractorMock.map(sortMock, getTable())).thenReturn(sortFieldsMock);
        when(selectConditionStepMock.orderBy(sortFieldsMock)).thenReturn(selectSeekStepNMock);
        when(selectSeekStepNMock.fetchInto(getRecordClass())).thenReturn(records);
        when(getMapper().map(getPersistedRecord())).thenReturn(getPersistedEntity());
        when(pageableMock.getOffset()).thenReturn(0);
        when(pageableMock.getPageSize()).thenReturn(20);

        when(dslMock.selectOne()).thenReturn(selectSelectStepMock);
        when(selectSelectStepMock.from(getTable())).thenReturn(selectJoinStepMock);
        when(selectJoinStepMock.where(isA(Condition.class))).thenReturn(selectConditionStepMock2);
        when(dslMock.fetchCount(selectConditionStepMock2)).thenReturn(2);

        Page<T> page = repo.findByFilter(filterMock, pageableMock);
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2).containsOnly(getPersistedEntity());

        verify(getDsl()).selectFrom(getTable());
        verify(selectWhereStepMock).where(isA(Condition.class));
        verify(pageableMock).getSort();
        verify(sortFieldExtractorMock).map(sortMock, getTable());
        verify(selectConditionStepMock).orderBy(sortFieldsMock);
        verify(selectSeekStepNMock).fetchInto(getRecordClass());
        verify(getMapper(), times(2)).map(getPersistedRecord());
        verify(pageableMock, times(2)).getOffset();
        verify(pageableMock).getPageSize();

        verify(dslMock).selectOne();
        verify(selectSelectStepMock).from(getTable());
        verify(selectJoinStepMock).where(isA(Condition.class));
        verify(dslMock).fetchCount(selectConditionStepMock2);
    }

}
