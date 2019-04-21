package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.*;

import org.jooq.TableField;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.search.*;
import ch.difty.scipamato.core.persistence.EntityRepository;
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class JooqSearchOrderRepoTest extends
    JooqEntityRepoTest<SearchOrderRecord, SearchOrder, Long, ch.difty.scipamato.core.db.tables.SearchOrder, SearchOrderRecordMapper, SearchOrderFilter> {

    private static final Long   SAMPLE_ID = 3L;
    private static final String LC        = "de";

    private JooqSearchOrderRepo repo;

    @Mock
    private SearchOrder             unpersistedEntity;
    @Mock
    private SearchOrder             persistedEntity;
    @Mock
    private SearchOrderRecord       persistedRecord;
    @Mock
    private SearchOrderRecordMapper mapperMock;
    @Mock
    private SearchOrderFilter       filterMock;

    private final SearchCondition sc1 = new SearchCondition();
    private final SearchCondition sc2 = new SearchCondition();

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqSearchOrderRepo getRepo() {
        if (repo == null) {
            repo = new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties());
        }
        return repo;
    }

    @Override
    protected EntityRepository<SearchOrder, Long, SearchOrderFilter> makeRepoFindingEntityById(
        SearchOrder searchOrder) {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()) {

            @Override
            public SearchOrder findById(Long id, int version) {
                return searchOrder;
            }
        };
    }

    @Override
    protected SearchOrder getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected SearchOrder getPersistedEntity() {
        return persistedEntity;
    }

    @Override
    protected SearchOrderRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Override
    protected SearchOrderRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected ch.difty.scipamato.core.db.tables.SearchOrder getTable() {
        return SEARCH_ORDER;
    }

    @Override
    protected TableField<SearchOrderRecord, Long> getTableId() {
        return SEARCH_ORDER.ID;
    }

    @Override
    protected TableField<SearchOrderRecord, Integer> getRecordVersion() {
        return SEARCH_ORDER.VERSION;
    }

    @Override
    protected EntityRepository<SearchOrder, Long, SearchOrderFilter> makeRepoSavingReturning(
        final SearchOrderRecord returning) {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()) {
            @Override
            protected SearchOrderRecord doSave(final SearchOrder entity, final String languageCode) {
                return returning;
            }
        };
    }

    @Override
    protected SearchOrderFilter getFilter() {
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
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()),
            "dsl");
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()),
            "mapper");
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()), "sortMapper");
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()),
            "filterConditionMapper");
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null,
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()), "dateTimeService");
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), null, getUpdateSetStepSetter(), getApplicationProperties()),
            "insertSetStepSetter");
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), null, getApplicationProperties()),
            "updateSetStepSetter");
        assertDegenerateSupplierParameter(
            () -> new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null),
            "applicationProperties");
    }

    @Test
    public void enrichingAssociatedEntities_withNullEntity_doesNothing() {
        repo.enrichAssociatedEntitiesOf(null, LC);
    }

    @Test
    public void enrichingAssociatedEntities_withEntityWithNullId_doesNothing() {
        SearchOrder so = new SearchOrder();
        assertThat(so.getId()).isNull();

        repo.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so.getSearchConditions()).isEmpty();
    }

    private JooqSearchOrderRepo makeRepoFindingNestedEntities() {
        return new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()) {

            final SearchTerm st1 = SearchTerm.newSearchTerm(1, SearchTermType.STRING.getId(), 3,
                Paper.PaperFields.AUTHORS.getName(), "joss");
            final SearchTerm st2 = SearchTerm.newSearchTerm(2, SearchTermType.INTEGER.getId(), 3,
                Paper.PaperFields.PUBL_YEAR.getName(), "2014");
            final SearchTerm st3 = SearchTerm.newSearchTerm(3, SearchTermType.INTEGER.getId(), 4,
                Paper.PaperFields.PUBL_YEAR.getName(), "2014-2016");
            final SearchTerm st4 = SearchTerm.newSearchTerm(4, SearchTermType.AUDIT.getId(), 5,
                Paper.PaperFields.CREATED_BY.getName(), "mkj");

            @Override
            protected List<SearchTerm> fetchSearchTermsForSearchOrderWithId(long searchOrderId) {
                if (searchOrderId == SAMPLE_ID) {
                    return Arrays.asList(st1, st2, st3, st4);
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            protected List<Long> fetchExcludedPaperIdsForSearchOrderWithId(long searchOrderId) {
                if (searchOrderId == SAMPLE_ID) {
                    return Arrays.asList(17L, 33L, 42L);
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            protected List<Code> fetchCodesForSearchConditionWithId(SearchCondition searchCondition,
                String languageCode) {
                return Collections.singletonList(new Code("1F", "Code 1F", "", false, 1, "CC 1", "", 0));
            }

            @Override
            protected SearchCondition fetchSearchConditionWithId(Long scId) {
                return new SearchCondition(scId);
            }

            @Override
            public List<Long> findConditionIdsWithSearchTerms(Long searchOrderId) {
                return new ArrayList<>();
            }

            @Override
            protected List<SearchCondition> findConditionsOf(final Long searchOrderId) {
                return new ArrayList<>();
            }
        };
    }

    @Test
    public void enrichingAssociatedEntities_withEntityId_fillsTheSearchConditionsAndTerms() {
        JooqSearchOrderRepo repoSpy = makeRepoFindingNestedEntities();
        SearchOrder so = new SearchOrder();
        so.setId(SAMPLE_ID);
        assertThat(so.getSearchConditions()).isEmpty();

        repoSpy.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so.getSearchConditions()).hasSize(3);

        SearchCondition so1 = so
            .getSearchConditions()
            .get(0);
        assertThat(so1).isNotNull();
        assertThat(so1.getAuthors()).isEqualTo("joss");
        assertThat(so1.getPublicationYear()).isEqualTo("2014");
        assertThat(so1.getDisplayValue()).isEqualTo("joss AND 2014 AND 1F");

        SearchCondition so2 = so
            .getSearchConditions()
            .get(1);
        assertThat(so2).isNotNull();
        assertThat(so2.getPublicationYear()).isEqualTo("2014-2016");
        assertThat(so2.getDisplayValue()).isEqualTo("2014-2016 AND 1F");

        SearchCondition so3 = so
            .getSearchConditions()
            .get(2);
        assertThat(so3).isNotNull();
        assertThat(so3.getCreatedBy()).isEqualTo("mkj");
        assertThat(so3.getDisplayValue()).isEqualTo("mkj AND 1F");
    }

    @Test
    public void enrichingAssociatedEntities_withEntityId_fillsTheExcludedPaperIds() {
        JooqSearchOrderRepo repoSpy = makeRepoFindingNestedEntities();
        SearchOrder so = new SearchOrder();
        so.setId(SAMPLE_ID);
        assertThat(so.getExcludedPaperIds()).isEmpty();

        repoSpy.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so.getExcludedPaperIds())
            .hasSize(3)
            .containsExactly(17L, 33L, 42L);
    }

    @Test
    public void hasDirtyNewsletterFields_withTwoEmptySearchConditions_isNotDirty() {
        assertThat(sc1.getNewsletterTopicId()).isNull();
        assertThat(sc2.getNewsletterTopicId()).isNull();
        assertThat(sc1.getNewsletterHeadline()).isNull();
        assertThat(sc2.getNewsletterHeadline()).isNull();
        assertThat(sc1.getNewsletterIssue()).isNull();
        assertThat(sc2.getNewsletterIssue()).isNull();

        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse();
    }

    @Test
    public void hasDirtyNewsletterFields_withSingleNewsletterTopic_isDirty() {
        sc1.setNewsletterTopic(new NewsletterTopic(1, "1"));
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue();
    }

    @Test
    public void hasDirtyNewsletterFields_withDifferentNewsletterTopic_isDirty() {
        sc1.setNewsletterTopic(new NewsletterTopic(1, "1"));
        sc2.setNewsletterTopic(new NewsletterTopic(2, "2"));
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue();
    }

    @Test
    public void hasDirtyNewsletterFields_withIdenticalNewsletterTopicIds_isNotDirty() {
        sc1.setNewsletterTopic(new NewsletterTopic(1, "foo"));
        sc2.setNewsletterTopic(new NewsletterTopic(1, "bar"));
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse();
    }

    @Test
    public void hasDirtyNewsletterFields_withSingleNewsletterHeadline_isDirty() {
        sc1.setNewsletterHeadline("foo");
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue();
    }

    @Test
    public void hasDirtyNewsletterFields_withDifferentNewsletterHeadlines_isDirty() {
        sc1.setNewsletterHeadline("foo");
        sc2.setNewsletterHeadline("bar");
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue();
    }

    @Test
    public void hasDirtyNewsletterFields_withIdenticalNewsletterHeadlines_isNotDirty() {
        sc1.setNewsletterHeadline("foo");
        sc2.setNewsletterHeadline("foo");
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse();
    }

    @Test
    public void hasDirtyNewsletterFields_withSingleNewsletterIssue_isDirty() {
        sc1.setNewsletterIssue("foo");
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue();
    }

    @Test
    public void hasDirtyNewsletterFields_withDifferentNewsletterIssue_isDirty() {
        sc2.setNewsletterIssue("bar");
        sc1.setNewsletterIssue("foo");
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isTrue();
    }

    @Test
    public void hasDirtyNewsletterFields_withIdenticalNewsletterIssue_isNotDirty() {
        sc1.setNewsletterIssue("foo");
        sc2.setNewsletterIssue("foo");
        assertThat(repo.hasDirtyNewsletterFields(sc1, sc2)).isFalse();
    }

    @Test
    public void addingSearchCondition_nonDirty_returnsPersistedEquivalentSearchCondition() {
        final SearchCondition equivalentPersistedSearchCondition = mock(SearchCondition.class);
        final JooqSearchOrderRepo repo = new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(),
            getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
            getApplicationProperties()) {
            @Override
            Optional<SearchCondition> findEquivalentPersisted(final SearchCondition searchCondition,
                final long searchOrderId, final String languageCode) {
                return Optional.of(equivalentPersistedSearchCondition);
            }

            @Override
            boolean hasDirtyNewsletterFields(final SearchCondition searchCondition, final SearchCondition psc) {
                return false;
            }
        };
        assertThat(repo.addSearchCondition(new SearchCondition(), 1, "en")).isEqualTo(
            equivalentPersistedSearchCondition);
    }

    @Test
    public void findingTermLessConditions() {
        final Map<Long, SearchCondition> idToSc = new HashMap<>();

        // sc without id - should be filtered out
        final SearchCondition sc1 = new SearchCondition();
        assertThat(sc1.getSearchConditionId()).isNull();
        idToSc.put(1L, sc1);

        // sc with id - which is also contained in the conditionId list - should be filtered out
        final SearchCondition sc2 = new SearchCondition();
        sc2.setSearchConditionId(2L);
        idToSc.put(sc2.getSearchConditionId(), sc2);

        // sc with id -> should be returned
        final SearchCondition sc3 = new SearchCondition();
        sc3.setSearchConditionId(3L);
        idToSc.put(sc3.getSearchConditionId(), sc3);

        final List<Long> conditionIdsWithSearchTerms = Arrays.asList(sc2.getSearchConditionId());

        assertThat(repo.findTermLessConditions(idToSc, conditionIdsWithSearchTerms)).containsExactly(sc3);
    }

    @Test
    public void storingExistingConditionsOf_withSearchConditionsWithIds_callsUpdateSearchConditionForEach() {
        final SearchOrder so = new SearchOrder();
        so.setId(1L);
        final SearchCondition sc1 = new SearchCondition(10L);

        final SearchCondition sc2 = new SearchCondition(20L);
        so.add(sc1);
        so.add(sc2);

        final long[] updateCalled = { 0 };
        final JooqSearchOrderRepo repo = new JooqSearchOrderRepo(getDsl(), getMapper(), getSortMapper(),
            getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
            getApplicationProperties()) {

            @Override
            public SearchCondition updateSearchCondition(final SearchCondition searchCondition,
                final long searchOrderId, final String languageCode) {
                updateCalled[0] = updateCalled[0] + searchCondition.getSearchConditionId();
                return null;
            }
        };
        repo.storeExistingConditionsOf(so, "de");

        assertThat(updateCalled[0]).isEqualTo(30L);

    }
}
