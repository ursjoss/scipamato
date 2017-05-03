package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.Tables.SEARCH_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.persistance.jooq.JooqTransactionalIntegrationTest;

/**
 * Note: The test will insert some records into the DB. It will try to wipe those records after the test suite terminates.
 *
 * If however, the number of records in the db does not match with the defined constants a few lines further down, the 
 * additional records in the db would be wiped out by the tearDown method. So please make sure the number of records (plus
 * the highest id) match the declarations further down.
 */
public class JooqSearchOrderRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    private static final Integer RECORD_COUNT_PREPOPULATED = 4;
    private static final Long MAX_ID_PREPOPULATED = 4l;

    @Autowired
    private DSLContext dsl;

    @Autowired
    private JooqSearchOrderRepo repo;

    @After
    public void teardown() {
        // Delete all search orders that were created in any test
        dsl.delete(SEARCH_ORDER).where(SEARCH_ORDER.ID.gt(RECORD_COUNT_PREPOPULATED.longValue())).execute();
    }

    @Test
    public void findingAll() {
        List<SearchOrder> searchOrders = repo.findAll();
        assertThat(searchOrders).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(searchOrders.get(0).getId()).isEqualTo(1);
        assertThat(searchOrders.get(1).getId()).isEqualTo(2);
        assertThat(searchOrders.get(2).getId()).isEqualTo(3);
        assertThat(searchOrders.get(3).getId()).isEqualTo(4);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        SearchOrder searchOrder = repo.findById(RECORD_COUNT_PREPOPULATED.longValue());
        assertThat(searchOrder.getId()).isEqualTo(MAX_ID_PREPOPULATED);
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1l)).isNull();
    }

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        SearchOrder so = makeMinimalSearchOrder();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setAuthors("foo");
        so.add(searchCondition);
        so.addExclusionOfPaperWithId(4l);
        assertThat(so.getId()).isNull();
        assertThat(so.getSearchConditions().get(0).getId()).isNull();
        assertThat(so.getSearchConditions().get(0).getStringSearchTerms().iterator().next().getId()).isNull();

        SearchOrder saved = repo.add(so);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        assertThat(saved.getOwner()).isEqualTo(10);
        assertThat(saved.getSearchConditions().get(0).getSearchConditionId()).isNotNull().isGreaterThan(5l);
        assertThat(saved.getSearchConditions().get(0).getStringSearchTerms().iterator().next().getId()).isNotNull();

        assertThat(saved.getExcludedPaperIds()).containsOnly(4l);
    }

    private SearchOrder makeMinimalSearchOrder() {
        SearchOrder so = new SearchOrder();
        so.setName(null);
        so.setOwner(10);
        so.setGlobal(true);
        return so;
    }

    @Test
    public void updatingRecord() {
        SearchOrder searchOrder = repo.add(makeMinimalSearchOrder());
        assertThat(searchOrder).isNotNull();
        assertThat(searchOrder.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = searchOrder.getId();
        assertThat(searchOrder.getOwner()).isEqualTo(10);
        assertThat(searchOrder.getName()).isNull();

        searchOrder.setOwner(20);
        searchOrder.setName("soName");
        repo.update(searchOrder);
        assertThat(searchOrder.getId()).isEqualTo(id);
        assertThat(searchOrder.getName()).isEqualTo("soName");

        SearchOrder newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(searchOrder);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getOwner()).isEqualTo(20);
        assertThat(newCopy.getName()).isEqualTo("soName");
    }

    @Test
    public void deletingRecord() {
        SearchOrder searchOrder = repo.add(makeMinimalSearchOrder());
        assertThat(searchOrder).isNotNull();
        assertThat(searchOrder.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = searchOrder.getId();
        assertThat(searchOrder.getOwner()).isEqualTo(10);

        SearchOrder deleted = repo.delete(id);
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

    @Test
    public void enrichingAssociatedEntities_hasConditionsAndTerms() {
        final SearchOrder so = new SearchOrder();
        so.setId(1l);
        repo.enrichAssociatedEntitiesOf(so);

        assertThat(so.getSearchConditions().size()).isGreaterThanOrEqualTo(2);

        SearchCondition so1 = so.getSearchConditions().get(0);
        assertThat(so1).isNotNull();
        assertThat(so1.getAuthors()).isEqualTo("kutlar");
        assertThat(so1.getDisplayValue()).isEqualTo("kutlar");

        SearchCondition so2 = so.getSearchConditions().get(1);
        assertThat(so2).isNotNull();
        assertThat(so2.getAuthors()).isEqualTo("turner");
        assertThat(so2.getPublicationYear()).isEqualTo("2014-2015");
        assertThat(so2.getDisplayValue()).isEqualTo("turner AND 2014-2015");
    }

    @Test
    public void enrichingAssociatedEntities_hasExcludedIds() {
        final SearchOrder so = new SearchOrder();
        so.setId(4l);
        repo.enrichAssociatedEntitiesOf(so);

        assertThat(so.getExcludedPaperIds()).hasSize(1).containsExactly(1l);
    }

    @Test
    public void addAndModifiyAndDeleteSearchConditions() {
        // make search order with single condition (string search term)
        SearchOrder initialSearchOrder = makeMinimalSearchOrder();
        initialSearchOrder.add(newConditionWithAuthors("foo"));
        assertThat(initialSearchOrder.getId()).isNull();

        SearchOrder savedSearchOrder = repo.add(initialSearchOrder);
        // saved search order now has a db-generated id, still has single condition. 
        long searchOrderId = savedSearchOrder.getId();
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1);

        // add additional title condition to existing search order
        SearchCondition titleCondition = newConditionWithTitle("PM2.5");
        SearchCondition savedCondition = repo.addSearchCondition(titleCondition, searchOrderId);
        assertSearchTermCount(1, 0, 0, savedCondition);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(2);

        // modify the currently savedCondition to also have a publicationYear integer search term
        savedCondition.setPublicationYear("2000");
        SearchCondition modifiedCondition = repo.updateSearchCondition(savedCondition, searchOrderId);
        assertSearchTermCount(1, 1, 0, modifiedCondition);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(3);

        // remove the new search condition
        repo.deleteSearchConditionWithId(savedCondition.getSearchConditionId());
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1);
    }

    private SearchCondition newConditionWithAuthors(String authors) {
        SearchCondition sc = new SearchCondition();
        sc.setAuthors(authors);
        assertSearchTermCount(1, 0, 0, sc);
        return sc;
    }

    private void assertSearchTermCount(int sst, int ist, int bst, SearchCondition sc) {
        assertThat(sc.getStringSearchTerms()).hasSize(sst);
        assertThat(sc.getIntegerSearchTerms()).hasSize(ist);
        assertThat(sc.getBooleanSearchTerms()).hasSize(bst);
    }

    private SearchCondition newConditionWithTitle(String title) {
        SearchCondition sc = new SearchCondition();
        sc.setTitle(title);
        assertSearchTermCount(1, 0, 0, sc);
        return sc;
    }

}
