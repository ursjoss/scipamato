package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.Condition;
import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.entity.IntegerSearchTerm;
import ch.difty.sipamato.entity.IntegerSearchTerm.MatchType;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepoTest;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;

public class JooqPaperSlimRepoTest extends JooqReadOnlyRepoTest<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, SimplePaperFilter> {

    private static final Long SAMPLE_ID = 3l;

    private JooqPaperSlimRepo repo;

    @Mock
    private IntegerSearchTerm istMock;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, SimplePaperFilter> getRepo() {
        if (repo == null) {
            repo = new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization());
        }
        return repo;
    }

    @Override
    protected ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, SimplePaperFilter> makeRepoFindingEntityById(PaperSlim entity) {
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
    private SimplePaperFilter filterMock;

    @Override
    protected SimplePaperFilter getFilter() {
        return filterMock;
    }

    @Test
    public void findingByFilter_withNullComplex_throws() {
        try {
            repo.findByFilter((CompositeComplexPaperFilter) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filter must not be null.");
        }
    }

    private void expectIntegerSearchTerm(MatchType type, int v) {
        expectIntegerSearchTerm(type, v, v);

    }

    private void expectIntegerSearchTerm(MatchType type, int v1, int v2) {
        when(istMock.getType()).thenReturn(type);
        when(istMock.getKey()).thenReturn("fieldX");
        when(istMock.getValue()).thenReturn(v1);
        when(istMock.getValue2()).thenReturn(v2);
    }

    @Test
    public void buildingIntegerCondition_withGreaterThanComparison() {
        expectIntegerSearchTerm(MatchType.GREATER_THAN, 10);
        Condition c = repo.applyIntegerSearchLogic(istMock);
        assertThat(c.toString()).isEqualTo("fieldX > 10");
    }

    @Test
    public void buildingIntegerCondition_withGreaterThanOrEqualComparison() {
        expectIntegerSearchTerm(MatchType.GREATER_OR_EQUAL, 10);
        Condition c = repo.applyIntegerSearchLogic(istMock);
        assertThat(c.toString()).isEqualTo("fieldX >= 10");
    }

    @Test
    public void buildingIntegerCondition_withExactValue() {
        expectIntegerSearchTerm(MatchType.EXACT, 10);
        Condition c = repo.applyIntegerSearchLogic(istMock);
        assertThat(c.toString()).isEqualTo("fieldX = 10");
    }

    @Test
    public void buildingIntegerCondition_withLessThanOrEqualComparison() {
        expectIntegerSearchTerm(MatchType.LESS_OR_EQUAL, 10);
        Condition c = repo.applyIntegerSearchLogic(istMock);
        assertThat(c.toString()).isEqualTo("fieldX <= 10");
    }

    @Test
    public void buildingIntegerCondition_withLessThanComparison() {
        expectIntegerSearchTerm(MatchType.LESS_THAN, 10);
        Condition c = repo.applyIntegerSearchLogic(istMock);
        assertThat(c.toString()).isEqualTo("fieldX < 10");
    }

    @Test
    public void buildingIntegerCondition_withRangeomparison() {
        expectIntegerSearchTerm(MatchType.RANGE, 10, 15);
        Condition c = repo.applyIntegerSearchLogic(istMock);
        assertThat(c.toString()).isEqualTo("fieldX between 10 and 15");
    }
}
