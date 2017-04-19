package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.paging.Page;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepoTest;
import ch.difty.sipamato.persistance.jooq.paper.searchorder.PaperBackedSearchOrderRepository;

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
    @Mock
    private PaperBackedSearchOrderRepository searchOrderRepositoryMock;
    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private Paper paperMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<Paper> pageMock;

    private final List<Paper> papers = new ArrayList<>();

    private final List<Paper> enrichtedEntities = new ArrayList<>();

    @Override
    protected void testSpecificSetUp() {
        papers.add(paperMock);
        papers.add(paperMock);
    }

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqPaperRepo getRepo() {
        if (repo == null) {
            repo = new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig(), searchOrderRepositoryMock);
        }
        return repo;
    }

    @Override
    protected EntityRepository<Paper, Long, PaperFilter> makeRepoFindingEntityById(Paper paper) {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getJooqConfig(), searchOrderRepositoryMock) {
            private static final long serialVersionUID = 1L;

            @Override
            public Paper findById(Long id) {
                return paper;
            }
        };
    }

    protected PaperRepository makeRepoStubbingEnriching() {
        return new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getJooqConfig(), searchOrderRepositoryMock) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void enrichAssociatedEntitiesOf(Paper entity) {
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
                    getJooqConfig(), searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig(),
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig(),
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig(),
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig(),
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dateTimeService must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig(),
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("localization must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), null, getUpdateSetStepSetter(), getJooqConfig(),
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), null, getJooqConfig(),
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null,
                    searchOrderRepositoryMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("jooqConfig must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrderRepository must not be null.");
        }
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
    public void gettingIdFromPaper() {
        when(paperMock.getId()).thenReturn(17l);
        assertThat(repo.getIdFrom(paperMock)).isEqualTo(17l);
        verify(paperMock).getId();
    }

    @Test
    public void gettingByIds_withNullIdList_throwsNullArgumentException() {
        try {
            repo.findByIds(null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("ids must not be null.");
        }
    }

    @Test
    public void findingBySearchOrder_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.findBySearchOrder(searchOrderMock)).thenReturn(papers);
        assertThat(makeRepoStubbingEnriching().findBySearchOrder(searchOrderMock)).containsExactly(paperMock, paperMock);
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
    public void findingBySearchOrder_withPageable_delegatesToSearchOrderFinder() {
        when(searchOrderRepositoryMock.findPagedBySearchOrder(searchOrderMock, pageableMock)).thenReturn(pageMock);
        when(searchOrderRepositoryMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        when(pageMock.getContent()).thenReturn(papers);

        assertThat(makeRepoStubbingEnriching().findBySearchOrder(searchOrderMock, pageableMock).getContent()).containsExactly(paperMock, paperMock);
        assertThat(enrichtedEntities).containsExactly(paperMock, paperMock);

        verify(searchOrderRepositoryMock).findPagedBySearchOrder(searchOrderMock, pageableMock);
        verify(searchOrderRepositoryMock).countBySearchOrder(searchOrderMock);
        verify(pageMock).getContent();
    }

    @Test
    public void gettingPapersByPmIds_withNoPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(new ArrayList<Integer>())).isEmpty();
    }

    @Test
    public void gettingPapersByPmIds_withNullPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(null)).isEmpty();
    }

}
