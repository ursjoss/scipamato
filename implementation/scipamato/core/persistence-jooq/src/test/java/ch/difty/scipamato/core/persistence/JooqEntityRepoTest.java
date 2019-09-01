package ch.difty.scipamato.core.persistence;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.jooq.*;
import org.jooq.impl.TableImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.FrozenDateTimeService;
import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

/**
 * TODO find a more feasible approach to test the actual jOOQ part via unit
 * tests. The current approach is too cumbersome.
 * <p>
 * I played around with the MockDataProvider (see branch exp/jooq_mocking), see
 * also https://www.jooq.org/doc/3.9/manual/tools/jdbc-mocking/ but that did not
 * seem to solve what I want to achieve (testing the query composition in the
 * repo methods).
 * <p>
 * Let's postpone this and remove the ignored test methods for now (gentle
 * pressure of sonarqube :-) )
 */
@ExtendWith(SpringExtension.class)
@SuppressWarnings("WeakerAccess")
public abstract class JooqEntityRepoTest<R extends Record, T extends IdScipamatoEntity<ID>, ID extends Number, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends ScipamatoFilter>
    extends JooqReadOnlyRepoTest<R, T, ID, TI, M, F> {

    private EntityRepository<T, ID, F> repo;

    @Mock
    private   InsertSetStepSetter<R, T> insertSetStepSetterMock;
    @Mock
    private   UpdateSetStepSetter<R, T> updateSetStepSetterMock;
    @Mock
    private   InsertSetStep<R>          insertSetStepMock;
    @Mock
    private   InsertSetMoreStep<R>      insertSetMoreStepMock;
    @Mock
    private   InsertResultStep<R>       insertResultStepMock;
    @Mock
    protected DeleteWhereStep<R>        deleteWhereStepMock;
    @Mock
    private   DeleteConditionStep<R>    deleteConditionStep1Mock, deleteConditionStep2Mock;
    @Mock
    private UpdateSetFirstStep<R>  updateSetFirstStepMock;
    @Mock
    private UpdateConditionStep<R> updateConditionStepMock;
    @Mock
    private UpdateSetMoreStep<R>   updateSetMoreStepMock;
    @Mock
    private UpdateResultStep<R>    updateResultStepMock;
    @Mock
    private PaginationContext      paginationContextMock;

    private final List<T> entities = new ArrayList<>();
    private final List<R> records  = new ArrayList<>();

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
    @Override
    protected abstract EntityRepository<T, ID, F> getRepo();

    /**
     * Hand-rolled spy that returns the provided entity in the method
     * {@code findById(ID id)}
     *
     * @param entity
     *     the entity to be found.
     * @return the entity
     */
    @Override
    protected abstract EntityRepository<T, ID, F> makeRepoFindingEntityById(T entity);

    protected abstract TableField<R, Integer> getRecordVersion();

    @Override
    protected final void specificSetUp() {
        repo = getRepo();
        testSpecificSetUp();
    }

    /**
     * Hook for concrete tests to inject their specific set up instructions.
     */
    protected void testSpecificSetUp() {
    }

    @Override
    public final void specificTearDown() {
        testSpecificTearDown();

        verifyNoMoreInteractions(insertSetStepMock, insertSetMoreStepMock, insertResultStepMock,
            insertSetStepSetterMock);
        verifyNoMoreInteractions(deleteWhereStepMock, deleteConditionStep1Mock, deleteConditionStep2Mock);
        verifyNoMoreInteractions(updateSetFirstStepMock, updateConditionStepMock, updateSetMoreStepMock,
            updateResultStepMock, updateSetStepSetterMock);
    }

    /**
     * Hook for concrete tests to inject their specific tear down instructions.
     */
    @SuppressWarnings("EmptyMethod")
    protected void testSpecificTearDown() {
    }

    @Override
    protected final void specificNullCheck() {
        assertThat(getInsertSetStepSetter()).isNotNull();
        assertThat(getUpdateSetStepSetter()).isNotNull();

        testSpecificNullCheck();
    }

    /**
     * Hook for concrete tests to inject their specific null checks
     */
    @SuppressWarnings("EmptyMethod")
    protected void testSpecificNullCheck() {
    }

    @Test
    void addingNullEntity_throws() {
        assertDegenerateSupplierParameter(() -> repo.add(null, "de"), "entity");
    }

    @Test
    void addingNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> repo.add(getUnpersistedEntity(), null), "languageCode");
    }

    @Test
    void adding_withSaveReturningNull_returnsNull() {
        repo = makeRepoSavingReturning(null);
        assertThat(repo.add(getUnpersistedEntity(), "de")).isNull();
    }

    /**
     * Hand-rolled spy that returns the provided entity in the method
     * {@code doSaving(T entity)}
     *
     * @param returning
     *     the record to be returned after the save.
     * @return the entity
     */
    protected abstract EntityRepository<T, ID, F> makeRepoSavingReturning(R returning);

    @Test
    void deleting_withIdNull_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> repo.delete(null, 1));
    }

    @Test
    void deleting_withIdNotFoundInDb_throwsOptimisticLockingException() {
        repo = makeRepoFindingEntityById(null);
        try {
            repo.delete(id, 0);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(OptimisticLockingException.class);
            assertThat(ex.getMessage()).isEqualTo("Record in table '" + getTable().getName()
                                                  + "' has been modified prior to the delete attempt. Aborting....");
        }
    }

    @Test
    void deleting_validPersistentEntity_returnsDeletedEntity() {
        repo = makeRepoFindingEntityById(getPersistedEntity());

        when(getDsl().delete(getTable())).thenReturn(deleteWhereStepMock);
        when(deleteWhereStepMock.where(getTableId().equal(id))).thenReturn(deleteConditionStep1Mock);
        when(deleteConditionStep1Mock.and(getRecordVersion().eq(0))).thenReturn(deleteConditionStep2Mock);
        when(deleteConditionStep2Mock.execute()).thenReturn(1);

        assertThat(repo.delete(id, 0)).isEqualTo(getPersistedEntity());

        verify(getDsl()).delete(getTable());
        verify(deleteWhereStepMock).where(getTableId().equal(id));
        verify(deleteConditionStep1Mock).and(getRecordVersion().eq(0));
        verify(deleteConditionStep2Mock).execute();
    }

    @Test
    void deleting_validPersistentEntity_withFailingDelete_returnsDeletedEntity() {
        repo = makeRepoFindingEntityById(getPersistedEntity());
        when(getDsl().delete(getTable())).thenReturn(deleteWhereStepMock);
        when(deleteWhereStepMock.where(getTableId().equal(id))).thenReturn(deleteConditionStep1Mock);
        when(deleteConditionStep1Mock.and(getRecordVersion().eq(0))).thenReturn(deleteConditionStep2Mock);
        when(deleteConditionStep2Mock.execute()).thenReturn(0);

        try {
            repo.delete(id, 0);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(OptimisticLockingException.class);
        }

        verify(getDsl()).delete(getTable());
        verify(deleteConditionStep1Mock).and(getRecordVersion().eq(0));
        verify(deleteWhereStepMock).where(getTableId().equal(id));
        verify(deleteConditionStep2Mock).execute();
    }

    @Test
    void updating_withEntityNull_throws() {
        assertDegenerateSupplierParameter(() -> repo.update(null, "de"), "entity");
    }

    @Test
    void updating_withLanguageCodeNull_throws() {
        assertDegenerateSupplierParameter(() -> repo.update(getPersistedEntity(), null), "languageCode");
    }

    @Test
    void updating_withEntityIdNull_throws() {
        expectUnpersistedEntityIdNull();
        assertDegenerateSupplierParameter(() -> repo.update(getUnpersistedEntity(), "de"), "entity.id");
        verifyUnpersistedEntityId();
    }
}
