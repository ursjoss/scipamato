package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepoTest;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

public class JooqPaperSlimRepoTest extends JooqReadOnlyRepoTest<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> {

    private static final Long SAMPLE_ID = 3l;

    private JooqPaperSlimRepo repo;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, PaperFilter> getRepo() {
        if (repo == null) {
            repo = new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization());
        }
        return repo;
    }

    @Override
    protected ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, PaperFilter> makeRepoFindingEntityById(PaperSlim entity) {
        return new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization()) {
            private static final long serialVersionUID = 1L;

            @Override
            public PaperSlim findById(Long id) {
                return entity;
            }
        };
    }

    @Mock
    private PaperSlim unpersistedEntity, persistedEntity;

    @Override
    protected PaperSlim getPersistedEntity() {
        return persistedEntity;
    }

    @Override
    protected PaperSlim getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Mock
    private PaperRecord persistedRecord;

    @Override
    protected PaperRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Mock
    private PaperSlimRecordMapper mapperMock;

    @Override
    protected PaperSlimRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected Class<PaperSlim> getEntityClass() {
        return PaperSlim.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected Class<PaperRecord> getRecordClass() {
        return PaperRecord.class;
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

    @Mock
    private PaperFilter filterMock;

    @Override
    protected PaperFilter getFilter() {
        return filterMock;
    }

    @Test
    public void findingBySearchOrder_withNullSearchOrder_throws() {
        try {
            repo.findBySearchOrder((SearchOrder) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder must not be null.");
        }
    }

    @Test
    public void countingBySearchOrder_withNullSearchOrder_throws() {
        try {
            repo.countBySearchOrder((SearchOrder) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder must not be null.");
        }
    }
}
