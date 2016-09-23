package ch.difty.sipamato.persistance.jooq.repo;

import static ch.difty.sipamato.db.h2.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.fail;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertResultStep;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.SelectConditionStep;
import org.jooq.SelectWhereStep;
import org.jooq.TableField;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateResultStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.mapper.PaperRecordMapper;

@RunWith(MockitoJUnitRunner.class)
public class JooqPaperRepoTest {

    private JooqPaperRepo repo;

    private final List<Paper> entities = new ArrayList<>();

    @Mock
    private DSLContext dslMock;
    @Mock
    private PaperRecordMapper mapperMock;

    @Mock
    private Paper unpersistedEntity, persistedEntity;
    @Mock
    private PaperRecord unpersistedRecord, persistedRecord;

    @Mock
    private SelectWhereStep<PaperRecord> selectWhereStepMock;
    @Mock
    private SelectConditionStep<PaperRecord> selectConditionStepMock;

    @Mock
    private InsertSetStep<PaperRecord> insertSetStepMock;
    @Mock
    private InsertSetMoreStep<PaperRecord> insertSetMoreStepMock;
    @Mock
    private InsertResultStep<PaperRecord> insertResultStepMock;
    @Mock
    private InsertSetStepSetter<PaperRecord, Paper> insertSetStepSetterMock;
    @Mock
    private DeleteWhereStep<PaperRecord> deleteWhereStepMock;
    @Mock
    private DeleteConditionStep<PaperRecord> deleteConditionStepMock;
    @Mock
    private UpdateSetFirstStep<PaperRecord> updateSetFirstStepMock;
    //    @Mock
    //    private UpdateSetStep<PaperRecord> updateSetStepMock;
    @Mock
    private UpdateConditionStep<PaperRecord> updateConditionStepMock;
    @Mock
    private UpdateSetMoreStep<PaperRecord> updateSetMoreStepMock;
    @Mock
    private UpdateResultStep<PaperRecord> updateResultStepMock;
    @Mock
    private UpdateSetStepSetter<PaperRecord, Paper> updateSetStepSetterMock;
    //    @Mock
    //    private UpdateConditionStep<PaperRecord> updateConditionStepMock;
    //    @Mock
    //    private UpdateSetMoreStep<PaperRecord> updateSetMoreStepMock;
    //

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        repo = new JooqPaperRepo(dslMock, mapperMock, insertSetStepSetterMock, updateSetStepSetterMock);

        when(dslMock.selectFrom(PAPER)).thenReturn(selectWhereStepMock);
        when(selectWhereStepMock.fetchInto(Paper.class)).thenReturn(entities);

        entities.add(persistedEntity);
        entities.add(persistedEntity);

        when(dslMock.insertInto(PAPER)).thenReturn(insertSetStepMock);
        when(insertSetStepSetterMock.setNonKeyFieldsFor(insertSetStepMock, unpersistedEntity)).thenReturn(insertSetMoreStepMock);
        when(insertSetStepMock.set(isA(TableField.class), eq(unpersistedEntity))).thenReturn(insertSetMoreStepMock);
        when(insertSetMoreStepMock.returning()).thenReturn(insertResultStepMock);

        when(mapperMock.map(persistedRecord)).thenReturn(persistedEntity);

        when(dslMock.delete(PAPER)).thenReturn(deleteWhereStepMock);

        when(dslMock.update(PAPER)).thenReturn(updateSetFirstStepMock);
        when(updateSetStepSetterMock.setFieldsFor(updateSetFirstStepMock, unpersistedEntity)).thenReturn(updateSetMoreStepMock);
        when(updateConditionStepMock.returning()).thenReturn(updateResultStepMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dslMock, mapperMock);
        verifyNoMoreInteractions(unpersistedEntity, persistedEntity, unpersistedRecord, persistedRecord);
        verifyNoMoreInteractions(selectWhereStepMock, selectConditionStepMock);
        verifyNoMoreInteractions(insertSetStepMock, insertSetMoreStepMock, insertResultStepMock, insertSetStepSetterMock);
        verifyNoMoreInteractions(deleteWhereStepMock, deleteConditionStepMock);
        verifyNoMoreInteractions(updateSetFirstStepMock, updateConditionStepMock, updateSetMoreStepMock, updateResultStepMock, updateSetStepSetterMock);
    }

    @Test
    public void nullCheck() {
        assertThat(repo).isNotNull();
        assertThat(repo.getDslContext()).isNotNull().isEqualTo(dslMock);
        assertThat(repo.getMapper()).isNotNull().isEqualTo(mapperMock);
        assertThat(repo.getInsertSetStepSetter()).isNotNull().isEqualTo(insertSetStepSetterMock);
        assertThat(repo.getUpdateSetStepSetter()).isNotNull().isEqualTo(updateSetStepSetterMock);
    }

    @Test
    public void degenerateConstruction() {
        try {
            new JooqPaperRepo(null, mapperMock, insertSetStepSetterMock, updateSetStepSetterMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqPaperRepo(dslMock, null, insertSetStepSetterMock, updateSetStepSetterMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqPaperRepo(dslMock, mapperMock, null, updateSetStepSetterMock);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(dslMock, mapperMock, insertSetStepSetterMock, null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
    }

    @Test
    public void findingAll_returnsPersistedEntities() {
        assertThat(repo.findAll()).hasSize(2).containsExactly(persistedEntity, persistedEntity);

        verify(dslMock).selectFrom(PAPER);
        verify(selectWhereStepMock).fetchInto(Paper.class);
    }

    @Test(expected = NullArgumentException.class)
    public void findingByIdNull_throws() {
        repo.findById(null);
    }

    @Test
    public void findingById() {
        Long id = 1l;
        when(selectWhereStepMock.where(PAPER.ID.equal(id))).thenReturn(selectConditionStepMock);
        when(selectConditionStepMock.fetchOneInto(Paper.class)).thenReturn(persistedEntity);

        repo.findById(id.intValue());

        verify(dslMock).selectFrom(PAPER);
        verify(selectWhereStepMock).where(PAPER.ID.equal(id));
        verify(selectConditionStepMock).fetchOneInto(Paper.class);
    }

    @Test(expected = NullArgumentException.class)
    public void addingNull_throws() {
        repo.add(null);
    }

    @Test
    public void adding_pesistsPaperToDb_andReturnsAsEntity() {
        when(insertResultStepMock.fetchOne()).thenReturn(persistedRecord);
        when(persistedRecord.getId()).thenReturn(2l);

        assertThat(repo.add(unpersistedEntity)).isEqualTo(persistedEntity);

        verify(dslMock).insertInto(PAPER);
        verify(insertSetStepSetterMock).setNonKeyFieldsFor(insertSetStepMock, unpersistedEntity);
        verify(insertSetStepSetterMock).considerSettingKeyOf(insertSetMoreStepMock, unpersistedEntity);
        verify(insertSetMoreStepMock).returning();
        verify(insertResultStepMock).fetchOne();
        verify(persistedRecord).getId();
        verify(mapperMock).map(persistedRecord);
    }

    @Test
    public void adding_pesistsPaperToDb_andReturnsAsEntity1() {
        when(insertResultStepMock.fetchOne()).thenReturn(null);

        assertThat(repo.add(unpersistedEntity)).isNull();

        verify(dslMock).insertInto(PAPER);
        verify(insertSetStepSetterMock).setNonKeyFieldsFor(insertSetStepMock, unpersistedEntity);
        verify(insertSetStepSetterMock).considerSettingKeyOf(insertSetMoreStepMock, unpersistedEntity);
        verify(insertSetMoreStepMock).returning();
        verify(insertResultStepMock).fetchOne();
    }

    @Test(expected = NullArgumentException.class)
    public void deleting_withIdNull_throws() {
        repo.delete(null);
    }

    @Test
    public void deleting_withIdNotFoundInDb_returnsNull() {
        final Integer id = 1;
        repo = new JooqPaperRepo(dslMock, mapperMock, insertSetStepSetterMock, updateSetStepSetterMock) {
            @Override
            public Paper findById(Integer id) {
                return null;
            }
        };
        assertThat(repo.delete(id)).isNull();
    }

    @Test
    public void deleting_validPersistentEntity_returnsDeletedEntity() {
        final Integer id = 10;
        repo = new JooqPaperRepo(dslMock, mapperMock, insertSetStepSetterMock, updateSetStepSetterMock) {
            @Override
            public Paper findById(Integer id) {
                return persistedEntity;
            }
        };
        when(deleteWhereStepMock.where(PAPER.ID.equal(id.longValue()))).thenReturn(deleteConditionStepMock);
        when(deleteConditionStepMock.execute()).thenReturn(1);

        assertThat(repo.delete(id)).isEqualTo(persistedEntity);

        verify(dslMock).delete(PAPER);
        verify(deleteWhereStepMock).where(PAPER.ID.equal(id.longValue()));
        verify(deleteConditionStepMock).execute();
    }

    @Test
    public void deleting_validPersistentEntity_withFailingDelete_returnsDeletedEntity() {
        final Integer id = 10;
        repo = new JooqPaperRepo(dslMock, mapperMock, insertSetStepSetterMock, updateSetStepSetterMock) {
            @Override
            public Paper findById(Integer id) {
                return persistedEntity;
            }
        };
        when(deleteWhereStepMock.where(PAPER.ID.equal(id.longValue()))).thenReturn(deleteConditionStepMock);
        when(deleteConditionStepMock.execute()).thenReturn(0);

        assertThat(repo.delete(id)).isEqualTo(persistedEntity);

        verify(dslMock).delete(PAPER);
        verify(deleteWhereStepMock).where(PAPER.ID.equal(id.longValue()));
        verify(deleteConditionStepMock).execute();
    }

    @Test(expected = NullArgumentException.class)
    public void updating_withEntityNull_throws() {
        repo.update(null);
    }

    @Test
    public void updating_withEntityIdNull_throws() {
        when(unpersistedEntity.getId()).thenReturn(null);
        try {
            repo.update(unpersistedEntity);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("entity.id must not be null.");
        }
        verify(unpersistedEntity).getId();
    }

    @Test
    public void updating_withValidEntity_changesAndPersistsEntityToDb_andReturnsFetchedEntity() {
        Integer id = 20;
        when(unpersistedEntity.getId()).thenReturn(id);
        when(unpersistedEntity.getId()).thenReturn(id);
        when(updateSetMoreStepMock.where(PAPER.ID.equal(id.longValue()))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(persistedRecord);
        when(mapperMock.map(persistedRecord)).thenReturn(persistedEntity);

        assertThat(repo.update(unpersistedEntity)).isEqualTo(persistedEntity);

        verify(unpersistedEntity).getId();
        verify(dslMock).update(PAPER);
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, unpersistedEntity);
        verify(updateSetMoreStepMock).where(PAPER.ID.equal(id.longValue()));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();
        verify(mapperMock).map(persistedRecord);
    }

    @Test
    public void updating_withUnsuccessfulRetrievalAfterPersistingAttempt_returnsNull() {
        Integer id = 20;
        when(unpersistedEntity.getId()).thenReturn(id);
        when(unpersistedEntity.getId()).thenReturn(id);
        when(updateSetMoreStepMock.where(PAPER.ID.equal(id.longValue()))).thenReturn(updateConditionStepMock);
        when(updateResultStepMock.fetchOne()).thenReturn(null);

        assertThat(repo.update(unpersistedEntity)).isNull();

        verify(unpersistedEntity).getId();
        verify(dslMock).update(PAPER);
        verify(updateSetStepSetterMock).setFieldsFor(updateSetFirstStepMock, unpersistedEntity);
        verify(updateSetMoreStepMock).where(PAPER.ID.equal(id.longValue()));
        verify(updateConditionStepMock).returning();
        verify(updateResultStepMock).fetchOne();
    }

}
