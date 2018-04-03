package ch.difty.scipamato.publ.persistence.paper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.entity.PopulationCode;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.StudyDesignCode;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.JooqTransactionalIntegrationTest;

public class JooqPublicPaperRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqPublicPaperRepo repo;

    private final PaginationContext pc = new PaginationRequest(0, 10);

    private final PublicPaperFilter filter = new PublicPaperFilter();

    private final PaginationRequest allSorted = new PaginationRequest(Direction.ASC, "authors");

    @Test
    public void findingByNumber_withExistingNumber_returnsEntity() {
        long number = 1;
        PublicPaper paper = repo.findByNumber(number);
        assertThat(paper.getId()).isEqualTo(number);
        assertThat(paper.getPmId()).isEqualTo(25395026);
        assertThat(paper.getAuthors()).isEqualTo(
            "Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.");
        assertThat(paper.getLocation()).isEqualTo("Am J Epidemiol. 2014; 180 (12): 1145-1149.");
        assertThat(paper.getJournal()).isEqualTo("Am J Epidemiol");
    }

    @Test
    public void findingByNumber_withNonExistingNumber_returnsNull() {
        assertThat(repo.findByNumber(-1l)).isNull();
    }

    @Test
    public void findingPageByFilter_forPapersAsOf2017_findsOne_() {
        filter.setPublicationYearFrom(2016);
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(1);
    }

    @Test
    public void findingPageByFilter_() {
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(3);
    }

    @Test
    public void countingByFilter_withNoFilterCriteria_findsTwo() {
        assertThat(repo.countByFilter(filter)).isEqualTo(3);
    }

    @Test
    public void countingByFilter_withAuthorMask_findsOne() {
        filter.setAuthorMask("Gapstur");
        assertThat(repo.countByFilter(filter)).isEqualTo(1);
    }

    @Test
    public void countingByFilter_withMethodsMask_findsOne() {
        filter.setMethodsMask("Sensitivitätsanalysen");
        assertThat(repo.countByFilter(filter)).isEqualTo(1);
    }

    @Test
    public void findingPageByFilter_adultsOnly() {
        filter.setPopulationCodes(Arrays.asList(PopulationCode.ADULTS));
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(2);
    }

    @Test
    public void findingPageByFilter_overViewMethodologyOnly() {
        filter.setStudyDesignCodes(Arrays.asList(StudyDesignCode.OVERVIEW_METHODOLOGY));
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(1);
    }

    @Test
    public void findingPageOfNumbersByFilter() {
        filter.setPublicationYearFrom(2015);
        filter.setPublicationYearUntil(2018);
        assertThat(repo.findPageOfNumbersByFilter(filter, allSorted)).isNotEmpty()
            .containsExactly(2l);
    }

    private List<Code> newCodes(String... codes) {
        final List<Code> list = new ArrayList<>();
        for (String c : codes)
            list.add(Code.builder()
                .code(c)
                .build());
        return list;
    }

    @Test
    public void findingPageByFilter_withCodes1Fand5H_finds2() {
        filter.setCodesOfClass1(newCodes("1F"));
        filter.setCodesOfClass5(newCodes("5H"));
        assertThat(repo.findPageByFilter(filter, allSorted)).isNotEmpty()
            .extracting("number")
            .containsOnly(1l, 2l);
    }

    @Test
    public void findingPageByFilter_withCodes6Mand7L_finds3() {
        filter.setCodesOfClass6(newCodes("6M"));
        filter.setCodesOfClass7(newCodes("7L"));
        assertThat(repo.findPageByFilter(filter, allSorted)).isNotEmpty()
            .extracting("number")
            .containsOnly(1l, 2l, 3l);
    }

    @Test
    public void findingPageByFilter_withCode2R_finds1() {
        filter.setCodesOfClass2(newCodes("2R"));
        assertThat(repo.findPageByFilter(filter, allSorted)).isNotEmpty()
            .extracting("number")
            .containsExactly(3l);
    }

    @Test
    public void findingPageByFilter_withOriginallySetAndThenClearedFilter_findsAll() {
        filter.setCodesOfClass2(newCodes("2R"));
        filter.getCodesOfClass2()
            .clear();
        assertThat(repo.findPageByFilter(filter, allSorted)).isNotEmpty()
            .extracting("number")
            .containsOnly(1l, 2l, 3l);
    }
}
