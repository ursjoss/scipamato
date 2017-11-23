package ch.difty.scipamato.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.entity.PopulationCode;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.StudyDesignCode;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.paging.PaginationContext;
import ch.difty.scipamato.persistence.paging.PaginationRequest;
import ch.difty.scipamato.persistence.paging.Sort.Direction;

public class JooqPublicPaperRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqPublicPaperRepo repo;

    private final PaginationContext pc = new PaginationRequest(0, 10);

    @Test
    public void findingByNumber_withExistingNumber_returnsEntity() {
        long number = 1;
        PublicPaper paper = repo.findByNumber(number);
        assertThat(paper.getId()).isEqualTo(number);
        assertThat(paper.getPmId()).isEqualTo(25395026);
        assertThat(paper.getAuthors()).isEqualTo("Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.");
    }

    @Test
    public void findingByNumber_withNonExistingNumber_returnsNull() {
        assertThat(repo.findByNumber(-1l)).isNull();
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
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(3);
    }

    @Test
    public void countingByFilter_withNoFilterCriteria_findsTwo() {
        PublicPaperFilter filter = new PublicPaperFilter();
        assertThat(repo.countByFilter(filter)).isEqualTo(3);
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

    @Test
    public void findingPageByFilter_adultsOnly() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setPopulationCodes(Arrays.asList(PopulationCode.ADULTS));
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(2);
    }

    @Test
    public void findingPageByFilter_overViewMethodologyOnly() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setStudyDesignCodes(Arrays.asList(StudyDesignCode.OVERVIEW_METHODOLOGY));
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(1);
    }

    @Test
    public void findingPageOfNumbersByFilter() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setPublicationYearFrom(2015);
        assertThat(repo.findPageOfNumbersByFilter(filter, new PaginationRequest(Direction.ASC, "authors"))).isNotEmpty().containsExactly(2l);
    }
}
