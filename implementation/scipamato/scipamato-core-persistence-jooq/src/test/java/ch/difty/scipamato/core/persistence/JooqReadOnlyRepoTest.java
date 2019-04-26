package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.*;
import org.jooq.impl.TableImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("WeakerAccess")
public abstract class JooqReadOnlyRepoTest<R extends Record, T extends IdScipamatoEntity<ID>, ID extends Number, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends ScipamatoFilter> {

    private ReadOnlyRepository<T, ID, F> repo;

    private final List<T> entities = new ArrayList<>();
    private final List<R> records  = new ArrayList<>();

    @Mock
    private   DSLContext                         dslMock;
    @Mock
    protected GenericFilterConditionMapper<F>    filterConditionMapperMock;
    @Mock
    private   R                                  unpersistedRecord;
    @Mock
    private   JooqSortMapper<R, T, TI>           sortMapperMock;
    @Mock
    private   SelectWhereStep<R>                 selectWhereStepMock;
    @Mock
    private   SelectConditionStep<R>             selectConditionStepMock;
    @Mock
    private   SelectSelectStep<Record1<Integer>> selectSelectStepMock;
    @Mock
    private   SelectJoinStep<Record1<Integer>>   selectJoinStepMock;
    @Mock
    private SelectConditionStep<Record1<Integer>> selectConditionStepMock2;

    @Mock
    private PaginationContext        paginationContextMock;
    @Mock
    private Sort                     sortMock;
    @Mock
    private Collection<SortField<T>> sortFieldsMock;
    @Mock
    private SelectSeekStepN<R>       selectSeekStepNMock;

    @Mock
    Condition conditionMock;

    @Mock
    private ApplicationProperties applicationPropertiesMock;

    final F filterMock = getFilter();

    protected DSLContext getDsl() {
        return dslMock;
    }

    protected GenericFilterConditionMapper<F> getFilterConditionMapper() {
        return filterConditionMapperMock;
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

    protected Condition getConditionMock() {
        return conditionMock;
    }

    /**
     * @return the specific repository instantiated
     */
    protected abstract ReadOnlyRepository<T, ID, F> getRepo();

    /**
     * Hand-rolled spy that returns the provided entity in the method
     * {@code findById(ID id)}
     *
     * @param entity
     *     the entity to be found.
     * @return the entity
     */
    protected abstract ReadOnlyRepository<T, ID, F> makeRepoFindingEntityById(T entity);

    protected abstract T getPersistedEntity();

    protected abstract T getUnpersistedEntity();

    protected abstract R getPersistedRecord();

    protected abstract M getMapper();

    protected abstract TI getTable();

    protected abstract TableField<R, ID> getTableId();

    protected abstract void expectEntityIdsWithValues();

    protected abstract void expectUnpersistedEntityIdNull();

    protected abstract void verifyUnpersistedEntityId();

    protected abstract void verifyPersistedRecordId();

    protected abstract F getFilter();

    protected ApplicationProperties getApplicationProperties() {
        return applicationPropertiesMock;
    }

    @BeforeEach
    public final void setUp() {
        repo = getRepo();

        entities.add(getPersistedEntity());
        entities.add(getPersistedEntity());

        records.add(getPersistedRecord());
        records.add(getPersistedRecord());

        specificSetUp();
    }

    protected void specificSetUp() {
    }

    @AfterEach
    public final void tearDown() {
        specificTearDown();
        verifyNoMoreInteractions(dslMock, getMapper(), sortMapperMock);
        verifyNoMoreInteractions(getUnpersistedEntity(), getPersistedEntity(), unpersistedRecord, getPersistedRecord());
        verifyNoMoreInteractions(selectWhereStepMock, selectConditionStepMock);
        verifyNoMoreInteractions(selectSelectStepMock, selectJoinStepMock);
        verifyNoMoreInteractions(paginationContextMock, sortMock, sortFieldsMock, selectSeekStepNMock);
        verifyNoMoreInteractions(getFilter(), conditionMock);
        verifyNoMoreInteractions(getApplicationProperties());
    }

    protected void specificTearDown() {
    }

    @Test
    public final void nullCheck() {
        assertThat(getRepo()).isNotNull();
        assertThat(getDsl()).isNotNull();
        assertThat(getMapper()).isNotNull();
        assertThat(getSortMapper()).isNotNull();
        assertThat(getApplicationProperties()).isNotNull();

        specificNullCheck();
    }

    protected void specificNullCheck() {
    }

    @Test
    public void findingByIdNull_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> repo.findById(null, "en"));
    }

    @Test
    public void countingByFilter() {
        when(filterConditionMapperMock.map(filterMock)).thenReturn(conditionMock);
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
