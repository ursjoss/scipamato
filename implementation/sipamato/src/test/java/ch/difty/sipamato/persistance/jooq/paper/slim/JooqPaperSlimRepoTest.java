package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepoTest;
import ch.difty.sipamato.persistance.jooq.Page;
import ch.difty.sipamato.persistance.jooq.Pageable;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.PaperSlimBackedSearchOrderRepository;

public class JooqPaperSlimRepoTest extends JooqReadOnlyRepoTest<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> {

    private static final Long SAMPLE_ID = 3l;

    private JooqPaperSlimRepo repo;

    @Mock
    private PaperSlim unpersistedEntity;
    @Mock
    private PaperSlim persistedEntity;
    @Mock
    private PaperRecord persistedRecord;
    @Mock
    private PaperSlimRecordMapper mapperMock;
    @Mock
    private PaperFilter filterMock;
    @Mock
    private PaperSlimBackedSearchOrderRepository searchOrderRepositoryMock;
    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private PaperSlim paperSlimMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<PaperSlim> pageMock;

    private final List<PaperSlim> paperSlims = new ArrayList<>();

    @Override
    protected void specificSetUp() {
        paperSlims.add(paperSlimMock);
        paperSlims.add(paperSlimMock);
    }

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected ReadOnlyRepository<PaperSlim, Long, PaperFilter> getRepo() {
        if (repo == null) {
            repo = new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), searchOrderRepositoryMock);
        }
        return repo;
    }

    @Override
    protected ReadOnlyRepository<PaperSlim, Long, PaperFilter> makeRepoFindingEntityById(PaperSlim entity) {
        return new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization(), searchOrderRepositoryMock) {
            private static final long serialVersionUID = 1L;

            @Override
            public PaperSlim findById(Long id) {
                return entity;
            }
        };
    }

    @Override
    protected PaperSlim getPersistedEntity() {
        return persistedEntity;
    }

    @Override
    protected PaperSlim getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected PaperRecord getPersistedRecord() {
        return persistedRecord;
    }

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

    @Override
    protected PaperFilter getFilter() {
        return filterMock;
    }

    @Test
    public void gettingEntityClass() {
        assertThat(repo.getEntityClass()).isEqualTo(getEntityClass());
    }

    @Test
    public void gettingRecordClass() {
        assertThat(repo.getRecordClass()).isEqualTo(getRecordClass());
    }

    @Test
    public void gettingTableId() {
        assertThat(repo.getTableId()).isEqualTo(getTableId());
    }

    @Test
    public void findingBySearchOrder_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.findBySearchOrder(searchOrderMock)).thenReturn(paperSlims);
        assertThat(repo.findBySearchOrder(searchOrderMock)).containsExactly(paperSlimMock, paperSlimMock);
        verify(searchOrderRepositoryMock).findBySearchOrder(searchOrderMock);
    }

    @Test
    public void countingBySearchOrder_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        assertThat(repo.countBySearchOrder(searchOrderMock)).isEqualTo(2);
        verify(searchOrderRepositoryMock).countBySearchOrder(searchOrderMock);
    }

    @Test
    public void findingBySearchOrder_withPageable_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.findPagedBySearchOrder(searchOrderMock, pageableMock)).thenReturn(pageMock);
        when(searchOrderRepositoryMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        when(pageMock.getContent()).thenReturn(paperSlims);

        assertThat(repo.findBySearchOrder(searchOrderMock, pageableMock).getContent()).containsExactly(paperSlimMock, paperSlimMock);

        verify(searchOrderRepositoryMock).findPagedBySearchOrder(searchOrderMock, pageableMock);
        verify(searchOrderRepositoryMock).countBySearchOrder(searchOrderMock);
        verify(pageMock).getContent();
    }

}
