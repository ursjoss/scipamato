package ch.difty.scipamato.core.persistence.paper.slim;

import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jooq.TableField;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.JooqReadOnlyRepoTest;
import ch.difty.scipamato.core.persistence.ReadOnlyRepository;
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperSlimBackedSearchOrderRepository;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class JooqPaperSlimRepoTest extends
    JooqReadOnlyRepoTest<PaperRecord, PaperSlim, Long, ch.difty.scipamato.core.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> {

    private static final Long SAMPLE_ID = 3L;

    private JooqPaperSlimRepo repo;

    @Mock
    private PaperSlim                            unpersistedEntity;
    @Mock
    private PaperSlim                            persistedEntity;
    @Mock
    private PaperRecord                          persistedRecord;
    @Mock
    private PaperSlimRecordMapper                mapperMock;
    @Mock
    private PaperFilter                          filterMock;
    @Mock
    private PaperSlimBackedSearchOrderRepository searchOrderRepositoryMock;
    @Mock
    private SearchOrder                          searchOrderMock;
    @Mock
    private PaperSlim                            paperSlimMock;
    @Mock
    private PaginationContext                    pageableMock;
    @Mock
    private DateTimeService                      dateTimeServiceMock;

    private final List<PaperSlim> paperSlims = new ArrayList<>();

    protected void specificSetUp() {
        paperSlims.add(paperSlimMock);
        paperSlims.add(paperSlimMock);
    }

    @Override
    protected ReadOnlyRepository<PaperSlim, Long, PaperFilter> getRepo() {
        if (repo == null) {
            repo = new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                searchOrderRepositoryMock, dateTimeServiceMock, getApplicationProperties());
        }
        return repo;
    }

    @Override
    protected ReadOnlyRepository<PaperSlim, Long, PaperFilter> makeRepoFindingEntityById(PaperSlim entity) {
        return new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            searchOrderRepositoryMock, dateTimeServiceMock, getApplicationProperties()) {
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
    protected ch.difty.scipamato.core.db.tables.Paper getTable() {
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

    @Override
    protected PaperFilter getFilter() {
        return filterMock;
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
    public void findingPageBySearchOrder_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, pageableMock)).thenReturn(paperSlims);
        assertThat(repo.findPageBySearchOrder(searchOrderMock, pageableMock)).containsExactly(paperSlimMock,
            paperSlimMock);
        verify(searchOrderRepositoryMock).findPageBySearchOrder(searchOrderMock, pageableMock);
    }

    @Test
    public void gettingVersion_returnsPapersVersion() {
        assertThat(repo.getRecordVersion()).isEqualTo(PAPER.VERSION);
    }

    @Test
    public void findingById_withVersion_withNullId() {
        TestUtils.assertDegenerateSupplierParameter(() -> repo.findById(null, 1, "de"), "id");
    }
}
