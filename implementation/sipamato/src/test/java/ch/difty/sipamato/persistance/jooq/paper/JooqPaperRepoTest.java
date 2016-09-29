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
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.GenericRepository;
import ch.difty.sipamato.persistance.jooq.JooqRepoTest;

public class JooqPaperRepoTest extends JooqRepoTest<PaperRecord, Paper, Long, ch.difty.sipamato.db.h2.tables.Paper, PaperRecordMapper, PaperFilter> {

    private static final Long SAMPLE_ID = 3l;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    private JooqPaperRepo repo;

    @Override
    protected JooqPaperRepo getRepo() {
        if (repo == null) {
            repo = new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getSortFieldExtractor(), getJooqConfig());
        }
        return repo;
    }

    @Override
    protected GenericRepository<PaperRecord, Paper, Long, PaperRecordMapper, PaperFilter> makeRepoFindingEntityById(Paper paper) {
        return new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getSortFieldExtractor(), getJooqConfig()) {
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
    protected Class<PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.h2.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    @Mock
    private PaperFilter filterMock;

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
            new JooqPaperRepo(null, getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getSortFieldExtractor(), getJooqConfig());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getSortFieldExtractor(), getJooqConfig());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), null, getUpdateSetStepSetter(), getSortFieldExtractor(), getJooqConfig());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), null, getSortFieldExtractor(), getJooqConfig());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null, getJooqConfig());
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqPaperRepo(getDsl(), getMapper(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getSortFieldExtractor(), null);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("jooqConfig must not be null.");
        }
    }

    private final PaperFilter filter = new PaperFilter();

    @Test
    public void creatingWhereCondition_withNoFilterCriteria_returnsNoOpCondition() {
        assertThat(repo.createWhereConditions(filter).toString()).isEqualTo("1 = 1");
    }

    @Test
    public void creatingWhereCondition_withAuthorMask_searchesFirstAuthorAndAuthors() {
        String pattern = "am";
        filter.setAuthorMask(pattern);
        assertThat(repo.createWhereConditions(filter).toString()).isEqualTo(makeWhereClause(pattern, "FIRST_AUTHOR", "AUTHORS"));
    }

    @Test
    public void creatingWhereCondition_withMethodsMask_searchesExposureAndMethodFields() {
        String pattern = "m";
        filter.setMethodsMask(pattern);
        // @formatter:off
        assertThat(repo.createWhereConditions(filter).toString()).isEqualTo(makeWhereClause(pattern
                , "EXPOSURE_POLLUTANT"
                , "EXPOSURE_ASSESSMENT"
                , "METHODS"
                , "METHOD_STUDY_DESIGN"
                , "METHOD_OUTCOME"
                , "METHOD_STATISTICS"
                , "METHOD_CONFOUNDERS"
        ));
        // @formatter:off
    }

    @Test
    public void creatingWhereCondition_withSearchMask_searchesRemainingTextFields() {
        String pattern = "foo";
        filter.setSearchMask(pattern);
        // @formatter:off
        assertThat(repo.createWhereConditions(filter).toString()).isEqualTo(makeWhereClause(pattern
                , "DOI"
                , "LOCATION"
                , "TITLE"
                , "GOALS"
                , "POPULATION"
                , "POPULATION_PLACE"
                , "POPULATION_PARTICIPANTS"
                , "POPULATION_DURATION"
                , "RESULT"
                , "RESULT_EXPOSURE_RANGE"
                , "RESULT_EFFECT_ESTIMATE"
                , "COMMENT"
                , "INTERN"
        ));
        // @formatter:off
    }

    @Test
    public void creatingWhereCondition_withPublicationYearFrom_searchesPublicationYear() {
        filter.setPublicationYearFrom(2016);
        assertThat(repo.createWhereConditions(filter).toString()).isEqualTo("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" >= 2016");
    }

    @Test
    public void creatingWhereCondition_withPublicationYearUntil_searchesPublicationYear() {
        filter.setPublicationYearUntil(2016);
        assertThat(repo.createWhereConditions(filter).toString()).isEqualTo("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" <= 2016");
    }

}
