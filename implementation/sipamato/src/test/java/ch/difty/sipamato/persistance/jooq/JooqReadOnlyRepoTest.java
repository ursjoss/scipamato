package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.RecordMapper;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSeekStepN;
import org.jooq.SelectSelectStep;
import org.jooq.SelectWhereStep;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.filter.SipamatoFilter;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.service.Localization;

@RunWith(MockitoJUnitRunner.class)
public abstract class JooqReadOnlyRepoTest<R extends Record, T extends IdSipamatoEntity<ID>, ID extends Number, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends SipamatoFilter> {

    private ReadOnlyRepository<T, ID, F> repo;

    private final List<T> entities = new ArrayList<>();
    private final List<R> records = new ArrayList<>();

    private final ID id = getSampleId();

    @Mock
    private DSLContext dslMock;
    @Mock
    private GenericFilterConditionMapper<F> filterConditionMapperMock;
    @Mock
    private Localization localizationMock;
    @Mock
    private R unpersistedRecord;
    @Mock
    private JooqSortMapper<R, T, TI> sortMapperMock;
    @Mock
    private SelectWhereStep<R> selectWhereStepMock;
    @Mock
    private SelectConditionStep<R> selectConditionStepMock;
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

    @Mock
    private Condition conditionMock;

    private F filterMock = getFilter();

    protected DSLContext getDsl() {
        return dslMock;
    }

    protected GenericFilterConditionMapper<F> getFilterConditionMapper() {
        return filterConditionMapperMock;
    }

    protected Localization getLocalization() {
        return localizationMock;
    }

    protected JooqSortMapper<R, T, TI> getSortMapper() {
        return sortMapperMock;
    }

    protected SelectWhereStep<R> getSelectWhereStepMock() {
        return selectWhereStepMock;
    }

    protected SelectConditionStep<R> getSelectConditionStepMock() {
        return selectConditionStepMock;
    }

    protected abstract ID getSampleId();

    /**
     * @return the specific repository instantiated
     */
    protected abstract ReadOnlyRepository<T, ID, F> getRepo();

    /**
     * Hand-rolled spy that returns the provided entity in the method <code>findById(ID id)</code>
     *
     * @param entity the entity to be found.
     * @return the entity
     */
    protected abstract ReadOnlyRepository<T, ID, F> makeRepoFindingEntityById(T entity);

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

    protected abstract F getFilter();

    @Before
    public final void setUp() {
        repo = getRepo();

        entities.add(getPersistedEntity());
        entities.add(getPersistedEntity());

        records.add(getPersistedRecord());
        records.add(getPersistedRecord());

        when(dslMock.selectFrom(getTable())).thenReturn(selectWhereStepMock);

        when(localizationMock.getLocalization()).thenReturn("de");
        when(selectWhereStepMock.fetchInto(getEntityClass())).thenReturn(entities);
        when(selectWhereStepMock.where(getTableId().equal(id))).thenReturn(selectConditionStepMock);

        when(getMapper().map(getPersistedRecord())).thenReturn(getPersistedEntity());

        when(filterConditionMapperMock.map(filterMock)).thenReturn(conditionMock);

        specificSetUp();
    }

    protected void specificSetUp() {
    }

    @After
    public final void tearDown() {
        specificTearDown();
        verifyNoMoreInteractions(dslMock, getMapper(), sortMapperMock);
        verifyNoMoreInteractions(getUnpersistedEntity(), getPersistedEntity(), unpersistedRecord, getPersistedRecord(), localizationMock);
        verifyNoMoreInteractions(selectWhereStepMock, selectConditionStepMock);
        verifyNoMoreInteractions(selectSelectStepMock, selectJoinStepMock);
        verifyNoMoreInteractions(pageableMock, sortMock, sortFieldsMock, selectSeekStepNMock);
        verifyNoMoreInteractions(getFilter(), conditionMock);
    }

    protected void specificTearDown() {
    }

    @Test
    public final void nullCheck() {
        assertThat(getRepo()).isNotNull();
        assertThat(getDsl()).isNotNull();
        assertThat(getMapper()).isNotNull();
        assertThat(getSortMapper()).isNotNull();
        assertThat(getLocalization()).isNotNull();

        specificNullCheck();
    }

    protected void specificNullCheck() {
    }

    @Test(expected = NullArgumentException.class)
    public void findingByIdNull_throws() {
        repo.findById(null);
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

}
