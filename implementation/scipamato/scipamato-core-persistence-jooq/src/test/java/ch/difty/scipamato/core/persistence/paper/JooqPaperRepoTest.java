package ch.difty.scipamato.core.persistence.paper;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.jooq.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.EntityRepository;
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest;
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperBackedSearchOrderRepository;

@SuppressWarnings({ "WeakerAccess", "ResultOfMethodCallIgnored" })
public class JooqPaperRepoTest extends
    JooqEntityRepoTest<PaperRecord, Paper, Long, ch.difty.scipamato.core.db.tables.Paper, PaperRecordMapper, PaperFilter> {

    private static final Long   SAMPLE_ID = 3L;
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

    private final List<Paper> enrichedEntities = new ArrayList<>();

    @Override
    protected void testSpecificSetUp() {
        papers.add(paperMock);
        papers.add(paperMock);

        when(unpersistedEntity.getVersion()).thenReturn(0);
        when(persistedEntity.getVersion()).thenReturn(0);
        when(persistedRecord.getVersion()).thenReturn(0);
    }

    @Override
    protected EntityRepository<Paper, Long, PaperFilter> makeRepoSavingReturning(final PaperRecord returning) {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
            getApplicationProperties()) {
            @Override
            protected PaperRecord doSave(final Paper entity, final String languageCode) {
                return returning;
            }
        };
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

            @Override
            protected void enrichAssociatedEntitiesOf(Paper entity, String language) {
                enrichedEntities.add(entity);
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
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()), "dsl");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()), "mapper");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()), "sortMapper");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()), "filterConditionMapper");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null,
                getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()), "dateTimeService");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), null, getUpdateSetStepSetter(), searchOrderRepositoryMock,
                getApplicationProperties()), "insertSetStepSetter");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), null, searchOrderRepositoryMock,
                getApplicationProperties()), "updateSetStepSetter");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null,
                getApplicationProperties()), "searchOrderRepository");
        assertDegenerateSupplierParameter(
            () -> new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
                null), "applicationProperties");
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
        when(paperMock.getId()).thenReturn(17L);
        assertThat(repo.getIdFrom(paperMock)).isEqualTo(17L);
        verify(paperMock).getId();
    }

    @Test
    public void gettingIdFromPaperRecord() {
        when(persistedRecord.getId()).thenReturn(17L);
        assertThat(repo.getIdFrom(persistedRecord)).isEqualTo(17L);
        verify(persistedRecord).getId();
    }

    @Test
    public void gettingByIds_withNullIdList_throwsNullArgumentException() {
        try {
            repo.findByIds(null);
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("ids must not be null.");
        }
    }

    @Test
    public void findingBySearchOrder_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.findBySearchOrder(searchOrderMock)).thenReturn(papers);
        assertThat(makeRepoStubbingEnriching().findBySearchOrder(searchOrderMock, LC)).containsExactly(paperMock,
            paperMock);
        assertThat(enrichedEntities).containsExactly(paperMock, paperMock);
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
        when(searchOrderRepositoryMock.findPageBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(
            papers);
        assertThat(makeRepoStubbingEnriching().findPageBySearchOrder(searchOrderMock, paginationContextMock,
            LC)).containsExactly(paperMock, paperMock);
        assertThat(enrichedEntities).containsExactly(paperMock, paperMock);
        verify(searchOrderRepositoryMock).findPageBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    public void gettingPapersByPmIds_withNoPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(new ArrayList<>(), LC)).isEmpty();
    }

    @Test
    public void findingByPmIds_withNullPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(null, LC)).isEmpty();
    }

    @Test
    public void findingByNumbers_withNoNumbers_returnsEmptyList() {
        assertThat(repo.findByNumbers(new ArrayList<>(), LC)).isEmpty();
    }

    @Test
    public void findingByNumbers_withNullNumbers_returnsEmptyList() {
        assertThat(repo.findByNumbers(null, LC)).isEmpty();
    }

    @Test
    public void findingByNumber_withNoLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> repo.findByNumbers(Collections.singletonList(1L), null),
            "languageCode");
    }

    @Test
    public void findingExistingPmIdsOutOf_withNoPmIds_returnsEmptyList() {
        assertThat(repo.findExistingPmIdsOutOf(new ArrayList<>())).isEmpty();
    }

    @Test
    public void findingExistingPmIdsOutOf_withNullPmIds_returnsEmptyList() {
        assertThat(repo.findExistingPmIdsOutOf(null)).isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findingPageByFilter() {
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
        SelectLimitPercentStep<PaperRecord> selectLimitPercentStepMock = mock(SelectLimitPercentStep.class);
        when(selectSeekStepNMock.limit(20)).thenReturn(selectLimitPercentStepMock);
        SelectForUpdateStep<PaperRecord> selectForUpdateStepMock = mock(SelectForUpdateStep.class);
        when(selectLimitPercentStepMock.offset(0)).thenReturn(selectForUpdateStepMock);
        // don't want to go into the enrichment test fixture, thus returning empty list
        when(selectForUpdateStepMock.fetch(getMapper())).thenReturn(Collections.emptyList());

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
        verify(selectLimitPercentStepMock).offset(0);
        verify(selectForUpdateStepMock).fetch(getMapper());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findingPageByFilter_withNoExplicitLanguageCode() {
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
        SelectLimitPercentStep<PaperRecord> selectLimitPercentStepMock = mock(SelectLimitPercentStep.class);
        when(selectSeekStepNMock.limit(20)).thenReturn(selectLimitPercentStepMock);
        SelectForUpdateStep<PaperRecord> selectForUpdateStepMock = mock(SelectForUpdateStep.class);
        when(selectLimitPercentStepMock.offset(0)).thenReturn(selectForUpdateStepMock);
        // don't want to go into the enrichment test fixture, thus returning empty list
        when(selectForUpdateStepMock.fetch(getMapper())).thenReturn(Collections.emptyList());

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
        verify(selectLimitPercentStepMock).offset(0);
        verify(selectForUpdateStepMock).fetch(getMapper());
    }

    @Test
    public void findingPageOfIdsBySearchOrder() {
        when(searchOrderRepositoryMock.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(
            Arrays.asList(17L, 3L, 5L));
        assertThat(repo.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).containsExactly(17L, 3L,
            5L);
        verify(searchOrderRepositoryMock).findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    public void deletingAttachment_withNullId_returnsNull() {
        assertThat(getRepo().deleteAttachment(null)).isNull();
    }

    @Test
    public void deletingIds() {
        List<Long> ids = Arrays.asList(3L, 5L, 7L);
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
        when(paperMock.getId()).thenReturn(17L);
        repo = makeRepoStubbingAttachmentEnriching();
        repo.enrichAssociatedEntitiesOf(paperMock, null);
        verify(paperMock).getId();
        verify(paperMock).setAttachments(Collections.singletonList(paperAttachmentMock));
    }

    protected JooqPaperRepo makeRepoStubbingAttachmentEnriching() {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), searchOrderRepositoryMock,
            getApplicationProperties()) {

            @Override
            public List<PaperAttachment> loadSlimAttachment(long paperId) {
                return Collections.singletonList(paperAttachmentMock);
            }
        };
    }

    @Test
    public void isDoiAlreadyAssigned_withNullDoi_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> repo.isDoiAlreadyAssigned(null, 1L), "doi");
    }

    @Test
    public void updateAssociatedEntities_withNullPaper_throws() {
        try {
            repo.updateAssociatedEntities(null, "de");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("paper must not be null.");
        }
    }
}
