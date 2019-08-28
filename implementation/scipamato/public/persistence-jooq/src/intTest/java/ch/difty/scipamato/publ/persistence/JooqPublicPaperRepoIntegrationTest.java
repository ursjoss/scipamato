package ch.difty.scipamato.publ.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.publ.entity.*;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.paper.JooqPublicPaperRepo;

@SuppressWarnings("SpellCheckingInspection")
@JooqTest
@Testcontainers
class JooqPublicPaperRepoIntegrationTest {

    @Autowired
    private JooqPublicPaperRepo repo;

    private final PaginationContext pc = new PaginationRequest(0, 10);

    private final PublicPaperFilter filter = new PublicPaperFilter();

    private final PaginationRequest allSorted = new PaginationRequest(Direction.ASC, "authors");

    @Test
    void findingByNumber_withExistingNumber_returnsEntity() {
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
    void findingByNumber_withNonExistingNumber_returnsNull() {
        assertThat(repo.findByNumber(-1L)).isNull();
    }

    @Test
    void findingPageByFilter_forPapersAsOf2017_findsOne_() {
        filter.setPublicationYearFrom(2016);
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(3);
    }

    @Test
    void findingPageByFilter_forAnyPaper() {
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(10);
    }

    @Test
    void countingByFilter_withNoFilterCriteria_findsTwo() {
        assertThat(repo.countByFilter(filter)).isEqualTo(13);
    }

    @Test
    void countingByFilter_withAuthorMask_findsOne() {
        filter.setAuthorMask("Gapstur");
        assertThat(repo.countByFilter(filter)).isEqualTo(1);
    }

    @Test
    void countingByFilter_withTitleMask_findsOne() {
        filter.setTitleMask("ambient");
        assertThat(repo.countByFilter(filter)).isEqualTo(3);
    }

    @Test
    void countingByFilter_withMethodsMask_findsOne() {
        filter.setMethodsMask("Sensitivitätsanalysen");
        assertThat(repo.countByFilter(filter)).isEqualTo(1);
    }

    @Test
    void findingPageByFilter_adultsOnly() {
        filter.setPopulationCodes(Collections.singletonList(PopulationCode.ADULTS));
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(2);
    }

    @Test
    void findingPageByFilter_overViewMethodologyOnly() {
        filter.setStudyDesignCodes(Collections.singletonList(StudyDesignCode.OVERVIEW_METHODOLOGY));
        assertThat(repo.findPageByFilter(filter, pc)).hasSize(1);
    }

    @Test
    void findingPageOfNumbersByFilter() {
        filter.setPublicationYearFrom(2015);
        filter.setPublicationYearUntil(2018);
        assertThat(repo.findPageOfNumbersByFilter(filter, allSorted))
            .isNotEmpty()
            .containsOnly(8984L, 8934L, 8924L, 2L, 8933L, 8983L, 8993L, 8861L, 8916L, 8973L, 8897L);
    }

    private List<Code> newCodes(String... codes) {
        final List<Code> list = new ArrayList<>();
        for (String c : codes)
            list.add(Code
                .builder()
                .code(c)
                .build());
        return list;
    }

    @Test
    void findingPageByFilter_withCodes1Fand5H_finds2() {
        filter.setCodesOfClass1(newCodes("1F"));
        filter.setCodesOfClass5(newCodes("5H"));
        assertThat(repo.findPageByFilter(filter, allSorted))
            .isNotEmpty()
            .extracting("number")
            .containsOnly(1L, 2L);
    }

    @Test
    void findingPageByFilter_withCodes6Mand7L_finds3() {
        filter.setCodesOfClass6(newCodes("6M"));
        filter.setCodesOfClass7(newCodes("7L"));
        assertThat(repo.findPageByFilter(filter, allSorted))
            .isNotEmpty()
            .extracting("number")
            .containsOnly(1L, 2L, 3L);
    }

    @Test
    void findingPageByFilter_withCode2R_finds1() {
        filter.setCodesOfClass2(newCodes("2R"));
        assertThat(repo.findPageByFilter(filter, allSorted))
            .isNotEmpty()
            .extracting("number")
            .containsExactly(3L);
    }

    @Test
    void findingPageByFilter_withOriginallySetAndThenClearedFilter_findsAll() {
        filter.setCodesOfClass2(newCodes("2R"));
        filter
            .getCodesOfClass2()
            .clear();
        assertThat(repo.findPageByFilter(filter, allSorted))
            .isNotEmpty()
            .extracting("number")
            .containsOnly(8984L, 8934L, 8924L, 2L, 8933L, 8983L, 8993L, 8861L, 8916L, 8973L, 1L, 3L, 8897L);
    }

    @Test
    void findingPageByFilter_withCodes6Mand7L_andWithKeywordWithId3_findsOnlyOne() {
        filter.setCodesOfClass6(newCodes("6M"));
        filter.setCodesOfClass7(newCodes("7L"));
        filter.setKeywords(List.of(new Keyword(1, 3, "en", "foo", null)));
        assertThat(repo.findPageByFilter(filter, allSorted))
            .isNotEmpty()
            .extracting("number")
            .containsOnly(3L);
    }

    @Test
    void findingPageByFilter_withKeywordWithId3And6_findsOnlyOne() {
        final Keyword kw1 = new Keyword(1, 3, "en", "foo", null);
        filter.setKeywords(List.of(kw1));
        assertThat(repo.findPageByFilter(filter, allSorted)).isNotEmpty();

        final Keyword kw2 = new Keyword(7, 2, "en", "bar", null);
        filter.setKeywords(List.of(kw1, kw2));
        assertThat(repo.findPageByFilter(filter, allSorted)).isEmpty();
    }
}
