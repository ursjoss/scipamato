package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertResultStep;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.TableField;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateResultStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.TableImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public abstract class JooqEntityRepoTest<R extends Record, T extends SipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends SipamatoFilter>
        extends JooqReadOnlyRepoTest<R, T, ID, TI, M, F> {

    private EntityRepository<R, T, ID, M, F> repo;

    private final List<T> entities = new ArrayList<>();
    private final List<R> records = new ArrayList<>();

    private final ID id = getSampleId();

    @Mock
    private InsertSetStepSetter<R, T> insertSetStepSetterMock;

    protected InsertSetStepSetter<R, T> getInsertSetStepSetter() {
        return insertSetStepSetterMock;
    }

    @Mock
    private UpdateSetStepSetter<R, T> updateSetStepSetterMock;

    protected UpdateSetStepSetter<R, T> getUpdateSetStepSetter() {
        return updateSetStepSetterMock;
    }

    @Mock
    private Configuration jooqConfig;

    protected Configuration getJooqConfig() {
        return jooqConfig;
    }

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
    private Pageable pageableMock;

    protected abstract ID getSampleId();

    /**
     * @return the specific repository instantiated 
     */
    protected abstract EntityRepository<R, T, ID, M, F> getRepo();

    /**
     * Hand-rolled spy that returns the provided entity in the method <code>findById(ID id)</code>
     *
     * @param entity the entity to be found.
     * @return the entity
     */
    protected abstract EntityRepository<R, T, ID, M, F> makeRepoFindingEntityById(T entity);

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

    @SuppressWarnings("unchecked")
    protected final void specificSetUp() {
        repo = getRepo();

        entities.add(getPersistedEntity());
        entities.add(getPersistedEntity());

        records.add(getPersistedRecord());
        records.add(getPersistedRecord());

        when(getDsl().insertInto(getTable())).thenReturn(insertSetStepMock);
        when(insertSetStepSetterMock.setNonKeyFieldsFor(insertSetStepMock, getUnpersistedEntity())).thenReturn(insertSetMoreStepMock);
        when(insertSetStepMock.set(isA(TableField.class), eq(getUnpersistedEntity()))).thenReturn(insertSetMoreStepMock);
        when(insertSetMoreStepMock.returning()).thenReturn(insertResultStepMock);

        when(getMapper().map(getPersistedRecord())).thenReturn(getPersistedEntity());

        when(getDsl().delete(getTable())).thenReturn(deleteWhereStepMock);
        when(deleteWhereStepMock.where(getTableId().equal(id))).thenReturn(deleteConditionStepMock);

        when(getDsl().update(getTable())).thenReturn(updateSetFirstStepMock);
        when(updateSetStepSetterMock.setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity())).thenReturn(updateSetMoreStepMock);
        when(updateConditionStepMock.returning()).thenReturn(updateResultStepMock);

        testSpecificSetUp();
    }

    /**
     * Hook for concrete tests to inject their specific set up instructions.
     */
    protected void testSpecificSetUp() {
    }

    public final void specificTearDown() {
        testSpecificTearDown();

        verifyNoMoreInteractions(insertSetStepMock, insertSetMoreStepMock, insertResultStepMock, insertSetStepSetterMock);
        verifyNoMoreInteractions(deleteWhereStepMock, deleteConditionStepMock);
        verifyNoMoreInteractions(updateSetFirstStepMock, updateConditionStepMock, updateSetMoreStepMock, updateResultStepMock, updateSetStepSetterMock);
    }

    /**
     * Hook for concrete tests to inject their specific tear down instructions.
     */
    protected void testSpecificTearDown() {
    }

    protected final void specificNullCheck() {
        assertThat(getInsertSetStepSetter()).isNotNull();
        assertThat(getUpdateSetStepSetter()).isNotNull();
        assertThat(getJooqConfig()).isNotNull();

        testSpecificNullCheck();
    }

    /**
     * Hook for concrete tests to inject their specific null checks
     */
    protected void testSpecificNullCheck() {
    }

    @Test(expected = NullArgumentException.class)
    public void addingNull_throws() {
        repo.add(null);
    }

    @Test
    @Ignore
    // TODO find more clever way of testing the jooq part - my current way is not maintainable
    public void adding_pesistsPaperToDb_andReturnsAsEntity() {
        when(insertResultStepMock.fetchOne()).thenReturn(getPersistedRecord());

        assertThat(repo.add(getUnpersistedEntity())).isEqualTo(getPersistedEntity());

        verify(getDsl()).insertInto(getTable());
        verify(insertSetStepSetterMock).setNonKeyFieldsFor(insertSetStepMock, getUnpersistedEntity());
        verify(insertSetStepSetterMock).considerSettingKeyOf(insertSetMoreStepMock, getUnpersistedEntity());
        verify(insertSetMoreStepMock).returning();
        verify(insertResultStepMock).fetchOne();
        verify(getMapper()).map(getPersistedRecord());

        verifyPersistedRecordId();
    }

    @Test
    @Ignore
    // TODO find more clever way of testing the jooq part - my current way is not maintainable
    public void adding_pesistsPaperToDb_andReturnsAsEntity1() {
        when(insertResultStepMock.fetchOne()).thenReturn(null);

        assertThat(repo.add(getUnpersistedEntity())).isNull();

        verify(getDsl()).insertInto(getTable());
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

        verify(getDsl()).delete(getTable());
        verify(deleteWhereStepMock).where(getTableId().equal(id));
        verify(deleteConditionStepMock).execute();
    }

    @Test
    public void deleting_validPersistentEntity_withFailingDelete_returnsDeletedEntity() {
        repo = makeRepoFindingEntityById(getPersistedEntity());
        when(deleteConditionStepMock.execute()).thenReturn(0);

        assertThat(repo.delete(id)).isEqualTo(getPersistedEntity());

        verify(getDsl()).delete(getTable());
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
    @Ignore
    // TODO find more clever way of testing the jooq part - my current way is not maintainable
    public void updating_withValidEntity_changesAndPersistsEntityToDb_andReturnsFetchedEntity() {
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(getPersistedRecord());
        when(getMapper().map(getPersistedRecord())).thenReturn(getPersistedEntity());

        assertThat(repo.update(getUnpersistedEntity())).isEqualTo(getPersistedEntity());

        verifyUnpersistedEntityId();
        verify(getDsl()).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();
        verify(getMapper()).map(getPersistedRecord());
    }

    @Test
    @Ignore
    // TODO find more clever way of testing the jooq part - my current way is not maintainable
    public void updating_withNonH2Db_withUnsuccessfulRetrievalAfterPersistingAttempt_returnsNull() {
        when(jooqConfig.dialect()).thenReturn(SQLDialect.POSTGRES_9_5);
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(null);

        assertThat(repo.update(getUnpersistedEntity())).isNull();

        verifyUnpersistedEntityId();
        verify(getDsl()).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();
    }

    @Test
    @Ignore
    // TODO find more clever way of testing the jooq part - my current way is not maintainable
    public void updating_withH2Db_withUnsuccessfulRetrievalAfterPersistingAttempt_andWithUnsuccessfulH2Retrieval_returnsNull() {
        when(jooqConfig.dialect()).thenReturn(SQLDialect.H2);
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(null);
        when(getSelectConditionStepMock().fetchOneInto(getEntityClass())).thenReturn(null);

        assertThat(repo.update(getUnpersistedEntity())).isNull();

        verifyUnpersistedEntityId();
        verify(getDsl()).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();

        verify(getDsl()).selectFrom(getTable());
        verify(getSelectWhereStepMock()).where(getTableId().equal(id));
        verify(getSelectConditionStepMock()).fetchOneInto(getEntityClass());
    }

    @Test
    @Ignore
    // TODO find more clever way of testing the jooq part - my current way is not maintainable
    public void updating_withH2Db_withUnsuccessfulRetrievalAfterPersistingAttempt_butSuccessfulFind_returnsEntity() {
        when(jooqConfig.dialect()).thenReturn(SQLDialect.H2);
        expectEntityIdsWithValues();
        when(updateSetMoreStepMock.where(getTableId().equal(id))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(null);
        when(getSelectConditionStepMock().fetchOneInto(getEntityClass())).thenReturn(getPersistedEntity());

        assertThat(repo.update(getUnpersistedEntity())).isEqualTo(getPersistedEntity());

        verifyUnpersistedEntityId();
        verify(getDsl()).update(getTable());
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, getUnpersistedEntity());
        verify(updateSetMoreStepMock).where(getTableId().equal(id));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();

        verify(getDsl()).selectFrom(getTable());
        verify(getSelectWhereStepMock()).where(getTableId().equal(id));
        verify(getSelectConditionStepMock()).fetchOneInto(getEntityClass());
    }

}
