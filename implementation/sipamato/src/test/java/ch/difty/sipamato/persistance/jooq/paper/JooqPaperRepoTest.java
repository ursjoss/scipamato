package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.h2.tables.Paper.PAPER;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.GenericRepository;
import ch.difty.sipamato.persistance.jooq.JooqRepoTest;

public class JooqPaperRepoTest extends JooqRepoTest<PaperRecord, Paper, Long, ch.difty.sipamato.db.h2.tables.Paper, PaperRecordMapper> {

    private static final Long SAMPLE_ID = 3l;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    private JooqPaperRepo repo;

    @Override
    protected JooqPaperRepo getRepo() {
        if (repo == null) {
            repo = new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter());
        }
        return repo;
    }

    @Override
    protected GenericRepository<PaperRecord, Paper, Long, PaperRecordMapper> makeRepoFindingEntityById(Paper paper) {
        return new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter()) {
            private static final long serialVersionUID = 1L;

            @Override
            public Paper findById(Long id) {
                return paper;
            }
        };
    }

    @Mock
    private Paper unpersistedEntity, persistedEntity;

    @Override
    protected Paper getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected Paper getPersistedEntity() {
        return persistedEntity;
    }

    @Mock
    private PaperRecord persistedRecord;

    @Override
    protected PaperRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Mock
    private PaperRecordMapper mapperMock;

    @Override
    protected PaperRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected Class<Paper> getEntityClass() {
        return Paper.class;
    }

    @Override
    protected ch.difty.sipamato.db.h2.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
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
            new JooqPaperRepo(null, getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), null, getInsertSetStepSetter(), getUpdateSetStepSetter());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), null, getUpdateSetStepSetter());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
    }

}
