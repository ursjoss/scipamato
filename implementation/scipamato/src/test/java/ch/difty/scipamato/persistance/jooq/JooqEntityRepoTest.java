package ch.difty.scipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DeleteConditionStep;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertResultStep;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.TableField;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateResultStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.TableImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.entity.IdScipamatoEntity;
import ch.difty.scipamato.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.lib.DateTimeService;
import ch.difty.scipamato.lib.FrozenDateTimeService;
import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.persistance.OptimisticLockingException;

/**
 * TODO find a more feasible approach to test the actual jOOQ part via unit tests. The current approach is too cumbersome.
 * <p>
 * I played around with the MockDataProvider (see branch exp/jooq_mocking), see also https://www.jooq.org/doc/3.9/manual/tools/jdbc-mocking/
 * but that did not seem to solve what I want to achieve (testing the query composition in the repo methods).
 * <p>
 * Let's postpone this and remove the ignored test methods for now (gentle pressure of sonarqube :-) )
 */
@RunWith(SpringRunner.class)
public abstract class JooqEntityRepoTest<R extends Record, T extends IdScipamatoEntity<ID>, ID extends Number, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends ScipamatoFilter>
        extends JooqReadOnlyRepoTest<R, T, ID, TI, M, F> {

    private EntityRepository<T, ID, F> repo;

    @Mock
    private InsertSetStepSetter<R, T> insertSetStepSetterMock;
    @Mock
    private UpdateSetStepSetter<R, T> updateSetStepSetterMock;
    @Mock
    private InsertSetStep<R> insertSetStepMock;
    @Mock
    private InsertSetMoreStep<R> insertSetMoreStepMock;
    @Mock
    private InsertResultStep<R> insertResultStepMock;
    @Mock
    private DeleteWhereStep<R> deleteWhereStepMock;
    @Mock
    private DeleteConditionStep<R> deleteConditionStep1Mock, deleteConditionStep2Mock;
    @Mock
    private UpdateSetFirstStep<R> updateSetFirstStepMock;
    @Mock
    private UpdateConditionStep<R> updateConditionStepMock;
    @Mock
    private UpdateSetMoreStep<R> updateSetMoreStepMock;
    @Mock
    private UpdateResultStep<R> updateResultStepMock;
    @Mock
    private PaginationContext paginationContextMock;

    private final List<T> entities = new ArrayList<>();
    private final List<R> records = new ArrayList<>();

    private final ID id = getSampleId();

    private final DateTimeService dateTimeService = new FrozenDateTimeService();

    protected DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    protected InsertSetStepSetter<R, T> getInsertSetStepSetter() {
        return insertSetStepSetterMock;
    }

    protected UpdateSetStepSetter<R, T> getUpdateSetStepSetter() {
        return updateSetStepSetterMock;
    }

    protected abstract ID getSampleId();

    /**
     * @return the specific repository instantiated 
     */
    protected abstract EntityRepository<T, ID, F> getRepo();

    /**
     * Hand-rolled spy that returns the provided entity in the method {@code findById(ID id)}
     *
     * @param entity the entity to be found.
     * @return the entity
     */
    protected abstract EntityRepository<T, ID, F> makeRepoFindingEntityById(T entity);

    protected abstract T getPersistedEntity();

    protected abstract T getUnpersistedEntity();

    protected abstract R getPersistedRecord();

    protected abstract M getMapper();

    protected abstract Class<T> getEntityClass();

    protected abstract TI getTable();

    protected abstract Class<R> getRecordClass();

    protected abstract TableField<R, ID> getTableId();

    protected abstract TableField<R, Integer> getRecordVersion();

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
        when(deleteWhereStepMock.where(getTableId().equal(id))).thenReturn(deleteConditionStep1Mock);

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
        verifyNoMoreInteractions(deleteWhereStepMock, deleteConditionStep1Mock, deleteConditionStep2Mock);
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

    @Test(expected = NullArgumentException.class)
    public void deleting_withIdNull_throws() {
        repo.delete(null, 1);
    }

    @Test
    public void deleting_withIdNotFoundInDb_throwsOptimisticLockingException() {
        repo = makeRepoFindingEntityById(null);
        try {
            repo.delete(id, 0);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(OptimisticLockingException.class);
            assertThat(ex.getMessage()).isEqualTo("Record in table '" + getTable().getName() + "' has been modified prior to the delete attempt. Aborting....");
        }
    }

    @Test
    public void deleting_validPersistentEntity_returnsDeletedEntity() {
        repo = makeRepoFindingEntityById(getPersistedEntity());

        when(deleteConditionStep1Mock.and(getRecordVersion().equal(Mockito.anyInt()))).thenReturn(deleteConditionStep2Mock);
        when(deleteConditionStep2Mock.execute()).thenReturn(1);

        assertThat(repo.delete(id, 0)).isEqualTo(getPersistedEntity());

        verify(getDsl()).delete(getTable());
        verify(deleteWhereStepMock).where(getTableId().equal(id));
        verify(deleteConditionStep1Mock).and(getRecordVersion().equal(Mockito.anyInt()));
        verify(deleteConditionStep2Mock).execute();
    }

    @Test
    public void deleting_validPersistentEntity_withFailingDelete_returnsDeletedEntity() {
        repo = makeRepoFindingEntityById(getPersistedEntity());
        when(deleteConditionStep1Mock.and(getRecordVersion().equal(Mockito.anyInt()))).thenReturn(deleteConditionStep2Mock);
        when(deleteConditionStep2Mock.execute()).thenReturn(0);

        try {
            repo.delete(id, 0);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(OptimisticLockingException.class);
        }

        verify(getDsl()).delete(getTable());
        verify(deleteConditionStep1Mock).and(getRecordVersion().equal(Mockito.anyInt()));
        verify(deleteWhereStepMock).where(getTableId().equal(id));
        verify(deleteConditionStep2Mock).execute();
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

}
