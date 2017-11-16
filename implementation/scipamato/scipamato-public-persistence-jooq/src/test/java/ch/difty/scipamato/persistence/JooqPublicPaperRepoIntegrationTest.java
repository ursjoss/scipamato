package ch.difty.scipamato.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.paging.PaginationContext;
import ch.difty.scipamato.persistence.paging.PaginationRequest;

/**
 * Note: The test will insert some records into the DB. It will try to wipe those records after the test suite terminates.
 *
 * If however, the number of records in the db does not match with the defined constants a few lines further down, the 
 * additional records in the db would be wiped out by the tearDown method. So please make sure the number of records (plus
 * the highest id) match the declarations further down.
 */
public class JooqPublicPaperRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqPublicPaperRepo repo;

    private final PaginationContext pc = new PaginationRequest(0, 10);

    @Test
    public void findingById_withExistingId_returnsEntity() {
        long id = 1;
        PublicPaper paper = repo.findById(id);
        assertThat(paper.getId()).isEqualTo(id);
        assertThat(paper.getPmId()).isEqualTo(25395026);
        assertThat(paper.getAuthors()).isEqualTo("Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.");
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1l)).isNull();
    }

    @Test
    public void findingPageByFilter_forPapersAsOf2017_findsOne_() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setPublicationYearFrom(2016);
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(1);
    }

    @Test
    public void findingPageByFilter_() {
        PublicPaperFilter filter = new PublicPaperFilter();
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(2);
    }

    @Test
    public void countingByFilter_withNoFilterCriteria_findsTwo() {
        PublicPaperFilter filter = new PublicPaperFilter();
        assertThat(repo.countByFilter(filter)).isEqualTo(2);
    }

    @Test
    public void countingByFilter_withAuthorMask_findsOne() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setAuthorMask("Gapstur");
        assertThat(repo.countByFilter(filter)).isEqualTo(1);
    }

    @Test
    public void countingByFilter_withMethodsMask_findsOne() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setMethodsMask("Sensitivitätsanalysen");
        assertThat(repo.countByFilter(filter)).isEqualTo(1);
    }
}
