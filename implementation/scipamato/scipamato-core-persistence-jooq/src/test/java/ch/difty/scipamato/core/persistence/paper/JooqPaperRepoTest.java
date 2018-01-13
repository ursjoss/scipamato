package ch.difty.scipamato.core.persistence.paper;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.DeleteConditionStep;
import org.jooq.SelectConditionStep;
import org.jooq.SelectForUpdateStep;
import org.jooq.SelectSeekStepN;
import org.jooq.SelectWhereStep;
import org.jooq.SelectWithTiesStep;
import org.jooq.SortField;
import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.filter.PaperFilter;
import ch.difty.scipamato.core.persistence.EntityRepository;
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest;
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperBackedSearchOrderRepository;

public class JooqPaperRepoTest extends
        JooqEntityRepoTest<PaperRecord, Paper, Long, ch.difty.scipamato.core.db.tables.Paper, PaperRecordMapper, PaperFilter> {

    private static final Long   SAMPLE_ID = 3l;
    private static final String LC        = "de";

    private JooqPaperRepo repo;

    @Mock
    private Paper                            unpersistedEntity;
    @Mock
    private Paper                            persistedEntity;
    @Mock
    private PaperRecord                      persistedRecord;
    @Mock
    private PaperRecordMapper                mapperMock;
    @Mock
    private PaperFilter                      filterMock;
    @Mock
    private PaperBackedSearchOrderRepository searchOrderRepositoryMock;
    @Mock
    private SearchOrder                      searchOrderMock;
    @Mock
    private Paper                            paperMock;
    @Mock
    private PaginationContext                paginationContextMock;
    @Mock
    private DeleteConditionStep<PaperRecord> deleteConditionStepMock;
    @Mock
    private PaperAttachment                  paperAttachmentMock;

    private final List<Paper> papers = new ArrayList<>();

    private final List<Paper> enrichtedEntities = new ArrayList<>();

    @Override
    protected void testSpecificSetUp() {
        papers.add(paperMock);
        papers.add(paperMock);

        when(unpersistedEntity.getVersion()).thenReturn(0);
        when(persistedEntity.getVersion()).thenReturn(0);
        when(persistedRecord.getVersion()).thenReturn(0);
    }

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqPaperRepo getRepo() {
        if (repo == null) {
            repo = new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                    getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                    getApplicationProperties());
        }
        return repo;
    }

    @Override
    protected EntityRepository<Paper, Long, PaperFilter> makeRepoFindingEntityById(Paper paper) {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()) {
            private static final long serialVersionUID = 1L;

            @Override
            public Paper findById(Long id, int version) {
                return paper;
            }
        };
    }

    protected PaperRepository makeRepoStubbingEnriching() {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void enrichAssociatedEntitiesOf(Paper entity, String language) {
                enrichtedEntities.add(entity);
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
    protected ch.difty.scipamato.core.db.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    @Override
    protected TableField<PaperRecord, Integer> getRecordVersion() {
        return PAPER.VERSION;
    }

    @Override
    protected PaperFilter getFilter() {
        return filterMock;
    }

    @Override
    protected void expectEntityIdsWithValues() {
        when(unpersistedEntity.getId()).thenReturn(SAMPLE_ID);
        when(unpersistedEntity.getVersion()).thenReturn(0);
        when(persistedRecord.getId()).thenReturn(SAMPLE_ID);
        when(persistedRecord.getVersion()).thenReturn(1);
    }

    @Override
    protected void expectUnpersistedEntityIdNull() {
        when(unpersistedEntity.getId()).thenReturn(null);
        when(unpersistedEntity.getVersion()).thenReturn(0);
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
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(null, getMapper(), getSortMapper(),
                getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                searchOrderRepositoryMock, getApplicationProperties()),
            "dsl");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), null, getSortMapper(),
                getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                searchOrderRepositoryMock, getApplicationProperties()),
            "mapper");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), getMapper(), null,
                getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                searchOrderRepositoryMock, getApplicationProperties()),
            "sortMapper");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), null,
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()),
            "filterConditionMapper");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(),
                getFilterConditionMapper(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(),
                searchOrderRepositoryMock, getApplicationProperties()),
            "dateTimeService");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(),
                getFilterConditionMapper(), getDateTimeService(), null, getUpdateSetStepSetter(),
                searchOrderRepositoryMock, getApplicationProperties()),
            "insertSetStepSetter");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(),
                getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), null,
                searchOrderRepositoryMock, getApplicationProperties()),
            "updateSetStepSetter");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(),
                getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                null, getApplicationProperties()),
            "searchOrderRepository");
        assertDegenerateSupplierParameter(() -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(),
                getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                searchOrderRepositoryMock, null),
            "applicationProperties");
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
    public void gettingRecordVersion() {
        assertThat(repo.getRecordVersion()).isEqualTo(PAPER.VERSION);
    }

    @Test
    public void gettingIdFromPaper() {
        when(paperMock.getId()).thenReturn(17l);
        assertThat(repo.getIdFrom(paperMock)).isEqualTo(17l);
        verify(paperMock).getId();
    }

    @Test
    public void gettingIdFromPaperRecord() {
        when(persistedRecord.getId()).thenReturn(17l);
        assertThat(repo.getIdFrom(persistedRecord)).isEqualTo(17l);
        verify(persistedRecord).getId();
    }

    @Test
    public void gettingByIds_withNullIdList_throwsNullArgumentException() {
        try {
            repo.findByIds(null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class)
                .hasMessage("ids must not be null.");
        }
    }

    @Test
    public void findingBySearchOrder_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.findBySearchOrder(searchOrderMock)).thenReturn(papers);
        assertThat(makeRepoStubbingEnriching().findBySearchOrder(searchOrderMock, LC)).containsExactly(paperMock,
            paperMock);
        assertThat(enrichtedEntities).containsExactly(paperMock, paperMock);
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
        when(searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, paginationContextMock))
            .thenReturn(papers);
        assertThat(makeRepoStubbingEnriching().findPageBySearchOrder(searchOrderMock, paginationContextMock, LC))
            .containsExactly(paperMock, paperMock);
        assertThat(enrichtedEntities).containsExactly(paperMock, paperMock);
        verify(searchOrderRepositoryMock).findPageBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    public void gettingPapersByPmIds_withNoPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(new ArrayList<Integer>(), LC)).isEmpty();
    }

    @Test
    public void findingByPmIds_withNullPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(null, LC)).isEmpty();
    }

    @Test
    public void findingByNumbers_withNoNumbers_returnsEmptyList() {
        assertThat(repo.findByNumbers(new ArrayList<Long>(), LC)).isEmpty();
    }

    @Test
    public void findingByNumbers_withNullNumbers_returnsEmptyList() {
        assertThat(repo.findByNumbers(null, LC)).isEmpty();
    }

    @Test
    public void findingByNumber_withNoLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> repo.findByNumbers(Arrays.asList(1l), null), "languageCode");
    }

    @Test
    public void findingExistingPmIdsOutOf_withNoPmIds_returnsEmptyList() {
        assertThat(repo.findExistingPmIdsOutOf(new ArrayList<Integer>())).isEmpty();
    }

    @Test
    public void findingExistingPmIdsOutOf_withNullPmIds_returnsEmptyList() {
        assertThat(repo.findExistingPmIdsOutOf(null)).isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findingPageByFilter() {
        // TODO actually return records, test mapping and enrichment also
        List<PaperRecord> paperRecords = new ArrayList<>();
        Collection<SortField<Paper>> sortFields = new ArrayList<>();
        Sort sort = new Sort(Direction.DESC, "id");
        when(paginationContextMock.getSort()).thenReturn(sort);
        when(paginationContextMock.getPageSize()).thenReturn(20);
        when(paginationContextMock.getOffset()).thenReturn(0);
        when(getFilterConditionMapper().map(filterMock)).thenReturn(getConditionMock());
        when(getSortMapper().map(sort, getTable())).thenReturn(sortFields);

        SelectWhereStep<PaperRecord> selectWhereStepMock = mock(SelectWhereStep.class);
        when(getDsl().selectFrom(getTable())).thenReturn(selectWhereStepMock);
        SelectConditionStep<PaperRecord> selectConditionStepMock = mock(SelectConditionStep.class);
        when(selectWhereStepMock.where(getConditionMock())).thenReturn(selectConditionStepMock);
        SelectSeekStepN<PaperRecord> selectSeekStepNMock = mock(SelectSeekStepN.class);
        when(selectConditionStepMock.orderBy(sortFields)).thenReturn(selectSeekStepNMock);
        SelectWithTiesStep<PaperRecord> selectWithTiesStepMock = mock(SelectWithTiesStep.class);
        when(selectSeekStepNMock.limit(20)).thenReturn(selectWithTiesStepMock);
        SelectForUpdateStep<PaperRecord> selectForUpdateStepMock = mock(SelectForUpdateStep.class);
        when(selectWithTiesStepMock.offset(0)).thenReturn(selectForUpdateStepMock);
        when(selectForUpdateStepMock.fetchInto(getRecordClass())).thenReturn(paperRecords);

        when(getMapper().map(persistedRecord)).thenReturn(paperMock);

        final List<Paper> papers = repo.findPageByFilter(filterMock, paginationContextMock, LC);
        assertThat(papers).isEmpty();

        verify(getFilterConditionMapper()).map(filterMock);
        verify(paginationContextMock).getSort();
        verify(paginationContextMock).getPageSize();
        verify(paginationContextMock).getOffset();
        verify(getSortMapper()).map(sort, getTable());

        verify(getDsl()).selectFrom(getTable());
        verify(selectWhereStepMock).where(getConditionMock());
        verify(selectConditionStepMock).orderBy(sortFields);
        verify(selectSeekStepNMock).limit(20);
        verify(selectWithTiesStepMock).offset(0);
        verify(selectForUpdateStepMock).fetchInto(getRecordClass());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findingPageByFilter_withNoExplicitLanguageCode() {
        // TODO actually return records, test mapping and enrichment also
        when(getApplicationProperties().getDefaultLocalization()).thenReturn(LC);

        List<PaperRecord> paperRecords = new ArrayList<>();
        Collection<SortField<Paper>> sortFields = new ArrayList<>();
        Sort sort = new Sort(Direction.DESC, "id");
        when(paginationContextMock.getSort()).thenReturn(sort);
        when(paginationContextMock.getPageSize()).thenReturn(20);
        when(paginationContextMock.getOffset()).thenReturn(0);
        when(getFilterConditionMapper().map(filterMock)).thenReturn(getConditionMock());
        when(getSortMapper().map(sort, getTable())).thenReturn(sortFields);

        SelectWhereStep<PaperRecord> selectWhereStepMock = mock(SelectWhereStep.class);
        when(getDsl().selectFrom(getTable())).thenReturn(selectWhereStepMock);
        SelectConditionStep<PaperRecord> selectConditionStepMock = mock(SelectConditionStep.class);
        when(selectWhereStepMock.where(getConditionMock())).thenReturn(selectConditionStepMock);
        SelectSeekStepN<PaperRecord> selectSeekStepNMock = mock(SelectSeekStepN.class);
        when(selectConditionStepMock.orderBy(sortFields)).thenReturn(selectSeekStepNMock);
        SelectWithTiesStep<PaperRecord> selectWithTiesStepMock = mock(SelectWithTiesStep.class);
        when(selectSeekStepNMock.limit(20)).thenReturn(selectWithTiesStepMock);
        SelectForUpdateStep<PaperRecord> selectForUpdateStepMock = mock(SelectForUpdateStep.class);
        when(selectWithTiesStepMock.offset(0)).thenReturn(selectForUpdateStepMock);
        when(selectForUpdateStepMock.fetchInto(getRecordClass())).thenReturn(paperRecords);

        when(getMapper().map(persistedRecord)).thenReturn(paperMock);

        final List<Paper> papers = repo.findPageByFilter(filterMock, paginationContextMock);
        assertThat(papers).isEmpty();

        verify(getApplicationProperties()).getDefaultLocalization();
        verify(getFilterConditionMapper()).map(filterMock);
        verify(paginationContextMock).getSort();
        verify(paginationContextMock).getPageSize();
        verify(paginationContextMock).getOffset();
        verify(getSortMapper()).map(sort, getTable());

        verify(getDsl()).selectFrom(getTable());
        verify(selectWhereStepMock).where(getConditionMock());
        verify(selectConditionStepMock).orderBy(sortFields);
        verify(selectSeekStepNMock).limit(20);
        verify(selectWithTiesStepMock).offset(0);
        verify(selectForUpdateStepMock).fetchInto(getRecordClass());
    }

    @Test
    public void findingPageOfIdsBySearchOrder() {
        when(searchOrderRepositoryMock.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock))
            .thenReturn(Arrays.asList(17l, 3l, 5l));
        assertThat(repo.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).containsExactly(17l, 3l,
            5l);
        verify(searchOrderRepositoryMock).findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    public void deleteîngAttachment_withNullId_returnsNull() {
        assertThat(getRepo().deleteAttachment(null)).isNull();
    }

    @Test
    public void deletingIds() {
        List<Long> ids = Arrays.asList(3l, 5l, 7l);
        when(getDsl().deleteFrom(getTable())).thenReturn(deleteWhereStepMock);
        when(deleteWhereStepMock.where(PAPER.ID.in(ids))).thenReturn(deleteConditionStepMock);

        repo.delete(ids);

        verify(getDsl()).deleteFrom(getTable());
        verify(deleteWhereStepMock).where(PAPER.ID.in(ids));
        verify(deleteConditionStepMock).execute();
    }

    @Test
    public void enrichingAssociatedEntitiesOf_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null, "de");
    }

    @Test
    public void enrichingAssociatedEntitiesOf_withNullLanguageCode_withNullPaperId_doesNotCallRepo() {
        when(paperMock.getId()).thenReturn(null);
        repo = makeRepoStubbingAttachmentEnriching();
        repo.enrichAssociatedEntitiesOf(paperMock, null);
        verify(paperMock).getId();
        verify(paperMock, never()).setAttachments(Mockito.anyList());
    }

    @Test
    public void enrichingAssociatedEntitiesOf_withNullLanguageCode_withPaperWithId_enrichesAttachments() {
        when(paperMock.getId()).thenReturn(17l);
        repo = makeRepoStubbingAttachmentEnriching();
        repo.enrichAssociatedEntitiesOf(paperMock, null);
        verify(paperMock).getId();
        verify(paperMock).setAttachments(Arrays.asList(paperAttachmentMock));
    }

    protected JooqPaperRepo makeRepoStubbingAttachmentEnriching() {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()) {
            private static final long serialVersionUID = 1L;

            @Override
            public List<PaperAttachment> loadSlimAttachment(long paperId) {
                return Arrays.asList(paperAttachmentMock);
            }
        };
    }

}
