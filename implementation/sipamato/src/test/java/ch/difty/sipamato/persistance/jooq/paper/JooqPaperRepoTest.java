package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepoTest;

public class JooqPaperRepoTest extends JooqEntityRepoTest<PaperRecord, Paper, Long, ch.difty.sipamato.db.tables.Paper, PaperRecordMapper, PaperFilter> {

    private static final Long SAMPLE_ID = 3l;

    private JooqPaperRepo repo;

    @Mock
    private Paper unpersistedEntity;
    @Mock
    private Paper persistedEntity;
    @Mock
    private PaperRecord persistedRecord;
    @Mock
    private PaperRecordMapper mapperMock;
    @Mock
    private PaperFilter filterMock;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqPaperRepo getRepo() {
        if (repo == null) {
            repo = new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
        }
        return repo;
    }

    @Override
    protected EntityRepository<Paper, Long, PaperFilter> makeRepoFindingEntityById(Paper paper) {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getJooqConfig()) {
            private static final long serialVersionUID = 1L;

            @Override
            public Paper findById(Long id) {
                return paper;
            }
        };
    }

    @Override
    protected Paper getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected Paper getPersistedEntity() {
        return persistedEntity;
    }

    @Override
    protected PaperRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Override
    protected PaperRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected Class<Paper> getEntityClass() {
        return Paper.class;
    }

    @Override
    protected Class<PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    @Override
    protected PaperFilter getFilter() {
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
            new JooqPaperRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dateTimeService must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("localization must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), null, getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), null, getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("jooqConfig must not be null.");
        }
    }

    @Test
    public void gettingByIds_withNullIdList_throwsNullArgumentException() {
        try {
            repo.findByIds(null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("ids must not be null.");
        }
    }

}
